package com.onyxdb.mdb.taskProcessing.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.onyxdb.mdb.generated.jooq.tables.records.TasksRecord;
import com.onyxdb.mdb.generated.jooq.tables.records.TasksToBlockerTasksRecord;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskStatus;
import com.onyxdb.mdb.taskProcessing.models.TaskWithBlockers;

import static com.onyxdb.mdb.generated.jooq.Tables.TASKS;
import static com.onyxdb.mdb.generated.jooq.Tables.TASKS_TO_BLOCKER_TASKS;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class TaskPostgresRepository implements TaskRepository {
    private final DSLContext dslContext;

    @Override
    public void createBulk(List<Task> tasks) {
        var records = tasks.stream()
                .map(Task::toJooqClusterTasksRecord)
                .toList();

        dslContext.batchInsert(records).execute();
    }

    @Override
    public void createBlockerTasksBulk(List<TaskWithBlockers> tasksWithBlockers) {
        var records = tasksWithBlockers.stream()
                .map(taskWithBlockers -> taskWithBlockers.blockerIds()
                        .stream()
                        .map(blockerId -> new TasksToBlockerTasksRecord(
                                taskWithBlockers.task().id(),
                                blockerId
                        ))
                        .toList()
                )
                .flatMap(List::stream)
                .toList();

        dslContext.batchInsert(records).execute();
    }

    @Override
    public List<Task> getTasksToProcess(
            int limit,
            LocalDateTime scheduledAt
    ) {
        var ct = TASKS.as("ct");
        var ctt = TASKS.as("ctt");
        var cttbt = TASKS_TO_BLOCKER_TASKS.as("cttbt");

        var noUnfinishedTasksCondition = DSL.notExists(
                DSL.selectOne().
                        from(cttbt)
                        .join(ct).on(ct.ID.eq(cttbt.BLOCKER_TASK_ID))
                        .where(ct.STATUS.in(
                                com.onyxdb.mdb.generated.jooq.enums.TaskStatus.scheduled,
                                com.onyxdb.mdb.generated.jooq.enums.TaskStatus.in_progress,
                                com.onyxdb.mdb.generated.jooq.enums.TaskStatus.error
                        ))
                        .and(ctt.ID.eq(cttbt.TASK_ID))
        );

        return dslContext.select(ctt.fields())
                .from(ctt)
                .where(
                        ctt.STATUS.eq(com.onyxdb.mdb.generated.jooq.enums.TaskStatus.scheduled)
                )
                .and(ctt.SCHEDULED_AT.le(scheduledAt))
                .and(noUnfinishedTasksCondition)
                .limit(limit)
                .forUpdate()
                .skipLocked()
                .fetchMany()
                .stream()
                .map(result -> result.into(TasksRecord.class))
                .flatMap(List::stream)
                .map(Task::fromJooqClusterTasksRecord)
                .toList();
    }

    @Override
    public void updateStatus(UUID id, TaskStatus status) {
        dslContext.update(TASKS)
                .set(
                        TASKS.STATUS,
                        com.onyxdb.mdb.generated.jooq.enums.TaskStatus.valueOf(status.value())
                )
                .where(TASKS.ID.eq(id))
                .execute();
    }

    @Override
    public void updateTask(
            UUID id,
            TaskStatus status,
            @Nullable
            Integer attemptsLeft,
            @Nullable
            LocalDateTime scheduledAt
    ) {
        var updateSetMoreStep = dslContext.update(TASKS)
                .set(
                        TASKS.STATUS,
                        com.onyxdb.mdb.generated.jooq.enums.TaskStatus.lookupLiteral(status.value())
                );

        if (Objects.nonNull(attemptsLeft)) {
            updateSetMoreStep = updateSetMoreStep.set(TASKS.ATTEMPTS_LEFT, attemptsLeft);
        }
        if (Objects.nonNull(scheduledAt)) {
            updateSetMoreStep = updateSetMoreStep.set(TASKS.SCHEDULED_AT, scheduledAt);
        }
        updateSetMoreStep.where(TASKS.ID.eq(id)).execute();
    }
}

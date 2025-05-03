package com.onyxdb.platform.mdb.scheduling.tasks;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.generated.jooq.tables.records.TasksRecord;
import com.onyxdb.platform.generated.jooq.tables.records.TasksToBlockerTasksRecord;
import com.onyxdb.platform.mdb.scheduling.tasks.models.Task;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskFilter;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskStatus;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskWithBlockers;
import com.onyxdb.platform.mdb.utils.TimeUtils;

import static com.onyxdb.platform.generated.jooq.Tables.TASKS;
import static com.onyxdb.platform.generated.jooq.Tables.TASKS_TO_BLOCKER_TASKS;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class TaskPostgresRepository implements TaskRepository {
    private final DSLContext dslContext;
    private final TaskMapper taskMapper;

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
    public List<Task> getTasksToConsume(
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
                        .where(ct.STATUS.notEqual(taskMapper.taskStatusToJooqTaskStatus(TaskStatus.SUCCESS)))
                        .and(ctt.ID.eq(cttbt.TASK_ID))
        );

        return dslContext.select(ctt.fields())
                .from(ctt)
                .where(ctt.STATUS.eq(taskMapper.taskStatusToJooqTaskStatus(TaskStatus.SCHEDULED))
                        .and(ctt.SCHEDULED_AT.le(scheduledAt))
                        .and(noUnfinishedTasksCondition)
                )
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
    public void updateTask(Task task) {
        dslContext.update(TASKS)
                .set(task.toJooqClusterTasksRecord())
                .where(TASKS.ID.eq(task.id()))
                .execute();
    }

    @Override
    public void updateStatus(UUID id, TaskStatus status) {
        dslContext.update(TASKS)
                .set(
                        TASKS.STATUS,
                        com.onyxdb.platform.generated.jooq.enums.TaskStatus.valueOf(status.value())
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
                        com.onyxdb.platform.generated.jooq.enums.TaskStatus.lookupLiteral(status.value())
                );

        if (Objects.nonNull(attemptsLeft)) {
            updateSetMoreStep = updateSetMoreStep.set(TASKS.ATTEMPTS_LEFT, attemptsLeft);
        }
        if (Objects.nonNull(scheduledAt)) {
            updateSetMoreStep = updateSetMoreStep.set(TASKS.CREATED_AT, scheduledAt);
        }
        updateSetMoreStep.where(TASKS.ID.eq(id)).execute();
    }

    @Override
    public List<Task> listTasks(TaskFilter filter) {
        return dslContext.select()
                .from(TASKS)
                .where(filter.buildCondition())
                .fetch(r -> Task.fromJooqClusterTasksRecord(r.into(TasksRecord.class)));
    }

    @Override
    public List<TaskWithBlockers> listTasksWithBlockers(TaskFilter filter) {
        List<TaskWithBlockers> tasksWithBlockers = dslContext.select()
                .from(TASKS)
                .leftJoin(TASKS_TO_BLOCKER_TASKS)
                .on(TASKS.ID.eq(TASKS_TO_BLOCKER_TASKS.TASK_ID))
                .where(filter.buildCondition())
                .fetch(r -> {
                    @Nullable
                    UUID blockerId = r.get(TASKS_TO_BLOCKER_TASKS.BLOCKER_TASK_ID);
                    return new TaskWithBlockers(
                            Task.fromJooqClusterTasksRecord(r.into(TasksRecord.class)),
                            blockerId == null? List.of() : List.of(blockerId)
                    );
                });

        Map<UUID, TaskWithBlockers> taskToTaskWithBlockers = new HashMap<>();
        for (TaskWithBlockers taskWithBlockers : tasksWithBlockers) {
            UUID taskId = taskWithBlockers.task().id();
            if (!taskToTaskWithBlockers.containsKey(taskId)) {
                taskToTaskWithBlockers.put(taskId, taskWithBlockers);
            } else{
                taskWithBlockers.blockerIds().add(taskId);
                taskToTaskWithBlockers.put(taskId, taskWithBlockers);
            }
        }

        return tasksWithBlockers;
    }

    @Override
    public void updateTasks(List<Task> tasks) {
        var records = tasks.stream().map(Task::toJooqClusterTasksRecord).toList();
        dslContext.batchUpdate(records).execute();
    }

    @Override
    public void startTask(UUID taskId, TaskStatus taskStatus) {
        dslContext.update(TASKS)
                .set(TASKS.STATUS, taskMapper.taskStatusToJooqTaskStatus(taskStatus))
                .set(TASKS.STARTED_AT, TimeUtils.now())
                .setNull(TASKS.FINISHED_AT)
                .where(TASKS.ID.eq(taskId))
                .execute();
    }

    @Override
    public void finishTask(UUID taskId, TaskStatus taskStatus) {
        dslContext.update(TASKS)
                .set(TASKS.STATUS, taskMapper.taskStatusToJooqTaskStatus(taskStatus))
                .set(TASKS.FINISHED_AT, TimeUtils.now())
                .where(TASKS.ID.eq(taskId))
                .execute();
    }
}

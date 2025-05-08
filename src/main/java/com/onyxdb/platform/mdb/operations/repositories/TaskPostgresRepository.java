package com.onyxdb.platform.mdb.operations.repositories;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.generated.jooq.tables.Tasks;
import com.onyxdb.platform.generated.jooq.tables.records.TasksRecord;
import com.onyxdb.platform.mdb.operations.mappers.TaskMapper;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskFilter;
import com.onyxdb.platform.mdb.operations.models.TaskStatus;

import static com.onyxdb.platform.generated.jooq.Tables.TASKS;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class TaskPostgresRepository implements TaskRepository {
    private final DSLContext dslContext;
    private final TaskMapper taskMapper;

    @Override
    public void createTasks(List<Task> tasks) {
        var records = tasks.stream()
                .map(taskMapper::taskToTasksRecord)
                .toList();

        dslContext.batchInsert(records).execute();
    }

    @Override
    public List<Task> getTasksToConsume(int limit) {
        Tasks blockers = TASKS.as("blocker");
        return dslContext.select()
                .from(TASKS)
                .where(TASKS.ATTEMPTS_LEFT.gt(0)
                        .and(TASKS.STATUS.eq(taskMapper.taskStatusToJooqTaskStatus(TaskStatus.SCHEDULED))
                                .or(TASKS.STATUS.eq(taskMapper.taskStatusToJooqTaskStatus(TaskStatus.RESCHEDULED))
                                        .and(DSL.localDateTimeAdd(TASKS.UPDATED_AT, TASKS.RETRY_DELAY_SECONDS, DatePart.SECOND)
                                                .lt(DSL.currentLocalDateTime()))
                                ))
                        .andNotExists(
                                DSL.selectOne()
                                        .from(blockers)
                                        .where(blockers.ID.eq(DSL.any(TASKS.BLOCKER_IDS)))
                                        .and(blockers.STATUS.ne(taskMapper.taskStatusToJooqTaskStatus(TaskStatus.SUCCESS))
                                                .or(blockers.FINISHED_AT.isNull())
                                                .or(DSL.localDateTimeAdd(blockers.FINISHED_AT, blockers.POST_DELAY_SECONDS, DatePart.SECOND)
                                                        .ge(DSL.currentLocalDateTime())
                                                )
                                        )
                        )
                )
                .limit(limit)
                .forUpdate()
                .skipLocked()
                .fetchMany()
                .stream()
                .map(result -> result.into(TasksRecord.class))
                .flatMap(List::stream)
                .map(taskMapper::tasksRecordToTask)
                .toList();
    }

    @Override
    public void updateTask(Task task) {
        dslContext.update(TASKS)
                .set(taskMapper.taskToTasksRecord(task))
                .where(TASKS.ID.eq(task.id()))
                .execute();
    }

    @Override
    public void updateTasks(List<Task> tasks) {
        dslContext.batchUpdate(tasks.stream().map(taskMapper::taskToTasksRecord).toList()).execute();
    }

    @Override
    public List<Task> listTasks(TaskFilter filter) {
        return dslContext.select()
                .from(TASKS)
                .where(filter.buildCondition())
                .fetchMany()
                .stream()
                .map(result -> result.into(TasksRecord.class))
                .flatMap(List::stream)
                .map(taskMapper::tasksRecordToTask)
                .toList();
    }
}

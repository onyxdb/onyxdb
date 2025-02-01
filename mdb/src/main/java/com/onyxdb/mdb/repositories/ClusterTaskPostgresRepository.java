package com.onyxdb.mdb.repositories;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.onyxdb.mdb.generated.jooq.tables.records.ClusterTasksRecord;
import com.onyxdb.mdb.generated.jooq.tables.records.ClusterTasksToBlockerTasksRecord;
import com.onyxdb.mdb.models.ClusterTask;
import com.onyxdb.mdb.models.ClusterTaskStatus;

import static com.onyxdb.mdb.generated.jooq.Tables.CLUSTER_TASKS;
import static com.onyxdb.mdb.generated.jooq.Tables.CLUSTER_TASKS_TO_BLOCKER_TASKS;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class ClusterTaskPostgresRepository implements ClusterTaskRepository {
    private final DSLContext dslContext;

    @Override
    public void createBulk(List<ClusterTask> tasks) {
        var records = tasks.stream()
                .map(ClusterTask::toJooqClusterTasksRecord)
                .toList();

        dslContext.batchInsert(records).execute();
    }

    @Override
    public void createBlockerTasksBulk(Map<UUID, List<UUID>> taskIdToBlockingTaskIds) {
        var records = taskIdToBlockingTaskIds.entrySet()
                .stream()
                .map(entry -> entry.getValue()
                        .stream()
                        .map(blockingTaskIds -> new ClusterTasksToBlockerTasksRecord(
                                entry.getKey(),
                                blockingTaskIds
                        ))
                        .toList()
                )
                .flatMap(List::stream)
                .toList();

        dslContext.batchInsert(records).execute();
    }

    @Override
    public List<ClusterTask> getTasksToProcess(int limit) {
        var ct = CLUSTER_TASKS.as("ct");
        var ctt = CLUSTER_TASKS.as("ctt");
        var cttbt = CLUSTER_TASKS_TO_BLOCKER_TASKS.as("cttbt");

        var noUnfinishedTasksCondition = DSL.notExists(
                DSL.selectOne().
                        from(cttbt)
                        .join(ct).on(ct.ID.eq(cttbt.BLOCKER_TASK_ID))
                        .where(ct.STATUS.in(
                                com.onyxdb.mdb.generated.jooq.enums.ClusterTaskStatus.scheduled,
                                com.onyxdb.mdb.generated.jooq.enums.ClusterTaskStatus.in_progress
                        ))
                        .and(ctt.ID.eq(cttbt.TASK_ID))
        );

        return dslContext.select(ctt.fields())
                .from(ctt)
                .where(ctt.STATUS.eq(com.onyxdb.mdb.generated.jooq.enums.ClusterTaskStatus.scheduled))
                .and(noUnfinishedTasksCondition)
                .limit(limit)
                .forUpdate()
                .skipLocked()
                .fetchMany()
                .stream()
                .map(result -> result.into(ClusterTasksRecord.class))
                .flatMap(List::stream)
                .map(ClusterTask::fromJooqClusterTasksRecord)
                .toList();
    }

    @Override
    public void updateStatus(UUID id, ClusterTaskStatus status) {
        dslContext.update(CLUSTER_TASKS)
                .set(
                        CLUSTER_TASKS.STATUS,
                        com.onyxdb.mdb.generated.jooq.enums.ClusterTaskStatus.valueOf(status.value())
                )
                .where(CLUSTER_TASKS.ID.eq(id))
                .execute();
    }
}

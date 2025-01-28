package com.onyxdb.mdb.repositories;

import java.util.List;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.onyxdb.mdb.common.MemoriousTransactionTemplate;
import com.onyxdb.mdb.models.ClusterTask;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class ClusterTaskPostgresRepository implements ClusterTaskRepository {
    private final DSLContext dslContext;
    private final MemoriousTransactionTemplate memoriousTransactionTemplate;

    @Override
    public void createBulk(List<ClusterTask> clusterTasks) {
        var records = clusterTasks.stream()
                .map(ClusterTask::toJooqClusterTasksRecord)
                .toList();

        memoriousTransactionTemplate.execute(
                new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                        dslContext.batchInsert(records).execute();
                        addBlockingTasks(clusterTasks);
                    }
                },
                TransactionDefinition.PROPAGATION_REQUIRED
        );
    }

    private void addBlockingTasks(List<ClusterTask> clusterTasks) {
        var records = clusterTasks.stream()
                .map(ClusterTask::toJooqClusterTasksToBlockingTasksRecords)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        dslContext.batchInsert(records).execute();
    }

    @Override
    public List<ClusterTask> getTasksToProcess(int limit) {
//        var r = dslContext.select(CLUSTER_TASKS.fields())
//                .from(CLUSTER_TASKS
//                .join(CLUSTER_TASKS_TO_BLOCKING_TASKS).on(CLUSTER_TASKS.ID.eq(CLUSTER_TASKS.ID));

//        var subQuery = selectOne()
//                .from(unnest(CLUSTER_TASKS).as("dep_id"))
//                .join(CLUSTER_TASKS_TABLE)
//                .on(field(name("dep_id")).eq(CLUSTER_TASKS_TABLE.ID))
//                .where(CLUSTER_TASKS_TABLE.STATUS.notIn(ClusterTaskStatus.failed, ClusterTaskStatus.done));
//        var r = dslContext
//                .select(CLUSTER_TASKS_TABLE.fields())
//                .from(CLUSTER_TASKS_TABLE)
//                .where(CLUSTER_TASKS_TABLE.STATUS.eq(ClusterTaskStatus.valueOf("scheduled"))
//                        .and(notExists(subQuery))
//                )
//                .orderBy(CLUSTER_TASKS_TABLE.SCHEDULED_AT.asc())
//                .limit(1)
//                .forUpdate().skipLocked()
//                .fetchOne();
//
//        if (r != null) {
//            var rr = r.into(ClusterTasksRecord.class);
//            System.err.println(rr.getClusterId() + " " + rr.getId());
//        } else {
//            System.err.println("{}");
//        }

//        return dslContext.select(
//
//                )
//                .from(CLUSTER_TASKS_TABLE)
//                .where(CLUSTER_TASKS_TABLE.STATUS.eq(ClusterTaskStatus.scheduled))
//                .andNotExists(select()
//                        .from(unnest(CLUSTER_TASKS_TABLE.DEPENDS_ON_TASK_IDS).as("dep_id"))
//                        .join(CLUSTER_TASKS_TABLE).on(CLUSTER_TASKS_TABLE.ID).eq)
        return List.of();
    }

    // SELECT t.*
    //FROM cluster_tasks t
    //WHERE t.status = 'scheduled'
    //  AND NOT EXISTS (SELECT 1
    //                  FROM UNNEST(t.depends_on_task_ids) dep_id
    //                           JOIN cluster_tasks dep_task ON dep_task.id = dep_id
    //                  WHERE dep_task.status not in ('failed', 'done'))
    //-- ORDER BY t.created_at
    //ORDER BY scheduled_at
    //    FOR UPDATE SKIP LOCKED
    //LIMIT 1;
}

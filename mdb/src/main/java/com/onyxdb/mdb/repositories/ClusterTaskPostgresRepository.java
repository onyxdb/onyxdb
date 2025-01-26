package com.onyxdb.mdb.repositories;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.mdb.generated.jooq.tables.ClusterTasks;
import com.onyxdb.mdb.models.ClusterTask;

import static com.onyxdb.mdb.generated.jooq.Tables.CLUSTER_TASKS;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class ClusterTaskPostgresRepository implements ClusterTaskRepository {
    private static final ClusterTasks CLUSTER_TASKS_TABLE = CLUSTER_TASKS;

    private final DSLContext dslContext;

    @Override
    public void createBulk(List<ClusterTask> clusterTasks) {
        var records = clusterTasks.stream()
                .map(ClusterTask::toJooqClusterTasksRecord)
                .toList();

        dslContext.insertInto(
                        CLUSTER_TASKS_TABLE,
                        CLUSTER_TASKS_TABLE.ID,
                        CLUSTER_TASKS_TABLE.CLUSTER_ID,
                        CLUSTER_TASKS_TABLE.OPERATION_ID,
                        CLUSTER_TASKS_TABLE.CLUSTER_TYPE,
                        CLUSTER_TASKS_TABLE.TYPE,
                        CLUSTER_TASKS_TABLE.STATUS,
                        CLUSTER_TASKS_TABLE.CREATED_AT,
                        CLUSTER_TASKS_TABLE.UPDATED_AT,
                        CLUSTER_TASKS_TABLE.SCHEDULED_AT,
                        CLUSTER_TASKS_TABLE.RETRIES_LEFT,
                        CLUSTER_TASKS_TABLE.DEPENDS_ON_TASK_IDS,
                        CLUSTER_TASKS_TABLE.IS_LAST
                )
                .valuesOfRecords(records)
                .execute();
    }
}

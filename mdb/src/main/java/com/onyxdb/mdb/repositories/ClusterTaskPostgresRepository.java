package com.onyxdb.mdb.repositories;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.mdb.generated.jooq.tables.ClusterTasks;
import com.onyxdb.mdb.models.ClusterTask;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class ClusterTaskPostgresRepository implements ClusterTaskRepository {
    private final DSLContext dslContext;

    @Override
    public void createBulk(List<ClusterTask> clusterTasks) {
        var t = ClusterTasks.CLUSTER_TASKS;

//        var records = clusterTasks.stream().map(ClusterTask::)
//
//        dslContext.insertInto(
//                t,
//                t.ID,
//                t.CLUSTER_ID,
//                t.OPERATION_ID,
//                t.CLUSTER_TYPE,
//                t.TYPE,
//                t.STATUS,
//                t.CREATED_AT,
//                t.UPDATED_AT,
//                t.SCHEDULED_AT,
//                t.IS_LAST
//        )
//                .valuesOfRecords()
//        ;
    }
}

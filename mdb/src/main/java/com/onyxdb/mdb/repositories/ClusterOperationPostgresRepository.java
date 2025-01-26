package com.onyxdb.mdb.repositories;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.mdb.models.ClusterOperation;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class ClusterOperationPostgresRepository implements ClusterOperationRepository {
    private final DSLContext dslContext;

    @Override
    public void create(ClusterOperation operation) {
        var t = com.onyxdb.mdb.generated.jooq.tables.ClusterOperations.CLUSTER_OPERATIONS;
        dslContext.insertInto(
                        t,
                        t.ID,
                        t.CLUSTER_ID,
                        t.TYPE,
                        t.STATUS,
                        t.CREATED_AT,
                        t.CREATED_BY,
                        t.UPDATED_AT
                )
                .valuesOfRecords(operation.toJooqClusterOperationsRecord())
                .execute();
    }
}

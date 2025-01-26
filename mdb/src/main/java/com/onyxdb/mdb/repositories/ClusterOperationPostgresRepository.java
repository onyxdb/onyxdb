package com.onyxdb.mdb.repositories;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.mdb.generated.jooq.tables.ClusterOperations;
import com.onyxdb.mdb.models.ClusterOperation;

import static com.onyxdb.mdb.generated.jooq.Tables.CLUSTER_OPERATIONS;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class ClusterOperationPostgresRepository implements ClusterOperationRepository {
    private static final ClusterOperations CLUSTER_OPERATIONS_TABLE = CLUSTER_OPERATIONS;

    private final DSLContext dslContext;

    @Override
    public void create(ClusterOperation operation) {
        dslContext.insertInto(
                        CLUSTER_OPERATIONS_TABLE,
                        CLUSTER_OPERATIONS_TABLE.ID,
                        CLUSTER_OPERATIONS_TABLE.CLUSTER_ID,
                        CLUSTER_OPERATIONS_TABLE.TYPE,
                        CLUSTER_OPERATIONS_TABLE.STATUS,
                        CLUSTER_OPERATIONS_TABLE.CREATED_AT,
                        CLUSTER_OPERATIONS_TABLE.CREATED_BY,
                        CLUSTER_OPERATIONS_TABLE.UPDATED_AT
                )
                .valuesOfRecords(operation.toJooqClusterOperationsRecord())
                .execute();
    }
}

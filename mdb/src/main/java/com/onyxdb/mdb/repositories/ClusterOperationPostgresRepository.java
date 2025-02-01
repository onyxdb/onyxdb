package com.onyxdb.mdb.repositories;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.mdb.models.ClusterOperation;
import com.onyxdb.mdb.models.ClusterOperationStatus;

import static com.onyxdb.mdb.generated.jooq.tables.ClusterOperations.CLUSTER_OPERATIONS;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class ClusterOperationPostgresRepository implements ClusterOperationRepository {
    private final DSLContext dslContext;

    @Override
    public void create(ClusterOperation operation) {
        dslContext.executeInsert(operation.toJooqClusterOperationsRecord());
    }

    @Override
    public void updateStatus(UUID id, ClusterOperationStatus status) {
        dslContext.update(CLUSTER_OPERATIONS)
                .set(
                        CLUSTER_OPERATIONS.STATUS,
                        com.onyxdb.mdb.generated.jooq.enums.ClusterOperationStatus.valueOf(status.value())
                )
                .where(CLUSTER_OPERATIONS.ID.eq(id))
                .execute();
    }
}

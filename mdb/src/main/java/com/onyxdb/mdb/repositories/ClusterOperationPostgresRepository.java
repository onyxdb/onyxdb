package com.onyxdb.mdb.repositories;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.mdb.core.clusters.models.ClusterOperation;
import com.onyxdb.mdb.core.clusters.models.ClusterOperationStatus;

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
        dslContext.insertInto(CLUSTER_OPERATIONS)
                .columns(
                        CLUSTER_OPERATIONS.ID,
                        CLUSTER_OPERATIONS.CLUSTER_ID,
                        CLUSTER_OPERATIONS.TYPE,
                        CLUSTER_OPERATIONS.STATUS,
                        CLUSTER_OPERATIONS.CREATED_AT
                )
                .values(
                        operation.id(),
                        operation.clusterId(),
                        com.onyxdb.mdb.generated.jooq.enums.ClusterOperationType.valueOf(operation.type().value()),
                        com.onyxdb.mdb.generated.jooq.enums.ClusterOperationStatus.valueOf(operation.status().value()),
                        operation.createdAt()
                )
                .execute();
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

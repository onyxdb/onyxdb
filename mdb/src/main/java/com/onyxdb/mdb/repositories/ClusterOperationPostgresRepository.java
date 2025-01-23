package com.onyxdb.mdb.repositories;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.mdb.generated.jooq.tables.records.ClusterOperationRecord;
import com.onyxdb.mdb.models.ClusterOperation;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class ClusterOperationPostgresRepository implements ClusterOperationRepository {
    private final DSLContext dslContext;

    @Override
    public void createBulk(List<ClusterOperation> clusterOperations) {
        var clusterOperationTable = com.onyxdb.mdb.generated.jooq.tables.ClusterOperation.CLUSTER_OPERATION;

        List<ClusterOperationRecord> records = clusterOperations.stream()
                .map(ClusterOperation::toJooqClusterOperationRecord)
                .toList();

        dslContext.insertInto(
                        clusterOperationTable,
                        clusterOperationTable.ID,
                        clusterOperationTable.CLUSTER_ID,
                        clusterOperationTable.TYPE,
                        clusterOperationTable.STATUS,
                        clusterOperationTable.CREATED_AT,
                        clusterOperationTable.RETRIES,
                        clusterOperationTable.EXECUTE_AT
                )
                .valuesOfRecords(records)
                .execute();
    }
}

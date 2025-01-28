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
        dslContext.executeInsert(operation.toJooqClusterOperationsRecord());
    }
}

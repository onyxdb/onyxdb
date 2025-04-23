package com.onyxdb.platform.taskProcessing.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.operations.OperationMapper;
import com.onyxdb.platform.taskProcessing.models.Operation;
import com.onyxdb.platform.taskProcessing.models.OperationStatus;
import com.onyxdb.platform.utils.TimeUtils;

import static com.onyxdb.platform.generated.jooq.tables.Operations.OPERATIONS;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class OperationPostgresRepository implements OperationRepository {
    private final DSLContext dslContext;
    private final OperationMapper operationMapper;

    @Override
    public void create(Operation operation) {
        dslContext.insertInto(OPERATIONS)
                .set(operationMapper.toOperationsRecord(operation))
                .execute();
    }

    @Override
    public void updateStatus(UUID id, OperationStatus status) {
        dslContext.update(OPERATIONS)
                .set(
                        OPERATIONS.STATUS,
                        status.value()
                )
                .set(OPERATIONS.UPDATED_AT, TimeUtils.now())
                .where(OPERATIONS.ID.eq(id))
                .execute();
    }

    @Override
    public List<Operation> listOperations(UUID clusterId) {
        return dslContext.select()
                .from(OPERATIONS)
                .where(OPERATIONS.CLUSTER_ID.eq(clusterId))
                .orderBy(OPERATIONS.CREATED_AT.desc())
                .fetch(operationMapper::fromJooqRecord);
    }

    @Override
    public Optional<Operation> getForUpdateO(UUID operationId) {
        return dslContext.select()
                .from(OPERATIONS)
                .where(OPERATIONS.ID.eq(operationId))
                .forUpdate()
                .fetchOptional(operationMapper::fromJooqRecord);
    }
}

package com.onyxdb.platform.mdb.scheduling.operations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.mdb.scheduling.operations.models.Operation;
import com.onyxdb.platform.mdb.scheduling.operations.models.OperationStatus;
import com.onyxdb.platform.mdb.utils.TimeUtils;

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
    public void createOperation(Operation operation) {
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
    public List<Operation> listOperations(@Nullable UUID clusterId, @Nullable OperationStatus status) {
        Condition condition = DSL.trueCondition();
        if (clusterId != null) {
            condition = condition.and(OPERATIONS.CLUSTER_ID.eq(clusterId));
        }
        if (status != null) {
            condition = condition.and(OPERATIONS.STATUS.eq(status.value()));
        }
        return dslContext.select()
                .from(OPERATIONS)
                .where(condition)
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

    @Override
    public Operation getOperation(UUID operationId) {
        return dslContext.select()
                .from(OPERATIONS)
                .where(OPERATIONS.ID.eq(operationId))
                .forUpdate()
                .fetchOptional(operationMapper::fromJooqRecord)
                .orElseThrow(() -> new RuntimeException("Operation not found"));
    }
}

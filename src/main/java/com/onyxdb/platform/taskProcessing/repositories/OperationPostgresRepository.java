package com.onyxdb.platform.taskProcessing.repositories;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.taskProcessing.models.Operation;
import com.onyxdb.platform.taskProcessing.models.OperationStatus;

import static com.onyxdb.platform.generated.jooq.tables.Operations.OPERATIONS;

/**
 * @author foxleren
 */
@Repository
@RequiredArgsConstructor
public class OperationPostgresRepository implements OperationRepository {
    private final DSLContext dslContext;

    @Override
    public void create(Operation operation) {
        dslContext.insertInto(OPERATIONS)
                .columns(
                        OPERATIONS.ID,
                        OPERATIONS.TYPE,
                        OPERATIONS.STATUS,
                        OPERATIONS.CREATED_AT
                )
                .values(
                        operation.id(),
                        com.onyxdb.platform.generated.jooq.enums.OperationType.valueOf(operation.type().value()),
                        com.onyxdb.platform.generated.jooq.enums.OperationStatus.valueOf(operation.status().value()),
                        operation.createdAt()
                )
                .execute();
    }

    @Override
    public void updateStatus(UUID id, OperationStatus status) {
        dslContext.update(OPERATIONS)
                .set(
                        OPERATIONS.STATUS,
                        com.onyxdb.platform.generated.jooq.enums.OperationStatus.valueOf(status.value())
                )
                .where(OPERATIONS.ID.eq(id))
                .execute();
    }
}

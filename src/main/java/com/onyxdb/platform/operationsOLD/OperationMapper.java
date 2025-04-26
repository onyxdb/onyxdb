package com.onyxdb.platform.operationsOLD;

import java.util.List;

import org.jooq.JSONB;
import org.jooq.Record;

import com.onyxdb.platform.generated.jooq.tables.records.OperationsRecord;
import com.onyxdb.platform.generated.openapi.models.ListOperationsResponseOA;
import com.onyxdb.platform.generated.openapi.models.OperationOA;
import com.onyxdb.platform.generated.openapi.models.OperationStatusOA;
import com.onyxdb.platform.generated.openapi.models.OperationTypeOA;
import com.onyxdb.platform.processing.models.Operation;
import com.onyxdb.platform.processing.models.OperationStatus;
import com.onyxdb.platform.processing.models.OperationType;

import static com.onyxdb.platform.generated.jooq.tables.Operations.OPERATIONS;

public class OperationMapper {
    public OperationsRecord toOperationsRecord(Operation o) {
        return new OperationsRecord(
                o.id(),
                o.type().value(),
                o.status().value(),
                o.createdAt(),
                o.createdBy(),
                o.updatedAt(),
                JSONB.valueOf(o.payload()),
                o.clusterId()
        );
    }

    public Operation fromJooqRecord(Record r) {
        return new Operation(
                r.get(OPERATIONS.ID),
                OperationType.R.fromValue(r.get(OPERATIONS.TYPE)),
                OperationStatus.R.fromValue(r.get(OPERATIONS.STATUS)),
                r.get(OPERATIONS.CREATED_AT),
                r.get(OPERATIONS.CREATED_BY),
                r.get(OPERATIONS.UPDATED_AT),
                r.get(OPERATIONS.PAYLOAD).data(),
                r.get(OPERATIONS.CLUSTER_ID)
        );
    }

    public ListOperationsResponseOA toListOperationsResponseOA(List<Operation> os) {
        List<OperationOA> mappedOperations = os.stream().map(this::toOperationOA).toList();
        return new ListOperationsResponseOA(mappedOperations);
    }

    public OperationOA toOperationOA(Operation o) {
        return new OperationOA(
                o.id(),
                toOperationTypeOA(o.type()),
                toOperationStatusOA(o.status()),
                o.createdAt(),
                o.createdBy(),
                o.updatedAt(),
                o.isRestartAllowed()
        );
    }

    public OperationTypeOA toOperationTypeOA(OperationType t) {
        return new OperationTypeOA(
                t.value(),
                t.getDisplayValue()
        );
    }

    public OperationStatusOA toOperationStatusOA(OperationStatus s) {
        return new OperationStatusOA(
                s.value(),
                s.getDisplayValue()
        );
    }
}

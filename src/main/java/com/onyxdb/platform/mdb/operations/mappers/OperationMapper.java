package com.onyxdb.platform.mdb.operations.mappers;

import java.util.List;

import org.jooq.JSONB;
import org.jooq.Record;

import com.onyxdb.platform.generated.jooq.tables.records.OperationsRecord;
import com.onyxdb.platform.generated.openapi.models.ListOperationsResponseDTO;
import com.onyxdb.platform.generated.openapi.models.OperationDTO;
import com.onyxdb.platform.generated.openapi.models.OperationStatusDTO;
import com.onyxdb.platform.generated.openapi.models.OperationTypeDTO;
import com.onyxdb.platform.mdb.operations.models.Operation;
import com.onyxdb.platform.mdb.operations.models.OperationStatus;
import com.onyxdb.platform.mdb.operations.models.OperationType;

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

    public ListOperationsResponseDTO toListOperationsResponseDTO(List<Operation> os) {
        List<OperationDTO> mappedOperations = os.stream().map(this::toOperationOA).toList();
        return new ListOperationsResponseDTO(mappedOperations);
    }

    public OperationDTO toOperationOA(Operation o) {
        return new OperationDTO(
                o.id(),
                toOperationTypeDTO(o.type()),
                toOperationStatusDTO(o.status()),
                o.createdAt(),
                o.createdBy(),
                o.updatedAt(),
                o.isRestartAllowed()
        );
    }

    public OperationTypeDTO toOperationTypeDTO(OperationType t) {
        return new OperationTypeDTO(
                t.value(),
                t.getDisplayValue()
        );
    }

    public OperationStatusDTO toOperationStatusDTO(OperationStatus s) {
        return new OperationStatusDTO(
                s.value(),
                s.getDisplayValue()
        );
    }
}

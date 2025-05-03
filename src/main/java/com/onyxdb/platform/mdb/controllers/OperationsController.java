package com.onyxdb.platform.mdb.controllers;


import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.OperationsApi;
import com.onyxdb.platform.generated.openapi.models.ListOperationsResponseOA;
import com.onyxdb.platform.mdb.exceptions.BadRequestException;
import com.onyxdb.platform.mdb.scheduling.operations.OperationMapper;
import com.onyxdb.platform.mdb.scheduling.operations.OperationService;
import com.onyxdb.platform.mdb.scheduling.operations.models.Operation;

@RestController
public class OperationsController implements OperationsApi {
    private final OperationService operationService;
    private final OperationMapper operationMapper;

    public OperationsController(
            OperationService operationService,
            OperationMapper operationMapper
    ) {
        this.operationService = operationService;
        this.operationMapper = operationMapper;
    }

    @Override
    public ResponseEntity<ListOperationsResponseOA> listOperations(
            @Nullable
            UUID clusterId
    ) {
        if (clusterId == null) {
            throw new BadRequestException("Nullable id will be supported later");
        }

        List<Operation> operations = operationService.listOperations(clusterId);
        return ResponseEntity.ok(operationMapper.toListOperationsResponseOA(operations));
    }

    @Override
    public ResponseEntity<Void> restartOperation(UUID operationId) {
        operationService.restartOperation(operationId);
        return ResponseEntity.ok().build();
    }
}

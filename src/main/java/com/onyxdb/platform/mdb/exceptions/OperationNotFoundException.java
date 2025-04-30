package com.onyxdb.platform.mdb.exceptions;

import java.util.UUID;

public class OperationNotFoundException extends BadRequestException {
    public OperationNotFoundException(String message) {
        super(message);
    }

    public OperationNotFoundException(UUID operationId) {
        super(String.format("Operation with id '%s' is not found", operationId));
    }
}

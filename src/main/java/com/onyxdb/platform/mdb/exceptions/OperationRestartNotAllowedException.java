package com.onyxdb.platform.mdb.exceptions;

import java.util.UUID;

public class OperationRestartNotAllowedException extends BadRequestException {
    public OperationRestartNotAllowedException(String message) {
        super(message);
    }

    public OperationRestartNotAllowedException(UUID operationId) {
        super(String.format("Operation with id '%s' is not allowed to restart", operationId));
    }
}

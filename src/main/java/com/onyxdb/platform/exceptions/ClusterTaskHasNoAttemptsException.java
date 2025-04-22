package com.onyxdb.platform.exceptions;

import java.util.UUID;

public class ClusterTaskHasNoAttemptsException extends RuntimeException {
    public ClusterTaskHasNoAttemptsException(UUID taskId) {
        super(String.format("Cluster task with id=%s has no attempts", taskId));
    }
}

package com.onyxdb.platform.mdb.exceptions;

import java.util.UUID;

/**
 * @author foxleren
 */
public class ClusterNotFoundException extends NotFoundException {
    public ClusterNotFoundException(String message) {
        super(message);
    }

    public ClusterNotFoundException(UUID id) {
        super(String.format("Cluster with '%s' is not found", id));
    }
}

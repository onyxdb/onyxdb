package com.onyxdb.platform.exceptions;

import java.util.UUID;

/**
 * @author foxleren
 */
public class ClusterNotFoundException extends BadRequestException {
    public ClusterNotFoundException(String message) {
        super(message);
    }

    public ClusterNotFoundException(UUID id) {
        super(String.format("Cluster is not found; id=%s", id));
    }
}

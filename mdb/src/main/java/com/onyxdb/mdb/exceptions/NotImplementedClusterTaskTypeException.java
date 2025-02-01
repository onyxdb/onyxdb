package com.onyxdb.mdb.exceptions;

import com.onyxdb.mdb.models.ClusterTaskType;

/**
 * @author foxleren
 */
public class NotImplementedClusterTaskTypeException extends BadRequestException {
    public NotImplementedClusterTaskTypeException(ClusterTaskType taskType) {
        super(String.format("Unsupported cluster task type: %s", taskType.value()));
    }
}

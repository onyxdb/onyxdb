package com.onyxdb.platform.exceptions;

import com.onyxdb.platform.processing.models.TaskType;

public class ClusterTaskNotFoundException extends RuntimeException {
    public ClusterTaskNotFoundException(TaskType taskType) {
        super(String.format(
                "Can't find task with type %s", taskType.value()
        ));
    }
}

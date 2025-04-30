package com.onyxdb.platform.mdb.exceptions;

import com.onyxdb.platform.mdb.processing.models.TaskType;

public class ClusterTaskNotFoundException extends RuntimeException {
    public ClusterTaskNotFoundException(TaskType taskType) {
        super(String.format(
                "Can't find task with type %s", taskType.value()
        ));
    }
}

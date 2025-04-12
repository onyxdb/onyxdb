package com.onyxdb.mdb.exceptions;

import com.onyxdb.mdb.taskProcessing.models.TaskType;

public class ClusterTaskNotFoundException extends RuntimeException {
    public ClusterTaskNotFoundException(TaskType taskType) {
        super(String.format(
                "Can't find task with type %s", taskType.value()
        ));
    }
}

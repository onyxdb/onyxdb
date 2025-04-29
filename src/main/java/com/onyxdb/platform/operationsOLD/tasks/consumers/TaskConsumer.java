package com.onyxdb.platform.operationsOLD.tasks.consumers;

import com.onyxdb.platform.processing.models.TaskType;

public abstract class TaskConsumer {
    public abstract TaskType getTaskType();

//    public abstract process(Operation<>)
}

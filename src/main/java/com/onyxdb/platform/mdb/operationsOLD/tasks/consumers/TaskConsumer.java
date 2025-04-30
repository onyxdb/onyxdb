package com.onyxdb.platform.mdb.operationsOLD.tasks.consumers;

import com.onyxdb.platform.mdb.processing.models.TaskType;

public abstract class TaskConsumer {
    public abstract TaskType getTaskType();

//    public abstract process(Operation<>)
}

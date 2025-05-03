package com.onyxdb.platform.mdb.scheduling.tasks.producers;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.scheduling.operations.models.Operation;
import com.onyxdb.platform.mdb.scheduling.tasks.models.ProducedTask;

public abstract class TaskProducer<PAYLOAD> {
    protected final ObjectMapper objectMapper;

    protected TaskProducer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

//    public abstract List<TaskType> getTaskTypesInOrder();

    public abstract PAYLOAD parsePayload(String payload);

    public abstract List<ProducedTask> produceTasks(Operation operation, PAYLOAD payload);

    public List<ProducedTask> produceTasks(Operation operation, String payload) {
        return produceTasks(operation, parsePayload(payload));
    }
}

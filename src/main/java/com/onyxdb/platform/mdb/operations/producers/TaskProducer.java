package com.onyxdb.platform.mdb.operations.producers;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.operations.models.Operation;
import com.onyxdb.platform.mdb.operations.models.ProducedTask;

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

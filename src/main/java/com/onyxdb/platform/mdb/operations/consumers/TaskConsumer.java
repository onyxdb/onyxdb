package com.onyxdb.platform.mdb.scheduling.tasks.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.scheduling.operations.models.payloads.Payload;
import com.onyxdb.platform.mdb.scheduling.tasks.models.Task;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskResult;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;

public abstract class TaskConsumer<PAYLOAD extends Payload> {
    protected final ObjectMapper objectMapper;
    protected final ClusterService clusterService;

    protected TaskConsumer(ObjectMapper objectMapper, ClusterService clusterService) {
        this.objectMapper = objectMapper;
        this.clusterService = clusterService;
    }

    public abstract TaskType getTaskType();

    protected abstract TaskResult internalProcess(Task task, PAYLOAD payload);

    protected abstract PAYLOAD parsePayload(Task task) throws JsonProcessingException;

    public TaskResult process(Task task) {
        try {
            return internalProcess(task, parsePayload(task));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

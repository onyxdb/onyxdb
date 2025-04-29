package com.onyxdb.platform.processing.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.processing.models.Task;
import com.onyxdb.platform.processing.models.TaskProcessingResult;
import com.onyxdb.platform.processing.models.TaskType;
import com.onyxdb.platform.processing.models.payloads.Payload;

public abstract class TaskProcessor<PAYLOAD extends Payload> {
    protected final ObjectMapper objectMapper;
    protected final ClusterService clusterService;

    protected TaskProcessor(ObjectMapper objectMapper, ClusterService clusterService) {
        this.objectMapper = objectMapper;
        this.clusterService = clusterService;
    }

    public abstract TaskType getTaskType();

    public TaskProcessingResult process(Task task) {
        try {
            return internalProcess(task, parsePayload(task));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract TaskProcessingResult internalProcess(Task task, PAYLOAD payload);

    protected abstract PAYLOAD parsePayload(Task task) throws JsonProcessingException;
}

package com.onyxdb.platform.taskProcessing.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.core.clusters.ClusterService;
import com.onyxdb.platform.taskProcessing.models.Task;
import com.onyxdb.platform.taskProcessing.models.TaskProcessingResult;
import com.onyxdb.platform.taskProcessing.models.TaskType;
import com.onyxdb.platform.taskProcessing.models.payloads.TaskPayload;

public abstract class TaskProcessor<PAYLOAD extends TaskPayload> {
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

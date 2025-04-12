package com.onyxdb.mdb.taskProcessing.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.payloads.ClusterTaskPayload;

public abstract class ClusterTaskProcessor extends TaskProcessor<ClusterTaskPayload> {
    protected ClusterTaskProcessor(ObjectMapper objectMapper, ClusterService clusterService) {
        super(objectMapper, clusterService);
    }

    @Override
    protected ClusterTaskPayload parsePayload(Task task) {
        try {
            return objectMapper.readValue(task.payload(), ClusterTaskPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

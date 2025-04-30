package com.onyxdb.platform.mdb.processing.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.processing.consumers.TaskProcessor;
import com.onyxdb.platform.mdb.processing.models.OperationStatus;
import com.onyxdb.platform.mdb.processing.models.Task;
import com.onyxdb.platform.mdb.processing.models.TaskProcessingResult;
import com.onyxdb.platform.mdb.processing.models.TaskType;
import com.onyxdb.platform.mdb.processing.models.payloads.EmptyPayload;
import com.onyxdb.platform.mdb.processing.repositories.OperationRepository;

@Component
public class FinalTaskConsumer extends TaskProcessor<EmptyPayload> {
    private final OperationRepository operationRepository;

    public FinalTaskConsumer(
            ObjectMapper objectMapper,
            ClusterService clusterService, OperationRepository operationRepository
    ) {
        super(objectMapper, clusterService);
        this.operationRepository = operationRepository;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.MONGO_CREATE_DATABASE;
    }

    @Override
    protected TaskProcessingResult internalProcess(Task task, EmptyPayload payload) {
        operationRepository.updateStatus(task.operationId(), OperationStatus.SUCCESS);

        return TaskProcessingResult.success();
    }

    @Override
    protected EmptyPayload parsePayload(Task task) throws JsonProcessingException {
        try {
            return objectMapper.readValue(task.payload(), EmptyPayload.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

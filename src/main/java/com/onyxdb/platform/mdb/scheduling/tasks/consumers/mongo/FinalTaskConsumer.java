package com.onyxdb.platform.mdb.scheduling.tasks.consumers.mongo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.scheduling.tasks.consumers.TaskConsumer;
import com.onyxdb.platform.mdb.scheduling.operations.models.OperationStatus;
import com.onyxdb.platform.mdb.scheduling.tasks.models.Task;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskResult;
import com.onyxdb.platform.mdb.scheduling.tasks.models.TaskType;
import com.onyxdb.platform.mdb.scheduling.operations.models.payloads.EmptyPayload;
import com.onyxdb.platform.mdb.scheduling.operations.OperationRepository;

@Component
public class FinalTaskConsumer extends TaskConsumer<EmptyPayload> {
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
    protected TaskResult internalProcess(Task task, EmptyPayload payload) {
        operationRepository.updateStatus(task.operationId(), OperationStatus.SUCCESS);

        return TaskResult.success();
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

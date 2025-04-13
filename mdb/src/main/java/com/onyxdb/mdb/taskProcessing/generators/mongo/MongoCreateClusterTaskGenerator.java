package com.onyxdb.mdb.taskProcessing.generators.mongo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.mdb.taskProcessing.generators.ClusterTaskGenerator;
import com.onyxdb.mdb.taskProcessing.models.OperationType;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskType;
import com.onyxdb.mdb.taskProcessing.models.TaskWithBlockers;
import com.onyxdb.mdb.taskProcessing.models.payloads.TaskPayload;

public class MongoCreateClusterTaskGenerator extends ClusterTaskGenerator {
    // TODO don't repeat blockers for each task
    private static final Map<TaskType, List<TaskType>> TASK_TYPE_TO_BLOCKER_TASK_TYPES = Map.ofEntries(
            Map.entry(
                    TaskType.MONGODB_CREATE_VECTOR_CONFIG, List.of()
            ),
            Map.entry(
                    TaskType.MONGODB_CREATE_CLUSTER, List.of(
                            TaskType.MONGODB_CREATE_VECTOR_CONFIG
                    )
            ),
            Map.entry(
                    TaskType.MONGODB_CHECK_CLUSTER_READINESS, List.of(
                            TaskType.MONGODB_CREATE_VECTOR_CONFIG,
                            TaskType.MONGODB_CREATE_CLUSTER
                    )
            ),
            Map.entry(
                    TaskType.MONGODB_CREATE_EXPORTER_SERVICE, List.of(
                            TaskType.MONGODB_CREATE_VECTOR_CONFIG,
                            TaskType.MONGODB_CREATE_CLUSTER,
                            TaskType.MONGODB_CHECK_CLUSTER_READINESS
                    )
            ),
            Map.entry(
                    TaskType.MONGODB_CHECK_EXPORTER_SERVICE_READINESS, List.of(
                            TaskType.MONGODB_CREATE_VECTOR_CONFIG,
                            TaskType.MONGODB_CREATE_CLUSTER,
                            TaskType.MONGODB_CHECK_CLUSTER_READINESS,
                            TaskType.MONGODB_CREATE_EXPORTER_SERVICE
                    )
            ),
            Map.entry(
                    TaskType.MONGODB_CREATE_EXPORTER_SERVICE_SCRAPE, List.of(
                            TaskType.MONGODB_CREATE_VECTOR_CONFIG,
                            TaskType.MONGODB_CREATE_CLUSTER,
                            TaskType.MONGODB_CHECK_CLUSTER_READINESS,
                            TaskType.MONGODB_CREATE_EXPORTER_SERVICE,
                            TaskType.MONGODB_CHECK_EXPORTER_SERVICE_READINESS
                    )
            ),
            Map.entry(
                    TaskType.MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_READINESS, List.of(
                            TaskType.MONGODB_CREATE_VECTOR_CONFIG,
                            TaskType.MONGODB_CREATE_CLUSTER,
                            TaskType.MONGODB_CHECK_CLUSTER_READINESS,
                            TaskType.MONGODB_CREATE_EXPORTER_SERVICE,
                            TaskType.MONGODB_CHECK_EXPORTER_SERVICE_READINESS,
                            TaskType.MONGODB_CREATE_EXPORTER_SERVICE_SCRAPE
                    )
            )
    );

    public MongoCreateClusterTaskGenerator(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.MONGODB_CREATE_CLUSTER;
    }

    @Override
    public Map<TaskType, List<TaskType>> getTaskTypeToBlockerTaskTypes() {
        return TASK_TYPE_TO_BLOCKER_TASK_TYPES;
    }

    @Override
    public List<TaskWithBlockers> generateTasks(UUID operationId, TaskPayload payload) {
        // TODO create TimeService that will provide LocalDateTime with fixed timezone
        LocalDateTime now = LocalDateTime.now();
        String stringPayload = payloadToString(payload);

        List<Task> tasks = List.of(
                Task.scheduledFirst(
                        TaskType.MONGODB_CREATE_VECTOR_CONFIG,
                        operationId,
                        now,
                        DEFAULT_RETRIES_LEFT,
                        stringPayload
                ),
                Task.scheduledMiddle(
                        TaskType.MONGODB_CREATE_CLUSTER,
                        operationId,
                        now,
                        DEFAULT_RETRIES_LEFT,
                        stringPayload
                ),
                Task.scheduledMiddle(
                        TaskType.MONGODB_CHECK_CLUSTER_READINESS,
                        operationId,
                        now,
                        DEFAULT_RETRIES_LEFT,
                        stringPayload
                ),
                Task.scheduledMiddle(
                        TaskType.MONGODB_CREATE_EXPORTER_SERVICE,
                        operationId,
                        now,
                        DEFAULT_RETRIES_LEFT,
                        stringPayload
                ),
                Task.scheduledMiddle(
                        TaskType.MONGODB_CHECK_EXPORTER_SERVICE_READINESS,
                        operationId,
                        now,
                        DEFAULT_RETRIES_LEFT,
                        stringPayload
                ),
                Task.scheduledMiddle(
                        TaskType.MONGODB_CREATE_EXPORTER_SERVICE_SCRAPE,
                        operationId,
                        now,
                        DEFAULT_RETRIES_LEFT,
                        stringPayload
                ),
                Task.scheduledLast(
                        TaskType.MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_READINESS,
                        operationId,
                        now,
                        DEFAULT_RETRIES_LEFT,
                        stringPayload
                )
        );

        return aggregateToTasksWithBlockers(tasks);
    }
}

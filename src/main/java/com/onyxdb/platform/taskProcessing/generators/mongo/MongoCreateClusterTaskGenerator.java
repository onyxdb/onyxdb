package com.onyxdb.platform.taskProcessing.generators.mongo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.taskProcessing.generators.ClusterTaskGenerator;
import com.onyxdb.platform.taskProcessing.models.OperationType;
import com.onyxdb.platform.taskProcessing.models.Task;
import com.onyxdb.platform.taskProcessing.models.TaskType;
import com.onyxdb.platform.taskProcessing.models.TaskWithBlockers;
import com.onyxdb.platform.taskProcessing.models.payloads.TaskPayload;

public class MongoCreateClusterTaskGenerator extends ClusterTaskGenerator {
    // TODO don't repeat blockers for each task
    private static final Map<TaskType, List<TaskType>> TASK_TYPE_TO_BLOCKER_TASK_TYPES = Map.ofEntries(
            Map.entry(
                    TaskType.MONGODB_CREATE_VECTOR_CONFIG, List.of()
            ),
            Map.entry(
                    TaskType.MONGODB_APPLY_PSMDB, List.of(
                            TaskType.MONGODB_CREATE_VECTOR_CONFIG
                    )
            ),
            Map.entry(
                    TaskType.MONGODB_CHECK_PSMDB_READINESS, List.of(
                            TaskType.MONGODB_CREATE_VECTOR_CONFIG,
                            TaskType.MONGODB_APPLY_PSMDB
                    )
            ),
            Map.entry(
                    TaskType.MONGODB_APPLY_ONYXDB_AGENT, List.of(
                            TaskType.MONGODB_CREATE_VECTOR_CONFIG,
                            TaskType.MONGODB_APPLY_PSMDB,
                            TaskType.MONGODB_CHECK_PSMDB_READINESS
                    )
            ),
            Map.entry(
                    TaskType.MONGODB_CHECK_ONYXDB_AGENT_READINESS, List.of(
                            TaskType.MONGODB_CREATE_VECTOR_CONFIG,
                            TaskType.MONGODB_APPLY_PSMDB,
                            TaskType.MONGODB_CHECK_PSMDB_READINESS,
                            TaskType.MONGODB_APPLY_ONYXDB_AGENT
                    )
            ),
            Map.entry(
                    TaskType.MONGODB_CREATE_EXPORTER_SERVICE, List.of(
                            TaskType.MONGODB_CREATE_VECTOR_CONFIG,
                            TaskType.MONGODB_APPLY_PSMDB,
                            TaskType.MONGODB_CHECK_PSMDB_READINESS,
                            TaskType.MONGODB_APPLY_ONYXDB_AGENT,
                            TaskType.MONGODB_CHECK_ONYXDB_AGENT_READINESS
                    )
            ),
            Map.entry(
                    TaskType.MONGODB_CREATE_EXPORTER_SERVICE_SCRAPE, List.of(
                            TaskType.MONGODB_CREATE_VECTOR_CONFIG,
                            TaskType.MONGODB_APPLY_PSMDB,
                            TaskType.MONGODB_CHECK_PSMDB_READINESS,
                            TaskType.MONGODB_APPLY_ONYXDB_AGENT,
                            TaskType.MONGODB_CHECK_ONYXDB_AGENT_READINESS,
                            TaskType.MONGODB_CREATE_EXPORTER_SERVICE
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
                        TaskType.MONGODB_APPLY_PSMDB,
                        operationId,
                        now,
                        DEFAULT_RETRIES_LEFT,
                        stringPayload
                ),
                Task.scheduledMiddle(
                        TaskType.MONGODB_CHECK_PSMDB_READINESS,
                        operationId,
                        now,
                        DEFAULT_RETRIES_LEFT + 5,
                        stringPayload
                ),
                Task.scheduledMiddle(
                        TaskType.MONGODB_APPLY_ONYXDB_AGENT,
                        operationId,
                        now,
                        DEFAULT_RETRIES_LEFT,
                        stringPayload
                ),
                Task.scheduledMiddle(
                        TaskType.MONGODB_CHECK_ONYXDB_AGENT_READINESS,
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
                Task.scheduledLast(
                        TaskType.MONGODB_CREATE_EXPORTER_SERVICE_SCRAPE,
                        operationId,
                        now,
                        DEFAULT_RETRIES_LEFT,
                        stringPayload
                )
        );

        return aggregateToTasksWithBlockers(tasks);
    }
}

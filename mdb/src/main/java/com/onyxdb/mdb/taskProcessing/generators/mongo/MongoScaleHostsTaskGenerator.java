package com.onyxdb.mdb.taskProcessing.generators.mongo;

import java.time.Duration;
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
import com.onyxdb.mdb.utils.TimeUtils;

public class MongoScaleHostsTaskGenerator extends ClusterTaskGenerator {
    // TODO don't repeat blockers for each task
    private static final Map<TaskType, List<TaskType>> TASK_TYPE_TO_BLOCKER_TASK_TYPES = Map.ofEntries(
            Map.entry(
                    TaskType.MONGODB_APPLY_PSMDB, List.of()
            ),
            Map.entry(
                    TaskType.MONGODB_CHECK_PSMDB_READINESS, List.of()
            )
    );

    public MongoScaleHostsTaskGenerator(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public OperationType getOperationType() {
        return OperationType.MONGODB_SCALE_HOSTS;
    }

    @Override
    public Map<TaskType, List<TaskType>> getTaskTypeToBlockerTaskTypes() {
        return TASK_TYPE_TO_BLOCKER_TASK_TYPES;
    }

    @Override
    public List<TaskWithBlockers> generateTasks(UUID operationId, TaskPayload payload) {
        // TODO create TimeService that will provide LocalDateTime with fixed timezone
        LocalDateTime now = TimeUtils.now();
        String stringPayload = payloadToString(payload);

        List<Task> tasks = List.of(
                Task.scheduledFirst(
                        TaskType.MONGODB_APPLY_PSMDB,
                        operationId,
                        now,
                        DEFAULT_RETRIES_LEFT,
                        stringPayload
                ),
                // TODO Support smart calculations for next task start
                Task.scheduledLast(
                        TaskType.MONGODB_CHECK_PSMDB_READINESS,
                        operationId,
                        now.plus(Duration.ofSeconds(30)),
                        DEFAULT_RETRIES_LEFT,
                        stringPayload
                )
        );

        return aggregateToTasksWithBlockers(tasks);
    }
}

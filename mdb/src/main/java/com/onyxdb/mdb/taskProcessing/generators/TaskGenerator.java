package com.onyxdb.mdb.taskProcessing.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.mdb.exceptions.ClusterTaskNotFoundException;
import com.onyxdb.mdb.taskProcessing.models.OperationType;
import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskType;
import com.onyxdb.mdb.taskProcessing.models.TaskWithBlockers;
import com.onyxdb.mdb.taskProcessing.models.payloads.TaskPayload;

public abstract class TaskGenerator<PAYLOAD extends TaskPayload> {
    protected static final int DEFAULT_RETRIES_LEFT = 5;

    protected final ObjectMapper objectMapper;

    protected TaskGenerator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public abstract OperationType getOperationType();

    public abstract Map<TaskType, List<TaskType>> getTaskTypeToBlockerTaskTypes();

    public abstract List<TaskWithBlockers> generateTasks(UUID operationId, TaskPayload payload);

    protected List<TaskWithBlockers> aggregateToTasksWithBlockers(List<Task> tasks) {
        List<TaskWithBlockers> tasksWithBlockers = new ArrayList<>(tasks.size());

        Map<TaskType, List<Task>> taskTypeToClusterTasks = tasks.stream()
                .collect(Collectors.groupingBy(Task::type));

        for (var taskTypeToBlockerTaskTypes : getTaskTypeToBlockerTaskTypes().entrySet()) {
            TaskType taskType = taskTypeToBlockerTaskTypes.getKey();
            List<TaskType> blockerTaskTypes = taskTypeToBlockerTaskTypes.getValue();

            if (!taskTypeToClusterTasks.containsKey(taskType)) {
                throw new ClusterTaskNotFoundException(taskType);
            }

            var taskWithBlockers = new TaskWithBlockers(
                    taskTypeToClusterTasks.get(taskType).getFirst(),
                    blockerTaskTypes.stream()
                            .map(blockerTaskType -> {
                                if (!taskTypeToClusterTasks.containsKey(blockerTaskType)) {
                                    throw new ClusterTaskNotFoundException(blockerTaskType);
                                }

                                return taskTypeToClusterTasks.get(blockerTaskType)
                                        .getFirst()
                                        .id();
                            })
                            .toList()
            );
            tasksWithBlockers.add(taskWithBlockers);
        }

        return tasksWithBlockers;
    }

    protected String payloadToString(TaskPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.onyxdb.platform.processing.producers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.onyxdb.platform.operationsOLD.tasks.ProducedTask;
import com.onyxdb.platform.processing.models.Operation;
import com.onyxdb.platform.processing.models.Task;
import com.onyxdb.platform.processing.models.TaskStatus;
import com.onyxdb.platform.processing.models.TaskType;
import com.onyxdb.platform.processing.models.TaskWithBlockers;
import com.onyxdb.platform.utils.ObjectMapperUtils;
import com.onyxdb.platform.utils.TimeUtils;

public abstract class TaskProducer<PAYLOAD> {
    protected final ObjectMapper objectMapper;

    protected TaskProducer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public abstract PAYLOAD parsePayload(String payload);

    public abstract List<ProducedTask> produceTasks(Operation operation, PAYLOAD payload);

    public List<ProducedTask> produceTasks(Operation operation, String payload) {
        return produceTasks(operation, parsePayload(payload));
    }

//    protected List<TaskWithBlockers> prepareProducedTasks(List<ProducedTask> producedTasks) {
//        List<TaskWithBlockers> tasks = new ArrayList<>();
//
//        for (int i = 0; i < producedTasks.size(); i++) {
//            int attempts = 5;
//            int delaySeconds = 1;
//
//            var prev = i > 0 ? tasks.get(i - 1) : null;
//            var current = producedTasks.get(i);
//
//            if (current.type().equalsStringEnum(TaskType.MONGO_APPLY_PSMDB)) {
//                delaySeconds = 30;
//            }
//
//            var scheduledAt = prev == null ? TimeUtils.now() : prev.task().getScheduledAtWithDelay(
//                    Duration.ofSeconds(delaySeconds)
//            );
//
//            tasks.add(new TaskWithBlockers(
//                    new Task(
//                            current.id(),
//                            current.type(),
//                            TaskStatus.SCHEDULED,
//                            current.operationId(),
//                            TimeUtils.now(),
//                            TimeUtils.now(),
//                            scheduledAt,
//                            attempts,
//                            ObjectMapperUtils.convertToString(objectMapper, current.payload())
//                    ),
//                    current.blockerIds()
//            ));
//        }
//        return tasks;
//    }

//    private Task producedTaskToTask(ProducedTask producedTask) {
//        return new Task(
//                producedTask.id(),
//                producedTask.type(),
//                TaskStatus.SCHEDULED,
//                producedTask.operationId(),
//                TimeUtils.now(),
//                TimeUtils.now(),
//
//                // scheduledAt
//                TimeUtils.now(),
//                producedTask
//        );
//    }

//    public abstract List<TaskWithBlockers> generateTasks(UUID operationId, PAYLOAD payload);

//    protected List<TaskWithBlockers> aggregateToTask(Operation operation, PAYLOAD payload) {

//    }

//    protected List<TaskWithBlockers> aggregateToTasksWithBlockers(List<Task> tasks) {
//        List<TaskWithBlockers> tasksWithBlockers = new ArrayList<>(tasks.size());
//
//        Map<TaskType, List<Task>> taskTypeToClusterTasks = tasks.stream()
//                .collect(Collectors.groupingBy(Task::type));
//
//        for (var taskTypeToBlockerTaskTypes : getTaskTypeToBlockerTaskTypes().entrySet()) {
//            TaskType taskType = taskTypeToBlockerTaskTypes.getKey();
//            List<TaskType> blockerTaskTypes = taskTypeToBlockerTaskTypes.getValue();
//
//            if (!taskTypeToClusterTasks.containsKey(taskType)) {
//                throw new ClusterTaskNotFoundException(taskType);
//            }
//
//            var taskWithBlockers = new TaskWithBlockers(
//                    taskTypeToClusterTasks.get(taskType).getFirst(),
//                    blockerTaskTypes.stream()
//                            .map(blockerTaskType -> {
//                                if (!taskTypeToClusterTasks.containsKey(blockerTaskType)) {
//                                    throw new ClusterTaskNotFoundException(blockerTaskType);
//                                }
//
//                                return taskTypeToClusterTasks.get(blockerTaskType)
//                                        .getFirst()
//                                        .id();
//                            })
//                            .toList()
//            );
//            tasksWithBlockers.add(taskWithBlockers);
//        }
//
//        return tasksWithBlockers;
//    }

//    protected String payloadToString(Payload payload) {
//        try {
//            return objectMapper.writeValueAsString(payload);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
}

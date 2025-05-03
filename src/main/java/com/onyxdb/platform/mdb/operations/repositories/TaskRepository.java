package com.onyxdb.platform.mdb.operations.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskFilter;
import com.onyxdb.platform.mdb.operations.models.TaskStatus;
import com.onyxdb.platform.mdb.operations.models.TaskWithBlockers;

/**
 * @author foxleren
 */
public interface TaskRepository {
    void createBulk(List<Task> tasks);

    void createBlockerTasksBulk(List<TaskWithBlockers> tasksWithBlockers);

    List<Task> getTasksToConsume(
            int limit,
            LocalDateTime scheduledAt
    );

    void updateTask(Task task);

    void updateStatus(UUID id, TaskStatus status);

    void updateTask(
            UUID id,
            TaskStatus status,
            @Nullable
            Integer attemptsLeft,
            @Nullable
            LocalDateTime scheduledAt
    );

    List<Task> listTasks(TaskFilter filter);

    List<TaskWithBlockers> listTasksWithBlockers(TaskFilter filter);

    void updateTasks(List<Task> tasks);

    void startTask(UUID taskId, TaskStatus taskStatus);

    void finishTask(UUID taskId, TaskStatus taskStatus);
}

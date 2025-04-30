package com.onyxdb.platform.mdb.processing.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.platform.mdb.operationsOLD.TaskFilter;
import com.onyxdb.platform.mdb.processing.models.Task;
import com.onyxdb.platform.mdb.processing.models.TaskStatus;
import com.onyxdb.platform.mdb.processing.models.TaskWithBlockers;

/**
 * @author foxleren
 */
public interface TaskRepository {
    void createBulk(List<Task> tasks);

    void createBlockerTasksBulk(List<TaskWithBlockers> tasksWithBlockers);

    List<Task> getTasksToProcess(
            int limit,
            LocalDateTime scheduledAt
    );

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

    void updateTasks(List<Task> tasks);
}

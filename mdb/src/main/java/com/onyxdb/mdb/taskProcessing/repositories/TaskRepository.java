package com.onyxdb.mdb.taskProcessing.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskStatus;
import com.onyxdb.mdb.taskProcessing.models.TaskWithBlockers;

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
}

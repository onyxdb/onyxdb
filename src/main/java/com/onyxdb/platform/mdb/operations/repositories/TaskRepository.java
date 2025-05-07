package com.onyxdb.platform.mdb.operations.repositories;

import java.util.List;

import com.onyxdb.platform.mdb.operations.models.Task;

/**
 * @author foxleren
 */
public interface TaskRepository {
    void createTasks(List<Task> tasks);

    List<Task> getTasksToConsume(int limit);

    void updateTask(Task task);
}

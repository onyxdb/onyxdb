package com.onyxdb.platform.mdb.processing;

import java.util.List;

import com.onyxdb.platform.mdb.processing.models.Task;
import com.onyxdb.platform.mdb.processing.models.TaskWithBlockers;

public class TaskProcessingUtils {
    public static List<Task> getTasksFromTasksWithBlockers(List<TaskWithBlockers> tasksWithBlockers) {
        return tasksWithBlockers.stream()
                .map(TaskWithBlockers::task)
                .toList();
    }
}

package com.onyxdb.platform.taskProcessing;

import java.util.List;

import com.onyxdb.platform.taskProcessing.models.Task;
import com.onyxdb.platform.taskProcessing.models.TaskWithBlockers;

public class TaskProcessingUtils {
    public static List<Task> getTasksFromTasksWithBlockers(List<TaskWithBlockers> tasksWithBlockers) {
        return tasksWithBlockers.stream()
                .map(TaskWithBlockers::task)
                .toList();
    }
}

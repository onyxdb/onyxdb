package com.onyxdb.mdb.taskProcessing;

import java.util.List;

import com.onyxdb.mdb.taskProcessing.models.Task;
import com.onyxdb.mdb.taskProcessing.models.TaskWithBlockers;

public class TaskProcessingUtils {
    public static List<Task> getTasksFromTasksWithBlockers(List<TaskWithBlockers> tasksWithBlockers) {
        return tasksWithBlockers.stream()
                .map(TaskWithBlockers::task)
                .toList();
    }
}

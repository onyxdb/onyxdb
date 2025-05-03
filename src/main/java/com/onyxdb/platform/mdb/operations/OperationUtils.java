package com.onyxdb.platform.mdb.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import com.onyxdb.platform.mdb.exceptions.InternalServerErrorException;
import com.onyxdb.platform.mdb.operations.models.TaskWithBlockers;

public class OperationUtils {
    public static List<TaskWithBlockers> sortTasksWithBlockers(List<TaskWithBlockers> tasks) {
        Map<UUID, Set<UUID>> dependencyGraph = new HashMap<>();
        Map<UUID, TaskWithBlockers> taskMap = new HashMap<>();
        Map<UUID, Integer> inDegree = new HashMap<>();

        for (TaskWithBlockers task : tasks) {
            UUID taskId = task.task().id();
            taskMap.put(taskId, task);
            dependencyGraph.putIfAbsent(taskId, new HashSet<>());
            inDegree.putIfAbsent(taskId, 0);
        }

        for (TaskWithBlockers task : tasks) {
            UUID taskId = task.task().id();
            for (UUID blockerId : task.blockerIds()) {
                if (taskMap.containsKey(blockerId)) {
                    dependencyGraph.get(blockerId).add(taskId);
                    inDegree.put(taskId, inDegree.getOrDefault(taskId, 0) + 1);
                }
            }
        }

        Queue<UUID> queue = new LinkedList<>();
        for (UUID taskId : inDegree.keySet()) {
            if (inDegree.get(taskId) == 0) {
                queue.add(taskId);
            }
        }

        List<TaskWithBlockers> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            UUID current = queue.poll();
            result.add(taskMap.get(current));

            for (UUID dependent : dependencyGraph.getOrDefault(current, Collections.emptySet())) {
                inDegree.put(dependent, inDegree.get(dependent) - 1);
                if (inDegree.get(dependent) == 0) {
                    queue.add(dependent);
                }
            }
        }

        if (result.size() != tasks.size()) {
            throw new InternalServerErrorException("Found cyclic dependency");
        }

        return result;
    }
}

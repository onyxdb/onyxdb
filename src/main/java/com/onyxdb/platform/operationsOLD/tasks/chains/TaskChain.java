package com.onyxdb.platform.operationsOLD.tasks.chains;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.operationsOLD.tasks.ProducedTask;

public class TaskChain {
    private final List<ProducedTask> tasks = new ArrayList<>();
    private final List<UUID> finalTaskBlockerIds = new ArrayList<>();

    public List<ProducedTask> getTasks() {
        return tasks;
    }

    public void addTask(ProducedTask task) {
        tasks.add(task);
    }

    public void addFinalTaskBlockerId(UUID taskId) {
        finalTaskBlockerIds.add(taskId);
    }

    public List<UUID> getFinalTaskBlockerIds() {
        return finalTaskBlockerIds;
    }
}

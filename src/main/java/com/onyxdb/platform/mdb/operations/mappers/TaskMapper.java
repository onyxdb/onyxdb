package com.onyxdb.platform.mdb.operations.mappers;


import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jooq.JSONB;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.generated.jooq.tables.records.TasksRecord;
import com.onyxdb.platform.mdb.operations.models.ProducedTask;
import com.onyxdb.platform.mdb.operations.models.Task;
import com.onyxdb.platform.mdb.operations.models.TaskStatus;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.utils.ObjectMapperUtils;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    private final ObjectMapper objectMapper;

    public TasksRecord taskToTasksRecord(Task t) {
        return new TasksRecord(
                t.id(),
                t.type().value(),
                com.onyxdb.platform.generated.jooq.enums.TaskStatus.valueOf(t.status().value()),
                t.operationId(),
                t.createdAt(),
                t.updatedAt(),
                t.attemptsLeft(),
                JSONB.jsonb(t.payload()),
                t.blockerIds().toArray(new UUID[0]),
                t.postDelaySeconds(),
                t.retryDelaySeconds(),
                t.startedAt(),
                t.finishedAt()
        );
    }

    public Task tasksRecordToTask(TasksRecord r) {
        return new Task(
                r.getId(),
                TaskType.R.fromValue(r.getType()),
                TaskStatus.fromValue(r.getStatus().getLiteral()),
                r.getOperationId(),
                r.getCreatedAt(),
                r.getUpdatedAt(),
                r.getAttemptsLeft(),
                r.getPayload().data(),
                List.of(r.getBlockerIds()),
                r.getPostDelaySeconds(),
                r.getRetryDelaySeconds(),
                r.getStartedAt(),
                r.getFinishedAt()
        );
    }

    public com.onyxdb.platform.generated.jooq.enums.TaskStatus taskStatusToJooqTaskStatus(TaskStatus taskStatus) {
        return com.onyxdb.platform.generated.jooq.enums.TaskStatus.lookupLiteral(taskStatus.value());
    }

    public Task producedTaskToTask(
            ProducedTask producedTask,
            int attempts,
            int postDelaySeconds,
            int retryDelaySeconds
    ) {
        return Task.create(
                producedTask.id(),
                producedTask.type(),
                TaskStatus.SCHEDULED,
                producedTask.operationId(),
                attempts,
                ObjectMapperUtils.convertToString(objectMapper, producedTask.payload()),
                producedTask.blockerIds(),
                postDelaySeconds,
                retryDelaySeconds
        );
    }
}

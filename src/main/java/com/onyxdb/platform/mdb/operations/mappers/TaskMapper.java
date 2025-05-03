package com.onyxdb.platform.mdb.operations.mappers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.operations.models.TaskStatus;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    public com.onyxdb.platform.generated.jooq.enums.TaskStatus taskStatusToJooqTaskStatus(TaskStatus taskStatus) {
        return com.onyxdb.platform.generated.jooq.enums.TaskStatus.lookupLiteral(taskStatus.value());
    }
}

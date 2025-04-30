package com.onyxdb.platform.mdb.operationsOLD;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import com.onyxdb.platform.mdb.processing.models.TaskStatus;

import static com.onyxdb.platform.generated.jooq.Tables.TASKS;

public record TaskFilter(
        @Nullable
        List<UUID> operationIds,
        @Nullable
        List<TaskStatus> statuses
) {
    public Condition buildCondition() {
        Condition condition = DSL.trueCondition();

        if (statuses != null && !statuses.isEmpty()) {
            condition = condition.and(TASKS.STATUS.in(statuses.stream().map(TaskStatus::value).toList()));
        }

        if (operationIds != null && !operationIds.isEmpty()) {
            condition = condition.and(TASKS.OPERATION_ID.in(operationIds));
        }
        System.err.println(condition);

        return condition;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<UUID> operationIds;
        private List<TaskStatus> statuses;

        public Builder withOperationIds(List<UUID> operationIds) {
            this.operationIds = operationIds;
            return this;
        }

        public Builder withStatuses(List<TaskStatus> statuses) {
            this.statuses = statuses;
            return this;
        }

        public TaskFilter build() {
            return new TaskFilter(
                    operationIds,
                    statuses
            );
        }
    }
}

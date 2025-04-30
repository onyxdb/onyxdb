package com.onyxdb.platform.mdb.projects;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import static com.onyxdb.platform.generated.jooq.Tables.PROJECTS;

public record ProjectFilter(
        @Nullable
        UUID projectId,
        @Nullable
        String projectName,
        @Nullable
        Boolean isDeleted
) {
    public Condition buildConditionForProjectsTable() {
        Condition condition = DSL.trueCondition();
        if (projectId != null) {
            condition = condition.and(PROJECTS.ID.eq(projectId));
        }

        if (projectName != null) {
            condition = condition.and(PROJECTS.NAME.equal(projectName));
        }

        if (isDeleted != null) {
            condition = condition.and(PROJECTS.IS_DELETED.equal(isDeleted));
        }

        return condition;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID projectId;
        private String projectName;
        private Boolean isDeleted;

        public Builder withProjectId(UUID projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder withProjectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public Builder withIsDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public ProjectFilter build() {
            return new ProjectFilter(
                    projectId,
                    projectName,
                    isDeleted
            );
        }
    }
}

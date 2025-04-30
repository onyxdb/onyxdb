package com.onyxdb.platform.mdb.projects;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import static com.onyxdb.platform.generated.jooq.Tables.PROJECTS;

public record ProjectFilter(
        @Nullable
        String projectName,
        @Nullable
        List<UUID> productIds
) {
    public Condition buildConditionForProjectsTable() {
        Condition condition = DSL.trueCondition();
        if (projectName != null) {
            condition = condition.and(PROJECTS.NAME.equal(projectName));
        }

        if (productIds != null && !productIds.isEmpty()) {
            condition = condition.and(PROJECTS.PRODUCT_ID.in(productIds));
        }

        return condition;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String projectName;
        private List<UUID> productIds;

        public Builder withProjectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public Builder withProductIds(List<UUID> productIds) {
            this.productIds = productIds;
            return this;
        }

        public ProjectFilter build() {
            return new ProjectFilter(projectName, productIds);
        }
    }
}

package com.onyxdb.platform.mdb.clusters.models;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import static com.onyxdb.platform.generated.jooq.Tables.CLUSTERS;

public record ClusterFilter(
        @Nullable
        List<UUID> projectIds,
        @Nullable
        Boolean isDeleted,
        @Nullable
        ClusterType type
) {
    public Condition buildCondition() {
        Condition condition = DSL.trueCondition();

        if (projectIds != null && !projectIds.isEmpty()) {
            condition = condition.and(CLUSTERS.PROJECT_ID.in(projectIds));
        }

        if (isDeleted != null) {
            condition = condition.and(CLUSTERS.IS_DELETED.eq(isDeleted));
        }

        if (type != null) {
            condition = condition.and(CLUSTERS.TYPE.eq(
                    com.onyxdb.platform.generated.jooq.enums.ClusterType.lookupLiteral(type.value())
            ));
        }

        return condition;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        @Nullable
        private List<UUID> projectIds;
        @Nullable
        private Boolean isDeleted;
        @Nullable
        private ClusterType type;

        public Builder withProjectIds(@Nullable List<UUID> projectIds) {
            this.projectIds = projectIds;
            return this;
        }

        public Builder withIsDeleted(@Nullable Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public Builder withType(@Nullable ClusterType type) {
            this.type = type;
            return this;
        }

        public ClusterFilter build() {
            return new ClusterFilter(
                    projectIds,
                    isDeleted,
                    type
            );
        }
    }
}

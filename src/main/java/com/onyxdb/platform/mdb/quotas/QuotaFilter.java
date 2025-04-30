package com.onyxdb.platform.mdb.quotas;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import com.onyxdb.platform.mdb.resources.ResourceType;

import static com.onyxdb.platform.generated.jooq.Tables.PRODUCTS;
import static com.onyxdb.platform.generated.jooq.Tables.RESOURCES;

public record QuotaFilter(
        @Nullable
        List<UUID> productIds,
        @Nullable
        List<ResourceType> resourceTypes
) {
    public static Builder builder() {
        return new Builder();
    }

    public Condition buildCondition() {
        Condition condition = DSL.trueCondition();
        if (productIds != null && !productIds.isEmpty()) {
            condition = condition.and(PRODUCTS.ID.in(productIds));
        }

        if (resourceTypes != null && !resourceTypes.isEmpty()) {
            condition = condition.and(RESOURCES.TYPE.in(resourceTypes));
        }

        return condition;
    }

    public static class Builder {
        @Nullable
        private List<UUID> productIds;
        @Nullable
        private List<ResourceType> resourceTypes;

        public Builder withProductIds(List<UUID> productIds) {
            this.productIds = productIds;
            return this;
        }

        public Builder withProductId(UUID productId) {
            this.productIds = List.of(productId);
            return this;
        }

        public Builder withResourceTypes(List<ResourceType> resourceTypes) {
            this.resourceTypes = resourceTypes;
            return this;
        }

        public QuotaFilter build() {
            return new QuotaFilter(
                    productIds,
                    resourceTypes
            );
        }
    }
}

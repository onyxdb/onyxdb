package com.onyxdb.platform.mdb.resources;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.impl.DSL;

import com.onyxdb.platform.mdb.quotas.QuotaProvider;

import static com.onyxdb.platform.generated.jooq.Tables.RESOURCES;

public record ResourceFilter(
        @Nullable
        List<ResourceType> types,
        @Nullable
        List<QuotaProvider> providers
) {
    public static ResourceFilter empty() {
        return new ResourceFilter(
                null,
                null
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public Condition buildCondition() {
        Condition condition = DSL.trueCondition();
        if (types != null && !types.isEmpty()) {
            condition = condition.and(RESOURCES.TYPE.in(types.stream().map(ResourceType::value).toList()));
        }

        if (providers != null && !providers.isEmpty()) {
            condition = condition.and(RESOURCES.PROVIDER.in(providers.stream().map(QuotaProvider::value).toList()));
        }

        return condition;
    }

    public static class Builder {
        @Nullable
        private List<ResourceType> types;
        @Nullable
        private List<QuotaProvider> providers;

        public Builder withTypes(List<ResourceType> types) {
            this.types = types;
            return this;
        }

        public Builder withType(ResourceType type) {
            this.types = List.of(type);
            return this;
        }

        public Builder withProviders(List<QuotaProvider> providers) {
            this.providers = providers;
            return this;
        }

        public Builder withProvider(QuotaProvider provider) {
            this.providers = List.of(provider);
            return this;
        }

        public ResourceFilter build() {
            return new ResourceFilter(
                    types,
                    providers
            );
        }
    }
}

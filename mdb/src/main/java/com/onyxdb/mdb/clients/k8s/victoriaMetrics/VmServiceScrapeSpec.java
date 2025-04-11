package com.onyxdb.mdb.clients.k8s.victoriaMetrics;

import java.util.List;
import java.util.Objects;

import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;

public record VmServiceScrapeSpec(
        LabelSelector selector,
        List<VmEndpoint> endpoints
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LabelSelector selector;
        private List<VmEndpoint> endpoints;

        public Builder withSelector(LabelSelector selector) {
            this.selector = selector;
            return this;
        }

        public Builder withEndpoints(List<VmEndpoint> endpoints) {
            this.endpoints = endpoints;
            return this;
        }

        public VmServiceScrapeSpec build() {
            return new VmServiceScrapeSpec(
                    Objects.requireNonNullElse(selector, new LabelSelectorBuilder().build()),
                    Objects.requireNonNullElse(endpoints, List.of())
            );
        }
    }
}



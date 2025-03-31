package com.onyxdb.mdb.clients.k8s.victoriametrics;

import java.util.List;
import java.util.Map;

import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;

public record ServiceScrape(
        Map<String, String> labelsToScrape,
        List<ServiceScrapeEndpoint> endpoints
) {
    public Map<String, Object> asMap() {
        return Map.ofEntries(
                Map.entry("endpoints", endpoints.stream()
                        .map(endpoint -> Map.ofEntries(
                                Map.entry("port", endpoint.port()),
                                Map.entry("path", endpoint.path())
                        ))
                        .toList()
                ),
                Map.entry("selector", new LabelSelectorBuilder()
                        .withMatchLabels(labelsToScrape)
                        .build()
                )
        );
    }
}

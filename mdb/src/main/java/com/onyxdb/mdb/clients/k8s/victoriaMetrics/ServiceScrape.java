package com.onyxdb.mdb.clients.k8s.victoriaMetrics;

import java.util.List;

import io.fabric8.kubernetes.api.model.LabelSelector;

public record ServiceScrape(
        LabelSelector selector,
        List<Endpoint> endpoints
) {
}

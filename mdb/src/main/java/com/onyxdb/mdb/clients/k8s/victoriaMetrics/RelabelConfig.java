package com.onyxdb.mdb.clients.k8s.victoriaMetrics;

public record RelabelConfig(
        String targetLabel,
        String replacement
) {
}

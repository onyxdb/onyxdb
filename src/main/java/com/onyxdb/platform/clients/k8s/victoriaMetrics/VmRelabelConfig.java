package com.onyxdb.platform.clients.k8s.victoriaMetrics;

public record VmRelabelConfig(
        String targetLabel,
        String replacement
) {
}

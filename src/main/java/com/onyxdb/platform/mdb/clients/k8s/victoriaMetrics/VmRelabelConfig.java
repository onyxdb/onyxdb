package com.onyxdb.platform.mdb.clients.k8s.victoriaMetrics;

public record VmRelabelConfig(
        String targetLabel,
        String replacement
) {
}

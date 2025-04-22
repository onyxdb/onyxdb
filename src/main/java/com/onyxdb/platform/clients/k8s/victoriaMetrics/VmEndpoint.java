package com.onyxdb.platform.clients.k8s.victoriaMetrics;

import java.util.List;

public record VmEndpoint(
        String port,
        String path,
        List<VmRelabelConfig> relabelConfigs
) {
}

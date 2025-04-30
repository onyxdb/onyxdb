package com.onyxdb.platform.mdb.clients.k8s.victoriaMetrics;

import java.util.List;

public record VmEndpoint(
        String port,
        String path,
        List<VmRelabelConfig> relabelConfigs
) {
}

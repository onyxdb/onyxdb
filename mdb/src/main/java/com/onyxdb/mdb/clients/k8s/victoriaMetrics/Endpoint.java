package com.onyxdb.mdb.clients.k8s.victoriaMetrics;

import java.util.List;

public record Endpoint(
        String port,
        String path,
        List<RelabelConfig> relabelConfigs
) {
}

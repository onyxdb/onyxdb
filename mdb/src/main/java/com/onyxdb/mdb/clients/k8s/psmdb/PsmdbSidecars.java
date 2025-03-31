package com.onyxdb.mdb.clients.k8s.psmdb;

import java.util.List;
import java.util.Map;

public record PsmdbSidecars(
        PsmdbExporterSidecar exporterSidecar
) {
    public List<Map<String, Object>> asMap() {
        return List.of(exporterSidecar.asMap());
    }
}

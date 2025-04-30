package com.onyxdb.platform.mdb.clients.k8s.psmdb;

public record PsmdbExporterService(
        String namespace,
        String name,
        String portName,
        int port
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        public PsmdbExporterService build(String namespace, String name) {
            return new PsmdbExporterService(
                    namespace,
                    name,
                    PsmdbExporterServiceClient.PORT_NAME,
                    PsmdbExporterServiceClient.PORT
            );
        }
    }
}

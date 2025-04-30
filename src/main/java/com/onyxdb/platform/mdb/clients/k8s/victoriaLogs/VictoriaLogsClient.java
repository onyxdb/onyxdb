package com.onyxdb.platform.mdb.clients.k8s.victoriaLogs;

public class VictoriaLogsClient {
    private final String baseUrl;

    public VictoriaLogsClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}

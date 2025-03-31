package com.onyxdb.mdb.clients.k8s.victoriametrics;

public record ServiceScrapeEndpoint(
        String port,
        String path
) {
}

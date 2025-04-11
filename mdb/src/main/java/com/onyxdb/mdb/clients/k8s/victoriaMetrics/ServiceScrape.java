package com.onyxdb.mdb.clients.k8s.victoriaMetrics;

public record ServiceScrape(
        String namespace,
        String name,
        ServiceScrapeSpec spec
) {
}

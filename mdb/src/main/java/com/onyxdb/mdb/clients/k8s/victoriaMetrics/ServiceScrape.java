package com.onyxdb.mdb.clients.k8s.victoriaMetrics;

public record ServiceScrape(
        String name,
        String namespace,
        ServiceScrapeSpec spec
) {
}

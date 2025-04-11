package com.onyxdb.mdb.clients.k8s.victoriaMetrics;

public record VmServiceScrape(
        String namespace,
        String name,
        VmServiceScrapeSpec spec
) {
}

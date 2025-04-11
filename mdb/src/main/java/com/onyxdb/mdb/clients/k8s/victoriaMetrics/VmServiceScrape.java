package com.onyxdb.mdb.clients.k8s.victoriaMetrics;

public record VmServiceScrape(
        String name,
        String namespace,
        VmServiceScrapeSpec spec
) {
}

package com.onyxdb.mdb.clients.k8s.victoriaMetrics;

public class VmOperatorClient {
    private final VmServiceScrapeFactory vmServiceScrapeFactory;

    public VmOperatorClient(VmServiceScrapeFactory vmServiceScrapeFactory) {
        this.vmServiceScrapeFactory = vmServiceScrapeFactory;
    }

    public void createServiceScrape(VmServiceScrape serviceScrape) {
        vmServiceScrapeFactory.createResource(serviceScrape);
    }
}

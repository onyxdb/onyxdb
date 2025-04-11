package com.onyxdb.mdb.clients.k8s.victoriaMetrics;

import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.client.KubernetesClient;

public class VmOperatorClient {
    private final KubernetesClient kubernetesClient;
    private final ServiceScrapeFactory serviceScrapeFactory;

    public VmOperatorClient(
            KubernetesClient kubernetesClient,
            ServiceScrapeFactory serviceScrapeFactory
    ) {
        this.kubernetesClient = kubernetesClient;
        this.serviceScrapeFactory = serviceScrapeFactory;
    }

    public void createServiceScrape(ServiceScrape serviceScrape) {
        GenericKubernetesResource cr = serviceScrapeFactory.buildResource(serviceScrape);

        kubernetesClient.genericKubernetesResources(
                        ServiceScrapeFactory.API_VERSION,
                        ServiceScrapeFactory.KIND
                )
                .inNamespace(serviceScrape.namespace())
                .resource(cr)
                .create();
    }
}

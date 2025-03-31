package com.onyxdb.mdb.clients.k8s.victoriametrics;

import java.util.List;
import java.util.Map;

import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import static com.onyxdb.mdb.clients.k8s.psmdb.PsmdbClient.DEFAULT_NAMESPACE;

public class VmOperatorClient {
    private static final String GROUP = "operator.victoriametrics.com";
    private static final String VERSION = "v1beta1";
    private static final String API_VERSION = GROUP + "/" + VERSION;
    private static final String SERVICE_SCRAPE_KIND = "VMServiceScrape";

    private final KubernetesClient kubernetesClient;

    public VmOperatorClient(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    public void createServiceScrape(
            String serviceName,
            Map<String, String> labelsToScrape,
            List<ServiceScrapeEndpoint> endpoints
    ) {
        var serviceScrape = new ServiceScrape(labelsToScrape, endpoints);

        System.err.println(serviceScrape.asMap());

        GenericKubernetesResource resource = new GenericKubernetesResourceBuilder()
                .withApiVersion(API_VERSION)
                .withKind(SERVICE_SCRAPE_KIND)
                .withNewMetadata()
                .withName(getServiceScrapeName(serviceName))
                .endMetadata()
                .addToAdditionalProperties("spec", serviceScrape.asMap())
                .build();

        kubernetesClient.genericKubernetesResources(API_VERSION, SERVICE_SCRAPE_KIND)
                .inNamespace(DEFAULT_NAMESPACE)
                .resource(resource)
                .create();
    }

    public String getServiceScrapeName(String serviceName) {
        return String.format("%s-scrape", serviceName);
    }
}

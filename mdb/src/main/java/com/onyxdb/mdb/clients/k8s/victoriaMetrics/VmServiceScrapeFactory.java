package com.onyxdb.mdb.clients.k8s.victoriaMetrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

import com.onyxdb.mdb.utils.ObjectMapperUtils;

public class VmServiceScrapeFactory {
    public static final String GROUP = "operator.victoriametrics.com";
    public static final String VERSION = "v1beta1";
    public static final String API_VERSION = GROUP + "/" + VERSION;
    public static final String KIND = "VMServiceScrape";

    private final KubernetesClient kubernetesClient;
    private final ObjectMapper objectMapper;

    public VmServiceScrapeFactory(
            KubernetesClient kubernetesClient,
            ObjectMapper objectMapper
    ) {
        this.kubernetesClient = kubernetesClient;
        this.objectMapper = objectMapper;
    }

    public void createResource(VmServiceScrape serviceScrape) {
        GenericKubernetesResource resource = buildResource(serviceScrape);

        kubernetesClient.genericKubernetesResources(
                        VmServiceScrapeFactory.API_VERSION,
                        VmServiceScrapeFactory.KIND
                )
                .inNamespace(serviceScrape.namespace())
                .resource(resource)
                .create();
    }

    public static String getPreparedName(String name) {
        return String.format("%s-scrape", name);
    }

    private GenericKubernetesResource buildResource(VmServiceScrape serviceScrape) {
        var specMap = ObjectMapperUtils.convertToMap(objectMapper, serviceScrape.spec());

        return new GenericKubernetesResourceBuilder()
                .withApiVersion(API_VERSION)
                .withKind(KIND)
                .withNewMetadata()
                .withName(getPreparedName(serviceScrape.name()))
                .endMetadata()
                .addToAdditionalProperties("spec", specMap)
                .build();
    }
}

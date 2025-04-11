package com.onyxdb.mdb.clients.k8s.victoriaMetrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.ResourceDefinitionContext;

import com.onyxdb.mdb.utils.ObjectMapperUtils;

public class VmServiceScrapeFactory {
    private static final String GROUP = "operator.victoriametrics.com";
    private static final String VERSION = "v1beta1";
    private static final String API_VERSION = GROUP + "/" + VERSION;
    private static final String KIND = "VMServiceScrape";
    private static final ResourceDefinitionContext CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(GROUP)
            .withVersion(VERSION)
            .withKind(KIND)
            .withNamespaced(true)
            .build();

    private final KubernetesClient kubernetesClient;
    private final ObjectMapper objectMapper;

    public VmServiceScrapeFactory(
            KubernetesClient kubernetesClient,
            ObjectMapper objectMapper
    ) {
        this.kubernetesClient = kubernetesClient;
        this.objectMapper = objectMapper;
    }

    public GenericKubernetesResource getResource(String namespace, String name) {
        return kubernetesClient.genericKubernetesResources(CONTEXT)
                .inNamespace(namespace)
                .withName(getPreparedName(name))
                .get();
    }

    public void createResource(VmServiceScrape vmServiceScrape) {
        GenericKubernetesResource resource = buildResource(vmServiceScrape);

        kubernetesClient.genericKubernetesResources(
                        VmServiceScrapeFactory.API_VERSION,
                        VmServiceScrapeFactory.KIND
                )
                .inNamespace(vmServiceScrape.namespace())
                .resource(resource)
                .create();
    }

    public boolean resourceExists(String namespace, String name) {
        return getResource(namespace, name) != null;
    }

    public static String getPreparedName(String name) {
        return String.format("%s-scrape", name);
    }

    private GenericKubernetesResource buildResource(VmServiceScrape vmServiceScrape) {
        var specMap = ObjectMapperUtils.convertToMap(objectMapper, vmServiceScrape.spec());

        return new GenericKubernetesResourceBuilder()
                .withApiVersion(API_VERSION)
                .withKind(KIND)
                .withNewMetadata()
                .withName(getPreparedName(vmServiceScrape.name()))
                .endMetadata()
                .addToAdditionalProperties("spec", specMap)
                .build();
    }
}

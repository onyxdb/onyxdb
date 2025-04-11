package com.onyxdb.mdb.clients.k8s.psmdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.ResourceDefinitionContext;

import com.onyxdb.mdb.utils.ObjectMapperUtils;

public class PsmdbFactory {
    public static final String GROUP = "psmdb.percona.com";
    public static final String VERSION = "v1";
    public static final String API_VERSION = GROUP + "/" + VERSION;
    public static final String KIND = "PerconaServerMongoDB";
    public static final String PLURAL = "perconaservermongodbs";
    public static final ResourceDefinitionContext CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(GROUP)
            .withVersion(VERSION)
            .withKind(KIND)
            .withPlural(PLURAL)
            .withNamespaced(true)
            .build();

    private final KubernetesClient kubernetesClient;
    private final ObjectMapper objectMapper;

    public PsmdbFactory(
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

    public void createResource(Psmdb psmdb) {
        GenericKubernetesResource resource = buildResource(psmdb);

        kubernetesClient.genericKubernetesResources(
                        API_VERSION,
                        KIND
                )
                .inNamespace(psmdb.namespace())
                .resource(resource)
                .create();
    }

    public static String getPreparedName(String name) {
        return String.format("managed-mongodb-%s", name);
    }

    public static String getPreparedSecretName(String name) {
        return String.format("managed-mongodb-%s-secrets", name);
    }

    private GenericKubernetesResource buildResource(Psmdb psmdb) {
        var specMap = ObjectMapperUtils.convertToMap(objectMapper, psmdb.spec());

        return new GenericKubernetesResourceBuilder()
                .withApiVersion(API_VERSION)
                .withKind(KIND)
                .withNewMetadata()
                .withName(getPreparedName(psmdb.name()))
                .endMetadata()
                .addToAdditionalProperties("spec", specMap)
                .build();
    }
}

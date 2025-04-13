package com.onyxdb.mdb.clients.k8s.psmdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.ResourceDefinitionContext;

import com.onyxdb.mdb.clients.k8s.victoriaLogs.VictoriaLogsClient;
import com.onyxdb.mdb.utils.ObjectMapperUtils;
import com.onyxdb.mdb.utils.TemplateProvider;

public class PsmdbClient extends AbstractPsmdbFactory {
    private static final String GROUP = "psmdb.percona.com";
    private static final String VERSION = "v1";
    private static final String API_VERSION = GROUP + "/" + VERSION;
    private static final String KIND = "PerconaServerMongoDB";
    private static final ResourceDefinitionContext CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(GROUP)
            .withVersion(VERSION)
            .withKind(KIND)
            .withNamespaced(true)
            .build();

    private final KubernetesClient kubernetesClient;
    private final TemplateProvider templateProvider;
    private final VictoriaLogsClient victoriaLogsClient;

    public PsmdbClient(
            ObjectMapper objectMapper,
            KubernetesClient kubernetesClient,
            TemplateProvider templateProvider,
            VictoriaLogsClient victoriaLogsClient
    ) {
        super(objectMapper);
        this.kubernetesClient = kubernetesClient;
        this.templateProvider = templateProvider;
        this.victoriaLogsClient = victoriaLogsClient;
    }

    public GenericKubernetesResource getResource(String namespace, String name) {
        return kubernetesClient.genericKubernetesResources(CONTEXT)
                .inNamespace(namespace)
                .withName(getPsmdbName(name))
                .get();
    }

    public void createResource(Psmdb psmdb) {
        String resource = templateProvider.buildPsmdbCr(
                getPsmdbName(psmdb.name()),
                getSecretName(psmdb.name()),
                getVectorConfigMapName(psmdb.name())
        );

        kubernetesClient.resource(resource)
                .inNamespace(psmdb.namespace())
                .create();
    }

    public boolean isResourceReady(String namespace, String name) {
        GenericKubernetesResource resource = getResource(namespace, name);

        return getStateO(resource)
                .stream()
                .anyMatch(state -> state.equals(STATE_READY_VALUE));
    }

    public void createVectorConfig(
            String namespace,
            String project,
            String cluster
    ) {
        String resource = templateProvider.buildMongoVectorConfigMapManifest(
                getVectorConfigMapName(cluster),
                project,
                cluster,
                victoriaLogsClient.getBaseUrl()
        );
        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .serverSideApply();
    }

    public static String getPsmdbName(String cluster) {
        return String.format("managed-mongodb-%s", cluster);
    }

    public static String getSecretName(String cluster) {
        return String.format("managed-mongodb-%s-secrets", cluster);
    }

    public static String getVectorConfigMapName(String cluster) {
        return String.format("managed-mongodb-%s-vector-config", cluster);
    }

    private GenericKubernetesResource buildResource(Psmdb psmdb) {
        var specMap = ObjectMapperUtils.convertToMap(objectMapper, psmdb.spec());

        return new GenericKubernetesResourceBuilder()
                .withApiVersion(API_VERSION)
                .withKind(KIND)
                .withNewMetadata()
                .withName(getPsmdbName(psmdb.name()))
                .endMetadata()
                .addToAdditionalProperties("spec", specMap)
                .build();
    }
}

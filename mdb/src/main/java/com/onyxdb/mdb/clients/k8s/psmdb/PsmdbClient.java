package com.onyxdb.mdb.clients.k8s.psmdb;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.ResourceDefinitionContext;
import org.jetbrains.annotations.Nullable;

import com.onyxdb.mdb.clients.k8s.victoriaLogs.VictoriaLogsClient;
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

    private static final String REPLSET_NAME = "rs01";

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

    @Nullable
    public GenericKubernetesResource getResource(String namespace, String project, String cluster) {
        return kubernetesClient.genericKubernetesResources(CONTEXT)
                .inNamespace(namespace)
                .withName(getPsmdbName(project, cluster))
                .get();
    }

    public void applyPsmdbCr(
            String namespace,
            String project,
            String cluster,
            int replsetSize,
            double vcpu,
            long ram
    ) {
        String resource = templateProvider.buildPsmdbCr(
                getPsmdbName(project, cluster),
                getSecretName(project, cluster),
                getVectorConfigMapName(project, cluster),
                REPLSET_NAME,
                replsetSize,
                vcpu,
                ram
        );

        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .serverSideApply();
    }

    public void deletePsmdb(String namespace, String project, String cluster) {
        kubernetesClient.genericKubernetesResources(CONTEXT)
                .inNamespace(namespace)
                .withName(getPsmdbName(project, cluster))
                .delete();
    }

    // TODO get namespace from project data
    public boolean psmdbExists(String namespace, String project, String cluster) {
        return getResource(namespace, project, cluster) != null;
    }

    public boolean isResourceReady(String namespace, String project, String name) {
        GenericKubernetesResource resource = getResource(namespace, project, name);
        if (resource == null) {
            return false;
        }

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
                getVectorConfigMapName(project, cluster),
                project,
                cluster,
                victoriaLogsClient.getBaseUrl()
        );
        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .serverSideApply();
    }

    public void deleteVectorConfig(
            String namespace,
            String project,
            String cluster
    ) {
        String resource = templateProvider.buildMongoVectorConfigMapManifest(
                getVectorConfigMapName(project, cluster),
                project,
                cluster,
                victoriaLogsClient.getBaseUrl()
        );
        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .delete();
    }

    public List<String> getPsmdbPods(String namespace, String project, String cluster) {
        return kubernetesClient.pods()
                .inNamespace(namespace)
                .withLabels(Map.ofEntries(
                        Map.entry("app.kubernetes.io/instance", getPsmdbName(project, cluster))
                ))
                .list()
                .getItems()
                .stream()
                .map(p -> p.getMetadata().getName())
                .toList();
    }

    public static String getPsmdbName(String project, String cluster) {
        return String.format("managed-mongodb-%s-%s", project, cluster);
    }

    public static String getSecretName(String project, String cluster) {
        return String.format("managed-mongodb-%s-%s-secrets", project, cluster);
    }

    public static String getVectorConfigMapName(String project, String cluster) {
        return String.format("managed-mongodb-%s-%s-vector-config", project, cluster);
    }

//    public static List<String> getPsmdbPods(String cluster, ) {
//
//    }

//    private GenericKubernetesResource buildResource(Psmdb psmdb) {
//        var specMap = ObjectMapperUtils.convertToMap(objectMapper, psmdb.spec());
//
//        return new GenericKubernetesResourceBuilder()
//                .withApiVersion(API_VERSION)
//                .withKind(KIND)
//                .withNewMetadata()
//                .withName(getPsmdbName(psmdb.name()))
//                .endMetadata()
//                .addToAdditionalProperties("spec", specMap)
//                .build();
//    }
}

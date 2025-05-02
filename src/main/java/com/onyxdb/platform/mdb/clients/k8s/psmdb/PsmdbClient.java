package com.onyxdb.platform.mdb.clients.k8s.psmdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.ResourceDefinitionContext;
import org.jetbrains.annotations.Nullable;

import com.onyxdb.platform.mdb.clients.k8s.victoriaLogs.VictoriaLogsClient;
import com.onyxdb.platform.mdb.utils.TemplateProvider;

// TODO rewrite all
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
    private final ObjectMapper yamlObjectMapper;

    private static final String REPLSET_NAME = "rs0";

    public PsmdbClient(
            ObjectMapper objectMapper,
            KubernetesClient kubernetesClient,
            TemplateProvider templateProvider,
            VictoriaLogsClient victoriaLogsClient,
            ObjectMapper yamlObjectMapper
    ) {
        super(objectMapper);
        this.kubernetesClient = kubernetesClient;
        this.templateProvider = templateProvider;
        this.victoriaLogsClient = victoriaLogsClient;
        this.yamlObjectMapper = yamlObjectMapper;
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

    public static List<String> calculatePsmdbHostNames(String project, String cluster, int replicas) {
        List<String> hosts = new ArrayList<>(replicas);
        for (int i = 0; i < replicas; i++) {
            hosts.add(String.format("%s-%s-mongo-%s-%d", cluster, project, REPLSET_NAME, i));
        }

        return hosts;
    }

    public String applyMongoUserSecret(
            String namespace,
            String project,
            String cluster,
            String user,
            String password
    ) {
        String secretName = getMongoUserSecretName(project, cluster, user);
        // TODO add extra labels
        Secret secret = new SecretBuilder()
                .withNewMetadata()
                .withName(secretName)
                .endMetadata()
                .addToStringData("username", user)
                .addToStringData("password", password)
                .build();

        kubernetesClient.secrets()
                .inNamespace(namespace)
                .resource(secret)
                .serverSideApply();

        return secretName;
    }

    public boolean deleteMongoUserSecret(
            String namespace,
            String project,
            String cluster,
            String user
    ) {
        return kubernetesClient.secrets()
                .inNamespace(namespace)
                .withName(getMongoUserSecretName(project, cluster, user))
                .delete().size() == 1;
    }

    public void deletePsmdbSecrets(
            String namespace,
            String project,
            String cluster
    ) {
        System.err.println(getPsmdbName(project, cluster));
        var r = kubernetesClient.secrets()
                .inNamespace(namespace)
                .withLabels(Map.ofEntries(
                        Map.entry("app.kubernetes.io/instance", getPsmdbName(project, cluster))
                ))
//                .withLabel("app.kubernetes.io/instance", getPsmdbName(project, cluster))
//                .withName(getSecretName(project, cluster))
                .delete();
        System.err.println(r);
    }

    public static String getPsmdbName(String project, String cluster) {
        return String.format("%s-%s-mongo", cluster, project);
    }

    public static String getPsmdbRsServiceName(String project, String cluster) {
        return String.format("%s-%s-mongo-rs0", cluster, project);
    }

    public static String getSecretName(String project, String cluster) {
        return String.format("%s-%s-mongo-users", cluster, project);
    }

    public static String getVectorConfigMapName(String project, String cluster) {
        return String.format("%s-%s-mongo-vector", cluster, project);
    }

    public static String getMongoUserSecretName(String project, String cluster, String user) {
        return String.format("%s-%s-user-%s", cluster, project, user);
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
//                .withName(getPsmdbName(psmdb.database()))
//                .endMetadata()
//                .addToAdditionalProperties("spec", specMap)
//                .build();
//    }
}

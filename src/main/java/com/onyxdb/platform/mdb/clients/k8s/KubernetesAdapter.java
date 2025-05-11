package com.onyxdb.platform.mdb.clients.k8s;

import java.util.UUID;

import io.fabric8.kubernetes.client.KubernetesClient;

import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.utils.TemplateProvider;

public class KubernetesAdapter {
    private final KubernetesClient kubernetesClient;
    private final TemplateProvider templateProvider;
    private final String onyxdbBaseUrl;

    public KubernetesAdapter(
            KubernetesClient kubernetesClient,
            TemplateProvider templateProvider,
            String onyxdbBaseUrl
    ) {
        this.kubernetesClient = kubernetesClient;
        this.templateProvider = templateProvider;
        this.onyxdbBaseUrl = onyxdbBaseUrl;
    }

    public void applyOnyxdbAgent(
            String namespace,
            String project,
            UUID clusterId,
            String clusterName
    ) {
        String resource = templateProvider.buildOnyxdbAgent(
                getOnyxdbAgentName(clusterName, project),
                namespace,
                onyxdbBaseUrl,
                clusterId,
                PsmdbClient.getSecretName(project, clusterName),
                PsmdbClient.getPsmdbRsServiceName(project, clusterName)
        );

        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .serverSideApply();
    }

    public void applyOnyxdbAgentService(
            String namespace,
            String project,
            String clusterName
    ) {
        String resource = templateProvider.buildOnyxdbAgentService(getOnyxdbAgentName(clusterName, project));

        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .serverSideApply();
    }

    public void deleteOnyxdbAgentService(
            String namespace,
            String project,
            String clusterName
    ) {
        String resource = templateProvider.buildOnyxdbAgentService(getOnyxdbAgentName(clusterName, project));

        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .delete();
    }

    public boolean isOnyxdbAgentReady(
            String namespace,
            String project,
            UUID clusterId,
            String clusterName
    ) {
        String resource = templateProvider.buildOnyxdbAgent(
                getOnyxdbAgentName(clusterName, project),
                namespace,
                onyxdbBaseUrl,
                clusterId,
                PsmdbClient.getSecretName(project, clusterName),
                PsmdbClient.getPsmdbRsServiceName(project, clusterName)
        );

        return kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .isReady();
    }

    public void deleteOnyxdbAgent(
            String namespace,
            String project,
            UUID clusterId,
            String clusterName
    ) {
        String resource = templateProvider.buildOnyxdbAgent(
                getOnyxdbAgentName(clusterName, project),
                namespace,
                onyxdbBaseUrl,
                clusterId,
                PsmdbClient.getSecretName(project, clusterName),
                PsmdbClient.getPsmdbRsServiceName(project, clusterName)
        );

        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .delete();
    }

    public boolean onyxdbAgentExists(
            String namespace,
            String project,
            UUID clusterId,
            String clusterName
    ) {
        String resource = templateProvider.buildOnyxdbAgent(
                getOnyxdbAgentName(clusterName, project),
                namespace,
                onyxdbBaseUrl,
                clusterId,
                PsmdbClient.getSecretName(project, clusterName),
                PsmdbClient.getPsmdbRsServiceName(project, clusterName)
        );

        return kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .get() != null;
    }

    public static String getOnyxdbAgentName(String cluster, String project) {
        return String.format("%s-%s-mongo-agent", cluster, project);
    }
}

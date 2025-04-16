package com.onyxdb.mdb.clients.k8s;

import java.util.UUID;

import io.fabric8.kubernetes.client.KubernetesClient;

import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.mdb.utils.TemplateProvider;

import static com.onyxdb.mdb.core.clusters.ClusterMapper.DEFAULT_NAMESPACE;

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
            String project,
            UUID clusterId,
            String clusterName
    ) {
        String resource = templateProvider.buildOnyxdbAgent(
                getOnyxdbAgentName(clusterName, project),
                onyxdbBaseUrl,
                clusterId,
                PsmdbClient.getSecretName(project, clusterName),
                PsmdbClient.getPsmdbRsServiceName(project, clusterName)
        );

        kubernetesClient.resource(resource)
                .inNamespace(DEFAULT_NAMESPACE)
                .serverSideApply();
    }

    public static String getOnyxdbAgentName(String cluster, String project) {
        return String.format("%s-%s-mongo-agent", cluster, project);
    }
}

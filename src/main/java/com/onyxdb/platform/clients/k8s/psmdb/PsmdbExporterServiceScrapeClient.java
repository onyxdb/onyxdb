package com.onyxdb.platform.clients.k8s.psmdb;

import io.fabric8.kubernetes.client.KubernetesClient;

import com.onyxdb.platform.utils.TemplateProvider;

public class PsmdbExporterServiceScrapeClient {
    private final KubernetesClient kubernetesClient;
    private final TemplateProvider templateProvider;

    public PsmdbExporterServiceScrapeClient(
            KubernetesClient kubernetesClient,
            TemplateProvider templateProvider
    ) {
        this.kubernetesClient = kubernetesClient;
        this.templateProvider = templateProvider;
    }

    public void applyPsmdbExporterServiceScrape(
            String namespace,
            String project,
            String cluster
    ) {
        String resource = templateProvider.buildPsmdbExporterServiceScrape(
                getExporterServiceScrapeName(project, cluster),
                PsmdbExporterServiceClient.PORT_NAME,
                PsmdbExporterServiceClient.PATH,
                project,
                cluster,
                PsmdbExporterServiceClient.getExporterServiceName(project, cluster)
        );

        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .serverSideApply();
    }

    public void deletePsmdbExporterServiceScrape(
            String namespace,
            String project,
            String cluster
    ) {
        String resource = templateProvider.buildPsmdbExporterServiceScrape(
                getExporterServiceScrapeName(project, cluster),
                PsmdbExporterServiceClient.PORT_NAME,
                PsmdbExporterServiceClient.PATH,
                project,
                cluster,
                PsmdbExporterServiceClient.getExporterServiceName(project, cluster)
        );

        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .delete();
    }

    public static String getExporterServiceScrapeName(String project, String cluster) {
        return String.format("%s-%s-mongo-exporter", cluster, project);
    }
}

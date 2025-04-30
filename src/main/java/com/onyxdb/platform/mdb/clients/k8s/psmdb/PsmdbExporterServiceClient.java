package com.onyxdb.platform.mdb.clients.k8s.psmdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.client.KubernetesClient;

import com.onyxdb.platform.mdb.utils.TemplateProvider;

public class PsmdbExporterServiceClient extends AbstractPsmdbFactory {
    public static final String PORT_NAME = "metrics";
    public static final int PORT = 9216;
    public static final String PATH = "/metrics";

    private final KubernetesClient kubernetesClient;
    private final TemplateProvider templateProvider;

    public PsmdbExporterServiceClient(
            ObjectMapper objectMapper,
            KubernetesClient kubernetesClient,
            TemplateProvider templateProvider
    ) {
        super(objectMapper);
        this.kubernetesClient = kubernetesClient;
        this.templateProvider = templateProvider;
    }

    public void applyPsmdbExporterService(
            String namespace,
            String project,
            String cluster
    ) {
        String resource = templateProvider.buildPsmdbExporterService(
                getExporterServiceName(project, cluster),
                PORT_NAME,
                PORT,
                PsmdbClient.getPsmdbName(project, cluster)
        );

        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .serverSideApply();
    }

    public void deletePsmdbExporterService(
            String namespace,
            String project,
            String cluster
    ) {
        String resource = templateProvider.buildPsmdbExporterService(
                getExporterServiceName(project, cluster),
                PORT_NAME,
                PORT,
                PsmdbClient.getPsmdbName(project, cluster)
        );

        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .delete();
    }

//    public void deleteResource(PsmdbExporterService psmdbExporterService) {
//        Service service = buildService(psmdbExporterService);
//        kubernetesClient.resource(service).delete();
//    }

//    public boolean resourceExists(String namespace, String databaseName) {
//        return kubernetesClient.services()
//                .inNamespace(namespace)
//                .withName(PsmdbExporterServiceClient.getPreparedName(databaseName))
//                .get() != null;
//    }

//    public static String getPreparedName(String databaseName) {
//        return String.format("managed-mongodb-%s-exporter", databaseName);
//    }

    public static String getExporterServiceName(String project, String cluster) {
        return String.format("%s-%s-mongo-exporter", cluster, project);
    }

//    public static Map<String, String> getLabels(String databaseName) {
//        return Map.ofEntries(
//                Map.entry("app.kubernetes.io/instance", getPreparedName(getPreparedName(databaseName)))
//        );
//    }

//    private Service buildService(PsmdbExporterService psmdbExporterService) {
//        String namespace = psmdbExporterService.namespace();
//        String databaseName = psmdbExporterService.databaseName();
//
//        return new ServiceBuilder()
//                .withMetadata(
//                        new ObjectMetaBuilder()
//                                .withName(getPreparedName(databaseName))
//                                .withNamespace(namespace)
//                                .withLabels(getLabels(databaseName))
//                                .build()
//                )
//                .withSpec(
//                        new ServiceSpecBuilder()
//                                .withSelector(getLabels(databaseName))
//                                .withPorts(
//                                        new ServicePortBuilder()
//                                                .withName(PORT_NAME)
//                                                .withPort(PORT)
//                                                .build()
//                                )
//                                .build()
//                )
//                .build();
//    }
}

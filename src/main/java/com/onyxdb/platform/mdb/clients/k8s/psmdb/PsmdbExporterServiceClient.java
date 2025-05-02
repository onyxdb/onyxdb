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

//    public boolean resourceExists(String namespace, String database) {
//        return kubernetesClient.services()
//                .inNamespace(namespace)
//                .withName(PsmdbExporterServiceClient.getPreparedName(database))
//                .get() != null;
//    }

//    public static String getPreparedName(String database) {
//        return String.format("managed-mongodb-%s-exporter", database);
//    }

    public static String getExporterServiceName(String project, String cluster) {
        return String.format("%s-%s-mongo-exporter", cluster, project);
    }

//    public static Map<String, String> getLabels(String database) {
//        return Map.ofEntries(
//                Map.entry("app.kubernetes.io/instance", getPreparedName(getPreparedName(database)))
//        );
//    }

//    private Service buildService(PsmdbExporterService psmdbExporterService) {
//        String namespace = psmdbExporterService.namespace();
//        String database = psmdbExporterService.database();
//
//        return new ServiceBuilder()
//                .withMetadata(
//                        new ObjectMetaBuilder()
//                                .withName(getPreparedName(database))
//                                .withNamespace(namespace)
//                                .withLabels(getLabels(database))
//                                .build()
//                )
//                .withSpec(
//                        new ServiceSpecBuilder()
//                                .withSelector(getLabels(database))
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

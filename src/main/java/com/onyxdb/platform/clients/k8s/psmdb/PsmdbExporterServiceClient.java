package com.onyxdb.platform.clients.k8s.psmdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.client.KubernetesClient;

import com.onyxdb.platform.utils.TemplateProvider;

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

//    public boolean resourceExists(String namespace, String name) {
//        return kubernetesClient.services()
//                .inNamespace(namespace)
//                .withName(PsmdbExporterServiceClient.getPreparedName(name))
//                .get() != null;
//    }

//    public static String getPreparedName(String name) {
//        return String.format("managed-mongodb-%s-exporter", name);
//    }

    public static String getExporterServiceName(String project, String cluster) {
        return String.format("%s-%s-mongo-exporter", cluster, project);
    }

//    public static Map<String, String> getLabels(String name) {
//        return Map.ofEntries(
//                Map.entry("app.kubernetes.io/instance", getPreparedName(getPreparedName(name)))
//        );
//    }

//    private Service buildService(PsmdbExporterService psmdbExporterService) {
//        String namespace = psmdbExporterService.namespace();
//        String name = psmdbExporterService.name();
//
//        return new ServiceBuilder()
//                .withMetadata(
//                        new ObjectMetaBuilder()
//                                .withName(getPreparedName(name))
//                                .withNamespace(namespace)
//                                .withLabels(getLabels(name))
//                                .build()
//                )
//                .withSpec(
//                        new ServiceSpecBuilder()
//                                .withSelector(getLabels(name))
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

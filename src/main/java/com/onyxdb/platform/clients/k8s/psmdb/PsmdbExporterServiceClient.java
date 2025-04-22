package com.onyxdb.platform.clients.k8s.psmdb;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.ServicePortBuilder;
import io.fabric8.kubernetes.api.model.ServiceSpecBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

public class PsmdbExporterServiceClient extends AbstractPsmdbFactory {
    public static final String PORT_NAME = "metrics";
    public static final int PORT = 9216;
    public static final String PATH = "/metrics";

    private final KubernetesClient kubernetesClient;

    public PsmdbExporterServiceClient(
            ObjectMapper objectMapper,
            KubernetesClient kubernetesClient
    ) {
        super(objectMapper);
        this.kubernetesClient = kubernetesClient;
    }

    public void createResource(PsmdbExporterService psmdbExporterService) {
        Service service = buildService(psmdbExporterService);
        kubernetesClient.resource(service).create();
    }

    public void deleteResource(PsmdbExporterService psmdbExporterService) {
        Service service = buildService(psmdbExporterService);
        kubernetesClient.resource(service).delete();
    }

    public boolean resourceExists(String namespace, String name) {
        return kubernetesClient.services()
                .inNamespace(namespace)
                .withName(PsmdbExporterServiceClient.getPreparedName(name))
                .get() != null;
    }

    public static String getPreparedName(String name) {
        return String.format("managed-mongodb-%s-exporter", name);
    }

    public static Map<String, String> getLabels(String name) {
        return Map.ofEntries(
                Map.entry("app.kubernetes.io/instance", getPreparedName(getPreparedName(name)))
        );
    }

    private Service buildService(PsmdbExporterService psmdbExporterService) {
        String namespace = psmdbExporterService.namespace();
        String name = psmdbExporterService.name();

        return new ServiceBuilder()
                .withMetadata(
                        new ObjectMetaBuilder()
                                .withName(getPreparedName(name))
                                .withNamespace(namespace)
                                .withLabels(getLabels(name))
                                .build()
                )
                .withSpec(
                        new ServiceSpecBuilder()
                                .withSelector(getLabels(name))
                                .withPorts(
                                        new ServicePortBuilder()
                                                .withName(PORT_NAME)
                                                .withPort(PORT)
                                                .build()
                                )
                                .build()
                )
                .build();
    }
}

package com.onyxdb.mdb.clients.k8s.psmdb;

import java.util.Map;

import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.ServicePortBuilder;
import io.fabric8.kubernetes.api.model.ServiceSpecBuilder;

public record PsmdbExporterService(
        String clusterName,
        String namespace,
        String portName,
        int port
) {
    public Service asService() {
        return new ServiceBuilder()
                .withMetadata(
                        new ObjectMetaBuilder()
                                .withName(getServiceName(clusterName))
                                .withNamespace(namespace)
                                .withLabels(getServiceLabels(clusterName))
                                .build()
                )
                .withSpec(
                        new ServiceSpecBuilder()
                                .withSelector(Map.ofEntries(
                                        Map.entry("app.kubernetes.io/instance", Psmdb.getInstanceLabel(clusterName))
                                ))
                                .withPorts(
                                        new ServicePortBuilder()
                                                .withName(portName)
                                                .withPort(port)
                                                .build()
                                )
                                .build()
                )
                .build();
    }

    public static Map<String, String> getServiceLabels(String clusterName) {
        return Map.ofEntries(
                Map.entry("app.kubernetes.io/instance", getInstanceLabel(clusterName))
        );
    }

    public static String getServiceName(String clusterName) {
        return String.format("managed-mongodb-%s-exporter", clusterName);
    }

    private static String getInstanceLabel(String clusterName) {
        return getServiceName(clusterName);
    }
}

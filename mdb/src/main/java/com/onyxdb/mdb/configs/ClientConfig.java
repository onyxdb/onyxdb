package com.onyxdb.mdb.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbExporterServiceFactory;
import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbFactory;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.VmServiceScrapeFactory;

@Configuration
public class ClientConfig {
    @Bean
    public KubernetesClient kubernetesClient() {
        return new DefaultKubernetesClient();
    }

    @Bean
    public PsmdbFactory psmdbFactory(
            ObjectMapper objectMapper,
            KubernetesClient kubernetesClient
    ) {
        return new PsmdbFactory(
                objectMapper,
                kubernetesClient
        );
    }

    @Bean
    public PsmdbExporterServiceFactory psmdbExporterServiceFactory(
            ObjectMapper objectMapper,
            KubernetesClient kubernetesClient
    ) {
        return new PsmdbExporterServiceFactory(
                objectMapper,
                kubernetesClient
        );
    }

    @Bean
    public VmServiceScrapeFactory vmServiceScrapeFactory(
            KubernetesClient kubernetesClient,
            ObjectMapper objectMapper
    ) {
        return new VmServiceScrapeFactory(
                kubernetesClient,
                objectMapper
        );
    }
}

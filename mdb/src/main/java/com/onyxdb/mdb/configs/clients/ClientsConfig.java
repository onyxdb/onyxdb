package com.onyxdb.mdb.configs.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbExporterServiceClient;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.VmServiceScrapeClient;

@Configuration
public class ClientsConfig {
    @Bean
    public KubernetesClient kubernetesClient() {
        return new DefaultKubernetesClient();
    }

    @Bean
    public PsmdbClient psmdbClient(
            ObjectMapper objectMapper,
            KubernetesClient kubernetesClient
    ) {
        return new PsmdbClient(
                objectMapper,
                kubernetesClient
        );
    }

    @Bean
    public PsmdbExporterServiceClient psmdbExporterServiceClient(
            ObjectMapper objectMapper,
            KubernetesClient kubernetesClient
    ) {
        return new PsmdbExporterServiceClient(
                objectMapper,
                kubernetesClient
        );
    }

    @Bean
    public VmServiceScrapeClient vmServiceScrapeClient(
            KubernetesClient kubernetesClient,
            ObjectMapper objectMapper
    ) {
        return new VmServiceScrapeClient(
                kubernetesClient,
                objectMapper
        );
    }
}

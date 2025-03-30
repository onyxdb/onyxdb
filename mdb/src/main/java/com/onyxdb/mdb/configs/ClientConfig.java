package com.onyxdb.mdb.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.clients.psmdb.PsmdbClient;

@Configuration
public class ClientConfig {
    @Bean
    public KubernetesClient kubernetesClient() {
        return new DefaultKubernetesClient();
    }

    @Bean
    public PsmdbClient psmdbClient(
            KubernetesClient kubernetesClient,
            ObjectMapper objectMapper
    ) {
        return new PsmdbClient(
                kubernetesClient,
                objectMapper
        );
    }
}

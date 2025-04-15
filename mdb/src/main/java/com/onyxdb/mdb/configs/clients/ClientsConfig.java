package com.onyxdb.mdb.configs.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbExporterServiceClient;
import com.onyxdb.mdb.clients.k8s.victoriaLogs.VictoriaLogsClient;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.VmServiceScrapeClient;
import com.onyxdb.mdb.utils.TemplateProvider;

@Configuration
public class ClientsConfig {
    @Bean
    public KubernetesClient kubernetesClient() {
        return new DefaultKubernetesClient();
    }

    @Bean
    public VictoriaLogsClient victoriaLogsClient(
            @Value("${onyxdb-app.victoria-logs.base-url}")
            String baseUrl
    ) {
        return new VictoriaLogsClient(baseUrl);
    }

    @Bean
    public PsmdbClient psmdbClient(
            ObjectMapper objectMapper,
            KubernetesClient kubernetesClient,
            TemplateProvider templateProvider,
            VictoriaLogsClient victoriaLogsClient
    ) {
        return new PsmdbClient(
                objectMapper,
                kubernetesClient,
                templateProvider,
                victoriaLogsClient
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

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setJmxEnabled(false);

        return config;
    }

    @Bean
    public JedisPool jedisPool(
            JedisPoolConfig config,
            @Value("${onyxdb.redis.host}")
            String host,
            @Value("${onyxdb.redis.port}")
            int port
    ) {
        return new JedisPool(config, host, port);
    }
}

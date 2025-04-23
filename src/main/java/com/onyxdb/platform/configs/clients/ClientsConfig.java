package com.onyxdb.platform.configs.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.onyxdb.platform.clients.k8s.KubernetesAdapter;
import com.onyxdb.platform.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.clients.k8s.psmdb.PsmdbExporterServiceClient;
import com.onyxdb.platform.clients.k8s.psmdb.PsmdbExporterServiceScrapeClient;
import com.onyxdb.platform.clients.k8s.victoriaLogs.VictoriaLogsClient;
import com.onyxdb.platform.clients.k8s.victoriaMetrics.VmServiceScrapeClient;
import com.onyxdb.platform.utils.TemplateProvider;

@Configuration
public class ClientsConfig {
    @Bean
    public KubernetesClient kubernetesClient() {
        return new DefaultKubernetesClient();
    }

    @Bean
    public KubernetesAdapter kubernetesAdapter(
            KubernetesClient kubernetesClient,
            TemplateProvider templateProvider,
            @Value("${onyxdb.base-url}")
            String onyxdbBaseUrl
    ) {
        return new KubernetesAdapter(
                kubernetesClient,
                templateProvider,
                onyxdbBaseUrl
        );
    }

    @Bean
    public VictoriaLogsClient victoriaLogsClient(
            @Value("${onyxdb.victoria-logs.base-url}")
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
            KubernetesClient kubernetesClient,
            TemplateProvider templateProvider
    ) {
        return new PsmdbExporterServiceClient(
                objectMapper,
                kubernetesClient,
                templateProvider
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

    @Bean
    public PsmdbExporterServiceScrapeClient psmdbExporterServiceScrapeClient(
            KubernetesClient kubernetesClient,
            TemplateProvider templateProvider
    ) {
        return new PsmdbExporterServiceScrapeClient(
                kubernetesClient,
                templateProvider
        );
    }
}

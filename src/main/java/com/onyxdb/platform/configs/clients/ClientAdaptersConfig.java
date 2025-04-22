package com.onyxdb.platform.configs.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.platform.clients.k8s.victoriaMetrics.adapters.MongoExporterServiceScrapeAdapter;

@Configuration
public class ClientAdaptersConfig {
    @Bean
    public MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter() {
        return new MongoExporterServiceScrapeAdapter();
    }
}

package com.onyxdb.mdb.configs.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.clients.k8s.victoriaMetrics.adapters.MongoExporterServiceScrapeAdapter;

@Configuration
public class ClientAdaptersConfig {
    @Bean
    public MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter() {
        return new MongoExporterServiceScrapeAdapter();
    }
}

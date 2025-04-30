package com.onyxdb.platform.mdb.context.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.platform.mdb.clients.k8s.victoriaMetrics.adapters.MongoExporterServiceScrapeAdapter;

@Configuration
public class ClientAdaptersContextConfiguration {
    @Bean
    public MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter() {
        return new MongoExporterServiceScrapeAdapter();
    }
}

package com.onyxdb.mdb.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.core.clusters.ClusterMapper;

/**
 * @author foxleren
 */
@Configuration
public class MapperConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ClusterMapper clusterMapper() {
        return new ClusterMapper();
    }
}

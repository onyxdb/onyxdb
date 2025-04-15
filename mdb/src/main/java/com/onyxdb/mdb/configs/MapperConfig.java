package com.onyxdb.mdb.configs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.core.clusters.ClusterMapper;
import com.onyxdb.mdb.core.clusters.mappers.HostMapper;

/**
 * @author foxleren
 */
@Configuration
public class MapperConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
    }

    @Bean
    public ClusterMapper clusterMapper(ObjectMapper objectMapper) {
        return new ClusterMapper(objectMapper);
    }

    @Bean
    public HostMapper hostMapper() {
        return new HostMapper();
    }
}

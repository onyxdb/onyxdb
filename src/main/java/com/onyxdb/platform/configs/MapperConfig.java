package com.onyxdb.platform.configs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.platform.billing.BillingMapper;
import com.onyxdb.platform.core.clusters.ClusterMapper;
import com.onyxdb.platform.core.clusters.mappers.DatabaseMapper;
import com.onyxdb.platform.core.clusters.mappers.HostMapper;
import com.onyxdb.platform.core.clusters.mappers.UserMapper;
import com.onyxdb.platform.operations.OperationMapper;
import com.onyxdb.platform.quotas.QuotaMapper;
import com.onyxdb.platform.resources.ResourceMapper;

/**
 * @author foxleren
 */
@Configuration
public class MapperConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
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

    @Bean
    public DatabaseMapper databaseMapper() {
        return new DatabaseMapper();
    }

    @Bean
    public UserMapper userMapper(ObjectMapper objectMapper) {
        return new UserMapper(objectMapper);
    }

    @Bean
    public ResourceMapper resourceMapper() {
        return new ResourceMapper();
    }

    @Bean
    public QuotaMapper quotaMapper() {
        return new QuotaMapper();
    }

    @Bean
    public BillingMapper billingMapper() {
        return new BillingMapper();
    }

    @Bean
    public OperationMapper operationMapper() {
        return new OperationMapper();
    }
}

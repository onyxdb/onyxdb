package com.onyxdb.platform.mdb.context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.platform.mdb.billing.BillingMapper;
import com.onyxdb.platform.mdb.clusters.ClusterMapper;
import com.onyxdb.platform.mdb.databases.DatabaseMapper;
import com.onyxdb.platform.mdb.hosts.HostMapper;
import com.onyxdb.platform.mdb.scheduling.operations.OperationMapper;
import com.onyxdb.platform.mdb.projects.ProjectMapper;
import com.onyxdb.platform.mdb.quotas.QuotaMapper;
import com.onyxdb.platform.mdb.resources.ResourceMapper;
import com.onyxdb.platform.mdb.users.UserMapper;

/**
 * @author foxleren
 */
@Configuration
public class MapperContextConfiguration {
    public static final String YAML_OBJECT_MAPPER_BEAN = "YAML_OBJECT_MAPPER";

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
    }

    @Bean(YAML_OBJECT_MAPPER_BEAN)
    public ObjectMapper yamlObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.findAndRegisterModules();

        return objectMapper;
    }

    @Bean
    public ProjectMapper projectMapper() {
        return new ProjectMapper();
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

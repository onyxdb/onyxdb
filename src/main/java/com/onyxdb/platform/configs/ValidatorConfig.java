package com.onyxdb.platform.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.platform.core.clusters.validators.V1ManagedMongoApiValidator;

/**
 * @author foxleren
 */
@Configuration
public class ValidatorConfig {
    @Bean
    public V1ManagedMongoApiValidator v1ManagedMongoApiValidator() {
        return new V1ManagedMongoApiValidator();
    }
}

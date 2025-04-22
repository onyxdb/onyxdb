package com.onyxdb.platform.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.onyxdb.platform.utils.TemplateProvider;

@Configuration
public class UtilsConfig {
    @Bean
    public TemplateProvider templateProvider(SpringTemplateEngine templateEngine) {
        return new TemplateProvider(templateEngine);
    }
}

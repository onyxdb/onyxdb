package com.onyxdb.mdb.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.onyxdb.mdb.utils.TemplateProvider;

@Configuration
public class UtilsConfig {
    @Bean
    public TemplateProvider templateProvider(SpringTemplateEngine templateEngine) {
        return new TemplateProvider(templateEngine);
    }
}

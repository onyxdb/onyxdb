package com.onyxdb.platform.mdb.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.onyxdb.platform.mdb.utils.TemplateProvider;

@Configuration
public class UtilsContextConfiguration {
    @Bean
    public TemplateProvider templateProvider(SpringTemplateEngine templateEngine) {
        return new TemplateProvider(templateEngine);
    }
}

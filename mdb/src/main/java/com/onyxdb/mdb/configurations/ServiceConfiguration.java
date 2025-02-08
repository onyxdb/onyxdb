package com.onyxdb.mdb.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.repositories.ProjectRepository;
import com.onyxdb.mdb.services.ProjectService;

/**
 * @author foxleren
 */
@Configuration
public class ServiceConfiguration {
    @Bean
    public ProjectService projectService(ProjectRepository projectRepository) {
        return new ProjectService(projectRepository);
    }
}

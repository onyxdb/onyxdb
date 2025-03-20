package com.onyxdb.mdb.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.common.MemoriousTransactionTemplate;
import com.onyxdb.mdb.repositories.ClusterRepository;
import com.onyxdb.mdb.repositories.ProjectRepository;
import com.onyxdb.mdb.services.ProjectService;

/**
 * @author foxleren
 */
@Configuration
public class ServiceConfiguration {
    @Bean
    public ProjectService projectService(
            ProjectRepository projectRepository,
            ClusterRepository clusterRepository,
            MemoriousTransactionTemplate memoriousTransactionTemplate
    ) {
        return new ProjectService(
                projectRepository,
                clusterRepository,
                memoriousTransactionTemplate
        );
    }
}

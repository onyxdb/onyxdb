package com.onyxdb.mdb.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.core.projects.ProjectRepository;
import com.onyxdb.mdb.core.projects.ProjectService;
import com.onyxdb.mdb.core.resourcePresets.ResourcePresetRepository;
import com.onyxdb.mdb.core.resourcePresets.ResourcePresetService;
import com.onyxdb.mdb.core.zones.ZoneRepository;
import com.onyxdb.mdb.core.zones.ZoneService;

/**
 * @author foxleren
 */
@Configuration
public class ServiceConfiguration {
    @Bean
    public ResourcePresetService resourcePresetService(ResourcePresetRepository resourcePresetRepository) {
        return new ResourcePresetService(resourcePresetRepository);
    }

    @Bean
    public ZoneService zoneService(ZoneRepository zoneRepository) {
        return new ZoneService(zoneRepository);
    }

    @Bean
    public ProjectService projectService(ProjectRepository projectRepository) {
        return new ProjectService(projectRepository);
    }
}

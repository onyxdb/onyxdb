package com.onyxdb.mdb.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

import com.onyxdb.mdb.core.clusters.ClusterMapper;
import com.onyxdb.mdb.core.clusters.mappers.DatabaseMapper;
import com.onyxdb.mdb.core.clusters.mappers.HostMapper;
import com.onyxdb.mdb.core.clusters.mappers.UserMapper;
import com.onyxdb.mdb.core.clusters.repositories.ClusterPostgresRepository;
import com.onyxdb.mdb.core.clusters.repositories.ClusterRepository;
import com.onyxdb.mdb.core.clusters.repositories.DatabasePostgresRepository;
import com.onyxdb.mdb.core.clusters.repositories.DatabaseRepository;
import com.onyxdb.mdb.core.clusters.repositories.EnrichedHostRedisRepository;
import com.onyxdb.mdb.core.clusters.repositories.EnrichedHostRepository;
import com.onyxdb.mdb.core.clusters.repositories.HostPostgresRepository;
import com.onyxdb.mdb.core.clusters.repositories.HostRepository;
import com.onyxdb.mdb.core.clusters.repositories.UserPostgresRepository;
import com.onyxdb.mdb.core.clusters.repositories.UserRepository;
import com.onyxdb.mdb.core.projects.ProjectPostgresRepository;
import com.onyxdb.mdb.core.projects.ProjectRepository;
import com.onyxdb.mdb.core.resourcePresets.ResourcePresetPostgresRepository;
import com.onyxdb.mdb.core.resourcePresets.ResourcePresetRepository;
import com.onyxdb.mdb.core.zones.ZonePostgresRepository;
import com.onyxdb.mdb.core.zones.ZoneRepository;
import com.onyxdb.mdb.quotas.QuotaMapper;
import com.onyxdb.mdb.quotas.QuotaPostgresRepository;
import com.onyxdb.mdb.quotas.QuotaRepository;
import com.onyxdb.mdb.resources.ResourceMapper;
import com.onyxdb.mdb.resources.ResourcePostgresRepository;

/**
 * @author foxleren
 */
@Configuration
public class RepositoryConfig {
    @Bean
    public ResourcePresetRepository resourcePresetRepository(DSLContext dslContext) {
        return new ResourcePresetPostgresRepository(dslContext);
    }

    @Bean
    public ZoneRepository zoneRepository(DSLContext dslContext) {
        return new ZonePostgresRepository(dslContext);
    }

    @Bean
    public ProjectRepository projectRepository(DSLContext dslContext) {
        return new ProjectPostgresRepository(dslContext);
    }

    @Bean
    public ClusterRepository clusterRepository(
            DSLContext dslContext,
            ClusterMapper clusterMapper,
            ObjectMapper objectMapper
    ) {
        return new ClusterPostgresRepository(
                dslContext,
                clusterMapper,
                objectMapper
        );
    }

    @Bean
    public HostRepository hostRepository(
            DSLContext dslContext,
            HostMapper hostMapper
    ) {
        return new HostPostgresRepository(
                dslContext,
                hostMapper
        );
    }

    @Bean
    public EnrichedHostRepository enrichedHostRepository(
            JedisPool jedisPool,
            ObjectMapper objectMapper
    ) {
        return new EnrichedHostRedisRepository(
                jedisPool,
                objectMapper
        );
    }

    @Bean
    public DatabaseRepository databaseRepository(DSLContext dslContext, DatabaseMapper databaseMapper) {
        return new DatabasePostgresRepository(dslContext, databaseMapper);
    }

    @Bean
    public UserRepository userRepository(
            DSLContext dslContext,
            UserMapper userMapper,
            ObjectMapper objectMapper
    ) {
        return new UserPostgresRepository(
                dslContext,
                userMapper,
                objectMapper
        );
    }

    @Bean
    public ResourcePostgresRepository resourcePostgresRepository(
            DSLContext dslContext,
            ResourceMapper resourceMapper
    ) {
        return new ResourcePostgresRepository(
                dslContext,
                resourceMapper
        );
    }

    @Bean
    public QuotaRepository quotaRepository(
            DSLContext dslContext,
            QuotaMapper quotaMapper,
            ResourceMapper resourceMapper
    ) {
        return new QuotaPostgresRepository(
                dslContext,
                quotaMapper,
                resourceMapper
        );
    }
}

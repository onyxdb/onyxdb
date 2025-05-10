package com.onyxdb.platform.mdb.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.JedisPool;

import com.onyxdb.platform.mdb.billing.BillingClickhouseRepository;
import com.onyxdb.platform.mdb.billing.BillingRepository;
import com.onyxdb.platform.mdb.clusters.ClusterMapper;
import com.onyxdb.platform.mdb.clusters.ClusterPostgresRepository;
import com.onyxdb.platform.mdb.clusters.ClusterRepository;
import com.onyxdb.platform.mdb.databases.DatabaseMapper;
import com.onyxdb.platform.mdb.databases.DatabasePostgresRepository;
import com.onyxdb.platform.mdb.databases.DatabaseRepository;
import com.onyxdb.platform.mdb.hosts.EnrichedHostRedisRepository;
import com.onyxdb.platform.mdb.hosts.EnrichedHostRepository;
import com.onyxdb.platform.mdb.hosts.HostMapper;
import com.onyxdb.platform.mdb.hosts.HostPostgresRepository;
import com.onyxdb.platform.mdb.hosts.HostRepository;
import com.onyxdb.platform.mdb.projects.ProjectMapper;
import com.onyxdb.platform.mdb.projects.ProjectPostgresRepository;
import com.onyxdb.platform.mdb.projects.ProjectRepository;
import com.onyxdb.platform.mdb.quotas.QuotaMapper;
import com.onyxdb.platform.mdb.quotas.QuotaPostgresRepository;
import com.onyxdb.platform.mdb.quotas.QuotaRepository;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetMapper;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetPostgresRepository;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetRepository;
import com.onyxdb.platform.mdb.resources.ResourceMapper;
import com.onyxdb.platform.mdb.resources.ResourcePostgresRepository;
import com.onyxdb.platform.mdb.users.UserMapper;
import com.onyxdb.platform.mdb.users.UserPostgresRepository;
import com.onyxdb.platform.mdb.users.UserRepository;
import com.onyxdb.platform.mdb.zones.ZonePostgresRepository;
import com.onyxdb.platform.mdb.zones.ZoneRepository;

/**
 * @author foxleren
 */
@Configuration
public class RepositoryContextConfiguration {
    @Bean
    public ResourcePresetRepository resourcePresetRepository(
            DSLContext dslContext,
            ResourcePresetMapper resourcePresetMapper
    ) {
        return new ResourcePresetPostgresRepository(dslContext, resourcePresetMapper);
    }

    @Bean
    public ZoneRepository zoneRepository(DSLContext dslContext) {
        return new ZonePostgresRepository(dslContext);
    }

    @Bean
    public ProjectRepository projectRepository(
            DSLContext dslContext,
            ProjectMapper projectMapper
    ) {
        return new ProjectPostgresRepository(
                dslContext,
                projectMapper
        );
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

    @Bean
    public BillingRepository billingRepository(
            @Qualifier(DatasourceContextConfiguration.CLICKHOUSE_JDBC_TEMPLATE_BEAN)
            JdbcTemplate jdbcTemplate
    ) {
        return new BillingClickhouseRepository(jdbcTemplate);
    }
}

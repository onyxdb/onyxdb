package com.onyxdb.platform.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.JedisPool;

import com.onyxdb.platform.billing.BillingClickhouseRepository;
import com.onyxdb.platform.billing.BillingRepository;
import com.onyxdb.platform.core.projects.ProjectPostgresRepository;
import com.onyxdb.platform.core.projects.ProjectRepository;
import com.onyxdb.platform.core.resourcePresets.ResourcePresetPostgresRepository;
import com.onyxdb.platform.core.resourcePresets.ResourcePresetRepository;
import com.onyxdb.platform.core.zones.ZonePostgresRepository;
import com.onyxdb.platform.core.zones.ZoneRepository;
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
import com.onyxdb.platform.mdb.users.UserMapper;
import com.onyxdb.platform.mdb.users.UserPostgresRepository;
import com.onyxdb.platform.mdb.users.UserRepository;
import com.onyxdb.platform.quotas.QuotaMapper;
import com.onyxdb.platform.quotas.QuotaPostgresRepository;
import com.onyxdb.platform.quotas.QuotaRepository;
import com.onyxdb.platform.resources.ResourceMapper;
import com.onyxdb.platform.resources.ResourcePostgresRepository;

/**
 * @author foxleren
 */
@Configuration
public class RepositoryContextConfiguration {
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

    @Bean
    public BillingRepository billingRepository(
            @Qualifier(DatasourceContextConfiguration.CLICKHOUSE_JDBC_TEMPLATE_BEAN)
            JdbcTemplate jdbcTemplate
    ) {
        return new BillingClickhouseRepository(jdbcTemplate);
    }
}

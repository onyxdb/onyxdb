package com.onyxdb.platform.mdb.context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.idm.repositories.AccountRepository;
import com.onyxdb.platform.idm.services.AuthService;
import com.onyxdb.platform.idm.services.RoleService;
import com.onyxdb.platform.mdb.initialization.InitializationRepository;
import com.onyxdb.platform.mdb.operations.ConsumeTasksWorker;
import com.onyxdb.platform.mdb.operations.OperationService;
import com.onyxdb.platform.mdb.operations.consumers.CompositeTaskConsumer;
import com.onyxdb.platform.mdb.utils.SpringProfileManager;

@Configuration
public class OnyxdbCommonContextConfiguration {
    public static final String CONSUME_TASKS_WORKER_EXECUTOR_BEAN = "consumeTasksWorkerExecutor";

    @Bean
    public OnyxdbInitializer onyxdbInitializer(
            @Value("${onyxdb.mdb.disabled-kube}")
            boolean disabledKube,
            @Value("${onyxdb.mdb.self-namespace}")
            String selfNamespace,
            @Qualifier(FlywayContextConfiguration.POSTGRES_FLYWAY_BEAN_NAME)
            Flyway postgresFlyway,
            @Qualifier(FlywayContextConfiguration.CLICKHOUSE_FLYWAY_BEAN_NAME)
            Flyway clickhouseFlyway,
            TransactionTemplate transactionTemplate,
            InitializationRepository initializationRepository,
            AccountRepository accountRepository,
            RoleService roleService,
            KubernetesClient kubernetesClient,
            AuthService authService,
            SpringProfileManager springProfileManager
    ) {
        return new OnyxdbInitializer(
                disabledKube,
                selfNamespace,
                transactionTemplate,
                initializationRepository,
                accountRepository,
                roleService,
                kubernetesClient,
                authService,
                springProfileManager
        );
    }

    @Bean(name = CONSUME_TASKS_WORKER_EXECUTOR_BEAN)
    public ExecutorService processClusterTasksWorkerExecutor() {
        ThreadFactory threadFactory = new BasicThreadFactory.Builder()
                .namingPattern("consume-tasks-worker-%d")
                .build();

        return Executors.newSingleThreadExecutor(threadFactory);
    }

    @Bean
    @Profile("!test")
    public ConsumeTasksWorker taskProcessingWorker(
            @Value("${onyxdb.workers.process-cluster-tasks.min-threads}")
            int minThreads,
            @Value("${onyxdb.workers.process-cluster-tasks.max-threads}")
            int maxThreads,
            @Value("${onyxdb.workers.process-cluster-tasks.polling-interval-seconds}")
            int pollingIntervalSeconds,
            CompositeTaskConsumer compositeTaskConsumer,
            OperationService operationService
    ) {
        return new ConsumeTasksWorker(
                minThreads,
                maxThreads,
                pollingIntervalSeconds,
                compositeTaskConsumer,
                operationService
        );
    }
}

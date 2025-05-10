package com.onyxdb.platform.mdb.context;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.Permission;
import com.onyxdb.platform.idm.models.Role;
import com.onyxdb.platform.idm.models.RoleWithPermissions;
import com.onyxdb.platform.idm.repositories.AccountRepository;
import com.onyxdb.platform.idm.services.RoleService;
import com.onyxdb.platform.mdb.initialization.InitializationRepository;


/**
 * @author ArtemFed
 */
public class OnyxdbInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(OnyxdbInitializer.class);

    public static final UUID ADMIN_ID = UUID.fromString("4a2770da-c806-4ae2-8e02-3b54641463df");

    private final TransactionTemplate transactionTemplate;
    private final InitializationRepository initializationRepository;
    private final AccountRepository accountRepository;
    private final RoleService roleService;

    public OnyxdbInitializer(
            TransactionTemplate transactionTemplate,
            InitializationRepository initializationRepository,
            AccountRepository accountRepository,
            RoleService roleService
    ) {
        this.transactionTemplate = transactionTemplate;
        this.initializationRepository = initializationRepository;
        this.accountRepository = accountRepository;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) {
        try {
            initialize();
        } catch (Exception e) {
            throw new RuntimeException("Failed to perform initialization", e);
        }
    }

    private void initialize() {
        transactionTemplate.executeWithoutResult(status -> {
            boolean isInitialized = initializationRepository.getIsInitializedForUpdate();

            if (isInitialized) {
                logger.info("Initialization is already performed, skipping");
                return;
            }

            logger.info("Started initialization");

            String login = "admin";
            String password = "admin";
            Account admin = new Account(
                    ADMIN_ID,
                    login,
                    password,
                    "admin@example.com",
                    "Admin",
                    "Admin",
                    Map.of(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            Account newAccount = accountRepository.create(admin);

            Role role = new Role(
                    null,
                    "Admin",
                    "admin",
                    "admin",
                    "Initial admin",
                    true,
                    null,
                    null,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            Permission permission = new Permission(
                    null,
                    "any",
                    null,
                    Map.of(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            RoleWithPermissions newRole = roleService.create(new RoleWithPermissions(role, List.of(permission)));
            accountRepository.addRole(newAccount.id(), newRole.role().id());

            logger.info("Created admin account: login={}, password={}", login, password);

            initializationRepository.markAsInitialized();

            logger.info("Initialization is completed");
        });
    }
}

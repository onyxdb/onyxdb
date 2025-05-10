package com.onyxdb.platform.mdb.context;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.Permission;
import com.onyxdb.platform.idm.models.Role;
import com.onyxdb.platform.idm.models.RoleWithPermissions;
import com.onyxdb.platform.idm.repositories.AccountRepository;
import com.onyxdb.platform.idm.services.AuthService;
import com.onyxdb.platform.idm.services.RoleService;
import com.onyxdb.platform.idm.services.jwt.JwtResponse;
import com.onyxdb.platform.mdb.initialization.InitializationRepository;
import com.onyxdb.platform.mdb.utils.PasswordGenerator;
import com.onyxdb.platform.mdb.utils.SpringProfileManager;


/**
 * @author ArtemFed
 */
public class OnyxdbInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(OnyxdbInitializer.class);

    // TODO don't hardcode admin id
    public static final UUID ADMIN_ID = UUID.fromString("4a2770da-c806-4ae2-8e02-3b54641463df");
    public static final String ONYXDB_ROBOT_SECRET = "onyxdb-robot";

    private final boolean disabledKube;
    private final String selfNamespace;
    private final TransactionTemplate transactionTemplate;
    private final InitializationRepository initializationRepository;
    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final KubernetesClient kubernetesClient;
    private final AuthService authService;
    private final SpringProfileManager springProfileManager;

    public OnyxdbInitializer(
            boolean disabledKube,
            String selfNamespace,
            TransactionTemplate transactionTemplate,
            InitializationRepository initializationRepository,
            AccountRepository accountRepository,
            RoleService roleService,
            KubernetesClient kubernetesClient,
            AuthService authService,
            SpringProfileManager springProfileManager
    ) {
        this.disabledKube = disabledKube;
        this.selfNamespace = selfNamespace;
        this.transactionTemplate = transactionTemplate;
        this.initializationRepository = initializationRepository;
        this.accountRepository = accountRepository;
        this.roleService = roleService;
        this.kubernetesClient = kubernetesClient;
        this.authService = authService;
        this.springProfileManager = springProfileManager;
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

            UUID adminRoleId = createAdminRole();
            createAdminAccount(
                    ADMIN_ID,
                    "admin",
                    "admin",
                    "admin@example.com",
                    "Admin",
                    "Admin",
                    adminRoleId
            );
            createOnyxdbRobotAccount(adminRoleId);

            initializationRepository.markAsInitialized();
            logger.info("Initialization is completed");
        });
    }

    private void createOnyxdbRobotAccount(UUID adminRoleId) {
        String login = "onyxdb-robot";
        String password = PasswordGenerator.generatePassword();

        createAdminAccount(
                UUID.randomUUID(),
                login,
                password,
                "onyxdb-robot@example.com",
                "onyxdb-robot",
                "onyxdb-robot",
                adminRoleId
        );

        JwtResponse tokens = authService.generateServiceToken(login, password);

        if (!springProfileManager.isTestProfile() && !disabledKube) {
            Secret secret = new SecretBuilder()
                    .withNewMetadata()
                    .withName(ONYXDB_ROBOT_SECRET)
                    .endMetadata()
                    .addToStringData("login", login)
                    .addToStringData("password", password)
                    .addToStringData("accessToken", tokens.getAccessToken())
                    .addToStringData("refreshToken", tokens.getRefreshToken())
                    .build();

            kubernetesClient.secrets()
                    .inNamespace(selfNamespace)
                    .resource(secret)
                    .serverSideApply();
        }
    }

    private void createAdminAccount(
            UUID id,
            String login,
            String password,
            String email,
            String firstName,
            String lastName,
            UUID adminRoleId
    ) {
        Account admin = new Account(
                id,
                login,
                password,
                email,
                firstName,
                lastName,
                Map.of(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Account newAccount = accountRepository.create(admin);
        accountRepository.addRole(newAccount.id(), adminRoleId);
    }

    private UUID createAdminRole() {
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

        return newRole.role().id();
    }
}

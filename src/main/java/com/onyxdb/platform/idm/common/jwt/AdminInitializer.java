package com.onyxdb.platform.idm.common.jwt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.clickhouse.logging.Logger;
import com.clickhouse.logging.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.Permission;
import com.onyxdb.platform.idm.models.Role;
import com.onyxdb.platform.idm.models.RoleWithPermissions;
import com.onyxdb.platform.idm.repositories.AccountRepository;
import com.onyxdb.platform.idm.services.RoleService;


/**
 * @author ArtemFed
 */
@Configuration
public class AdminInitializer {
    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

    public static final UUID ADMIN_ID = UUID.fromString("4a2770da-c806-4ae2-8e02-3b54641463df");

    @Bean
    public CommandLineRunner createAdminAccount(
            AccountRepository accountRepository,
            RoleService roleService
    ) {
        return args -> {
            // Проверяем, пустая ли база
            if (accountRepository.count() == 0) {
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
                        UUID.randomUUID(),
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
                        UUID.randomUUID(),
                        "any",
                        null,
                        Map.of(),
                        LocalDateTime.now(),
                        LocalDateTime.now()
                );
                RoleWithPermissions newRole = roleService.create(new RoleWithPermissions(role, List.of(permission)));
                accountRepository.addRole(newAccount.id(), newRole.role().id());

                logger.info("Created admin account: login={}, password={}", login, password);
            }
        };
    }
}
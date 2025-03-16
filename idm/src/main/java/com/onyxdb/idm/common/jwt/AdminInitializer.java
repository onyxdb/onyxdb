package com.onyxdb.idm.common.jwt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.Permission;
import com.onyxdb.idm.models.Role;
import com.onyxdb.idm.models.RoleWithPermissions;
import com.onyxdb.idm.repositories.AccountRepository;
import com.onyxdb.idm.services.RoleService;


/**
 * @author ArtemFed
 */
@Configuration
public class AdminInitializer {
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
                        UUID.randomUUID(),
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

                System.out.printf("Admin account created: login=%s, password=%s", login, password);
            }
        };
    }
}
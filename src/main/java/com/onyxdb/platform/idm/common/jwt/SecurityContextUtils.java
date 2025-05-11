package com.onyxdb.platform.idm.common.jwt;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.exceptions.UnauthorizedException;


/**
 * @author ArtemFed
 */
@Component
public class SecurityContextUtils {
    public static Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        if (authentication.getPrincipal().getClass().equals(String.class)) {
            throw new UnauthorizedException("Unauthorized as Anonymous");
        }
        System.out.println("authentication " + authentication.getPrincipal());
        return (Account) authentication.getPrincipal();
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Optional<Map<String, Object>>> getCurrentPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getDetails() == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        if (authentication.getDetails().getClass().equals(String.class)) {
            throw new UnauthorizedException("Unauthorized as Anonymous");
        }

        Object details = authentication.getDetails();
        if (!(details instanceof Map)) {
            throw new IllegalStateException("Authentication details is not a Map");
        }

        try {
            Map<?, ?> rawMap = (Map<?, ?>) details;
            if (!rawMap.isEmpty()) {
                // Проверяем первый элемент для примера
                Map.Entry<?, ?> entry = rawMap.entrySet().iterator().next();
                if (!(entry.getKey() instanceof String) || !(entry.getValue() instanceof Optional)) {
                    throw new IllegalStateException("Authentication details has wrong structure");
                }
            }
            return (Map<String, Optional<Map<String, Object>>>) details;
        } catch (ClassCastException e) {
            throw new IllegalStateException("Authentication details has wrong type structure", e);
        }
    }

    public static Map<String, Optional<Map<String, Object>>> getCurrentPermissionsRaw() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getDetails() == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        if (authentication.getDetails().getClass().equals(String.class)) {
            throw new UnauthorizedException("Unauthorized as Anonymous");
        }
        System.out.println("authentication " + authentication.getDetails());
        return (Map<String, Optional<Map<String, Object>>>) authentication.getDetails();
    }
}
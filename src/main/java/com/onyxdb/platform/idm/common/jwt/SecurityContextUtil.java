package com.onyxdb.platform.idm.common.jwt;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.idm.models.Account;


/**
 * @author ArtemFed
 */
@Component
public class SecurityContextUtil {
    public static Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AccessDeniedException("Unauthorized");
        }
        return (Account) authentication.getPrincipal();
    }
}
package com.onyxdb.platform.idm.common.jwt;

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
}
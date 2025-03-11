package com.onyxdb.idm.common.jwt;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.services.AccountService;
import com.onyxdb.idm.services.AuthService;

/**
 * @author ArtemFed
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final AuthService authService;
    private final AccountService accountService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String accountId = authService.extractAccountId(token);

        if (accountId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Account account = accountService.findById(UUID.fromString(accountId));

            if (authService.validateToken(token, account)) {
                Set<String> permissions = authService.extractPermissions(token);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(account, null, permissions.stream()
                                .map(SimpleGrantedAuthority::new)
                                .toList());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
package com.onyxdb.idm.common.jwt;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

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
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            String accountId = authService.extractAccountId(token);
            if (accountId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UUID accountUUID = UUID.fromString(accountId);
                Account account = accountService.findById(accountUUID);

                if (authService.validateToken(token, account)) {
                    List<String> permissions = accountService.getAllPermissions(accountUUID);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(account, null, permissions.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        }

        chain.doFilter(request, response);
    }
}
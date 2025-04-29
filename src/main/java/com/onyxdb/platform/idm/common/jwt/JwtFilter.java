package com.onyxdb.platform.idm.common.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.services.AccountService;
import com.onyxdb.platform.idm.services.AuthService;

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
                    Map<String, Optional<Map<String, Object>>> permissions = accountService.getAllPermissionBits(accountUUID);

                    List<SimpleGrantedAuthority> authorities = permissions.keySet().stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(account, null, authorities);

                    // Прокидываем permissions как details
                    authentication.setDetails(permissions);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        }

        chain.doFilter(request, response);
    }
}
package com.onyxdb.idm.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.AccountWithRoles;
import com.onyxdb.idm.models.Role;
import com.onyxdb.idm.repositories.AccountRepository;
import com.onyxdb.idm.repositories.RoleRepository;
import com.onyxdb.idm.services.jwt.JwtProvider;
import com.onyxdb.idm.services.jwt.JwtResponse;

/**
 * @author ArtemFed
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final JwtProvider jwtProvider;
    private final Map<String, String> refreshStorage = new HashMap<>();

    /**
     * Аутентификация пользователя.
     *
     * @param login    Имя пользователя.
     * @param password Пароль.
     * @return Access и Refresh токены.
     * @throws RuntimeException Если аутентификация не удалась.
     */
    public JwtResponse login(String login, String password) {
        Optional<Account> accountOptional = accountRepository.findByLogin(login);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            List<Role> roles = accountRepository.getRoles(account.id());
            AccountWithRoles awr = new AccountWithRoles(account, roles);

            if (passwordEncoder.matches(password, account.password())) {
                String accessToken = jwtProvider.generateAccessToken(awr);
                String refreshToken = jwtProvider.generateRefreshToken(awr);
                refreshStorage.put(account.login(), refreshToken);
                return new JwtResponse(accessToken, refreshToken);
            }
        }
        throw new RuntimeException("Неверное имя пользователя или пароль");
    }

    public void logout(String refreshToken) {
        refreshStorage.remove(refreshToken);
    }

    public JwtResponse getAccessToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                Account account = accountRepository.findByLogin(login)
                        .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
                List<Role> roles = accountRepository.getRoles(account.id());
                AccountWithRoles awr = new AccountWithRoles(account, roles);

                final String accessToken = jwtProvider.generateAccessToken(awr);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                Account account = accountRepository.findByLogin(login)
                        .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
                List<Role> roles = accountRepository.getRoles(account.id());
                AccountWithRoles awr = new AccountWithRoles(account, roles);

                final String accessToken = jwtProvider.generateAccessToken(awr);
                final String newRefreshToken = jwtProvider.generateRefreshToken(awr);
                refreshStorage.put(account.login(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new RuntimeException("Невалидный JWT токен");
    }

    /**
     * Получение текущего пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @return Аккаунт пользователя.
     */
    public Account getCurrentUser(UUID userId) {
        return accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}
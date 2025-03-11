package com.onyxdb.idm.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.AccountWithRoles;
import com.onyxdb.idm.models.Role;
import com.onyxdb.idm.repositories.AccountRepository;
import com.onyxdb.idm.repositories.RefreshTokenRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final Map<String, String> refreshStorage = new HashMap<>();

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    public String extractLogin(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, Account account) {
        final String login = extractLogin(token);
        return (Objects.equals(login, account.login()) && !isTokenExpired(token));
    }

    public String GenerateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    ////////////////////////

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
            List<Role> roles = accountRepository.getAccountRoles(account.id());
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
                List<Role> roles = accountRepository.getAccountRoles(account.id());
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
                List<Role> roles = accountRepository.getAccountRoles(account.id());
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
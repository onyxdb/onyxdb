package com.onyxdb.platform.idm.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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

import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.redis.RefreshToken;
import com.onyxdb.platform.idm.repositories.AccountRepository;
import com.onyxdb.platform.idm.services.jwt.JwtResponse;
import com.onyxdb.platform.mdb.exceptions.BadRequestException;
import com.onyxdb.platform.idm.models.exceptions.UnauthorizedException;

/**
 * @author ArtemFed
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final RefreshTokenService refreshTokenService;

    public String extractAccountId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Set<String> extractPermissions(String token) {
        Claims claims = extractAllClaims(token);
        return ((Set<String>) claims.get("permissions", List.class));
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
//        TODO как-то странно работаю с account Id и проверкой самого токена, что-то лишнее!
        final String accountId = extractAccountId(token);
        return (Objects.equals(accountId, account.id().toString()) && !isTokenExpired(token));
    }

    public String generateAccessToken(UUID accountId, Set<String> permissions, int minutes) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("permissions", permissions);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(accountId.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * minutes))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateServiceToken(UUID accountId) {
        Map<String, Optional<Map<String, Object>>> permissions = accountService.getAllPermissionBits(accountId);
        return generateAccessToken(accountId, permissions.keySet(), 60 * 24 * 356);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    ////////////////////////

    public JwtResponse login(String login, String password) {
        Optional<Account> accountOptional = accountRepository.findByLogin(login);
        if (accountOptional.isEmpty()) {
            throw new BadRequestException("Неверное имя пользователя или пароль");

        }
        Account account = accountOptional.get();
        if (!passwordEncoder.matches(password, account.password())) {
            throw new BadRequestException("Неверное имя пользователя или пароль");
        }

        Map<String, Optional<Map<String, Object>>> permissions = accountService.getAllPermissionBits(account.id());
        String accessToken = generateAccessToken(account.id(), permissions.keySet(), 30);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(account.id());

        return new JwtResponse(accessToken, refreshToken.token().toString());
    }

    public JwtResponse generateServiceToken(String login, String password) {
        Optional<Account> accountOptional = accountRepository.findByLogin(login);
        if (accountOptional.isEmpty()) {
            throw new BadRequestException("Неверное имя пользователя или пароль");

        }
        Account account = accountOptional.get();
        if (!passwordEncoder.matches(password, account.password())) {
            throw new BadRequestException("Неверное имя пользователя или пароль");
        }

        String accessToken = generateServiceToken(account.id());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(account.id());

        return new JwtResponse(accessToken, refreshToken.token().toString());
    }

    public void logout(String refreshTokenRequest) {
        Optional<RefreshToken> refreshToken = refreshTokenService.findByToken(refreshTokenRequest);
        refreshToken.ifPresent(refreshTokenService::deleteToken);
    }

    public JwtResponse refresh(String refreshTokenRequest) {
        Optional<RefreshToken> refreshTokenNullable = refreshTokenService.findByToken(refreshTokenRequest);
        if (refreshTokenNullable.isEmpty()) {
            throw new UnauthorizedException("Login is required");
        }

        RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenNullable.get());

        Account account = accountRepository.findById(refreshToken.accountId())
                .orElseThrow(() -> new UnauthorizedException("Account not found"));

        Map<String, Optional<Map<String, Object>>> permissions = accountService.getAllPermissionBits(account.id());
        String accessToken = generateAccessToken(account.id(), permissions.keySet(), 30);

        return new JwtResponse(accessToken, refreshToken.token().toString());
    }
}
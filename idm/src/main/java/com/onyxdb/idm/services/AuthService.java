package com.onyxdb.idm.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.redis.RefreshToken;
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
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

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

    public List<String> extractPermissions(String token) {
        Claims claims = extractAllClaims(token);
        return (claims.get("permissions", List.class));
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

    public String generateAccessToken(Account account, Set<String> permissions) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("permissions", permissions);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(account.id().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 минут
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    ////////////////////////

    public JwtResponse login(String login, String password) {
        Optional<Account> accountOptional = accountRepository.findByLogin(login);
        if (accountOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверное имя пользователя или пароль");

        }
        Account account = accountOptional.get();

//        TODO поменять на passwordEncoder для паролей
//        if (passwordEncoder.matches(password, account.password())) {
        if (!Objects.equals(password, account.password())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверное имя пользователя или пароль");
        }

//        Set<String> permissions = roleRepository.getPermissionsByAccountId(account.getId());
        Set<String> permissions = Set.of();
        String accessToken = generateAccessToken(account, permissions);
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login is required");
        }

        RefreshToken refreshToken = refreshTokenService.verifyExpiration(refreshTokenNullable.get());

        Account account = accountRepository.findById(refreshToken.accountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Account not found"));

//        Set<String> permissions = roleRepository.getPermissionsByAccountId(account.id());
        Set<String> permissions = Set.of();

        String accessToken = generateAccessToken(account, permissions);

        return new JwtResponse(accessToken, refreshToken.token().toString());
    }
}
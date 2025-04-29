package com.onyxdb.platform.idm.services.jwt;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.onyxdb.platform.idm.models.AccountWithRoles;

/**
 * @author ArtemFed
 */
@Service
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${onyxdb.jwt-token.salt-jwt}")
    private String tokenJwtSalt;
    @Value("${onyxdb.jwt-token.salt-refresh}")
    private String tokenRefreshSalt;
    @Value("${onyxdb.jwt-token.expiration}")
    private Long tokenExpiration;

    public String generateAccessToken(AccountWithRoles account) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(tokenExpiration).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(account.account().login())
                .setExpiration(accessExpiration)
                .signWith(getJwtSigningKey(), SignatureAlgorithm.HS256)
                .claim("roles", account.roles())
                .claim("login", account.account().login())
                .claim("id", account.account().id())
                .compact();
    }

    public String generateRefreshToken(AccountWithRoles account) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(account.account().login())
                .setExpiration(refreshExpiration)
                .signWith(getRefreshSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, getJwtSigningKey());
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, getRefreshSigningKey());
    }

    private boolean validateToken(String token, Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            logger.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            logger.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            logger.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            logger.error("Invalid signature", sEx);
        } catch (Exception e) {
            logger.error("invalid token", e);
        }
        return false;
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, getJwtSigningKey());
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, getRefreshSigningKey());
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getJwtSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenJwtSalt);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getRefreshSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenRefreshSalt);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
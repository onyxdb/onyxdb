package com.onyxdb.platform.idm.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.onyxdb.platform.idm.models.redis.RefreshToken;
import com.onyxdb.platform.idm.repositories.AccountRepository;
import com.onyxdb.platform.idm.repositories.RedisRefreshTokenRepository;

/**
 * @author ArtemFed
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final AccountRepository accountRepository;
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;

    public RefreshToken createRefreshToken(UUID accountId) {
        RefreshToken refreshToken = RefreshToken.create(
                accountId,
                UUID.randomUUID(),
                LocalDateTime.now().plusMinutes(10));
        redisRefreshTokenRepository.saveToken(refreshToken);
//        TODO: удалять старый refresh токен?
        return refreshToken;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return redisRefreshTokenRepository.getByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.expireDate().isBefore(LocalDateTime.now())) {
            deleteToken(token);
            throw new RuntimeException(token.token() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

    public void deleteToken(RefreshToken token) {
        redisRefreshTokenRepository.deleteToken(token);
    }
}

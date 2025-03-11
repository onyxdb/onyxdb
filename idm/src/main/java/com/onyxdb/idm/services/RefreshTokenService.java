package com.onyxdb.idm.services;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.onyxdb.idm.models.redis.RefreshToken;
import com.onyxdb.idm.repositories.AccountRepository;
import com.onyxdb.idm.repositories.RefreshTokenRepository;

/**
 * @author ArtemFed
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(UUID accountId) {
        RefreshToken refreshToken = RefreshToken.create(
                accountId,
                UUID.randomUUID(),
                LocalDateTime.now().plusMinutes(10));
        refreshTokenRepository.saveToken(refreshToken);
        return refreshToken;
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.getByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.expireDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.deleteToken(token);
            throw new RuntimeException(token.token() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }
}

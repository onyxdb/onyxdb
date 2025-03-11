package com.onyxdb.idm.common.jwt;

import java.time.LocalDateTime;

import com.onyxdb.idm.models.Account;

/**
 * @author ArtemFed
 */
public record RefreshToken(
        int id,
        String token,
        LocalDateTime expiryDate,
        Account account
) {
}

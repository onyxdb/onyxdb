package com.onyxdb.platform.idm.common.jwt;

import java.time.LocalDateTime;

import com.onyxdb.platform.idm.models.Account;

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

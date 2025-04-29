package com.onyxdb.platform.idm.services.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ArtemFed
 */
@Getter
@AllArgsConstructor
public class JwtResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
}
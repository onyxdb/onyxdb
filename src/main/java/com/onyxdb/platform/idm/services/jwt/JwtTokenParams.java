package com.onyxdb.platform.idm.services.jwt;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * @author ArtemFed
 */
@Data
public class JwtTokenParams {
    private String subject;
    private Map<String, String> extraClaims = new HashMap<>();
}
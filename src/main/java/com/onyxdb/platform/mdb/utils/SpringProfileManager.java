package com.onyxdb.platform.mdb.utils;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpringProfileManager {
    public static final String PROD_PROFILE = "prod";
    public static final String TEST_PROFILE = "test";

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    public boolean isProductionProfile() {
        return Arrays.asList(activeProfile.split(",")).contains(PROD_PROFILE);
    }

    public boolean isTestProfile() {
        return Arrays.asList(activeProfile.split(",")).contains(TEST_PROFILE);
    }
}

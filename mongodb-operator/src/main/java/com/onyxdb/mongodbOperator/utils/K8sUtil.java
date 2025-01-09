package com.onyxdb.mongodbOperator.utils;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author foxleren
 */
public final class K8sUtil {
    public static String getPodFqdn(String pod, String service, String namespace) {
        return String.format("%s.%s.%s", pod, service, namespace);
    }

    // TODO find better place for this method
    public static String generateBase64Key(int numBytes) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[numBytes];
        secureRandom.nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }
}

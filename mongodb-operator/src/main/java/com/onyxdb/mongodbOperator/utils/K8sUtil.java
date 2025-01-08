package com.onyxdb.mongodbOperator.utils;

/**
 * @author foxleren
 */
public final class K8sUtil {
    public static String getPodFqdn(String pod, String service, String namespace) {
        return String.format("%s.%s.%s", pod, service, namespace);
    }
}

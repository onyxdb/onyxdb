package com.onyxdb.mongodbOperator.utils;

import java.util.List;

/**
 * @author foxleren
 */
public final class MongoUtil {
    public static String buildInitReplicaSetCommand(String rsName, List<String> hosts, int port) {
        return String.format("""
                rs.initiate({_id: "%s", members: %s})
                """,
                rsName,
                getRsMembersArrayString(hosts, port)
        );
    }

    private static String getRsMembersArrayString(List<String> hosts, int port) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < hosts.size(); i++) {
            sb.append(getRsMemberString(i, hosts.get(i), port));
            if (i < hosts.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private static String getRsMemberString(int id, String host, int port) {
        return String.format("{ _id: %d, host: \"%s:%d\"}", id, host, port);
    }
}

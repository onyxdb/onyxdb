package com.onyxdb.mdb.clients.k8s.psmdb;

public record Psmdb(
        String name
) {
    public static String getInstanceLabel(String name) {
        return String.format("managed-mongodb-%s", name);
    }
}

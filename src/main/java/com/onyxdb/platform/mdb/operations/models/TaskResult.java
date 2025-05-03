package com.onyxdb.platform.mdb.operations.models;

public record TaskResult(
        boolean ok
) {
    public static TaskResult error() {
        return new TaskResult(false);
    }

    public static TaskResult success() {
        return new TaskResult(true);
    }
}

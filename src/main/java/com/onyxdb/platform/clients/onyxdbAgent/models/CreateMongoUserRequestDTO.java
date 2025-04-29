package com.onyxdb.platform.clients.onyxdbAgent.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateMongoUserRequestDTO(
        String username,
        @JsonProperty("password_secret_name")
        String passwordSecretName,
        @JsonProperty("password_secret_namespace")
        String passwordSecretNamespace,
        List<MongoPermissionDTO> permissions
) {
}

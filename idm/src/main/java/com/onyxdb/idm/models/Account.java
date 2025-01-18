package com.onyxdb.idm.models;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.UUID;


/**
 * @author ArtemFed
 */
@Data
@Builder
@EqualsAndHashCode
@FieldNameConstants
public class Account {
    private UUID id;
    private String username;
    private String email;
    private List<UUID> groupIds;
    private List<Role> roles;
}


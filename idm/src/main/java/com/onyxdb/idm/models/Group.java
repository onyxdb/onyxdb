package com.onyxdb.idm.models;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

/**
 * @author ArtemFed
 */
@Data
@Builder
@EqualsAndHashCode
@FieldNameConstants
public class Group {
    private UUID id;
    private String name;
    private List<UUID> accountIds;
    private List<UUID> groupIds;
    private List<Role> roles;
    private List<Account> accounts;
    private List<Group> groups;
}

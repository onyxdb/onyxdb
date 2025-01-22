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
public class GroupDTO {
    private UUID id;
    private String name;
    private List<UUID> accountIds;
    private List<UUID> groupIds;
    private List<RoleDTO> roles;
    private List<AccountDTO> accounts;
    private List<GroupDTO> groups;
}

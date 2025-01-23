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
public class GroupInputDTO {
    private UUID id;
    private String name;
    private List<AccountDTO> accounts;
    private List<GroupDTO> groups;
    private List<RoleDTO> roles;
}

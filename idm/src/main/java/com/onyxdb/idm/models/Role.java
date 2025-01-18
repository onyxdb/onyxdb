package com.onyxdb.idm.models;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import com.onyxdb.idm.codegen.types.RoleType;


/**
 * @author ArtemFed
 */
@Data
@Builder
@EqualsAndHashCode
@FieldNameConstants
public class Role {
    private UUID id;
    private RoleType name;
    private List<String> permissions;
}


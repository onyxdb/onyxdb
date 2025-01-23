package com.onyxdb.idm.models;

import java.util.List;

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
public class RoleUpdateDTO {
    private List<String> permissions;
}


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
public class AccountInputDTO {
    private String username;
    private String email;
    private List<String> ldapGroups;
    private List<String> adGroups;
}

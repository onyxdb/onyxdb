package com.onyxdb.idm.models;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AccountResourceRoleDTO {
    private UUID accountId;
    private UUID resourceId;
    private UUID roleId;
}

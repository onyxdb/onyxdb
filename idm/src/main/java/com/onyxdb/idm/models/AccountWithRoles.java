package com.onyxdb.idm.models;

import java.util.List;

/**
 * @author ArtemFed
 */
public record AccountWithRoles(
        Account account,
        List<Role> roles
) {
}
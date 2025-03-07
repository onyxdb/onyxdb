package com.onyxdb.idm.models;

import java.util.ArrayList;
import java.util.List;

import com.onyxdb.idm.generated.openapi.models.OrganizationTreeDTO;

/**
 * @author ArtemFed
 */
public record OrganizationTree(
        OrganizationUnit orgUnit,
        List<OrganizationTree> children
) {
    public OrganizationTreeDTO toDTO() {
        return new OrganizationTreeDTO()
                .item(orgUnit.toDTO())
                .children(children.isEmpty() ? new ArrayList<>() : children.stream().map(OrganizationTree::toDTO).toList());
    }
}
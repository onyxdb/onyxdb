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
                .unit(orgUnit.toDTO())
                .items(children.isEmpty() ? new ArrayList<>() : children.stream().map(OrganizationTree::toDTO).toList());
    }
}
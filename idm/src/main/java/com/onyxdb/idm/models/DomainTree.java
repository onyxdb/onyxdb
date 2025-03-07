package com.onyxdb.idm.models;

import java.util.ArrayList;
import java.util.List;

import com.onyxdb.idm.generated.openapi.models.DomainTreeDTO;

/**
 * @author ArtemFed
 */
public record DomainTree(
        DomainComponent domainComponent,
        List<OrganizationTree> children
) {
    public DomainTreeDTO toDTO() {
        return new DomainTreeDTO()
                .item(domainComponent.toDTO())
                .children(children.isEmpty() ? new ArrayList<>() : children.stream().map(OrganizationTree::toDTO).toList());
    }
}
package com.onyxdb.platform.idm.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.DomainComponentsApi;
import com.onyxdb.platform.generated.openapi.models.DomainComponentPostDTO;
import com.onyxdb.platform.generated.openapi.models.DomainComponentDTO;
import com.onyxdb.platform.generated.openapi.models.DomainTreeDTO;
import com.onyxdb.platform.generated.openapi.models.OrganizationUnitDTO;
import com.onyxdb.platform.idm.common.PermissionCheck;
import com.onyxdb.platform.idm.models.DomainComponent;
import com.onyxdb.platform.idm.models.DomainTree;
import com.onyxdb.platform.idm.models.OrganizationUnit;
import com.onyxdb.platform.idm.services.DomainComponentService;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class DomainComponentsController implements DomainComponentsApi {
    private final DomainComponentService domainComponentService;

    @Override
    @PermissionCheck(entity = "domain-component", action = "create")
    public ResponseEntity<DomainComponentDTO> createDomainComponent(@Valid DomainComponentPostDTO domainComponentDTO) {
        DomainComponent domainComponent = DomainComponent.fromPostDTO(domainComponentDTO);
        DomainComponent createdDomainComponent = domainComponentService.create(domainComponent);
        return new ResponseEntity<>(createdDomainComponent.toDTO(), HttpStatus.CREATED);
    }

    @Override
    @PermissionCheck(entity = "domain-component", action = "delete")
    public ResponseEntity<Void> deleteDomainComponent(UUID domainComponentId) {
        domainComponentService.delete(domainComponentId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PermissionCheck(entity = "domain-component", action = "get")
    public ResponseEntity<DomainComponentDTO> getDomainComponentById(UUID domainComponentId) {
        DomainComponent domainComponent = domainComponentService.findById(domainComponentId);
        return ResponseEntity.ok(domainComponent.toDTO());
    }

    @Override
    @PermissionCheck(entity = "domain-component", action = "get")
    public ResponseEntity<List<OrganizationUnitDTO>> getDomainComponentRootsOrganizationUnits(UUID dcId) {
        List<OrganizationUnit> data = domainComponentService.findRootOrgUnits(dcId);
        List<OrganizationUnitDTO> res = data.stream().map(OrganizationUnit::toDTO).toList();
        return ResponseEntity.ok(res);
    }

    @Override
    @PermissionCheck(entity = "domain-component", action = "get")
    public ResponseEntity<DomainTreeDTO> getDomainComponentTree(UUID dcId) {
        DomainTree tree = domainComponentService.findDomainTree(dcId);
        return ResponseEntity.ok(tree.toDTO());
    }

    @Override
    @PermissionCheck(entity = "domain-component", action = "get")
    public ResponseEntity<List<DomainComponentDTO>> getAllDomainComponents() {
        List<DomainComponent> domainComponents = domainComponentService.findAll();
        List<DomainComponentDTO> domainComponentDTOs = domainComponents.stream().map(DomainComponent::toDTO).toList();
        return ResponseEntity.ok(domainComponentDTOs);
    }

    @Override
    @PermissionCheck(entity = "domain-component", action = "update")
    public ResponseEntity<DomainComponentDTO> updateDomainComponent(
            UUID domainComponentId, @Valid DomainComponentPostDTO domainComponentDTO
    ) {
        domainComponentDTO.setId(domainComponentId);
        DomainComponent domainComponent = DomainComponent.fromPostDTO(domainComponentDTO);
        DomainComponent updatedDomainComponent = domainComponentService.update(domainComponent);
        return ResponseEntity.ok(updatedDomainComponent.toDTO());
    }
}
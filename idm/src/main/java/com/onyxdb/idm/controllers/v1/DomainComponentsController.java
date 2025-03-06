package com.onyxdb.idm.controllers.v1;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.idm.generated.openapi.apis.DomainComponentsApi;
import com.onyxdb.idm.generated.openapi.models.DomainComponentDTO;
import com.onyxdb.idm.generated.openapi.models.OrganizationUnitDTO;
import com.onyxdb.idm.models.DomainComponent;
import com.onyxdb.idm.models.OrganizationUnit;
import com.onyxdb.idm.services.DomainComponentService;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class DomainComponentsController implements DomainComponentsApi {
    private final DomainComponentService domainComponentService;

    @Override
    public ResponseEntity<DomainComponentDTO> createDomainComponent(@Valid DomainComponentDTO domainComponentDTO) {
        DomainComponent domainComponent = DomainComponent.fromDTO(domainComponentDTO);
        DomainComponent createdDomainComponent = domainComponentService.create(domainComponent);
        return new ResponseEntity<>(createdDomainComponent.toDTO(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteDomainComponent(UUID domainComponentId) {
        domainComponentService.delete(domainComponentId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<DomainComponentDTO> getDomainComponentById(UUID domainComponentId) {
        DomainComponent domainComponent = domainComponentService.findById(domainComponentId);
        return ResponseEntity.ok(domainComponent.toDTO());
    }

    @Override
    public ResponseEntity<List<OrganizationUnitDTO>> getDomainComponentRootsOrganizationUnits(UUID dcId) {
        List<OrganizationUnit> data = domainComponentService.findRootOrgUnits(dcId);
        List<OrganizationUnitDTO> res = data.stream().map(OrganizationUnit::toDTO).toList();
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<List<DomainComponentDTO>> getAllDomainComponents() {
        List<DomainComponent> domainComponents = domainComponentService.findAll();
        List<DomainComponentDTO> domainComponentDTOs = domainComponents.stream().map(DomainComponent::toDTO).toList();
        return ResponseEntity.ok(domainComponentDTOs);
    }

    @Override
    public ResponseEntity<DomainComponentDTO> updateDomainComponent(
            UUID domainComponentId, @Valid DomainComponentDTO domainComponentDTO
    ) {
        domainComponentDTO.setId(domainComponentId);
        DomainComponent domainComponent = DomainComponent.fromDTO(domainComponentDTO);
        DomainComponent updatedDomainComponent = domainComponentService.update(domainComponent);
        return ResponseEntity.ok(updatedDomainComponent.toDTO());
    }
}
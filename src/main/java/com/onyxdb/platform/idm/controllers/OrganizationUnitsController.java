package com.onyxdb.platform.idm.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.OrganizationUnitsApi;
import com.onyxdb.platform.generated.openapi.models.AccountDTO;
import com.onyxdb.platform.generated.openapi.models.OrganizationTreeDTO;
import com.onyxdb.platform.generated.openapi.models.OrganizationUnitPostDTO;
import com.onyxdb.platform.generated.openapi.models.OrganizationUnitDTO;
import com.onyxdb.platform.generated.openapi.models.PaginatedOrganizationUnitResponse;
import com.onyxdb.platform.idm.common.PermissionCheck;
import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.idm.models.OrganizationTree;
import com.onyxdb.platform.idm.models.OrganizationUnit;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.services.OrganizationUnitService;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class OrganizationUnitsController implements OrganizationUnitsApi {
    private final OrganizationUnitService organizationUnitService;

    @Override
    @PermissionCheck(entity = "organization-unit", action = "create")
    public ResponseEntity<OrganizationUnitDTO> createOrganizationUnit(@Valid OrganizationUnitPostDTO organizationUnitDTO) {
        OrganizationUnit organizationUnit = OrganizationUnit.fromPostDTO(organizationUnitDTO);
        OrganizationUnit createdOrganizationUnit = organizationUnitService.create(organizationUnit);
        return new ResponseEntity<>(createdOrganizationUnit.toDTO(), HttpStatus.CREATED);
    }

    @Override
    @PermissionCheck(entity = "organization-unit", action = "delete")
    public ResponseEntity<Void> deleteOrganizationUnit(UUID ouId) {
        organizationUnitService.delete(ouId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PermissionCheck(entity = "organization-unit", action = "get", resourceId = "#ouId")
    public ResponseEntity<List<AccountDTO>> getAccountsByouId(UUID ouId) {
        List<Account> data = organizationUnitService.getOUAccounts(ouId);
        List<AccountDTO> res = data.stream().map(Account::toDTO).toList();
        return ResponseEntity.ok(res);
    }

    @Override
    @PermissionCheck(entity = "organization-unit", action = "get")
    public ResponseEntity<PaginatedOrganizationUnitResponse> getAllOrganizationUnits(
            String search, UUID parentOuId, UUID dcId, Integer limit, Integer offset
    ) {
        PaginatedResult<OrganizationUnit> data = organizationUnitService.findAll(search, dcId, parentOuId, limit, offset);
        List<OrganizationUnitDTO> res = data.data().stream().map(OrganizationUnit::toDTO).toList();
        return ResponseEntity.ok(new PaginatedOrganizationUnitResponse()
                .data(res)
                .totalCount(data.totalCount())
                .startPosition(data.startPosition())
                .endPosition(data.endPosition())
        );
    }

    @Override
    @PermissionCheck(entity = "organization-unit", action = "get", resourceId = "#ouId")
    public ResponseEntity<OrganizationUnitDTO> getOrganizationUnitById(UUID ouId) {
        OrganizationUnit organizationUnit = organizationUnitService.findById(ouId);
        return ResponseEntity.ok(organizationUnit.toDTO());
    }

    @Override
    @PermissionCheck(entity = "organization-unit", action = "get", resourceId = "#ouId")
    public ResponseEntity<List<OrganizationUnitDTO>> getOrganizationUnitChildren(UUID ouId) {
        PaginatedResult<OrganizationUnit> data = organizationUnitService.findChildren(ouId);
        List<OrganizationUnitDTO> res = data.data().stream().map(OrganizationUnit::toDTO).toList();
        return ResponseEntity.ok(res);
    }

    @Override
    @PermissionCheck(entity = "organization-unit", action = "get", resourceId = "#ouId")
    public ResponseEntity<List<OrganizationUnitDTO>> getOrganizationUnitParents(UUID ouId) {
        List<OrganizationUnit> data = organizationUnitService.findAllParentOrganizationUnits(ouId);
        List<OrganizationUnitDTO> res = data.stream().map(OrganizationUnit::toDTO).toList();
        return ResponseEntity.ok(res);
    }

    @Override
    @PermissionCheck(entity = "organization-unit", action = "get", resourceId = "#ouId")
    public ResponseEntity<OrganizationTreeDTO> getOrganizationUnitTree(UUID ouId) {
        OrganizationTree tree = organizationUnitService.findChildrenTree(ouId);
        return ResponseEntity.ok(tree.toDTO());
    }

    @Override
    @PermissionCheck(entity = "organization-unit", action = "update", resourceId = "#ouId")
    public ResponseEntity<OrganizationUnitDTO> updateOrganizationUnit(
            UUID ouId, @Valid OrganizationUnitPostDTO organizationUnitDTO
    ) {
        organizationUnitDTO.setId(ouId);
        OrganizationUnit organizationUnit = OrganizationUnit.fromPostDTO(organizationUnitDTO);
        OrganizationUnit updatedOrganizationUnit = organizationUnitService.update(organizationUnit);
        return ResponseEntity.ok(updatedOrganizationUnit.toDTO());
    }

    @Override
    @PermissionCheck(entity = "organization-unit", action = "update", resourceId = "#ouId")
    public ResponseEntity<Void> addAccountToOrganizationUnit(UUID ouId, UUID accountId) {
        organizationUnitService.addAccount(ouId, accountId);
        return null;
    }

    @Override
    @PermissionCheck(entity = "organization-unit", action = "update", resourceId = "#ouId")
    public ResponseEntity<Void> removeAccountFromOrganizationUnit(UUID ouId, UUID accountId) {
        organizationUnitService.removeAccount(ouId, accountId);
        return null;
    }
}

package com.onyxdb.idm.controllers.v1;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.idm.generated.openapi.apis.OrganizationUnitsApi;
import com.onyxdb.idm.generated.openapi.models.AccountDTO;
import com.onyxdb.idm.generated.openapi.models.OrganizationTreeDTO;
import com.onyxdb.idm.generated.openapi.models.OrganizationUnitDTO;
import com.onyxdb.idm.generated.openapi.models.PaginatedOrganizationUnitResponse;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.OrganizationTree;
import com.onyxdb.idm.models.OrganizationUnit;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.services.OrganizationUnitService;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class OrganizationUnitsController implements OrganizationUnitsApi {
    private final OrganizationUnitService organizationUnitService;

    @Override
    public ResponseEntity<OrganizationUnitDTO> createOrganizationUnit(@Valid OrganizationUnitDTO organizationUnitDTO) {
        OrganizationUnit organizationUnit = OrganizationUnit.fromDTO(organizationUnitDTO);
        OrganizationUnit createdOrganizationUnit = organizationUnitService.create(organizationUnit);
        return new ResponseEntity<>(createdOrganizationUnit.toDTO(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteOrganizationUnit(UUID ouId) {
        organizationUnitService.delete(ouId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AccountDTO>> getAccountsByouId(UUID ouId) {
        List<Account> data = organizationUnitService.getOUAccounts(ouId);
        List<AccountDTO> res = data.stream().map(Account::toDTO).toList();
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<PaginatedOrganizationUnitResponse> getAllOrganizationUnits(
            UUID parentOuId, UUID dcId, Integer limit, Integer offset
    ) {
        PaginatedResult<OrganizationUnit> data = organizationUnitService.findAll(dcId, parentOuId, limit, offset);
        List<OrganizationUnitDTO> res = data.data().stream().map(OrganizationUnit::toDTO).toList();
        return ResponseEntity.ok(new PaginatedOrganizationUnitResponse()
                .data(res)
                .totalCount(data.totalCount())
                .startPosition(data.startPosition())
                .endPosition(data.endPosition())
        );
    }

    @Override
    public ResponseEntity<OrganizationUnitDTO> getOrganizationUnitById(UUID ouId) {
        OrganizationUnit organizationUnit = organizationUnitService.findById(ouId);
        return ResponseEntity.ok(organizationUnit.toDTO());
    }

    @Override
    public ResponseEntity<List<OrganizationUnitDTO>> getOrganizationUnitChildren(UUID ouId) {
        PaginatedResult<OrganizationUnit> data = organizationUnitService.findChildren(ouId);
        List<OrganizationUnitDTO> res = data.data().stream().map(OrganizationUnit::toDTO).toList();
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<List<OrganizationUnitDTO>> getOrganizationUnitParents(UUID ouId) {
        List<OrganizationUnit> data = organizationUnitService.findAllParentOrganizationUnits(ouId);
        List<OrganizationUnitDTO> res = data.stream().map(OrganizationUnit::toDTO).toList();
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<OrganizationTreeDTO> getOrganizationUnitTree(UUID ouId) {
        OrganizationTree tree = organizationUnitService.findChildrenTree(ouId);
        return ResponseEntity.ok(tree.toDTO());
    }

    @Override
    public ResponseEntity<OrganizationUnitDTO> updateOrganizationUnit(
            UUID ouId, @Valid OrganizationUnitDTO organizationUnitDTO
    ) {
        organizationUnitDTO.setId(ouId);
        OrganizationUnit organizationUnit = OrganizationUnit.fromDTO(organizationUnitDTO);
        OrganizationUnit updatedOrganizationUnit = organizationUnitService.update(organizationUnit);
        return ResponseEntity.ok(updatedOrganizationUnit.toDTO());
    }

    @Override
    public ResponseEntity<Void> addAccountToOrganizationUnit(UUID ouId, UUID accountId) {
        organizationUnitService.addAccount(ouId, accountId);
        return null;
    }

    @Override
    public ResponseEntity<Void> removeAccountFromOrganizationUnit(UUID ouId, UUID accountId) {
        organizationUnitService.removeAccount(ouId, accountId);
        return null;
    }
}

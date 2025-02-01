package com.onyxdb.idm.controllers.v1;

import com.onyxdb.idm.generated.openapi.apis.OrganizationUnitsApi;
import com.onyxdb.idm.generated.openapi.models.AccountDTO;
import com.onyxdb.idm.generated.openapi.models.OrganizationUnitDTO;
import com.onyxdb.idm.models.Account;
import com.onyxdb.idm.models.OrganizationUnit;
import com.onyxdb.idm.services.OrganizationUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<OrganizationUnitDTO> getOrganizationUnitById(UUID ouId) {
        OrganizationUnit organizationUnit = organizationUnitService.findById(ouId);
        return ResponseEntity.ok(organizationUnit.toDTO());
    }

    @Override
    public ResponseEntity<List<OrganizationUnitDTO>> getAllOrganizationUnits() {
        List<OrganizationUnit> organizationUnits = organizationUnitService.findAll();
        List<OrganizationUnitDTO> organizationUnitDTOs = organizationUnits
                .stream().map(OrganizationUnit::toDTO).toList();
        return ResponseEntity.ok(organizationUnitDTOs);
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
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> removeAccountFromOrganizationUnit(UUID ouId, UUID accountId) {
        organizationUnitService.removeAccount(ouId, accountId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AccountDTO>> getAccountsByOrganizationUnitId(UUID ouId) {
        List<Account> accounts = organizationUnitService.getOUAccounts(ouId);
        List<AccountDTO> accountDTOs = accounts.stream().map(Account::toDTO).toList();
        return ResponseEntity.ok(accountDTOs);
    }
}

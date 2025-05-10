package com.onyxdb.platform.idm.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.RolesRequestsApi;
import com.onyxdb.platform.generated.openapi.models.PaginatedRoleRequestResponse;
import com.onyxdb.platform.generated.openapi.models.RoleRequestFullDTO;
import com.onyxdb.platform.generated.openapi.models.RoleRequestPostDTO;
import com.onyxdb.platform.generated.openapi.models.RoleRequestDTO;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.RoleRequest;
import com.onyxdb.platform.idm.models.RoleRequestFull;
import com.onyxdb.platform.idm.models.RoleRequestStatus;
import com.onyxdb.platform.idm.services.RoleRequestService;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class RolesRequestController implements RolesRequestsApi {
    private final RoleRequestService roleRequestService;

    @Override
    public ResponseEntity<RoleRequestDTO> createRoleRequest(@Valid RoleRequestPostDTO roleRequestDTO) {
        RoleRequest roleRequest = RoleRequest.fromPostDTO(roleRequestDTO);
        RoleRequest createdRoleRequest = roleRequestService.create(roleRequest);
        return new ResponseEntity<>(createdRoleRequest.toDTO(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PaginatedRoleRequestResponse> getAllRolesRequests(UUID ownerId, String status, UUID accountId, UUID roleId, Integer limit, Integer offset) {
        PaginatedResult<RoleRequestFull> roleRequests = roleRequestService.findAll(status, accountId, ownerId, roleId, limit, offset);
        List<RoleRequestFullDTO> roleRequestDTOs = roleRequests.data().stream().map(RoleRequestFull::toDTO).toList();
        return ResponseEntity.ok(new PaginatedRoleRequestResponse()
                .data(roleRequestDTOs)
                .totalCount(roleRequests.totalCount())
                .startPosition(roleRequests.startPosition())
                .endPosition(roleRequests.endPosition())
        );
    }

    @Override
    public ResponseEntity<RoleRequestDTO> getRoleRequestById(UUID roleRequestId) {
        RoleRequest roleRequest = roleRequestService.findById(roleRequestId);
        return ResponseEntity.ok(roleRequest.toDTO());
    }

    @Override
    public ResponseEntity<RoleRequestDTO> updateRoleRequest(UUID roleRequestId, @Valid RoleRequestPostDTO roleRequestDTO) {
        roleRequestDTO.setId(roleRequestId);
        RoleRequest roleRequest = RoleRequest.fromPostDTO(roleRequestDTO);
        RoleRequest updatedRoleRequest = roleRequestService.update(roleRequest);
        return ResponseEntity.ok(updatedRoleRequest.toDTO());
    }

    @Override
    public ResponseEntity<RoleRequestDTO> updateRoleRequestStatus(UUID roleRequestId, String newStatus) {
        RoleRequest updatedRoleRequest = roleRequestService.setStatus(roleRequestId, RoleRequestStatus.valueOf(newStatus));
        return ResponseEntity.ok(updatedRoleRequest.toDTO());
    }
}

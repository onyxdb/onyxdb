package com.onyxdb.idm.services;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

import com.onyxdb.idm.controllers.v1.ResourceNotFoundException;
import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.RoleRequest;
import com.onyxdb.idm.models.RoleRequestStatus;
import com.onyxdb.idm.repositories.RoleRequestRepository;

/**
 * @author ArtemFed
 */
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class RoleRequestService {
    private final RoleRequestRepository roleRequestRepository;
    private final AccountService accountService;

    public RoleRequest findById(UUID id) {
        return roleRequestRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoleRequest not found"));
    }

    public PaginatedResult<RoleRequest> findAll(String status, UUID accountId, UUID ownerId, Integer limit, Integer offset) {
        return roleRequestRepository.findAll(status, accountId, ownerId, limit, offset);
    }

    public RoleRequest create(RoleRequest roleRequest) {
        return roleRequestRepository.create(roleRequest);
    }

    public RoleRequest setStatus(UUID id, RoleRequestStatus newStatus) {
        RoleRequest request = roleRequestRepository.setStatus(id, newStatus.toString());
        if (newStatus == RoleRequestStatus.APPROVED) {
            accountService.addRole(request.accountId(), request.roleId());
        }
        return request;
    }

    public RoleRequest update(RoleRequest roleRequest) {
        return roleRequestRepository.update(roleRequest);
    }
}
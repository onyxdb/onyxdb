package com.onyxdb.platform.idm.services;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

import com.onyxdb.platform.idm.v1.ResourceNotFoundException;
import com.onyxdb.platform.idm.models.PaginatedResult;
import com.onyxdb.platform.idm.models.RoleRequest;
import com.onyxdb.platform.idm.models.RoleRequestStatus;
import com.onyxdb.platform.idm.repositories.RoleRequestRepository;

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

    public PaginatedResult<RoleRequest> findAll(String status, UUID accountId, UUID ownerId, UUID roleId, Integer limit, Integer offset) {
        return roleRequestRepository.findAll(status, accountId, ownerId, roleId, limit, offset);
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
package com.onyxdb.idm.repositories;

import java.util.Optional;
import java.util.UUID;

import com.onyxdb.idm.models.PaginatedResult;
import com.onyxdb.idm.models.RoleRequest;

/**
 * @author ArtemFed
 */
public interface RoleRequestRepository {
    Optional<RoleRequest> findById(UUID id);

    PaginatedResult<RoleRequest> findAll(String status, UUID ownerId, UUID accountId, Integer limit, Integer offset);

    PaginatedResult<RoleRequest> search(String query, Integer limit, Integer offset);

    RoleRequest create(RoleRequest RoleRequest);

    RoleRequest update(RoleRequest RoleRequest);

    RoleRequest setStatus(UUID id, String status);
}

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

    PaginatedResult<RoleRequest> findAll(String status, UUID ownerId, UUID accountId, int limit, int offset);

    PaginatedResult<RoleRequest> search(String query, int limit, int offset);

    RoleRequest create(RoleRequest RoleRequest);

    RoleRequest update(RoleRequest RoleRequest);

    void setStatus(UUID id, String status);
}

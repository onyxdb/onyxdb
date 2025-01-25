package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.BusinessRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessRoleRepository {
    Optional<BusinessRole> findById(UUID id);

    List<BusinessRole> findAll();

    void create(BusinessRole businessRole);

    void update(BusinessRole businessRole);

    void delete(UUID id);

    List<BusinessRole> findByParentId(UUID parentId);
}

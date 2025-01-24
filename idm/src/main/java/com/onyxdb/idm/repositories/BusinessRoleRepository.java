package com.onyxdb.idm.repositories;

import com.onyxdb.idm.models.BusinessRoleDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessRoleRepository {
    Optional<BusinessRoleDTO> findById(UUID id);

    List<BusinessRoleDTO> findAll();

    void create(BusinessRoleDTO businessRole);

    void update(BusinessRoleDTO businessRole);

    void delete(UUID id);

    List<BusinessRoleDTO> findByParentId(UUID parentId);
}

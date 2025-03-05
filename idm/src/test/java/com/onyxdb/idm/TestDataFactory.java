package com.onyxdb.idm;

import com.onyxdb.idm.generated.openapi.models.AccountDTO;
import com.onyxdb.idm.generated.openapi.models.RoleDTO;
import com.onyxdb.idm.generated.openapi.models.BusinessRoleDTO;
import com.onyxdb.idm.generated.openapi.models.PermissionDTO;
import com.onyxdb.idm.generated.openapi.models.DomainComponentDTO;
import com.onyxdb.idm.generated.openapi.models.OrganizationUnitDTO;
import com.onyxdb.idm.generated.openapi.models.ProductDTO;
import com.onyxdb.idm.generated.openapi.models.RoleWithPermissionsDTO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestDataFactory {

    public static AccountDTO createAccountDTO(
            UUID id, String username, String password, String email, String firstName, String lastName,
            Map<String, Object> data, LocalDateTime createdAt, LocalDateTime updatedAt) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(id != null ? id : UUID.randomUUID());
        accountDTO.setUsername(username);
        accountDTO.setPassword(password);
        accountDTO.setEmail(email);
        accountDTO.setFirstName(firstName);
        accountDTO.setLastName(lastName);
        accountDTO.setData(data != null ? data : new HashMap<>());
        accountDTO.setCreatedAt(createdAt != null ? createdAt : LocalDateTime.now());
        accountDTO.setUpdatedAt(updatedAt != null ? updatedAt : LocalDateTime.now());
        return accountDTO;
    }

    public static RoleDTO createRoleDTO(UUID id, String roleType, String name, String description,
                                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(id != null ? id : UUID.randomUUID());
        roleDTO.setRoleType(roleType);
        roleDTO.setName(name);
        roleDTO.setDescription(description);
        roleDTO.setCreatedAt(createdAt != null ? createdAt : LocalDateTime.now());
        roleDTO.setUpdatedAt(updatedAt != null ? updatedAt : LocalDateTime.now());
        return roleDTO;
    }

    public static BusinessRoleDTO createBusinessRoleDTO(UUID id, String name, String description,
                                                        LocalDateTime createdAt, LocalDateTime updatedAt) {
        BusinessRoleDTO businessRoleDTO = new BusinessRoleDTO();
        businessRoleDTO.setId(id != null ? id : UUID.randomUUID());
        businessRoleDTO.setName(name);
        businessRoleDTO.setDescription(description);
        businessRoleDTO.setCreatedAt(createdAt != null ? createdAt : LocalDateTime.now());
        businessRoleDTO.setUpdatedAt(updatedAt != null ? updatedAt : LocalDateTime.now());
        return businessRoleDTO;
    }

    public static PermissionDTO createPermissionDTO(UUID id, String actionType, String resourceType,
                                                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setId(id != null ? id : UUID.randomUUID());
        permissionDTO.setActionType(actionType);
        permissionDTO.setResourceType(resourceType);
        permissionDTO.setCreatedAt(createdAt != null ? createdAt : LocalDateTime.now());
        permissionDTO.setUpdatedAt(updatedAt != null ? updatedAt : LocalDateTime.now());
        return permissionDTO;
    }

    public static DomainComponentDTO createDomainComponentDTO(UUID id, String name, String description,
                                                              LocalDateTime createdAt, LocalDateTime updatedAt) {
        DomainComponentDTO domainComponentDTO = new DomainComponentDTO();
        domainComponentDTO.setId(id != null ? id : UUID.randomUUID());
        domainComponentDTO.setName(name);
        domainComponentDTO.setDescription(description);
        domainComponentDTO.setCreatedAt(createdAt != null ? createdAt : LocalDateTime.now());
        domainComponentDTO.setUpdatedAt(updatedAt != null ? updatedAt : LocalDateTime.now());
        return domainComponentDTO;
    }

    public static OrganizationUnitDTO createOrganizationUnitDTO(UUID id, String name, String description,
                                                                UUID domainComponentId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        OrganizationUnitDTO organizationUnitDTO = new OrganizationUnitDTO();
        organizationUnitDTO.setId(id != null ? id : UUID.randomUUID());
        organizationUnitDTO.setName(name);
        organizationUnitDTO.setDescription(description);
        organizationUnitDTO.setDomainComponentId(domainComponentId); // Добавляем domainComponentId
        organizationUnitDTO.setCreatedAt(createdAt != null ? createdAt : LocalDateTime.now());
        organizationUnitDTO.setUpdatedAt(updatedAt != null ? updatedAt : LocalDateTime.now());
        return organizationUnitDTO;
    }

    public static ProductDTO createProductDTO(UUID id, String name, String description,
                                              LocalDateTime createdAt, LocalDateTime updatedAt) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id != null ? id : UUID.randomUUID());
        productDTO.setName(name);
        productDTO.setDescription(description);
        productDTO.setCreatedAt(createdAt != null ? createdAt : LocalDateTime.now());
        productDTO.setUpdatedAt(updatedAt != null ? updatedAt : LocalDateTime.now());
        return productDTO;
    }

    public static RoleWithPermissionsDTO createRoleWithPermissionsDTO(RoleDTO role, List<PermissionDTO> permissions) {
        RoleWithPermissionsDTO roleWithPermissionsDTO = new RoleWithPermissionsDTO();
        roleWithPermissionsDTO.setRole(role);
        roleWithPermissionsDTO.setPermissions(permissions);
        return roleWithPermissionsDTO;
    }
}
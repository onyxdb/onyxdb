package com.onyxdb.idm;

import com.onyxdb.idm.generated.openapi.models.AccountDTO;
import com.onyxdb.idm.generated.openapi.models.RoleDTO;
import com.onyxdb.idm.generated.openapi.models.BusinessRoleDTO;
import com.onyxdb.idm.generated.openapi.models.PermissionDTO;
import com.onyxdb.idm.generated.openapi.models.DomainComponentDTO;
import com.onyxdb.idm.generated.openapi.models.OrganizationUnitDTO;
import com.onyxdb.idm.generated.openapi.models.ProductDTO;
import com.onyxdb.idm.generated.openapi.models.RoleRequestDTO;
import com.onyxdb.idm.generated.openapi.models.RoleWithPermissionsDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestDataFactory {

    public static AccountDTO createAccountDTO(
            UUID id, String username, String password, String email, String firstName, String lastName,
            Map<String, Object> data
    ) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(id != null ? id : UUID.randomUUID());
        accountDTO.setUsername(username);
        accountDTO.setPassword(password);
        accountDTO.setEmail(email);
        accountDTO.setFirstName(firstName);
        accountDTO.setLastName(lastName);
        accountDTO.setData(data != null ? data : new HashMap<>());
        return accountDTO;
    }

    public static RoleDTO createRoleDTO(
            UUID id, String roleType, String name, String description, Boolean isShopHidden, UUID productId, UUID orgUnitId
    ) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(id != null ? id : UUID.randomUUID());
        roleDTO.setRoleType(roleType);
        roleDTO.setIsShopHidden(isShopHidden);
        roleDTO.setName(name);
        roleDTO.setDescription(description);
        roleDTO.setProductId(productId);
        roleDTO.setOrgUnitId(orgUnitId);
        return roleDTO;
    }

    public static BusinessRoleDTO createBusinessRoleDTO(
            UUID id, String name, String description, UUID parentId
    ) {
        BusinessRoleDTO businessRoleDTO = new BusinessRoleDTO();
        businessRoleDTO.setId(id != null ? id : UUID.randomUUID());
        businessRoleDTO.setName(name);
        businessRoleDTO.setDescription(description);
        businessRoleDTO.setParentId(parentId);
        return businessRoleDTO;
    }

    public static PermissionDTO createPermissionDTO(
            UUID id, String actionType, String resourceType
    ) {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setId(id != null ? id : UUID.randomUUID());
        permissionDTO.setActionType(actionType);
        permissionDTO.setResourceType(resourceType);
        return permissionDTO;
    }


    public static RoleRequestDTO createRoleRequestDTO(
            UUID id, UUID roleId, UUID accountId, UUID ownerId, String reason, RoleRequestDTO.StatusEnum status
    ) {
        RoleRequestDTO roleRequestDTO = new RoleRequestDTO();
        roleRequestDTO.setId(id != null ? id : UUID.randomUUID());
        roleRequestDTO.setRoleId(roleId);
        roleRequestDTO.setAccountId(accountId);
        roleRequestDTO.setOwnerId(ownerId);
        roleRequestDTO.setReason(reason);
        roleRequestDTO.setStatus(status);
        return roleRequestDTO;
    }

    public static DomainComponentDTO createDomainComponentDTO(
            UUID id, String name, String description
    ) {
        DomainComponentDTO domainComponentDTO = new DomainComponentDTO();
        domainComponentDTO.setId(id != null ? id : UUID.randomUUID());
        domainComponentDTO.setName(name);
        domainComponentDTO.setDescription(description);
        return domainComponentDTO;
    }

    public static OrganizationUnitDTO createOrganizationUnitDTO(
            UUID id, String name, String description, UUID domainComponentId, UUID parentId
    ) {
        OrganizationUnitDTO organizationUnitDTO = new OrganizationUnitDTO();
        organizationUnitDTO.setId(id != null ? id : UUID.randomUUID());
        organizationUnitDTO.setName(name);
        organizationUnitDTO.setDescription(description);
        organizationUnitDTO.setDomainComponentId(domainComponentId);
        organizationUnitDTO.setParentId(parentId);
        return organizationUnitDTO;
    }

    public static ProductDTO createProductDTO(
            UUID id, String name, String description, UUID parentId
    ) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(id != null ? id : UUID.randomUUID());
        productDTO.setName(name);
        productDTO.setDescription(description);
        productDTO.setParentId(parentId);
        return productDTO;
    }

    public static RoleWithPermissionsDTO createRoleWithPermissionsDTO(RoleDTO role, List<PermissionDTO> permissions) {
        RoleWithPermissionsDTO roleWithPermissionsDTO = new RoleWithPermissionsDTO();
        roleWithPermissionsDTO.setRole(role);
        roleWithPermissionsDTO.setPermissions(permissions);
        return roleWithPermissionsDTO;
    }
}
package com.onyxdb.platform.idm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.onyxdb.platform.generated.openapi.models.AccountCreateDTO;
import com.onyxdb.platform.generated.openapi.models.BusinessRoleCreateDTO;
import com.onyxdb.platform.generated.openapi.models.DomainComponentCreateDTO;
import com.onyxdb.platform.generated.openapi.models.OrganizationUnitCreateDTO;
import com.onyxdb.platform.generated.openapi.models.PermissionCreateDTO;
import com.onyxdb.platform.generated.openapi.models.ProductCreateDTO;
import com.onyxdb.platform.generated.openapi.models.RoleCreateDTO;
import com.onyxdb.platform.generated.openapi.models.RoleRequestCreateDTO;
import com.onyxdb.platform.generated.openapi.models.RoleWithPermissionsCreateDTO;

public class TestDataFactory {

    public static AccountCreateDTO createAccountDTO(
            String username, String password, String email, String firstName, String lastName,
            Map<String, Object> data
    ) {
        AccountCreateDTO accountDTO = new AccountCreateDTO();
        accountDTO.setUsername(username);
        accountDTO.setPassword(password);
        accountDTO.setEmail(email);
        accountDTO.setFirstName(firstName);
        accountDTO.setLastName(lastName);
        accountDTO.setData(data != null ? data : new HashMap<>());
        return accountDTO;
    }

    public static RoleCreateDTO createRoleDTO(
            String roleType, String name, String shopName, String description, Boolean isShopHidden, String entity, UUID productId, UUID orgUnitId
    ) {
        RoleCreateDTO roleDTO = new RoleCreateDTO();
        roleDTO.setRoleType(roleType);
        roleDTO.setName(name);
        roleDTO.setShopName(shopName);
        roleDTO.setIsShopHidden(isShopHidden);
        roleDTO.setDescription(description);
        roleDTO.setEntity(entity);
        roleDTO.setProductId(productId);
        roleDTO.setOrgUnitId(orgUnitId);
        return roleDTO;
    }

    public static BusinessRoleCreateDTO createBusinessRoleDTO(
            String name, String description, UUID parentId
    ) {
        BusinessRoleCreateDTO businessRoleDTO = new BusinessRoleCreateDTO();
        businessRoleDTO.setName(name.toLowerCase().replace(' ', '-'));
        businessRoleDTO.setShopName(name);
        businessRoleDTO.setDescription(description);
        businessRoleDTO.setParentId(parentId);
        return businessRoleDTO;
    }

    public static PermissionCreateDTO createPermissionDTO(
            String actionType, String resourceType, Map<String, Object> data
    ) {
        PermissionCreateDTO permissionDTO = new PermissionCreateDTO();
        permissionDTO.setActionType(actionType);
        permissionDTO.setResourceType(resourceType);
        permissionDTO.setData(data != null ? data : new HashMap<>());
        return permissionDTO;
    }


    public static RoleRequestCreateDTO createRoleRequestDTO(
            UUID roleId, UUID accountId, UUID ownerId, String reason
    ) {
        RoleRequestCreateDTO roleRequestDTO = new RoleRequestCreateDTO();
        roleRequestDTO.setRoleId(roleId);
        roleRequestDTO.setAccountId(accountId);
        roleRequestDTO.setOwnerId(ownerId);
        roleRequestDTO.setReason(reason);
        roleRequestDTO.setStatus(RoleRequestCreateDTO.StatusEnum.WAITING);
        return roleRequestDTO;
    }

    public static DomainComponentCreateDTO createDomainComponentDTO(
            String name, String description
    ) {
        DomainComponentCreateDTO domainComponentDTO = new DomainComponentCreateDTO();
        domainComponentDTO.setName(name);
        domainComponentDTO.setDescription(description);
        return domainComponentDTO;
    }

    public static OrganizationUnitCreateDTO createOrganizationUnitDTO(
            String name, String description, UUID ownerId, UUID domainComponentId, UUID parentId
    ) {
        OrganizationUnitCreateDTO organizationUnitDTO = new OrganizationUnitCreateDTO();
        organizationUnitDTO.setName(name);
        organizationUnitDTO.setOwnerId(ownerId);
        organizationUnitDTO.setDescription(description);
        organizationUnitDTO.setDomainComponentId(domainComponentId);
        organizationUnitDTO.setParentId(parentId);
        return organizationUnitDTO;
    }

    public static ProductCreateDTO createProductDTO(
            String name, String description, UUID parentId, UUID ownerId
    ) {
        ProductCreateDTO productDTO = new ProductCreateDTO();
        productDTO.setName(name);
        productDTO.setDescription(description);
        productDTO.setParentId(parentId);
        productDTO.setOwnerId(ownerId);
        return productDTO;
    }

    public static RoleWithPermissionsCreateDTO createRoleWithPermissionsDTO(RoleCreateDTO role, List<PermissionCreateDTO> permissions) {
        RoleWithPermissionsCreateDTO roleWithPermissionsDTO = new RoleWithPermissionsCreateDTO();
        roleWithPermissionsDTO.setRole(role);
        roleWithPermissionsDTO.setPermissions(permissions);
        return roleWithPermissionsDTO;
    }
}
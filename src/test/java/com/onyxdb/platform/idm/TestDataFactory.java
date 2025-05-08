package com.onyxdb.platform.idm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.onyxdb.platform.generated.openapi.models.AccountPostDTO;
import com.onyxdb.platform.generated.openapi.models.BusinessRolePostDTO;
import com.onyxdb.platform.generated.openapi.models.DomainComponentPostDTO;
import com.onyxdb.platform.generated.openapi.models.OrganizationUnitPostDTO;
import com.onyxdb.platform.generated.openapi.models.PermissionPostDTO;
import com.onyxdb.platform.generated.openapi.models.ProductPostDTO;
import com.onyxdb.platform.generated.openapi.models.RolePostDTO;
import com.onyxdb.platform.generated.openapi.models.RoleRequestPostDTO;
import com.onyxdb.platform.generated.openapi.models.RoleWithPermissionsPostDTO;

public class TestDataFactory {

    public static AccountPostDTO createAccountDTO(
            String username, String password, String email, String firstName, String lastName,
            Map<String, Object> data
    ) {
        AccountPostDTO accountDTO = new AccountPostDTO();
        accountDTO.setUsername(username);
        accountDTO.setPassword(password);
        accountDTO.setEmail(email);
        accountDTO.setFirstName(firstName);
        accountDTO.setLastName(lastName);
        accountDTO.setData(data != null ? data : new HashMap<>());
        return accountDTO;
    }

    public static RolePostDTO createRoleDTO(
            String roleType, String name, String shopName, String description, Boolean isShopHidden, String entity, UUID productId, UUID orgUnitId
    ) {
        RolePostDTO roleDTO = new RolePostDTO();
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

    public static BusinessRolePostDTO createBusinessRoleDTO(
            String name, String description, UUID parentId
    ) {
        BusinessRolePostDTO businessRoleDTO = new BusinessRolePostDTO();
        businessRoleDTO.setName(name.toLowerCase().replace(' ', '-'));
        businessRoleDTO.setShopName(name);
        businessRoleDTO.setDescription(description);
        businessRoleDTO.setParentId(parentId);
        return businessRoleDTO;
    }

    public static PermissionPostDTO createPermissionDTO(
            String actionType, String resourceType, Map<String, Object> data
    ) {
        PermissionPostDTO permissionDTO = new PermissionPostDTO();
        permissionDTO.setActionType(actionType);
        permissionDTO.setResourceType(resourceType);
        permissionDTO.setData(data != null ? data : new HashMap<>());
        return permissionDTO;
    }


    public static RoleRequestPostDTO createRoleRequestDTO(
            UUID roleId, UUID accountId, UUID ownerId, String reason
    ) {
        RoleRequestPostDTO roleRequestDTO = new RoleRequestPostDTO();
        roleRequestDTO.setRoleId(roleId);
        roleRequestDTO.setAccountId(accountId);
        roleRequestDTO.setOwnerId(ownerId);
        roleRequestDTO.setReason(reason);
        roleRequestDTO.setStatus(RoleRequestPostDTO.StatusEnum.WAITING);
        return roleRequestDTO;
    }

    public static DomainComponentPostDTO createDomainComponentDTO(
            String name, String description
    ) {
        DomainComponentPostDTO domainComponentDTO = new DomainComponentPostDTO();
        domainComponentDTO.setName(name);
        domainComponentDTO.setDescription(description);
        return domainComponentDTO;
    }

    public static OrganizationUnitPostDTO createOrganizationUnitDTO(
            String name, String description, UUID ownerId, UUID domainComponentId, UUID parentId
    ) {
        OrganizationUnitPostDTO organizationUnitDTO = new OrganizationUnitPostDTO();
        organizationUnitDTO.setName(name);
        organizationUnitDTO.setOwnerId(ownerId);
        organizationUnitDTO.setDescription(description);
        organizationUnitDTO.setDomainComponentId(domainComponentId);
        organizationUnitDTO.setParentId(parentId);
        return organizationUnitDTO;
    }

    public static ProductPostDTO createProductDTO(
            String name, String description, UUID parentId, UUID ownerId
    ) {
        ProductPostDTO productDTO = new ProductPostDTO();
        productDTO.setName(name);
        productDTO.setDescription(description);
        productDTO.setParentId(parentId);
        productDTO.setOwnerId(ownerId);
        return productDTO;
    }

    public static RoleWithPermissionsPostDTO createRoleWithPermissionsDTO(RolePostDTO role, List<PermissionPostDTO> permissions) {
        RoleWithPermissionsPostDTO roleWithPermissionsDTO = new RoleWithPermissionsPostDTO();
        roleWithPermissionsDTO.setRole(role);
        roleWithPermissionsDTO.setPermissions(permissions);
        return roleWithPermissionsDTO;
    }
}
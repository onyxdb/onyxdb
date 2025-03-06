//package com.onyxdb.idm.services;
//
//import com.onyxdb.idm.models.Permission;
//import com.onyxdb.idm.repositories.PermissionRepository;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.UUID;
//
//
///**
// * @author ArtemFed
// */
//@Service
//@RequiredArgsConstructor
//public class AccessCheckService {
//
//    private final PermissionRepository permissionRepository;
//
//    public Optional<Permission> checkAccountPermissionToProduct(UUID accountId, UUID productId, String permissionType) {
//        return permissionRepository.findAccountPermissionToProduct(accountId, productId, permissionType);
//    }
//
//    public Optional<Permission> checkAccountPermissionToOrgUnit(UUID accountId, UUID orgUnitId, String permissionType) {
//        return permissionRepository.findAccountPermissionToOrgUnit(accountId, orgUnitId, permissionType);
//    }
//
//    public Map<String, Permission> getAllAccountPermissions(UUID accountId, String resourceType) {
//        return permissionRepository.findAllAccountPermissions(accountId, resourceType);
//    }
//
//    public List<String> getAccountPermissionsToProduct(UUID accountId, UUID productId, String resourceType) {
//        return permissionRepository.findAccountPermissionsToProduct(accountId, productId, resourceType);
//    }
//
//    public List<String> getAccountPermissionsToOrgUnit(UUID accountId, UUID orgUnitId, String resourceType) {
//        return permissionRepository.findAccountPermissionsToOrgUnit(accountId, orgUnitId, resourceType);
//    }
//}
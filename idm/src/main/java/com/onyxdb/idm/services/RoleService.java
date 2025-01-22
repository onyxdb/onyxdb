//package com.onyxdb.idm.services;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import com.onyxdb.idm.models.RoleDTO;
//import com.onyxdb.idm.repositories.RoleRepository;
//
//@Service
//@RequiredArgsConstructor
//public class RoleService {
//    private final RoleRepository roleRepository;
//
//    public Optional<RoleDTO> getRoleById(UUID id) {
//        return roleRepository.findById(id);
//    }
//
//    public List<RoleDTO> getAllRoles() {
//        return roleRepository.findAll();
//    }
//
//    public RoleDTO createRole(RoleDTO role) {
//        roleRepository.create(role);
//        return role;
//    }
//
//    public RoleDTO updateRole(RoleDTO role) {
//        roleRepository.update(role);
//        return role;
//    }
//
//    public void deleteRole(UUID id) {
//        roleRepository.delete(id);
//    }
//}

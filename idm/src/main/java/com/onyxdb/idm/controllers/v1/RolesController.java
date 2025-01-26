//package com.onyxdb.idm.controllers.v1;
//
//import java.util.List;
//import java.util.UUID;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.onyxdb.idm.generated.openapi.apis.AccountsApi;
//import com.onyxdb.idm.generated.openapi.apis.RolesApi;
//import com.onyxdb.idm.generated.openapi.models.AccountDTO;
//import com.onyxdb.idm.generated.openapi.models.RoleDTO;
//import com.onyxdb.idm.models.Account;
//import com.onyxdb.idm.services.AccountService;
//
//@RestController
//@RequiredArgsConstructor
//public class RolesController implements RolesApi {
//
//    private final RolesService accountService;
//
//    /**
//     * POST /api/v1/roles : Create a new role
//     *
//     * @param roleDTO (required)
//     * @return Created (status code 201)
//     * or Bad Request (status code 400)
//     */
//    @Override
//    public ResponseEntity<RoleDTO> createRole(RoleDTO roleDTO) {
//        return null;
//    }
//
//    /**
//     * DELETE /api/v1/roles/{roleId} : Delete a role by ID
//     *
//     * @param roleId (required)
//     * @return No Content (status code 204)
//     * or Not Found (status code 404)
//     * or Bad Request (status code 400)
//     */
//    @Override
//    public ResponseEntity<Void> deleteRole(UUID roleId) {
//        return null;
//    }
//
//    /**
//     * GET /api/v1/roles : Get all roles
//     *
//     * @return OK (status code 200)
//     * or Bad Request (status code 400)
//     */
//    @Override
//    public ResponseEntity<List<RoleDTO>> getAllRoles() {
//        return null;
//    }
//
//    /**
//     * GET /api/v1/roles/{roleId} : Get a role by ID
//     *
//     * @param roleId (required)
//     * @return OK (status code 200)
//     * or Not Found (status code 404)
//     * or Bad Request (status code 400)
//     */
//    @Override
//    public ResponseEntity<RoleDTO> getRoleById(UUID roleId) {
//        return null;
//    }
//
//    /**
//     * PUT /api/v1/roles/{roleId} : Update a role by ID
//     *
//     * @param roleId  (required)
//     * @param roleDTO (required)
//     * @return OK (status code 200)
//     * or Not Found (status code 404)
//     * or Bad Request (status code 400)
//     */
//    @Override
//    public ResponseEntity<RoleDTO> updateRole(UUID roleId, RoleDTO roleDTO) {
//        return null;
//    }
//}
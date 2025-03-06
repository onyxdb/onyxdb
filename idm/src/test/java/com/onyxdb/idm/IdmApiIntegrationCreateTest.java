package com.onyxdb.idm;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.onyxdb.idm.generated.openapi.models.AccountDTO;
import com.onyxdb.idm.generated.openapi.models.BusinessRoleDTO;
import com.onyxdb.idm.generated.openapi.models.DomainComponentDTO;
import com.onyxdb.idm.generated.openapi.models.OrganizationUnitDTO;
import com.onyxdb.idm.generated.openapi.models.PermissionDTO;
import com.onyxdb.idm.generated.openapi.models.ProductDTO;
import com.onyxdb.idm.generated.openapi.models.RoleDTO;
import com.onyxdb.idm.generated.openapi.models.RoleRequestDTO;
import com.onyxdb.idm.generated.openapi.models.RoleWithPermissionsDTO;

import static com.onyxdb.idm.TestDataFactory.createRoleRequestDTO;


@Disabled
@ActiveProfiles({"dev"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IdmApiIntegrationCreateTest extends PostgresTests {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        this.webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:9002")
                .build();
    }

    @Test
    public void testCreateAllEntities() {
        // 1. Создаем два Domain Components (DC)
        DomainComponentDTO dc1 = TestDataFactory.createDomainComponentDTO(null, "DC1", "First Domain Component");
        DomainComponentDTO dc2 = TestDataFactory.createDomainComponentDTO(null, "DC2", "Second Domain Component");

        dc1 = createDomainComponent(dc1);
        dc2 = createDomainComponent(dc2);

        // 2. Создаем иерархию Organization Units (OU) для первого DC
        OrganizationUnitDTO ou1 = TestDataFactory.createOrganizationUnitDTO(null, "OU1", "First OU", dc1.getId(), null);
        OrganizationUnitDTO ou2 = TestDataFactory.createOrganizationUnitDTO(null, "OU2", "Second OU", dc1.getId(), null);
        OrganizationUnitDTO ou3 = TestDataFactory.createOrganizationUnitDTO(null, "OU3", "Third OU", dc1.getId(), null);

        ou1 = createOrganizationUnit(ou1);
        ou2 = createOrganizationUnit(ou2);
        ou3 = createOrganizationUnit(ou3);

        // Создаем дочерние OU для каждого из трех главных OU
        OrganizationUnitDTO ou1Child1 = TestDataFactory.createOrganizationUnitDTO(null, "OU1-Child1", "Child of OU1", dc1.getId(), ou1.getId());
        OrganizationUnitDTO ou2Child1 = TestDataFactory.createOrganizationUnitDTO(null, "OU2-Child1", "Child of OU2", dc1.getId(), ou2.getId());
        OrganizationUnitDTO ou2Child2 = TestDataFactory.createOrganizationUnitDTO(null, "OU2-Child2", "Child of OU2", dc1.getId(), ou2.getId());
        OrganizationUnitDTO ou3Child1 = TestDataFactory.createOrganizationUnitDTO(null, "OU3-Child1", "Child of OU3", dc1.getId(), ou3.getId());
        OrganizationUnitDTO ou3Child2 = TestDataFactory.createOrganizationUnitDTO(null, "OU3-Child2", "Child of OU3", dc1.getId(), ou3.getId());
        OrganizationUnitDTO ou3Child3 = TestDataFactory.createOrganizationUnitDTO(null, "OU3-Child3", "Child of OU3", dc1.getId(), ou3.getId());

        ou1Child1 = createOrganizationUnit(ou1Child1);
        ou2Child1 = createOrganizationUnit(ou2Child1);
        ou2Child2 = createOrganizationUnit(ou2Child2);
        ou3Child1 = createOrganizationUnit(ou3Child1);
        ou3Child2 = createOrganizationUnit(ou3Child2);
        ou3Child3 = createOrganizationUnit(ou3Child3);

        // Создаем еще одного ребенка для одного из детей OU3
        OrganizationUnitDTO ou3Child1Child1 = TestDataFactory.createOrganizationUnitDTO(null, "OU3-Child1-Child1", "Child of OU3-Child1", dc1.getId(), ou3Child1.getId());
        ou3Child1Child1 = createOrganizationUnit(ou3Child1Child1);

        // 3. Создаем один OU для второго DC
        OrganizationUnitDTO ou4 = TestDataFactory.createOrganizationUnitDTO(null, "OU4", "Fourth OU", dc2.getId(), null);
        ou4 = createOrganizationUnit(ou4);

        // Создаем дочерний OU для OU4
        OrganizationUnitDTO ou4Child1 = TestDataFactory.createOrganizationUnitDTO(null, "OU4-Child1", "Child of OU4", dc2.getId(), ou4.getId());
        ou4Child1 = createOrganizationUnit(ou4Child1);

        // 4. Создаем иерархию продуктов
        ProductDTO rootProduct = TestDataFactory.createProductDTO(null, "RootProduct", "Root Product", null);
        rootProduct = createProduct(rootProduct);

        ProductDTO product1 = TestDataFactory.createProductDTO(null, "Product1", "First Product", rootProduct.getId());
        ProductDTO product2 = TestDataFactory.createProductDTO(null, "Product2", "Second Product", rootProduct.getId());
        ProductDTO product3 = TestDataFactory.createProductDTO(null, "Product3", "Third Product", rootProduct.getId());

        product1 = createProduct(product1);
        product2 = createProduct(product2);
        product3 = createProduct(product3);

        ProductDTO product1Child1 = TestDataFactory.createProductDTO(null, "Product1Child1", "First Product Child1", product1.getId());
        ProductDTO product1Child2 = TestDataFactory.createProductDTO(null, "Product1Child2", "First Product Child2", product1.getId());

        product1Child1 = createProduct(product1Child1);
        product1Child2 = createProduct(product1Child2);

        ProductDTO product2Child1 = TestDataFactory.createProductDTO(null, "Product2Child1", "Second Product Child1", product2.getId());
        product2Child1 = createProduct(product2Child1);

        // 5. Создаем аккаунты
        AccountDTO account1 = TestDataFactory.createAccountDTO(null, "user1", "pass1", "user1@example.com", "User", "One", null);
        AccountDTO account2 = TestDataFactory.createAccountDTO(null, "user2", "pass2", "user2@example.com", "User", "Two", null);
        AccountDTO account3 = TestDataFactory.createAccountDTO(null, "user3", "pass3", "user3@example.com", "User", "Three", null);
        AccountDTO account4 = TestDataFactory.createAccountDTO(null, "user4", "pass4", "user4@example.com", "User", "Four", null);
        AccountDTO account5 = TestDataFactory.createAccountDTO(null, "user5", "pass5", "user5@example.com", "User", "Five", null);

        account1 = createAccount(account1);
        account2 = createAccount(account2);
        account3 = createAccount(account3);
        account4 = createAccount(account4);
        account5 = createAccount(account5);

        // Создаем 2 аккаунта для второго DC
        AccountDTO account6 = TestDataFactory.createAccountDTO(null, "user6", "pass6", "user6@example.com", "User", "Six", null);
        AccountDTO account7 = TestDataFactory.createAccountDTO(null, "user7", "pass7", "user7@example.com", "User", "Seven", null);

        account6 = createAccount(account6);
        account7 = createAccount(account7);

        // 6. Связываем аккаунты с OU
        linkAccountToOrganizationUnit(ou1.getId(), account1.getId());
        linkAccountToOrganizationUnit(ou2.getId(), account2.getId());
        linkAccountToOrganizationUnit(ou3.getId(), account3.getId());
        linkAccountToOrganizationUnit(ou1Child1.getId(), account4.getId());
        linkAccountToOrganizationUnit(ou2Child1.getId(), account5.getId());
        linkAccountToOrganizationUnit(ou4.getId(), account6.getId());
        linkAccountToOrganizationUnit(ou4Child1.getId(), account7.getId());

        // 7. Создаем роли с разными наборами прав
        PermissionDTO anyPermission = TestDataFactory.createPermissionDTO(null, "ANY", "IDM");
        PermissionDTO createPermission = TestDataFactory.createPermissionDTO(null, "CREATE", "IDM");
        PermissionDTO patchPermission = TestDataFactory.createPermissionDTO(null, "PATCH", "IDM");
        PermissionDTO getPermission = TestDataFactory.createPermissionDTO(null, "GET", "IDM");
        PermissionDTO deletePermission = TestDataFactory.createPermissionDTO(null, "DELETE", "IDM");

        RoleDTO adminOu1Role = TestDataFactory.createRoleDTO(null, "ADMIN", "Admin OU1", "Admin role for OU1", false, null, ou1.getId());
        RoleDTO adminOu2Role = TestDataFactory.createRoleDTO(null, "ADMIN", "Admin OU2", "Admin role for OU2", false, null, ou2.getId());
        RoleDTO adminMdbRole = TestDataFactory.createRoleDTO(null, "ADMIN", "Admin MDB", "Admin role for root product", false, rootProduct.getId(), null);
        RoleDTO ownerOuRole = TestDataFactory.createRoleDTO(null, "OWNER", "Owner OU", "Owner role for OU", false, null, ou3.getId());
        RoleDTO ownerProductRole = TestDataFactory.createRoleDTO(null, "OWNER", "Owner Product", "Owner role for Product", false, product1.getId(), null);
        RoleDTO developerRole = TestDataFactory.createRoleDTO(null, "DEVELOPER", "Developer", "Developer role for leaf products", false, product1Child1.getId(), null);
        RoleDTO auditorRole = TestDataFactory.createRoleDTO(null, "AUDITOR", "Auditor", "Auditor role for root products and OUs", false, rootProduct.getId(), null);

        RoleWithPermissionsDTO adminOu1RoleWP = TestDataFactory.createRoleWithPermissionsDTO(adminOu1Role, List.of(anyPermission));
        RoleWithPermissionsDTO adminOu2RoleWP = TestDataFactory.createRoleWithPermissionsDTO(adminOu2Role, List.of(anyPermission));
        RoleWithPermissionsDTO adminMdbRoleWP = TestDataFactory.createRoleWithPermissionsDTO(adminMdbRole, List.of(anyPermission));
        RoleWithPermissionsDTO ownerOuRoleWP = TestDataFactory.createRoleWithPermissionsDTO(ownerOuRole, List.of(createPermission, patchPermission, getPermission));
        RoleWithPermissionsDTO ownerProductRoleWP = TestDataFactory.createRoleWithPermissionsDTO(ownerProductRole, List.of(createPermission, patchPermission, getPermission));
        RoleWithPermissionsDTO developerRoleWP = TestDataFactory.createRoleWithPermissionsDTO(developerRole, List.of(patchPermission, getPermission));
        RoleWithPermissionsDTO auditorRoleWP = TestDataFactory.createRoleWithPermissionsDTO(auditorRole, List.of(getPermission));

        adminOu1RoleWP = createRole(adminOu1RoleWP);
        adminOu2RoleWP = createRole(adminOu2RoleWP);
        adminMdbRoleWP = createRole(adminMdbRoleWP);
        ownerOuRoleWP = createRole(ownerOuRoleWP);
        ownerProductRoleWP = createRole(ownerProductRoleWP);
        developerRoleWP = createRole(developerRoleWP);
        auditorRoleWP = createRole(auditorRoleWP);

        // 8. Создаем бизнес-роли и распределяем роли
        BusinessRoleDTO employee = TestDataFactory.createBusinessRoleDTO(null, "Employee", "Base employee", null);
        employee = createBusinessRole(employee);

        BusinessRoleDTO javaDeveloper = TestDataFactory.createBusinessRoleDTO(null, "Java Developer", "Role for Java developers", employee.getId());
        BusinessRoleDTO manager = TestDataFactory.createBusinessRoleDTO(null, "Manager", "Role for managers", employee.getId());
        BusinessRoleDTO cto = TestDataFactory.createBusinessRoleDTO(null, "CTO", "Role for CTO", employee.getId());
        BusinessRoleDTO admin = TestDataFactory.createBusinessRoleDTO(null, "Admin", "Role for admins", employee.getId());

        javaDeveloper = createBusinessRole(javaDeveloper);
        manager = createBusinessRole(manager);
        cto = createBusinessRole(cto);
        admin = createBusinessRole(admin);

        // 9. Связываем роли с бизнес-ролями
        linkRoleToBusinessRole(employee.getId(), auditorRoleWP.getRole().getId());

        linkRoleToBusinessRole(javaDeveloper.getId(), developerRoleWP.getRole().getId());
        linkRoleToBusinessRole(manager.getId(), ownerOuRoleWP.getRole().getId());
        linkRoleToBusinessRole(cto.getId(), adminMdbRoleWP.getRole().getId());
        linkRoleToBusinessRole(admin.getId(), adminOu1RoleWP.getRole().getId());
        linkRoleToBusinessRole(admin.getId(), adminOu2RoleWP.getRole().getId());

        // 10. Делаем запросы ролей на аккаунты
        RoleRequestDTO roleRequestAdminOu1 = createRoleRequestDTO(null, adminOu1RoleWP.getRole().getId(), account1.getId(), account1.getId(), "I'm the first user", RoleRequestDTO.StatusEnum.WAITING);
        RoleRequestDTO roleRequestAdminOu2 = createRoleRequestDTO(null, adminOu2RoleWP.getRole().getId(), account1.getId(), account1.getId(), "I'm the first user", RoleRequestDTO.StatusEnum.WAITING);
        RoleRequestDTO roleRequestAdminMDB = createRoleRequestDTO(null, adminMdbRoleWP.getRole().getId(), account1.getId(), account1.getId(), "I'm the first user", RoleRequestDTO.StatusEnum.WAITING);
        RoleRequestDTO roleRequestDeveloper = createRoleRequestDTO(null, ownerProductRoleWP.getRole().getId(), account3.getId(), account1.getId(), "I'm the owner of the product 1", RoleRequestDTO.StatusEnum.WAITING);

        roleRequestAdminOu1 = createRoleRequest(roleRequestAdminOu1);
        roleRequestAdminOu2 = createRoleRequest(roleRequestAdminOu2);
        roleRequestAdminMDB = createRoleRequest(roleRequestAdminMDB);
        roleRequestDeveloper = createRoleRequest(roleRequestDeveloper);

        updateRoleRequestStatus(roleRequestAdminOu1.getId(), RoleRequestDTO.StatusEnum.APPROVED);
        updateRoleRequestStatus(roleRequestAdminOu2.getId(), RoleRequestDTO.StatusEnum.APPROVED);
        updateRoleRequestStatus(roleRequestAdminMDB.getId(), RoleRequestDTO.StatusEnum.APPROVED);
        updateRoleRequestStatus(roleRequestDeveloper.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // 11. Связи Аккаунтов и Бизнес ролей
        linkAccountToBusinessRole(account2.getId(), admin.getId());
        linkAccountToBusinessRole(account3.getId(), manager.getId());
        linkAccountToBusinessRole(account4.getId(), javaDeveloper.getId());

        linkAccountToBusinessRole(account6.getId(), cto.getId());
    }

    private DomainComponentDTO createDomainComponent(DomainComponentDTO domainComponentDTO) {
        return webTestClient.post()
                .uri("/api/v1/domain-components")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(domainComponentDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(DomainComponentDTO.class)
                .returnResult()
                .getResponseBody();
    }

    private OrganizationUnitDTO createOrganizationUnit(OrganizationUnitDTO organizationUnitDTO) {
        return webTestClient.post()
                .uri("/api/v1/organization-units")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(organizationUnitDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(OrganizationUnitDTO.class)
                .returnResult()
                .getResponseBody();
    }

    private ProductDTO createProduct(ProductDTO productDTO) {
        return webTestClient.post()
                .uri("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductDTO.class)
                .returnResult()
                .getResponseBody();
    }

    private AccountDTO createAccount(AccountDTO accountDTO) {
        return webTestClient.post()
                .uri("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accountDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AccountDTO.class)
                .returnResult()
                .getResponseBody();
    }

    private RoleWithPermissionsDTO createRole(RoleWithPermissionsDTO roleWithPermissionsDTO) {
        return webTestClient.post()
                .uri("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roleWithPermissionsDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(RoleWithPermissionsDTO.class)
                .returnResult()
                .getResponseBody();
    }

    private BusinessRoleDTO createBusinessRole(BusinessRoleDTO businessRoleDTO) {
        return webTestClient.post()
                .uri("/api/v1/business-roles")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(businessRoleDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BusinessRoleDTO.class)
                .returnResult()
                .getResponseBody();
    }

    private RoleRequestDTO createRoleRequest(RoleRequestDTO roleRequestDTO) {
        return webTestClient.post()
                .uri("/api/v1/roles-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roleRequestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(RoleRequestDTO.class)
                .returnResult()
                .getResponseBody();
    }

    private void linkAccountToOrganizationUnit(UUID ouId, UUID accountId) {
        webTestClient.post()
                .uri("/api/v1/organization-units/{ouId}/accounts/{accountId}", ouId, accountId)
                .exchange()
                .expectStatus().isOk();
    }

    private void linkRoleToBusinessRole(UUID businessRoleId, UUID roleId) {
        webTestClient.post()
                .uri("/api/v1/business-roles/{businessRoleId}/roles/{roleId}", businessRoleId, roleId)
                .exchange()
                .expectStatus().isOk();
    }

    private void linkAccountToBusinessRole(UUID accountId, UUID businessRoleId) {
        webTestClient.post()
                .uri("/api/v1/accounts/{accountId}/business-roles/{businessRoleId}", accountId, businessRoleId)
                .exchange()
                .expectStatus().isOk();
    }

    private void updateRoleRequestStatus(UUID roleRequestId, RoleRequestDTO.StatusEnum status) {
        webTestClient.patch()
                .uri("/api/v1/roles-requests/{roleRequestId}?newStatus={status}", roleRequestId, status.toString())
                .exchange()
                .expectStatus().isOk();
    }
}

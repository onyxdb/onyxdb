package com.onyxdb.idm;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.onyxdb.idm.generated.openapi.models.PaginatedAccountResponse;
import com.onyxdb.idm.generated.openapi.models.PaginatedBusinessRoleResponse;
import com.onyxdb.idm.generated.openapi.models.PaginatedProductResponse;
import com.onyxdb.idm.generated.openapi.models.PaginatedRoleResponse;
import com.onyxdb.idm.generated.openapi.models.ProductDTO;
import com.onyxdb.idm.generated.openapi.models.RoleWithPermissionsDTO;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IdmApiIntegrationGetTest {

    final private String apiAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJwZXJtaXNzaW9ucyI6WyJnbG9iYWwtYW55Il0sInN1YiI6ImQ3NDliYjMxLTA1NWEtNDdkNi1hZDYwLTMyYzNlZTI3NTVjYiIsImlhdCI6MTc0MTc4NTY2OCwiZXhwIjozNTg3Mjg5NjY4fQ.NJCRSpVyqNjqNAYrAQPwfyjOrSQrUSoQKbH01o9puk0";

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        this.webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:9003")
                .defaultHeader("Authorization", "Bearer " + apiAccessToken)
                .build();
    }

    @Test
    public void testGetRequestsOrg() {
        // 1. Получаем все Domain Components
        webTestClient.get()
                .uri("/api/v1/domain-components")
                .exchange()
                .expectStatus().isOk();

        // 2. Получаем все Organization Units
        webTestClient.get()
                .uri("/api/v1/organization-units")
                .exchange()
                .expectStatus().isOk();

        // 3. Получаем Organization Units с пагинацией
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/organization-units")
                        .queryParam("limit", 10)
                        .queryParam("offset", 0)
                        .build())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testGetRequestsProducts() {
        // 4. Получаем все Products
        webTestClient.get()
                .uri("/api/v1/products")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaginatedProductResponse.class)
                .returnResult()
                .getResponseBody();

        // 5. Получаем корневые Products
        List<ProductDTO> rootProducts = webTestClient.get()
                .uri("/api/v1/products-roots")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductDTO.class)
                .returnResult()
                .getResponseBody();

        // 6. Получаем дочерние элементы продукта
        UUID productId = rootProducts != null && !rootProducts.isEmpty() ? rootProducts.getFirst().getId() : null;
        assertNotNull(productId, "Product ID should not be null");

        webTestClient.get()
                .uri("/api/v1/products/{productId}/children", productId)
                .exchange()
                .expectStatus().isOk();

        // 7. Получаем дерево продуктов
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/products/{productId}/tree")
                        .queryParam("depth", 2)
                        .build(productId))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testAllGetRequestsAccountsRoles() {
        // 8. Получаем все Accounts
        PaginatedAccountResponse accounts = webTestClient.get()
                .uri("/api/v1/accounts")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaginatedAccountResponse.class)
                .returnResult()
                .getResponseBody();

        UUID accountId = accounts != null && !accounts.getData().isEmpty() ? accounts.getData().getFirst().getId() : null;
        assertNotNull(accountId, "Account ID should not be null");

        // 9. Получаем Accounts с пагинацией и поиском
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/accounts")
                        .queryParam("search", "user")
                        .queryParam("limit", 5)
                        .queryParam("offset", 0)
                        .build())
                .exchange()
                .expectStatus().isOk();

        // 10. Получаем Account по ID
        webTestClient.get()
                .uri("/api/v1/accounts/{accountId}", accountId)
                .exchange()
                .expectStatus().isOk();

        // 11. Получаем роли аккаунта
        webTestClient.get()
                .uri("/api/v1/accounts/{accountId}/roles", accountId)
                .exchange()
                .expectStatus().isOk();

        // 12. Получаем бизнес-роли аккаунта
        webTestClient.get()
                .uri("/api/v1/accounts/{accountId}/business-roles", accountId)
                .exchange()
                .expectStatus().isOk();

        // 13. Получаем все Roles
        PaginatedRoleResponse roles = webTestClient.get()
                .uri("/api/v1/roles")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaginatedRoleResponse.class)
                .returnResult()
                .getResponseBody();

        UUID roleId = roles != null && !roles.getData().isEmpty() ? roles.getData().getFirst().getId() : null;
        assertNotNull(roleId, "Role ID should not be null");

        // 14. Получаем Roles с пагинацией и поиском
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/roles")
                        .queryParam("search", "admin")
                        .queryParam("limit", 5)
                        .queryParam("offset", 0)
                        .build())
                .exchange()
                .expectStatus().isOk();

        // 15. Получаем Role по ID
        webTestClient.get()
                .uri("/api/v1/roles/{roleId}", roleId)
                .exchange()
                .expectStatus().isOk();

        // 16. Получаем права роли
        RoleWithPermissionsDTO roleWithPermissions = webTestClient.get()
                .uri("/api/v1/roles/{roleId}/permissions", roleId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RoleWithPermissionsDTO.class)
                .returnResult()
                .getResponseBody();

        UUID permissionId = roleWithPermissions != null && !roleWithPermissions.getPermissions().isEmpty() ? roleWithPermissions.getPermissions().getFirst().getId() : null;
        assertNotNull(permissionId, "Permission ID should not be null for role=" + roleId);

        // 17. Получаем все Business Roles
        webTestClient.get()
                .uri("/api/v1/business-roles")
                .exchange()
                .expectStatus().isOk();

        // 18. Получаем Business Roles с пагинацией и поиском
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/business-roles")
                        .queryParam("search", "developer")
                        .queryParam("limit", 5)
                        .queryParam("offset", 0)
                        .build())
                .exchange()
                .expectStatus().isOk();

        // 19. Получаем Business Role по ID
        PaginatedBusinessRoleResponse businessRoles = webTestClient.get()
                .uri("/api/v1/business-roles")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaginatedBusinessRoleResponse.class)
                .returnResult()
                .getResponseBody();

        UUID businessRoleId = businessRoles != null && !businessRoles.getData().isEmpty() ? businessRoles.getData().getFirst().getId() : null;
        assertNotNull(businessRoleId, "Business Role ID should not be null");

        webTestClient.get()
                .uri("/api/v1/business-roles/{businessRoleId}", businessRoleId)
                .exchange()
                .expectStatus().isOk();

        // 20. Получаем дочерние бизнес-роли
        webTestClient.get()
                .uri("/api/v1/business-roles/{businessRoleId}/children", businessRoleId)
                .exchange()
                .expectStatus().isOk();

        // 21. Получаем родительские бизнес-роли
        webTestClient.get()
                .uri("/api/v1/business-roles/{businessRoleId}/parents", businessRoleId)
                .exchange()
                .expectStatus().isOk();

        // 22. Получаем роли бизнес-роли
        webTestClient.get()
                .uri("/api/v1/business-roles/{businessRoleId}/roles", businessRoleId)
                .exchange()
                .expectStatus().isOk();


        // 23. Получаем Permission по ID (используем permissionId, полученный из прав роли)
        webTestClient.get()
                .uri("/api/v1/permissions/{permissionId}", permissionId)
                .exchange()
                .expectStatus().isOk();

        // 24. Проверка доступа пользователя
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/permissions/check")
                        .queryParam("actionType", "CREATE")
                        .queryParam("resourceId", UUID.randomUUID().toString())
                        .build())
                .exchange()
                .expectStatus().isOk();

        // 25. Проверка получения запросов на доступы
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/roles-requests")
                        .queryParam("ownerId", UUID.randomUUID().toString())
                        .queryParam("status", "WAITING")
                        .queryParam("accountId", UUID.randomUUID().toString())
                        .queryParam("limit", 10)
                        .queryParam("offset", 1)
                        .build())
                .exchange()
                .expectStatus().isOk();
    }
}
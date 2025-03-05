package com.onyxdb.idm;

import com.onyxdb.idm.generated.openapi.models.AccountDTO;
import com.onyxdb.idm.generated.openapi.models.RoleDTO;
import com.onyxdb.idm.generated.openapi.models.DomainComponentDTO;
import com.onyxdb.idm.generated.openapi.models.OrganizationUnitDTO;
import com.onyxdb.idm.generated.openapi.models.ProductDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IdmApiIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        this.webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:9002")
                .build();
    }

    @Test
    public void testCreateAccount() {
        AccountDTO accountDTO = TestDataFactory.createAccountDTO();

        webTestClient.post()
                .uri("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(accountDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AccountDTO.class)
                .value(createdAccount -> {
                    assertNotNull(createdAccount.getId());
                    assertNotNull(createdAccount.getUsername());
                });
    }

    @Test
    public void testCreateRole() {
        RoleDTO roleDTO = TestDataFactory.createRoleDTO();

        webTestClient.post()
                .uri("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roleDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(RoleDTO.class)
                .value(createdRole -> {
                    assertNotNull(createdRole.getId());
                    assertNotNull(createdRole.getName());
                });
    }

    @Test
    public void testCreateDomainComponent() {
        DomainComponentDTO domainComponentDTO = TestDataFactory.createDomainComponentDTO();

        webTestClient.post()
                .uri("/api/v1/domain-components")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(domainComponentDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(DomainComponentDTO.class)
                .value(createdDC -> {
                    assertNotNull(createdDC.getId());
                    assertNotNull(createdDC.getName());
                });
    }

    @Test
    public void testCreateOrganizationUnit() {
        OrganizationUnitDTO organizationUnitDTO = TestDataFactory.createOrganizationUnitDTO();

        webTestClient.post()
                .uri("/api/v1/organization-units")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(organizationUnitDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(OrganizationUnitDTO.class)
                .value(createdOU -> {
                    assertNotNull(createdOU.getId());
                    assertNotNull(createdOU.getName());
                });
    }

    @Test
    public void testCreateProduct() {
        ProductDTO productDTO = TestDataFactory.createProductDTO();

        webTestClient.post()
                .uri("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ProductDTO.class)
                .value(createdProduct -> {
                    assertNotNull(createdProduct.getId());
                    assertNotNull(createdProduct.getName());
                });
    }
}
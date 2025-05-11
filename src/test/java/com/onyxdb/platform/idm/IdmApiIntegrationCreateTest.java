package com.onyxdb.platform.idm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.onyxdb.platform.generated.openapi.models.AccountPostDTO;
import com.onyxdb.platform.generated.openapi.models.AccountDTO;
import com.onyxdb.platform.generated.openapi.models.BusinessRolePostDTO;
import com.onyxdb.platform.generated.openapi.models.BusinessRoleDTO;
import com.onyxdb.platform.generated.openapi.models.DomainComponentPostDTO;
import com.onyxdb.platform.generated.openapi.models.DomainComponentDTO;
import com.onyxdb.platform.generated.openapi.models.JwtResponseDTO;
import com.onyxdb.platform.generated.openapi.models.OrganizationUnitPostDTO;
import com.onyxdb.platform.generated.openapi.models.OrganizationUnitDTO;
import com.onyxdb.platform.generated.openapi.models.PermissionPostDTO;
import com.onyxdb.platform.generated.openapi.models.ProductPostDTO;
import com.onyxdb.platform.generated.openapi.models.ProductDTO;
import com.onyxdb.platform.generated.openapi.models.RolePostDTO;
import com.onyxdb.platform.generated.openapi.models.RoleRequestPostDTO;
import com.onyxdb.platform.generated.openapi.models.RoleRequestDTO;
import com.onyxdb.platform.generated.openapi.models.RoleWithPermissionsPostDTO;
import com.onyxdb.platform.generated.openapi.models.RoleWithPermissionsDTO;

import static com.onyxdb.platform.idm.TestDataFactory.createRoleRequestDTO;


@Disabled
@ActiveProfiles({"dev"})
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IdmApiIntegrationCreateTest {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        WebTestClient webLoginClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:9001")
                .build();

        var loginBody = new HashMap<String, String>();
        loginBody.put("username", "admin");
        loginBody.put("password", "admin");

        var loginResponse = webLoginClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtResponseDTO.class)
                .returnResult()
                .getResponseBody();

        this.webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:9001")
                .defaultHeader("Authorization", "Bearer " + loginResponse.getAccessToken())
                .build();
    }

    @Test
    public void testCreateAllEntities() {
        // 1. Создаем аккаунты
        var accountCreate1 = TestDataFactory.createAccountDTO("alexey.ivanov", "pass1", "alexey.ivanov@example.com", "Alexey", "Ivanov", null);
        var accountCreate2 = TestDataFactory.createAccountDTO("ekaterina.smirnova", "pass2", "ekaterina.smirnova@example.com", "Ekaterina", "Smirnova", null);
        var accountCreate3 = TestDataFactory.createAccountDTO("dmitry.petrov", "pass3", "dmitry.petrov@example.com", "Dmitry", "Petrov", null);
        var accountCreate4 = TestDataFactory.createAccountDTO("olga.sidorova", "pass4", "olga.sidorova@example.com", "Olga", "Sidorova", null);
        var accountCreate5 = TestDataFactory.createAccountDTO("sergey.kuznetsov", "pass5", "sergey.kuznetsov@example.com", "Sergey", "Kuznetsov", null);
        var accountCreate6 = TestDataFactory.createAccountDTO("anna.volkova", "pass6", "anna.volkova@example.com", "Anna", "Volkova", null);
        var accountCreate7 = TestDataFactory.createAccountDTO("pavel.morozov", "pass7", "pavel.morozov@example.com", "Pavel", "Morozov", null);
        var accountCreate8 = TestDataFactory.createAccountDTO("marina.egorova", "pass8", "marina.egorova@example.com", "Marina", "Egorova", null);
        var accountCreate9 = TestDataFactory.createAccountDTO("ivan.vasiliev", "pass9", "ivan.vasiliev@example.com", "Ivan", "Vasiliev", null);
        var accountCreate10 = TestDataFactory.createAccountDTO("tatiana.romanova", "pass10", "tatiana.romanova@example.com", "Tatiana", "Romanova", null);
        var accountCreate11 = TestDataFactory.createAccountDTO("vladimir.sokolov", "pass11", "vladimir.sokolov@example.com", "Vladimir", "Sokolov", null);
        var accountCreate12 = TestDataFactory.createAccountDTO("elena.lebedeva", "pass12", "elena.lebedeva@example.com", "Elena", "Lebedeva", null);
        var accountCreate13 = TestDataFactory.createAccountDTO("nikolay.kozlov", "pass13", "nikolay.kozlov@example.com", "Nikolay", "Kozlov", null);
        var accountCreate14 = TestDataFactory.createAccountDTO("irina.fedorova", "pass14", "irina.fedorova@example.com", "Irina", "Fedorova", null);
        var accountCreate15 = TestDataFactory.createAccountDTO("andrey.belov", "pass15", "andrey.belov@example.com", "Andrey", "Belov", null);
        var accountCreate16 = TestDataFactory.createAccountDTO("svetlana.dmitrieva", "pass16", "svetlana.dmitrieva@example.com", "Svetlana", "Dmitrieva", null);
        var accountCreate17 = TestDataFactory.createAccountDTO("mikhail.efimov", "pass17", "mikhail.efimov@example.com", "Mikhail", "Efimov", null);
        var accountCreate18 = TestDataFactory.createAccountDTO("yulia.andreeva", "pass18", "yulia.andreeva@example.com", "Yulia", "Andreeva", null);
        var accountCreate19 = TestDataFactory.createAccountDTO("artem.nikitin", "pass19", "artem.nikitin@example.com", "Artem", "Nikitin", null);
        var accountCreate20 = TestDataFactory.createAccountDTO("nadezhda.tikhonova", "pass20", "nadezhda.tikhonova@example.com", "Nadezhda", "Tikhonova", null);

        // Создаем аккаунты
        AccountDTO account1 = createAccount(accountCreate1);
        AccountDTO account2 = createAccount(accountCreate2);
        AccountDTO account3 = createAccount(accountCreate3);
        AccountDTO account4 = createAccount(accountCreate4);
        AccountDTO account5 = createAccount(accountCreate5);
        AccountDTO account6 = createAccount(accountCreate6);
        AccountDTO account7 = createAccount(accountCreate7);
        AccountDTO account8 = createAccount(accountCreate8);
        AccountDTO account9 = createAccount(accountCreate9);
        AccountDTO account10 = createAccount(accountCreate10);
        AccountDTO account11 = createAccount(accountCreate11);
        AccountDTO account12 = createAccount(accountCreate12);
        AccountDTO account13 = createAccount(accountCreate13);
        AccountDTO account14 = createAccount(accountCreate14);
        AccountDTO account15 = createAccount(accountCreate15);
        AccountDTO account16 = createAccount(accountCreate16);
        AccountDTO account17 = createAccount(accountCreate17);
        AccountDTO account18 = createAccount(accountCreate18);
        AccountDTO account19 = createAccount(accountCreate19);
        AccountDTO account20 = createAccount(accountCreate20);

        // 2. Создаем Domain Components (DC)
        var dc1Create = TestDataFactory.createDomainComponentDTO("Сonnectify", "DC1");
        var dc2Create = TestDataFactory.createDomainComponentDTO("Outsource", "DC2");

        var dc1 = createDomainComponent(dc1Create);
        var dc2 = createDomainComponent(dc2Create);

        // 3. Создаем Organization Units (OU) для DC1
        var ou1Create = TestDataFactory.createOrganizationUnitDTO("Управление", "OU1", account1.getId(), dc1.getId(), null);
        var ou2Create = TestDataFactory.createOrganizationUnitDTO("Разработка", "OU2", account2.getId(), dc1.getId(), null);
        var ou3Create = TestDataFactory.createOrganizationUnitDTO("Аналитика и данные", "OU3", account3.getId(), dc1.getId(), null);

        var ou1 = createOrganizationUnit(ou1Create);
        var ou2 = createOrganizationUnit(ou2Create);
        var ou3 = createOrganizationUnit(ou3Create);

        // 3.1. Дочерние OU для DC1
        var ou1Child1Create = TestDataFactory.createOrganizationUnitDTO("Отдел стратегии и планирования", "OU1-Child1", account1.getId(), dc1.getId(), ou1.getId());
        var ou2Child1Create = TestDataFactory.createOrganizationUnitDTO("Команда Backend-разработки", "OU2-Child1", account2.getId(), dc1.getId(), ou2.getId());
        var ou2Child2Create = TestDataFactory.createOrganizationUnitDTO("Команда Frontend-разработки", "OU2-Child2", account2.getId(), dc1.getId(), ou2.getId());
        var ou3Child1Create = TestDataFactory.createOrganizationUnitDTO("Команда аналитики", "OU3-Child1", account3.getId(), dc1.getId(), ou3.getId());
        var ou3Child2Create = TestDataFactory.createOrganizationUnitDTO("Команда Data Science", "OU3-Child2", account3.getId(), dc1.getId(), ou3.getId());
        var ou3Child3Create = TestDataFactory.createOrganizationUnitDTO("Команда BI (Business Intelligence)", "OU3-Child3", account3.getId(), dc1.getId(), ou3.getId());

        var ou1Child1 = createOrganizationUnit(ou1Child1Create);
        var ou2Child1 = createOrganizationUnit(ou2Child1Create);
        var ou2Child2 = createOrganizationUnit(ou2Child2Create);
        var ou3Child1 = createOrganizationUnit(ou3Child1Create);
        var ou3Child2 = createOrganizationUnit(ou3Child2Create);
        var ou3Child3 = createOrganizationUnit(ou3Child3Create);

        // 3.2. Вложенные OU для аналитики
        var ou3Child1Child1Create = TestDataFactory.createOrganizationUnitDTO("Группа анализа данных", "OU3-Child1-Child1", account14.getId(), dc1.getId(), ou3Child1.getId());
        var ou3Child1Child1 = createOrganizationUnit(ou3Child1Child1Create);

        // 4. Создаем OU для DC2
        var ou4Create = TestDataFactory.createOrganizationUnitDTO("Маркетинг и продажи", "OU4", account6.getId(), dc2.getId(), null);
        var ou4 = createOrganizationUnit(ou4Create);

        var ou4Child1Create = TestDataFactory.createOrganizationUnitDTO("Команда цифрового маркетинга", "OU4-Child1", account6.getId(), dc2.getId(), ou4.getId());
        var ou4Child1 = createOrganizationUnit(ou4Child1Create);

        // 5. Связываем аккаунты с OU
        linkAccountToOrganizationUnit(ou1.getId(), account1.getId()); // Управление
        linkAccountToOrganizationUnit(ou1Child1.getId(), account4.getId()); // Отдел стратегии

        linkAccountToOrganizationUnit(ou2.getId(), account2.getId()); // Разработка
        linkAccountToOrganizationUnit(ou2Child1.getId(), account5.getId()); // Backend
        linkAccountToOrganizationUnit(ou2Child1.getId(), account11.getId());
        linkAccountToOrganizationUnit(ou2Child1.getId(), account9.getId());
        linkAccountToOrganizationUnit(ou2Child2.getId(), account12.getId()); // Frontend
        linkAccountToOrganizationUnit(ou2Child2.getId(), account13.getId());
        linkAccountToOrganizationUnit(ou2Child2.getId(), account8.getId());

        linkAccountToOrganizationUnit(ou3.getId(), account3.getId()); // Аналитика
        linkAccountToOrganizationUnit(ou3Child1.getId(), account14.getId()); // Аналитика
        linkAccountToOrganizationUnit(ou3Child1Child1.getId(), account15.getId()); // Группа анализа данных
        linkAccountToOrganizationUnit(ou3Child1Child1.getId(), account16.getId());
        linkAccountToOrganizationUnit(ou3Child2.getId(), account17.getId()); // Data Science
        linkAccountToOrganizationUnit(ou3Child3.getId(), account18.getId()); // BI
        linkAccountToOrganizationUnit(ou3Child3.getId(), account19.getId());
        linkAccountToOrganizationUnit(ou3Child3.getId(), account20.getId());

        linkAccountToOrganizationUnit(ou4.getId(), account6.getId()); // Маркетинг
        linkAccountToOrganizationUnit(ou4Child1.getId(), account7.getId()); // Цифровой маркетинг
        linkAccountToOrganizationUnit(ou4Child1.getId(), account10.getId());

        // 4. Создаем иерархию продуктов
        // 1. Главный продукт
        var connectifyCreate = TestDataFactory.createProductDTO("Connectify", "Социальная сеть Connectify", null, account1.getId());
        var connectify = createProduct(connectifyCreate);

        // 2. Разработка (Development)
        // 2.1. Production
        var productionCreate = TestDataFactory.createProductDTO("Production", "Основной функционал Connectify", connectify.getId(), account1.getId());
        var production = createProduct(productionCreate);

        var accountManagementCreate = TestDataFactory.createProductDTO("AccountManagement", "Управление аккаунтами", production.getId(), account2.getId());
        var messengerCreate = TestDataFactory.createProductDTO("Messenger", "Мессенджер", production.getId(), account3.getId());
        var contentFeedCreate = TestDataFactory.createProductDTO("ContentFeed", "Лента контента", production.getId(), account4.getId());
        var notificationsCreate = TestDataFactory.createProductDTO("Notifications", "Система уведомлений", production.getId(), account5.getId());

        var accountManagement = createProduct(accountManagementCreate);
        var messenger = createProduct(messengerCreate);
        var contentFeed = createProduct(contentFeedCreate);
        var notifications = createProduct(notificationsCreate);

        // 2.2. Infrastructure
        var infrastructureCreate = TestDataFactory.createProductDTO("Infrastructure", "Инфраструктура платформы", connectify.getId(), account1.getId());
        var infrastructure = createProduct(infrastructureCreate);

        var k8sManagementCreate = TestDataFactory.createProductDTO("K8sManagement", "Управление Kubernetes", infrastructure.getId(), account6.getId());
        var monitoringLoggingCreate = TestDataFactory.createProductDTO("MonitoringLogging", "Мониторинг и логирование", infrastructure.getId(), account7.getId());

        var k8sManagement = createProduct(k8sManagementCreate);
        var monitoringLogging = createProduct(monitoringLoggingCreate);

        // 2.3. Backoffice
        var backofficeCreate = TestDataFactory.createProductDTO("Backoffice", "Внутренний инструмент для сотрудников", connectify.getId(), account1.getId());
        var backoffice = createProduct(backofficeCreate);

        // 3. Аналитика (Analytics)
        var analyticsCreate = TestDataFactory.createProductDTO("Analytics", "Аналитика данных", connectify.getId(), account3.getId());
        var analytics = createProduct(analyticsCreate);

        var userAnalyticsCreate = TestDataFactory.createProductDTO("UserAnalytics", "Аналитика пользователей", analytics.getId(), account14.getId());
        var businessIntelligenceCreate = TestDataFactory.createProductDTO("BusinessIntelligence", "Бизнес-аналитика", analytics.getId(), account15.getId());

        var userAnalytics = createProduct(userAnalyticsCreate);
        var businessIntelligence = createProduct(businessIntelligenceCreate);

        // 4. Маркетинг (Marketing)
        var marketingCreate = TestDataFactory.createProductDTO("Marketing", "Маркетинг и реклама", connectify.getId(), account6.getId());
        var marketing = createProduct(marketingCreate);

        var advertisingCreate = TestDataFactory.createProductDTO("Advertising", "Рекламные кампании", marketing.getId(), account6.getId());
        var socialMediaIntegrationCreate = TestDataFactory.createProductDTO("SocialMediaIntegration", "Интеграция с соцсетями", marketing.getId(), account10.getId());

        var advertising = createProduct(advertisingCreate);
        var socialMediaIntegration = createProduct(socialMediaIntegrationCreate);

        // 8. Создаем бизнес-роли
        var employeeCreate = TestDataFactory.createBusinessRoleDTO("Employee", "Base employee", null);
        var employee = createBusinessRole(employeeCreate);

        var adminCreate = TestDataFactory.createBusinessRoleDTO("Admin", "Role for admins", employee.getId());
        var developerCreate = TestDataFactory.createBusinessRoleDTO("Developer", "Role for developers", employee.getId());
        var backofficerCreate = TestDataFactory.createBusinessRoleDTO("Backoffice", "Role for backoffice", employee.getId());
        var managerCreate = TestDataFactory.createBusinessRoleDTO("Manager", "Role for managers", employee.getId());
        var analyticCreate = TestDataFactory.createBusinessRoleDTO("Analytic", "Role for analytics", employee.getId());
        var marketerCreate = TestDataFactory.createBusinessRoleDTO("Marketer", "Role for marketers", employee.getId());

        var backofficer = createBusinessRole(backofficerCreate);
        var developer = createBusinessRole(developerCreate);
        var manager = createBusinessRole(managerCreate);
        var admin = createBusinessRole(adminCreate);
        var analytic = createBusinessRole(analyticCreate);
        var marketer = createBusinessRole(marketerCreate);

        var backendDevCreate = TestDataFactory.createBusinessRoleDTO("Backend Developer", "Role for backend developer", manager.getId());
        var frontedDevCreate = TestDataFactory.createBusinessRoleDTO("Frontend Developer", "Role for frontend developer", manager.getId());
        var devOpsCreate = TestDataFactory.createBusinessRoleDTO("DevOps", "Role for DevOps", manager.getId());

        var backendDev = createBusinessRole(backendDevCreate);
        var frontedDev = createBusinessRole(frontedDevCreate);
        var devOps = createBusinessRole(devOpsCreate);

        // 7. Создаем права
        var anyPermissionCreate = TestDataFactory.createPermissionDTO("ANY", "IDM", null);
        var createPermissionCreate = TestDataFactory.createPermissionDTO("CREATE", "IDM", null);
        var patchPermissionCreate = TestDataFactory.createPermissionDTO("PATCH", "IDM", null);
        var getPermissionCreate = TestDataFactory.createPermissionDTO("GET", "IDM", null);
        var deletePermissionCreate = TestDataFactory.createPermissionDTO("DELETE", "IDM", null);
        var anyDomainComponentPermissionCreate = TestDataFactory.createPermissionDTO("ANY", "IDM", Map.of("entity", "domain-component"));
        var anyBusinessRolePermissionCreate = TestDataFactory.createPermissionDTO("ANY", "IDM", Map.of("entity", "business-role"));
        var anyRolePermissionCreate = TestDataFactory.createPermissionDTO("ANY", "IDM", Map.of("entity", "role"));
        var anyPermissionPermissionCreate = TestDataFactory.createPermissionDTO("ANY", "IDM", Map.of("entity", "permission"));

        // 8. Создаем роли

        // Глобальные роли
        var  domainComponentAdminCreate = TestDataFactory.createRoleDTO("ADMIN", "Domain Component Admin", "Domain Component Admin", "Admin role for Domain Component", false, "domain-component", null, null);
        var  businessRoleAdminCreate = TestDataFactory.createRoleDTO("ADMIN", "Business Role Admin", "Business Role Admin", "Admin role for Business Role", false, "business-role", null, null);
        var  roleAdminCreate = TestDataFactory.createRoleDTO("ADMIN", "Role Admin", "Role Admin", "Admin role for Role", false, "role", null, null);
        var  permissionAdminCreate = TestDataFactory.createRoleDTO("ADMIN", "Permission Admin", "Permission Admin", "Admin role for Permission", false, "permission", null, null);

        var domainComponentAdminWPCreate = TestDataFactory.createRoleWithPermissionsDTO(domainComponentAdminCreate, List.of(anyDomainComponentPermissionCreate));
        var businessRoleAdminWPCreate = TestDataFactory.createRoleWithPermissionsDTO(businessRoleAdminCreate, List.of(anyBusinessRolePermissionCreate));
        var roleAdminWPCreate = TestDataFactory.createRoleWithPermissionsDTO(roleAdminCreate, List.of(anyRolePermissionCreate));
        var permissionAdminWPCreate = TestDataFactory.createRoleWithPermissionsDTO(permissionAdminCreate, List.of(anyPermissionPermissionCreate));

        var domainComponentAdminWP = createRole(domainComponentAdminWPCreate);
        var businessRoleAdminWP = createRole(businessRoleAdminWPCreate);
        var roleAdminWP = createRole(roleAdminWPCreate);
        var permissionAdminWP = createRole(permissionAdminWPCreate);

        // Создаем роли для каждого продукта (viewer, editor, owner)
        Map<String, RoleWithPermissionsDTO> accountManagementRoles = createProductRoles(accountManagement.getId(), "Account Management");
        Map<String, RoleWithPermissionsDTO> messengerRoles = createProductRoles(messenger.getId(), "Messenger");
        Map<String, RoleWithPermissionsDTO> contentFeedRoles = createProductRoles(contentFeed.getId(), "Content Feed");
        Map<String, RoleWithPermissionsDTO> notificationsRoles = createProductRoles(notifications.getId(), "Notifications");
        Map<String, RoleWithPermissionsDTO> k8sManagementRoles = createProductRoles(k8sManagement.getId(), "K8s Management");
        Map<String, RoleWithPermissionsDTO> monitoringLoggingRoles = createProductRoles(monitoringLogging.getId(), "Monitoring Logging");
        Map<String, RoleWithPermissionsDTO> backofficeRoles = createProductRoles(backoffice.getId(), "Backoffice");
        Map<String, RoleWithPermissionsDTO> analyticsRoles = createProductRoles(analytics.getId(), "Analytics");
        Map<String, RoleWithPermissionsDTO> userAnalyticsRoles = createProductRoles(userAnalytics.getId(), "User Analytics");
        Map<String, RoleWithPermissionsDTO> businessIntelligenceRoles = createProductRoles(businessIntelligence.getId(), "Business Intelligence");
        Map<String, RoleWithPermissionsDTO> marketingRoles1 = createProductRoles(marketing.getId(), "Marketing");

        // Создаем роли для родительских продуктов (admin и auditor)
        Map<String, RoleWithPermissionsDTO> connectifyRoles = createAdminAndAuditorRoles(connectify.getId(), "Connectify");
        Map<String, RoleWithPermissionsDTO> productionRoles = createAdminAndAuditorRoles(production.getId(), "Production");
        Map<String, RoleWithPermissionsDTO> infrastructureRoles = createAdminAndAuditorRoles(infrastructure.getId(), "Infrastructure");
        Map<String, RoleWithPermissionsDTO> analyticsParentRoles = createAdminAndAuditorRoles(analytics.getId(), "Analytics");
        Map<String, RoleWithPermissionsDTO> marketingRoles2 = createAdminAndAuditorRoles(marketing.getId(), "Marketing");

        // Связываем роли с бизнес-ролями
        // Admin
        linkRoleToBusinessRole(admin.getId(), connectifyRoles.get("admin").getRole().getId());
        linkRoleToBusinessRole(admin.getId(), productionRoles.get("admin").getRole().getId());
        linkRoleToBusinessRole(admin.getId(), infrastructureRoles.get("admin").getRole().getId());
        linkRoleToBusinessRole(admin.getId(), analyticsParentRoles.get("admin").getRole().getId());
        linkRoleToBusinessRole(admin.getId(), marketingRoles2.get("admin").getRole().getId());

        // Developer
        linkRoleToBusinessRole(developer.getId(), accountManagementRoles.get("editor").getRole().getId());
        linkRoleToBusinessRole(developer.getId(), messengerRoles.get("editor").getRole().getId());
        linkRoleToBusinessRole(developer.getId(), contentFeedRoles.get("editor").getRole().getId());

        // Backofficer
        linkRoleToBusinessRole(backofficer.getId(), backofficeRoles.get("viewer").getRole().getId());

        // Manager
        linkRoleToBusinessRole(manager.getId(), accountManagementRoles.get("owner").getRole().getId());
        linkRoleToBusinessRole(manager.getId(), k8sManagementRoles.get("owner").getRole().getId());

        // Analytic
        linkRoleToBusinessRole(analytic.getId(), userAnalyticsRoles.get("viewer").getRole().getId());
        linkRoleToBusinessRole(analytic.getId(), businessIntelligenceRoles.get("viewer").getRole().getId());

        // Marketer
        linkRoleToBusinessRole(marketer.getId(), marketingRoles1.get("editor").getRole().getId());

        // Backend Developer
        linkRoleToBusinessRole(backendDev.getId(), accountManagementRoles.get("editor").getRole().getId());
        linkRoleToBusinessRole(backendDev.getId(), notificationsRoles.get("editor").getRole().getId());

        // Frontend Developer
        linkRoleToBusinessRole(frontedDev.getId(), contentFeedRoles.get("editor").getRole().getId());
        linkRoleToBusinessRole(frontedDev.getId(), messengerRoles.get("editor").getRole().getId());

        // DevOps
        linkRoleToBusinessRole(devOps.getId(), k8sManagementRoles.get("editor").getRole().getId());
        linkRoleToBusinessRole(devOps.getId(), monitoringLoggingRoles.get("editor").getRole().getId());

        // Заявка на роль Connectify Admin
        RoleRequestPostDTO roleRequestConnectifyAdminCreate = createRoleRequestDTO(connectifyRoles.get("admin").getRole().getId(), account1.getId(), account1.getId(), "I'm the admin");
        RoleRequestDTO roleRequestConnectifyAdmin = createRoleRequest(roleRequestConnectifyAdminCreate);
        updateRoleRequestStatus(roleRequestConnectifyAdmin.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Production Admin
        RoleRequestPostDTO roleRequestProductionAdminCreate = createRoleRequestDTO(productionRoles.get("admin").getRole().getId(), account1.getId(), account1.getId(), "I'm the admin");
        RoleRequestDTO roleRequestProductionAdmin = createRoleRequest(roleRequestProductionAdminCreate);
        updateRoleRequestStatus(roleRequestProductionAdmin.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Account Management Editor
        RoleRequestPostDTO roleRequestAccountManagementEditorCreate = createRoleRequestDTO(accountManagementRoles.get("editor").getRole().getId(), account2.getId(), account1.getId(), "I'm a developer");
        RoleRequestDTO roleRequestAccountManagementEditor = createRoleRequest(roleRequestAccountManagementEditorCreate);
        updateRoleRequestStatus(roleRequestAccountManagementEditor.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Messenger Editor
        RoleRequestPostDTO roleRequestMessengerEditorCreate = createRoleRequestDTO(messengerRoles.get("editor").getRole().getId(), account2.getId(), account1.getId(), "I'm a developer");
        RoleRequestDTO roleRequestMessengerEditor = createRoleRequest(roleRequestMessengerEditorCreate);
        updateRoleRequestStatus(roleRequestMessengerEditor.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Backoffice Viewer
        RoleRequestPostDTO roleRequestBackofficeViewerCreate = createRoleRequestDTO(backofficeRoles.get("viewer").getRole().getId(), account3.getId(), account1.getId(), "I'm a backofficer");
        RoleRequestDTO roleRequestBackofficeViewer = createRoleRequest(roleRequestBackofficeViewerCreate);
        updateRoleRequestStatus(roleRequestBackofficeViewer.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Account Management Owner
        RoleRequestPostDTO roleRequestAccountManagementOwnerCreate = createRoleRequestDTO(accountManagementRoles.get("owner").getRole().getId(), account4.getId(), account1.getId(), "I'm a manager");
        RoleRequestDTO roleRequestAccountManagementOwner = createRoleRequest(roleRequestAccountManagementOwnerCreate);
        updateRoleRequestStatus(roleRequestAccountManagementOwner.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль K8s Management Owner
        RoleRequestPostDTO roleRequestK8sManagementOwnerCreate = createRoleRequestDTO(k8sManagementRoles.get("owner").getRole().getId(), account4.getId(), account1.getId(), "I'm a manager");
        RoleRequestDTO roleRequestK8sManagementOwner = createRoleRequest(roleRequestK8sManagementOwnerCreate);
        updateRoleRequestStatus(roleRequestK8sManagementOwner.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль User Analytics Viewer
        RoleRequestPostDTO roleRequestUserAnalyticsViewerCreate = createRoleRequestDTO(userAnalyticsRoles.get("viewer").getRole().getId(), account5.getId(), account1.getId(), "I'm an analyst");
        RoleRequestDTO roleRequestUserAnalyticsViewer = createRoleRequest(roleRequestUserAnalyticsViewerCreate);
        updateRoleRequestStatus(roleRequestUserAnalyticsViewer.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Business Intelligence Viewer
        RoleRequestPostDTO roleRequestBusinessIntelligenceViewerCreate = createRoleRequestDTO(businessIntelligenceRoles.get("viewer").getRole().getId(), account5.getId(), account1.getId(), "I'm an analyst");
        RoleRequestDTO roleRequestBusinessIntelligenceViewer = createRoleRequest(roleRequestBusinessIntelligenceViewerCreate);
        updateRoleRequestStatus(roleRequestBusinessIntelligenceViewer.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Account Management Editor
        RoleRequestPostDTO roleRequestAccountManagementEditor2Create = createRoleRequestDTO(accountManagementRoles.get("editor").getRole().getId(), account7.getId(), account1.getId(), "I'm a backend developer");
        RoleRequestDTO roleRequestAccountManagementEditor2 = createRoleRequest(roleRequestAccountManagementEditor2Create);
        updateRoleRequestStatus(roleRequestAccountManagementEditor2.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Marketing Editor
        RoleRequestPostDTO roleRequestMarketingEditorCreate = createRoleRequestDTO(marketingRoles1.get("editor").getRole().getId(), account6.getId(), account1.getId(), "I'm a marketer");
        RoleRequestDTO roleRequestMarketingEditor = createRoleRequest(roleRequestMarketingEditorCreate);
        updateRoleRequestStatus(roleRequestMarketingEditor.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Notifications Editor
        RoleRequestPostDTO roleRequestNotificationsEditorCreate = createRoleRequestDTO(notificationsRoles.get("editor").getRole().getId(), account7.getId(), account1.getId(), "I'm a backend developer");
        RoleRequestDTO roleRequestNotificationsEditor = createRoleRequest(roleRequestNotificationsEditorCreate);
        updateRoleRequestStatus(roleRequestNotificationsEditor.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Content Feed Editor
        RoleRequestPostDTO roleRequestContentFeedEditorCreate = createRoleRequestDTO(contentFeedRoles.get("editor").getRole().getId(), account8.getId(), account1.getId(), "I'm a frontend developer");
        RoleRequestDTO roleRequestContentFeedEditor = createRoleRequest(roleRequestContentFeedEditorCreate);
        updateRoleRequestStatus(roleRequestContentFeedEditor.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Messenger Editor
        RoleRequestPostDTO roleRequestMessengerEditor3Create = createRoleRequestDTO(messengerRoles.get("editor").getRole().getId(), account8.getId(), account1.getId(), "I'm a frontend developer");
        RoleRequestDTO roleRequestMessengerEditor3 = createRoleRequest(roleRequestMessengerEditor3Create);
        updateRoleRequestStatus(roleRequestMessengerEditor3.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль K8s Management Editor
        RoleRequestPostDTO roleRequestK8sManagementEditorCreate = createRoleRequestDTO(k8sManagementRoles.get("editor").getRole().getId(), account9.getId(), account1.getId(), "I'm a DevOps engineer");
        RoleRequestDTO roleRequestK8sManagementEditor = createRoleRequest(roleRequestK8sManagementEditorCreate);
        updateRoleRequestStatus(roleRequestK8sManagementEditor.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Monitoring Logging Editor
        RoleRequestPostDTO roleRequestMonitoringLoggingEditorCreate = createRoleRequestDTO(monitoringLoggingRoles.get("editor").getRole().getId(), account9.getId(), account1.getId(), "I'm a DevOps engineer");
        RoleRequestDTO roleRequestMonitoringLoggingEditor = createRoleRequest(roleRequestMonitoringLoggingEditorCreate);
        updateRoleRequestStatus(roleRequestMonitoringLoggingEditor.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // 1. Связываем аккаунты с бизнес-ролями
        // Admin
        linkAccountToBusinessRole(account1.getId(), admin.getId());

        // Developer
        linkAccountToBusinessRole(account2.getId(), developer.getId());

        // Backofficer
        linkAccountToBusinessRole(account3.getId(), backofficer.getId());

        // Manager
        linkAccountToBusinessRole(account4.getId(), manager.getId());

        // Analytic
        linkAccountToBusinessRole(account5.getId(), analytic.getId());

        // Marketer
        linkAccountToBusinessRole(account6.getId(), marketer.getId());

        // Backend Developer
        linkAccountToBusinessRole(account7.getId(), backendDev.getId());

        // Frontend Developer
        linkAccountToBusinessRole(account8.getId(), frontedDev.getId());

        // DevOps
        linkAccountToBusinessRole(account9.getId(), devOps.getId());
    }

    /**
     * Создает роли viewer, editor, owner для указанного продукта.
     *
     * @param productId   ID продукта, для которого создаются роли.
     * @param productName Название продукта (для формирования имени роли).
     * @return Map<String, RoleWithPermissionsDTO>, содержащий созданные роли.
     */
    public Map<String, RoleWithPermissionsDTO> createProductRoles(UUID productId, String productName) {
        PermissionPostDTO anyPermission = TestDataFactory.createPermissionDTO("ANY", "IDM", null);
        PermissionPostDTO createPermission = TestDataFactory.createPermissionDTO("CREATE", "IDM", null);
        PermissionPostDTO patchPermission = TestDataFactory.createPermissionDTO("PATCH", "IDM", null);
        PermissionPostDTO getPermission = TestDataFactory.createPermissionDTO("GET", "IDM", null);

        // Создаем роли
        RolePostDTO viewerRoleCreate = TestDataFactory.createRoleDTO("VIEWER", productName + " Viewer", productName + " Viewer", "Viewer role for " + productName, false, "product", productId, null);
        RolePostDTO editorRoleCreate = TestDataFactory.createRoleDTO("EDITOR", productName + " Editor", productName + " Editor", "Editor role for " + productName, false, "product", productId, null);
        RolePostDTO ownerRoleCreate = TestDataFactory.createRoleDTO("OWNER", productName + " Owner", productName + " Owner", "Owner role for " + productName, false, "product", productId, null);

        // Создаем RoleWithPermissions
        RoleWithPermissionsPostDTO viewerRoleWPCreate = TestDataFactory.createRoleWithPermissionsDTO(viewerRoleCreate, List.of(getPermission));
        RoleWithPermissionsPostDTO editorRoleWPCreate = TestDataFactory.createRoleWithPermissionsDTO(editorRoleCreate, List.of(getPermission, createPermission, patchPermission));
        RoleWithPermissionsPostDTO ownerRoleWPCreate = TestDataFactory.createRoleWithPermissionsDTO(ownerRoleCreate, List.of(anyPermission));

        // Вызываем createRole
        RoleWithPermissionsDTO viewerRoleWP = createRole(viewerRoleWPCreate);
        RoleWithPermissionsDTO editorRoleWP = createRole(editorRoleWPCreate);
        RoleWithPermissionsDTO ownerRoleWP = createRole(ownerRoleWPCreate);

        // Возвращаем созданные роли
        return Map.of(
                "viewer", viewerRoleWP,
                "editor", editorRoleWP,
                "owner", ownerRoleWP
        );
    }

    /**
     * Создает роли admin и auditor для указанного продукта.
     *
     * @param productId   ID продукта, для которого создаются роли.
     * @param productName Название продукта (для формирования имени роли).
     * @return Map<String, RoleWithPermissionsDTO>, содержащий созданные роли.
     */
    public Map<String, RoleWithPermissionsDTO> createAdminAndAuditorRoles(UUID productId, String productName) {
        PermissionPostDTO anyPermission = TestDataFactory.createPermissionDTO("ANY", "IDM", null);
        PermissionPostDTO createPermission = TestDataFactory.createPermissionDTO("CREATE", "IDM", null);
        PermissionPostDTO patchPermission = TestDataFactory.createPermissionDTO("PATCH", "IDM", null);
        PermissionPostDTO getPermission = TestDataFactory.createPermissionDTO("GET", "IDM", null);
        PermissionPostDTO deletePermission = TestDataFactory.createPermissionDTO("DELETE", "IDM", null);

        // Создаем роли
        RolePostDTO adminRoleCreate = TestDataFactory.createRoleDTO("ADMIN", productName + " Admin", productName + " Admin", "Admin role for " + productName, false, "product", productId, null);
        RolePostDTO auditorRoleCreate = TestDataFactory.createRoleDTO("AUDITOR", productName + " Auditor", productName + " Auditor", "Auditor role for " + productName, false, "product", productId, null);

        // Создаем RoleWithPermissions
        RoleWithPermissionsPostDTO adminRoleWPCreate = TestDataFactory.createRoleWithPermissionsDTO(adminRoleCreate, List.of(getPermission, createPermission, patchPermission, deletePermission));
        RoleWithPermissionsPostDTO auditorRoleWPCreate = TestDataFactory.createRoleWithPermissionsDTO(auditorRoleCreate, List.of(getPermission));

        // Вызываем createRole
        RoleWithPermissionsDTO adminRoleWP = createRole(adminRoleWPCreate);
        RoleWithPermissionsDTO auditorRoleWP = createRole(auditorRoleWPCreate);

        // Возвращаем созданные роли
        return Map.of(
                "admin", adminRoleWP,
                "auditor", auditorRoleWP
        );
    }

    private DomainComponentDTO createDomainComponent(DomainComponentPostDTO domainComponentDTO) {
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

    private OrganizationUnitDTO createOrganizationUnit(OrganizationUnitPostDTO organizationUnitDTO) {
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

    private ProductDTO createProduct(ProductPostDTO productDTO) {
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

    private AccountDTO createAccount(AccountPostDTO accountDTO) {
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

    private RoleWithPermissionsDTO createRole(RoleWithPermissionsPostDTO roleWithPermissionsDTO) {
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

    private BusinessRoleDTO createBusinessRole(BusinessRolePostDTO businessRoleDTO) {
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

    private RoleRequestDTO createRoleRequest(RoleRequestPostDTO roleRequestDTO) {
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

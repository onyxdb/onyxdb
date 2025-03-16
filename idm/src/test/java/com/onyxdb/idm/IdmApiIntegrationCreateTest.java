package com.onyxdb.idm;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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


//@Disabled
//@ActiveProfiles({"dev"})
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IdmApiIntegrationCreateTest {

    final private String apiAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJwZXJtaXNzaW9ucyI6W10sInN1YiI6ImQ3NDliYjMxLTA1NWEtNDdkNi1hZDYwLTMyYzNlZTI3NTVjYiIsImlhdCI6MTc0MTc4MzkyNCwiZXhwIjoxNzQxNzg1NzI0fQ.lME8AS0pT_yA0DMEh2ZaI6fCm_hJx5waAx_5cWY6gQ4";

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
    public void testCreateAllEntities() {
        // 1. Создаем аккаунты
        AccountDTO account1 = TestDataFactory.createAccountDTO(null, "alexey.ivanov", "pass1", "alexey.ivanov@example.com", "Alexey", "Ivanov", null);
        AccountDTO account2 = TestDataFactory.createAccountDTO(null, "ekaterina.smirnova", "pass2", "ekaterina.smirnova@example.com", "Ekaterina", "Smirnova", null);
        AccountDTO account3 = TestDataFactory.createAccountDTO(null, "dmitry.petrov", "pass3", "dmitry.petrov@example.com", "Dmitry", "Petrov", null);
        AccountDTO account4 = TestDataFactory.createAccountDTO(null, "olga.sidorova", "pass4", "olga.sidorova@example.com", "Olga", "Sidorova", null);
        AccountDTO account5 = TestDataFactory.createAccountDTO(null, "sergey.kuznetsov", "pass5", "sergey.kuznetsov@example.com", "Sergey", "Kuznetsov", null);
        AccountDTO account6 = TestDataFactory.createAccountDTO(null, "anna.volkova", "pass6", "anna.volkova@example.com", "Anna", "Volkova", null);
        AccountDTO account7 = TestDataFactory.createAccountDTO(null, "pavel.morozov", "pass7", "pavel.morozov@example.com", "Pavel", "Morozov", null);
        AccountDTO account8 = TestDataFactory.createAccountDTO(null, "marina.egorova", "pass8", "marina.egorova@example.com", "Marina", "Egorova", null);
        AccountDTO account9 = TestDataFactory.createAccountDTO(null, "ivan.vasiliev", "pass9", "ivan.vasiliev@example.com", "Ivan", "Vasiliev", null);
        AccountDTO account10 = TestDataFactory.createAccountDTO(null, "tatiana.romanova", "pass10", "tatiana.romanova@example.com", "Tatiana", "Romanova", null);
        AccountDTO account11 = TestDataFactory.createAccountDTO(null, "vladimir.sokolov", "pass11", "vladimir.sokolov@example.com", "Vladimir", "Sokolov", null);
        AccountDTO account12 = TestDataFactory.createAccountDTO(null, "elena.lebedeva", "pass12", "elena.lebedeva@example.com", "Elena", "Lebedeva", null);
        AccountDTO account13 = TestDataFactory.createAccountDTO(null, "nikolay.kozlov", "pass13", "nikolay.kozlov@example.com", "Nikolay", "Kozlov", null);
        AccountDTO account14 = TestDataFactory.createAccountDTO(null, "irina.fedorova", "pass14", "irina.fedorova@example.com", "Irina", "Fedorova", null);
        AccountDTO account15 = TestDataFactory.createAccountDTO(null, "andrey.belov", "pass15", "andrey.belov@example.com", "Andrey", "Belov", null);
        AccountDTO account16 = TestDataFactory.createAccountDTO(null, "svetlana.dmitrieva", "pass16", "svetlana.dmitrieva@example.com", "Svetlana", "Dmitrieva", null);
        AccountDTO account17 = TestDataFactory.createAccountDTO(null, "mikhail.efimov", "pass17", "mikhail.efimov@example.com", "Mikhail", "Efimov", null);
        AccountDTO account18 = TestDataFactory.createAccountDTO(null, "yulia.andreeva", "pass18", "yulia.andreeva@example.com", "Yulia", "Andreeva", null);
        AccountDTO account19 = TestDataFactory.createAccountDTO(null, "artem.nikitin", "pass19", "artem.nikitin@example.com", "Artem", "Nikitin", null);
        AccountDTO account20 = TestDataFactory.createAccountDTO(null, "nadezhda.tikhonova", "pass20", "nadezhda.tikhonova@example.com", "Nadezhda", "Tikhonova", null);

        // Создаем аккаунты
        account1 = createAccount(account1);
        account2 = createAccount(account2);
        account3 = createAccount(account3);
        account4 = createAccount(account4);
        account5 = createAccount(account5);
        account6 = createAccount(account6);
        account7 = createAccount(account7);
        account8 = createAccount(account8);
        account9 = createAccount(account9);
        account10 = createAccount(account10);
        account11 = createAccount(account11);
        account12 = createAccount(account12);
        account13 = createAccount(account13);
        account14 = createAccount(account14);
        account15 = createAccount(account15);
        account16 = createAccount(account16);
        account17 = createAccount(account17);
        account18 = createAccount(account18);
        account19 = createAccount(account19);
        account20 = createAccount(account20);

        // 2. Создаем Domain Components (DC)
        DomainComponentDTO dc1 = TestDataFactory.createDomainComponentDTO(null, "DC1", "Сonnectify");
        DomainComponentDTO dc2 = TestDataFactory.createDomainComponentDTO(null, "DC2", "Outsource");

        dc1 = createDomainComponent(dc1);
        dc2 = createDomainComponent(dc2);

        // 3. Создаем Organization Units (OU) для DC1
        OrganizationUnitDTO ou1 = TestDataFactory.createOrganizationUnitDTO(null, "OU1", "Управление", account1.getId(), dc1.getId(), null);
        OrganizationUnitDTO ou2 = TestDataFactory.createOrganizationUnitDTO(null, "OU2", "Разработка", account2.getId(), dc1.getId(), null);
        OrganizationUnitDTO ou3 = TestDataFactory.createOrganizationUnitDTO(null, "OU3", "Аналитика и данные", account3.getId(), dc1.getId(), null);

        ou1 = createOrganizationUnit(ou1);
        ou2 = createOrganizationUnit(ou2);
        ou3 = createOrganizationUnit(ou3);

        // 3.1. Дочерние OU для DC1
        OrganizationUnitDTO ou1Child1 = TestDataFactory.createOrganizationUnitDTO(null, "OU1-Child1", "Отдел стратегии и планирования", account1.getId(), dc1.getId(), ou1.getId());
        OrganizationUnitDTO ou2Child1 = TestDataFactory.createOrganizationUnitDTO(null, "OU2-Child1", "Команда Backend-разработки", account2.getId(), dc1.getId(), ou2.getId());
        OrganizationUnitDTO ou2Child2 = TestDataFactory.createOrganizationUnitDTO(null, "OU2-Child2", "Команда Frontend-разработки", account2.getId(), dc1.getId(), ou2.getId());
        OrganizationUnitDTO ou3Child1 = TestDataFactory.createOrganizationUnitDTO(null, "OU3-Child1", "Команда аналитики", account3.getId(), dc1.getId(), ou3.getId());
        OrganizationUnitDTO ou3Child2 = TestDataFactory.createOrganizationUnitDTO(null, "OU3-Child2", "Команда Data Science", account3.getId(), dc1.getId(), ou3.getId());
        OrganizationUnitDTO ou3Child3 = TestDataFactory.createOrganizationUnitDTO(null, "OU3-Child3", "Команда BI (Business Intelligence)", account3.getId(), dc1.getId(), ou3.getId());

        ou1Child1 = createOrganizationUnit(ou1Child1);
        ou2Child1 = createOrganizationUnit(ou2Child1);
        ou2Child2 = createOrganizationUnit(ou2Child2);
        ou3Child1 = createOrganizationUnit(ou3Child1);
        ou3Child2 = createOrganizationUnit(ou3Child2);
        ou3Child3 = createOrganizationUnit(ou3Child3);

        // 3.2. Вложенные OU для аналитики
        OrganizationUnitDTO ou3Child1Child1 = TestDataFactory.createOrganizationUnitDTO(null, "OU3-Child1-Child1", "Группа анализа данных", account14.getId(), dc1.getId(), ou3Child1.getId());
        ou3Child1Child1 = createOrganizationUnit(ou3Child1Child1);

        // 4. Создаем OU для DC2
        OrganizationUnitDTO ou4 = TestDataFactory.createOrganizationUnitDTO(null, "OU4", "Маркетинг и продажи", account6.getId(), dc2.getId(), null);
        ou4 = createOrganizationUnit(ou4);

        OrganizationUnitDTO ou4Child1 = TestDataFactory.createOrganizationUnitDTO(null, "OU4-Child1", "Команда цифрового маркетинга", account6.getId(), dc2.getId(), ou4.getId());
        ou4Child1 = createOrganizationUnit(ou4Child1);

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
        ProductDTO connectify = TestDataFactory.createProductDTO(null, "Connectify", "Социальная сеть Connectify", null, account1.getId());
        connectify = createProduct(connectify);

        // 2. Разработка (Development)
        // 2.1. Production
        ProductDTO production = TestDataFactory.createProductDTO(null, "Production", "Основной функционал Connectify", connectify.getId(), account1.getId());
        production = createProduct(production);

        ProductDTO accountManagement = TestDataFactory.createProductDTO(null, "AccountManagement", "Управление аккаунтами", production.getId(), account2.getId());
        ProductDTO messenger = TestDataFactory.createProductDTO(null, "Messenger", "Мессенджер", production.getId(), account3.getId());
        ProductDTO contentFeed = TestDataFactory.createProductDTO(null, "ContentFeed", "Лента контента", production.getId(), account4.getId());
        ProductDTO notifications = TestDataFactory.createProductDTO(null, "Notifications", "Система уведомлений", production.getId(), account5.getId());

        accountManagement = createProduct(accountManagement);
        messenger = createProduct(messenger);
        contentFeed = createProduct(contentFeed);
        notifications = createProduct(notifications);

        // 2.2. Infrastructure
        ProductDTO infrastructure = TestDataFactory.createProductDTO(null, "Infrastructure", "Инфраструктура платформы", connectify.getId(), account1.getId());
        infrastructure = createProduct(infrastructure);

        ProductDTO k8sManagement = TestDataFactory.createProductDTO(null, "K8sManagement", "Управление Kubernetes", infrastructure.getId(), account6.getId());
        ProductDTO monitoringLogging = TestDataFactory.createProductDTO(null, "MonitoringLogging", "Мониторинг и логирование", infrastructure.getId(), account7.getId());

        k8sManagement = createProduct(k8sManagement);
        monitoringLogging = createProduct(monitoringLogging);

        // 2.3. Backoffice
        ProductDTO backoffice = TestDataFactory.createProductDTO(null, "Backoffice", "Внутренний инструмент для сотрудников", connectify.getId(), account1.getId());
        backoffice = createProduct(backoffice);

        // 3. Аналитика (Analytics)
        ProductDTO analytics = TestDataFactory.createProductDTO(null, "Analytics", "Аналитика данных", connectify.getId(), account3.getId());
        analytics = createProduct(analytics);

        ProductDTO userAnalytics = TestDataFactory.createProductDTO(null, "UserAnalytics", "Аналитика пользователей", analytics.getId(), account14.getId());
        ProductDTO businessIntelligence = TestDataFactory.createProductDTO(null, "BusinessIntelligence", "Бизнес-аналитика", analytics.getId(), account15.getId());

        userAnalytics = createProduct(userAnalytics);
        businessIntelligence = createProduct(businessIntelligence);

        // 4. Маркетинг (Marketing)
        ProductDTO marketing = TestDataFactory.createProductDTO(null, "Marketing", "Маркетинг и реклама", connectify.getId(), account6.getId());
        marketing = createProduct(marketing);

        ProductDTO advertising = TestDataFactory.createProductDTO(null, "Advertising", "Рекламные кампании", marketing.getId(), account6.getId());
        ProductDTO socialMediaIntegration = TestDataFactory.createProductDTO(null, "SocialMediaIntegration", "Интеграция с соцсетями", marketing.getId(), account10.getId());

        advertising = createProduct(advertising);
        socialMediaIntegration = createProduct(socialMediaIntegration);

        // 8. Создаем бизнес-роли
        BusinessRoleDTO employee = TestDataFactory.createBusinessRoleDTO(null, "Employee", "Base employee", null);
        employee = createBusinessRole(employee);

        BusinessRoleDTO admin = TestDataFactory.createBusinessRoleDTO(null, "Admin", "Role for admins", employee.getId());
        BusinessRoleDTO developer = TestDataFactory.createBusinessRoleDTO(null, "Developer", "Role for developers", employee.getId());
        BusinessRoleDTO backofficer = TestDataFactory.createBusinessRoleDTO(null, "Backoffice", "Role for backoffice", employee.getId());
        BusinessRoleDTO manager = TestDataFactory.createBusinessRoleDTO(null, "Manager", "Role for managers", employee.getId());
        BusinessRoleDTO analytic = TestDataFactory.createBusinessRoleDTO(null, "Analytic", "Role for analytics", employee.getId());
        BusinessRoleDTO marketer = TestDataFactory.createBusinessRoleDTO(null, "Marketer", "Role for marketers", employee.getId());

        backofficer = createBusinessRole(backofficer);
        developer = createBusinessRole(developer);
        manager = createBusinessRole(manager);
        admin = createBusinessRole(admin);
        analytic = createBusinessRole(analytic);
        marketer = createBusinessRole(marketer);

        BusinessRoleDTO backendDev = TestDataFactory.createBusinessRoleDTO(null, "Backend Developer", "Role for backend developer", manager.getId());
        BusinessRoleDTO frontedDev = TestDataFactory.createBusinessRoleDTO(null, "Frontend Developer", "Role for frontend developer", manager.getId());
        BusinessRoleDTO devOps = TestDataFactory.createBusinessRoleDTO(null, "DevOps", "Role for DevOps", manager.getId());

        backendDev = createBusinessRole(backendDev);
        frontedDev = createBusinessRole(frontedDev);
        devOps = createBusinessRole(devOps);

        // 7. Создаем права
        PermissionDTO anyPermission = TestDataFactory.createPermissionDTO(null, "ANY", "IDM", null);
        PermissionDTO createPermission = TestDataFactory.createPermissionDTO(null, "CREATE", "IDM", null);
        PermissionDTO patchPermission = TestDataFactory.createPermissionDTO(null, "PATCH", "IDM", null);
        PermissionDTO getPermission = TestDataFactory.createPermissionDTO(null, "GET", "IDM", null);
        PermissionDTO deletePermission = TestDataFactory.createPermissionDTO(null, "DELETE", "IDM", null);
        PermissionDTO anyDomainComponentPermission = TestDataFactory.createPermissionDTO(null, "ANY", "IDM", Map.of("entity", "domain-component"));
        PermissionDTO anyBusinessRolePermission = TestDataFactory.createPermissionDTO(null, "ANY", "IDM", Map.of("entity", "business-role"));
        PermissionDTO anyRolePermission = TestDataFactory.createPermissionDTO(null, "ANY", "IDM", Map.of("entity", "role"));
        PermissionDTO anyPermissionPermission = TestDataFactory.createPermissionDTO(null, "ANY", "IDM", Map.of("entity", "permission"));

        // 8. Создаем роли

        // Глобальные роли
        RoleDTO domainComponentAdmin = TestDataFactory.createRoleDTO(null, "ADMIN", "Domain Component Admin", "Domain Component Admin", "Admin role for Domain Component", false, "domain-component", null, null);
        RoleDTO businessRoleAdmin = TestDataFactory.createRoleDTO(null, "ADMIN", "Business Role Admin", "Business Role Admin", "Admin role for Business Role", false, "business-role", null, null);
        RoleDTO roleAdmin = TestDataFactory.createRoleDTO(null, "ADMIN", "Role Admin", "Role Admin", "Admin role for Role", false, "role", null, null);
        RoleDTO permissionAdmin = TestDataFactory.createRoleDTO(null, "ADMIN", "Permission Admin", "Permission Admin", "Admin role for Permission", false, "permission", null, null);

        RoleWithPermissionsDTO domainComponentAdminWP = TestDataFactory.createRoleWithPermissionsDTO(domainComponentAdmin, List.of(anyDomainComponentPermission));
        RoleWithPermissionsDTO businessRoleAdminWP = TestDataFactory.createRoleWithPermissionsDTO(businessRoleAdmin, List.of(anyBusinessRolePermission));
        RoleWithPermissionsDTO roleAdminWP = TestDataFactory.createRoleWithPermissionsDTO(roleAdmin, List.of(anyRolePermission));
        RoleWithPermissionsDTO permissionAdminWP = TestDataFactory.createRoleWithPermissionsDTO(permissionAdmin, List.of(anyPermissionPermission));

        domainComponentAdminWP = createRole(domainComponentAdminWP);
        businessRoleAdminWP = createRole(businessRoleAdminWP);
        roleAdminWP = createRole(roleAdminWP);
        permissionAdminWP = createRole(permissionAdminWP);

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
        RoleRequestDTO roleRequestConnectifyAdmin = createRoleRequestDTO(null, connectifyRoles.get("admin").getRole().getId(), account1.getId(), account1.getId(), "I'm the admin", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestConnectifyAdmin = createRoleRequest(roleRequestConnectifyAdmin);
        updateRoleRequestStatus(roleRequestConnectifyAdmin.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Production Admin
        RoleRequestDTO roleRequestProductionAdmin = createRoleRequestDTO(null, productionRoles.get("admin").getRole().getId(), account1.getId(), account1.getId(), "I'm the admin", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestProductionAdmin = createRoleRequest(roleRequestProductionAdmin);
        updateRoleRequestStatus(roleRequestProductionAdmin.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Account Management Editor
        RoleRequestDTO roleRequestAccountManagementEditor = createRoleRequestDTO(null, accountManagementRoles.get("editor").getRole().getId(), account2.getId(), account1.getId(), "I'm a developer", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestAccountManagementEditor = createRoleRequest(roleRequestAccountManagementEditor);
        updateRoleRequestStatus(roleRequestAccountManagementEditor.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Messenger Editor
        RoleRequestDTO roleRequestMessengerEditor = createRoleRequestDTO(null, messengerRoles.get("editor").getRole().getId(), account2.getId(), account1.getId(), "I'm a developer", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestMessengerEditor = createRoleRequest(roleRequestMessengerEditor);
        updateRoleRequestStatus(roleRequestMessengerEditor.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Backoffice Viewer
        RoleRequestDTO roleRequestBackofficeViewer = createRoleRequestDTO(null, backofficeRoles.get("viewer").getRole().getId(), account3.getId(), account1.getId(), "I'm a backofficer", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestBackofficeViewer = createRoleRequest(roleRequestBackofficeViewer);
        updateRoleRequestStatus(roleRequestBackofficeViewer.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Account Management Owner
        RoleRequestDTO roleRequestAccountManagementOwner = createRoleRequestDTO(null, accountManagementRoles.get("owner").getRole().getId(), account4.getId(), account1.getId(), "I'm a manager", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestAccountManagementOwner = createRoleRequest(roleRequestAccountManagementOwner);
        updateRoleRequestStatus(roleRequestAccountManagementOwner.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль K8s Management Owner
        RoleRequestDTO roleRequestK8sManagementOwner = createRoleRequestDTO(null, k8sManagementRoles.get("owner").getRole().getId(), account4.getId(), account1.getId(), "I'm a manager", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestK8sManagementOwner = createRoleRequest(roleRequestK8sManagementOwner);
        updateRoleRequestStatus(roleRequestK8sManagementOwner.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль User Analytics Viewer
        RoleRequestDTO roleRequestUserAnalyticsViewer = createRoleRequestDTO(null, userAnalyticsRoles.get("viewer").getRole().getId(), account5.getId(), account1.getId(), "I'm an analyst", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestUserAnalyticsViewer = createRoleRequest(roleRequestUserAnalyticsViewer);
        updateRoleRequestStatus(roleRequestUserAnalyticsViewer.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Business Intelligence Viewer
        RoleRequestDTO roleRequestBusinessIntelligenceViewer = createRoleRequestDTO(null, businessIntelligenceRoles.get("viewer").getRole().getId(), account5.getId(), account1.getId(), "I'm an analyst", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestBusinessIntelligenceViewer = createRoleRequest(roleRequestBusinessIntelligenceViewer);
        updateRoleRequestStatus(roleRequestBusinessIntelligenceViewer.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Account Management Editor
        RoleRequestDTO roleRequestAccountManagementEditor2 = createRoleRequestDTO(null, accountManagementRoles.get("editor").getRole().getId(), account7.getId(), account1.getId(), "I'm a backend developer", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestAccountManagementEditor2 = createRoleRequest(roleRequestAccountManagementEditor2);
        updateRoleRequestStatus(roleRequestAccountManagementEditor2.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Marketing Editor
        RoleRequestDTO roleRequestMarketingEditor = createRoleRequestDTO(null, marketingRoles1.get("editor").getRole().getId(), account6.getId(), account1.getId(), "I'm a marketer", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestMarketingEditor = createRoleRequest(roleRequestMarketingEditor);
        updateRoleRequestStatus(roleRequestMarketingEditor.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Notifications Editor
        RoleRequestDTO roleRequestNotificationsEditor = createRoleRequestDTO(null, notificationsRoles.get("editor").getRole().getId(), account7.getId(), account1.getId(), "I'm a backend developer", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestNotificationsEditor = createRoleRequest(roleRequestNotificationsEditor);
        updateRoleRequestStatus(roleRequestNotificationsEditor.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Content Feed Editor
        RoleRequestDTO roleRequestContentFeedEditor = createRoleRequestDTO(null, contentFeedRoles.get("editor").getRole().getId(), account8.getId(), account1.getId(), "I'm a frontend developer", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestContentFeedEditor = createRoleRequest(roleRequestContentFeedEditor);
        updateRoleRequestStatus(roleRequestContentFeedEditor.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Messenger Editor
        RoleRequestDTO roleRequestMessengerEditor3 = createRoleRequestDTO(null, messengerRoles.get("editor").getRole().getId(), account8.getId(), account1.getId(), "I'm a frontend developer", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestMessengerEditor3 = createRoleRequest(roleRequestMessengerEditor3);
        updateRoleRequestStatus(roleRequestMessengerEditor3.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль K8s Management Editor
        RoleRequestDTO roleRequestK8sManagementEditor = createRoleRequestDTO(null, k8sManagementRoles.get("editor").getRole().getId(), account9.getId(), account1.getId(), "I'm a DevOps engineer", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestK8sManagementEditor = createRoleRequest(roleRequestK8sManagementEditor);
        updateRoleRequestStatus(roleRequestK8sManagementEditor.getId(), RoleRequestDTO.StatusEnum.APPROVED);

        // Заявка на роль Monitoring Logging Editor
        RoleRequestDTO roleRequestMonitoringLoggingEditor = createRoleRequestDTO(null, monitoringLoggingRoles.get("editor").getRole().getId(), account9.getId(), account1.getId(), "I'm a DevOps engineer", RoleRequestDTO.StatusEnum.WAITING);
        roleRequestMonitoringLoggingEditor = createRoleRequest(roleRequestMonitoringLoggingEditor);
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
        PermissionDTO anyPermission = TestDataFactory.createPermissionDTO(null, "ANY", "IDM", null);
        PermissionDTO createPermission = TestDataFactory.createPermissionDTO(null, "CREATE", "IDM", null);
        PermissionDTO patchPermission = TestDataFactory.createPermissionDTO(null, "PATCH", "IDM", null);
        PermissionDTO getPermission = TestDataFactory.createPermissionDTO(null, "GET", "IDM", null);

        // Создаем роли
        RoleDTO viewerRole = TestDataFactory.createRoleDTO(null, "VIEWER", productName + " Viewer", productName + " Viewer", "Viewer role for " + productName, false, "product", productId, null);
        RoleDTO editorRole = TestDataFactory.createRoleDTO(null, "EDITOR", productName + " Editor", productName + " Editor", "Editor role for " + productName, false, "product", productId, null);
        RoleDTO ownerRole = TestDataFactory.createRoleDTO(null, "OWNER", productName + " Owner", productName + " Owner", "Owner role for " + productName, false, "product", productId, null);

        // Создаем RoleWithPermissions
        RoleWithPermissionsDTO viewerRoleWP = TestDataFactory.createRoleWithPermissionsDTO(viewerRole, List.of(getPermission));
        RoleWithPermissionsDTO editorRoleWP = TestDataFactory.createRoleWithPermissionsDTO(editorRole, List.of(getPermission, createPermission, patchPermission));
        RoleWithPermissionsDTO ownerRoleWP = TestDataFactory.createRoleWithPermissionsDTO(ownerRole, List.of(anyPermission));

        // Вызываем createRole
        viewerRoleWP = createRole(viewerRoleWP);
        editorRoleWP = createRole(editorRoleWP);
        ownerRoleWP = createRole(ownerRoleWP);

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
        PermissionDTO anyPermission = TestDataFactory.createPermissionDTO(null, "ANY", "IDM", null);
        PermissionDTO createPermission = TestDataFactory.createPermissionDTO(null, "CREATE", "IDM", null);
        PermissionDTO patchPermission = TestDataFactory.createPermissionDTO(null, "PATCH", "IDM", null);
        PermissionDTO getPermission = TestDataFactory.createPermissionDTO(null, "GET", "IDM", null);
        PermissionDTO deletePermission = TestDataFactory.createPermissionDTO(null, "DELETE", "IDM", null);

        // Создаем роли
        RoleDTO adminRole = TestDataFactory.createRoleDTO(null, "ADMIN", productName + " Admin", productName + " Admin", "Admin role for " + productName, false, "product", productId, null);
        RoleDTO auditorRole = TestDataFactory.createRoleDTO(null, "AUDITOR", productName + " Auditor", productName + " Auditor", "Auditor role for " + productName, false, "product", productId, null);

        // Создаем RoleWithPermissions
        RoleWithPermissionsDTO adminRoleWP = TestDataFactory.createRoleWithPermissionsDTO(adminRole, List.of(getPermission, createPermission, patchPermission, deletePermission));
        RoleWithPermissionsDTO auditorRoleWP = TestDataFactory.createRoleWithPermissionsDTO(auditorRole, List.of(getPermission));

        // Вызываем createRole
        adminRoleWP = createRole(adminRoleWP);
        auditorRoleWP = createRole(auditorRoleWP);

        // Возвращаем созданные роли
        return Map.of(
                "admin", adminRoleWP,
                "auditor", auditorRoleWP
        );
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

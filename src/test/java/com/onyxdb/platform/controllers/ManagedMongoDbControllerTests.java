package com.onyxdb.platform.controllers;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.onyxdb.platform.BaseTest;
import com.onyxdb.platform.TestUtils;
import com.onyxdb.platform.generated.openapi.models.BadRequestResponse;
import com.onyxdb.platform.generated.openapi.models.ClusterBackupConfigDTO;
import com.onyxdb.platform.generated.openapi.models.ClusterResourcesDTO;
import com.onyxdb.platform.generated.openapi.models.CreateMongoClusterRequestDTO;
import com.onyxdb.platform.generated.openapi.models.CreateMongoClusterResponseDTO;
import com.onyxdb.platform.generated.openapi.models.MongoConfigDTO;
import com.onyxdb.platform.generated.openapi.models.MongoDatabaseDTO;
import com.onyxdb.platform.generated.openapi.models.MongoUserDTO;
import com.onyxdb.platform.idm.services.ProductService;
import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clusters.ClusterRepository;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.ClusterBackupConfig;
import com.onyxdb.platform.mdb.clusters.models.ClusterConfig;
import com.onyxdb.platform.mdb.clusters.models.ClusterResources;
import com.onyxdb.platform.mdb.clusters.models.ClusterType;
import com.onyxdb.platform.mdb.clusters.models.ClusterVersion;
import com.onyxdb.platform.mdb.databases.DatabaseRepository;
import com.onyxdb.platform.mdb.exceptions.ClusterAlreadyExistsException;
import com.onyxdb.platform.mdb.exceptions.ProjectNotFoundException;
import com.onyxdb.platform.mdb.exceptions.ResourcePresetNotFoundException;
import com.onyxdb.platform.mdb.hosts.HostMapper;
import com.onyxdb.platform.mdb.hosts.HostRepository;
import com.onyxdb.platform.mdb.models.CreateMongoPermission;
import com.onyxdb.platform.mdb.models.CreateUserWithSecret;
import com.onyxdb.platform.mdb.models.Database;
import com.onyxdb.platform.mdb.models.Host;
import com.onyxdb.platform.mdb.models.MongoRole;
import com.onyxdb.platform.mdb.models.User;
import com.onyxdb.platform.mdb.processing.models.Operation;
import com.onyxdb.platform.mdb.processing.models.OperationType;
import com.onyxdb.platform.mdb.processing.models.payloads.MongoCreateClusterPayload;
import com.onyxdb.platform.mdb.processing.repositories.OperationRepository;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.projects.ProjectRepository;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePreset;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetRepository;
import com.onyxdb.platform.mdb.users.UserMapper;
import com.onyxdb.platform.mdb.users.UserRepository;
import com.onyxdb.platform.mdb.utils.ObjectMapperUtils;
import com.onyxdb.platform.mdb.utils.OnyxdbConsts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class ManagedMongoDbControllerTests extends BaseTest {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ResourcePresetRepository resourcePresetRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ClusterRepository clusterRepository;
    @Autowired
    private HostRepository hostRepository;
    @Autowired
    private HostMapper hostMapper;
    @Autowired
    private DatabaseRepository databaseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        truncateTables();
        prepare();
    }

    @Test
    public void createCluster() {
        try {
            Project project = TestUtils.SANDBOX_PROJECT;
            ResourcePreset resourcePreset = TestUtils.RESOURCE_PRESET;
            String namespace = OnyxdbConsts.NAMESPACE;
            UUID createdBy = TestUtils.ADMIN_ID;
            List<MongoRole> userRoles = List.of(MongoRole.READ_WRITE);

            var rq = new CreateMongoClusterRequestDTO(
                    "test",
                    "test cluster",
                    project.id(),
                    new MongoConfigDTO(
                            "8.0",
                            new ClusterResourcesDTO(
                                    resourcePreset.id(),
                                    "standard",
                                    1073741824L
                            ),
                            3,
                            new ClusterBackupConfigDTO(
                                    true,
                                    "0 0 * * *"
                            )
                    ),
                    new MongoDatabaseDTO(
                            "db1"
                    ),
                    new MongoUserDTO(
                            "u1",
                            "p1"
                    )
            );

            String userSecretName = PsmdbClient.getMongoUserSecretName(
                    project.name(),
                    rq.getName(),
                    rq.getUser().getName()
            );

            Mockito.when(psmdbClient.applyMongoUserSecret(
                    namespace,
                    project.name(),
                    rq.getName(),
                    rq.getUser().getName(),
                    rq.getUser().getPassword()
            )).thenReturn(userSecretName);

            ResponseEntity<CreateMongoClusterResponseDTO> response = restTemplate.exchange(
                    "/api/mdb/mongodb/clusters",
                    HttpMethod.POST,
                    new HttpEntity<>(rq, getHeaders()),
                    CreateMongoClusterResponseDTO.class
            );

            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());

            UUID createdClusterId = response.getBody().getClusterId();
            UUID operationId = response.getBody().getOperationId();

            var expectedCluster = Cluster.create(
                    createdClusterId,
                    rq.getName(),
                    rq.getDescription(),
                    rq.getProjectId(),
                    namespace,
                    ClusterType.MONGODB,
                    new ClusterConfig(
                            ClusterVersion.MONGODB_8_0,
                            new ClusterResources(
                                    rq.getConfig().getResources().getPresetId(),
                                    rq.getConfig().getResources().getStorageClass(),
                                    rq.getConfig().getResources().getStorage()
                            ),
                            rq.getConfig().getReplicas(),
                            new ClusterBackupConfig(
                                    rq.getConfig().getBackup().getIsEnabled(),
                                    rq.getConfig().getBackup().getSchedule()
                            )
                    ),
                    createdBy
            );

            List<String> hostNames = PsmdbClient.calculatePsmdbHostNames(
                    project.name(),
                    rq.getName(),
                    rq.getConfig().getReplicas()
            );
            List<Host> expectedHosts = hostMapper.hostNamesToHosts(
                    hostNames,
                    createdClusterId
            );

            var expectedDatabase = Database.create(
                    rq.getDatabase().getName(),
                    createdClusterId,
                    createdBy
            );

            var expectedUser = userMapper.createUserWithSecretToUser(new CreateUserWithSecret(
                    rq.getUser().getName(),
                    PsmdbClient.getMongoUserSecretName(
                            project.name(),
                            rq.getName(),
                            rq.getUser().getName()
                    ),
                    createdClusterId,
                    List.of(
                            new CreateMongoPermission(
                                    rq.getUser().getName(),
                                    rq.getDatabase().getName(),
                                    createdClusterId,
                                    userRoles
                            )
                    )
            ));

            Cluster createdCluster = clusterRepository.getClusterOrThrow(expectedCluster.id());
            List<Host> createHosts = hostRepository.listHosts(createdClusterId);
            Database createdDatabase = databaseRepository.getDatabase(createdClusterId, rq.getDatabase().getName());
            User createdUser = userRepository.getUser(createdClusterId, rq.getUser().getName());
            Operation createdOperation = operationRepository.getOperation(operationId);

            var operationPayload = new MongoCreateClusterPayload(
                    createdClusterId,
                    expectedDatabase.name(),
                    expectedUser.name(),
                    PsmdbClient.getMongoUserSecretName(
                            project.name(),
                            rq.getName(),
                            rq.getUser().getName()
                    ),
                    namespace,
                    userRoles
            );
            var expectedOperation = Operation.scheduledWithPayload(
                    OperationType.MONGO_CREATE_CLUSTER,
                    createdClusterId,
                    ObjectMapperUtils.convertToString(objectMapper, operationPayload)
            );

            assertThat(createdCluster)
                    .usingRecursiveComparison()
                    .ignoringFields("createdAt")
                    .isEqualTo(expectedCluster);

            assertThat(createHosts)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedHosts);

            assertThat(createdDatabase)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt")
                    .isEqualTo(expectedDatabase);

            assertThat(createdUser)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "permissions.id", "permissions.createdAt")
                    .isEqualTo(expectedUser);

            assertThat(createdOperation)
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt", "payload")
                    .isEqualTo(expectedOperation);

            MongoCreateClusterPayload payload = objectMapper.readValue(
                    createdOperation.payload(),
                    MongoCreateClusterPayload.class
            );

            assertThat(operationPayload)
                    .usingRecursiveComparison()
                    .isEqualTo(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void whenCreateClusterWithDuplicateName_then400() {
        Project project = TestUtils.SANDBOX_PROJECT;
        ResourcePreset resourcePreset = TestUtils.RESOURCE_PRESET;
        String namespace = OnyxdbConsts.NAMESPACE;

        var rq = new CreateMongoClusterRequestDTO(
                "test",
                "test cluster",
                project.id(),
                new MongoConfigDTO(
                        "8.0",
                        new ClusterResourcesDTO(
                                resourcePreset.id(),
                                "standard",
                                1073741824L
                        ),
                        3,
                        new ClusterBackupConfigDTO(
                                true,
                                "0 0 * * *"
                        )
                ),
                new MongoDatabaseDTO(
                        "db1"
                ),
                new MongoUserDTO(
                        "u1",
                        "p1"
                )
        );

        String userSecretName = PsmdbClient.getMongoUserSecretName(
                project.name(),
                rq.getName(),
                rq.getUser().getName()
        );

        Mockito.when(psmdbClient.applyMongoUserSecret(
                namespace,
                project.name(),
                rq.getName(),
                rq.getUser().getName(),
                rq.getUser().getPassword()
        )).thenReturn(userSecretName);

        ResponseEntity<CreateMongoClusterResponseDTO> response = restTemplate.exchange(
                "/api/mdb/mongodb/clusters",
                HttpMethod.POST,
                new HttpEntity<>(rq, getHeaders()),
                CreateMongoClusterResponseDTO.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

        ResponseEntity<BadRequestResponse> response2 = restTemplate.exchange(
                "/api/mdb/mongodb/clusters",
                HttpMethod.POST,
                new HttpEntity<>(rq, getHeaders()),
                BadRequestResponse.class
        );

        var expected = new BadRequestResponse(ClusterAlreadyExistsException.buildMessage(rq.getName()));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
        Assertions.assertNotNull(response2.getBody());
        MatcherAssert.assertThat(response2.getBody(), is(expected));
    }

    @Test
    public void whenCreateClusterWithNotExisingProject_then404() {
        ResourcePreset resourcePreset = TestUtils.RESOURCE_PRESET;

        var rq = new CreateMongoClusterRequestDTO(
                "test",
                "test cluster",
                TestUtils.NOT_EXISTING_PROJECT_ID,
                new MongoConfigDTO(
                        "8.0",
                        new ClusterResourcesDTO(
                                resourcePreset.id(),
                                "standard",
                                1073741824L
                        ),
                        3,
                        new ClusterBackupConfigDTO(
                                true,
                                "0 0 * * *"
                        )
                ),
                new MongoDatabaseDTO(
                        "db1"
                ),
                new MongoUserDTO(
                        "u1",
                        "p1"
                )
        );

        ResponseEntity<BadRequestResponse> response = restTemplate.exchange(
                "/api/mdb/mongodb/clusters",
                HttpMethod.POST,
                new HttpEntity<>(rq, getHeaders()),
                BadRequestResponse.class
        );

        var expected = new BadRequestResponse(ProjectNotFoundException.buildMessage(rq.getProjectId()));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        MatcherAssert.assertThat(response.getBody(), is(expected));
    }

    @Test
    public void whenCreateClusterWithNotExisingResourcePreset_then404() {
        Project project = TestUtils.SANDBOX_PROJECT;

        var rq = new CreateMongoClusterRequestDTO(
                "test",
                "test cluster",
                project.id(),
                new MongoConfigDTO(
                        "8.0",
                        new ClusterResourcesDTO(
                                TestUtils.NOT_EXISTING_RESOURCE_PRESET_ID,
                                "standard",
                                1073741824L
                        ),
                        3,
                        new ClusterBackupConfigDTO(
                                true,
                                "0 0 * * *"
                        )
                ),
                new MongoDatabaseDTO(
                        "db1"
                ),
                new MongoUserDTO(
                        "u1",
                        "p1"
                )
        );

        ResponseEntity<BadRequestResponse> response = restTemplate.exchange(
                "/api/mdb/mongodb/clusters",
                HttpMethod.POST,
                new HttpEntity<>(rq, getHeaders()),
                BadRequestResponse.class
        );

        String expectedMessage = ResourcePresetNotFoundException.buildMessage(rq.getConfig().getResources().getPresetId());
        var expected = new BadRequestResponse(expectedMessage);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        MatcherAssert.assertThat(response.getBody(), is(expected));
    }

    private void prepare() {
        productService.create(TestUtils.PRODUCT);
        resourcePresetRepository.create(TestUtils.RESOURCE_PRESET);
        projectRepository.createProject(TestUtils.SANDBOX_PROJECT);
    }
}

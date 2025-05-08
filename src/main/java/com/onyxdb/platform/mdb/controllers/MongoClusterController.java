package com.onyxdb.platform.mdb.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ManagedMongoDbClustersApi;
import com.onyxdb.platform.generated.openapi.models.CreateMongoClusterRequestDTO;
import com.onyxdb.platform.generated.openapi.models.CreateMongoClusterResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ListMongoClustersResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ListMongoVersionsResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ListStorageClassesResponseDTO;
import com.onyxdb.platform.generated.openapi.models.MongoClusterDTO;
import com.onyxdb.platform.generated.openapi.models.ScheduledOperationDTO;
import com.onyxdb.platform.generated.openapi.models.UpdateMongoClusterRequestDTO;
import com.onyxdb.platform.idm.common.jwt.SecurityContextUtils;
import com.onyxdb.platform.idm.models.Account;
import com.onyxdb.platform.mdb.clusters.ClusterMapper;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.clusters.models.Cluster;
import com.onyxdb.platform.mdb.clusters.models.ClusterFilter;
import com.onyxdb.platform.mdb.clusters.models.ClusterType;
import com.onyxdb.platform.mdb.clusters.models.ClusterVersion;
import com.onyxdb.platform.mdb.clusters.models.CreateCluster;
import com.onyxdb.platform.mdb.clusters.models.CreateClusterResult;
import com.onyxdb.platform.mdb.clusters.models.UpdateCluster;

@RestController
public class MongoClusterController implements ManagedMongoDbClustersApi {
    private final ClusterMapper clusterMapper;
    private final ClusterService clusterService;
    private final List<String> storageClasses;

    public MongoClusterController(
            ClusterMapper clusterMapper,
            ClusterService clusterService,
            @Value("${onyxdb.mdb.storage-classes:}")
            String storageClassesString
    ) {
        this.clusterMapper = clusterMapper;
        this.clusterService = clusterService;
        this.storageClasses = Arrays.stream(storageClassesString.split(","))
                .filter(s -> !s.isEmpty()).
                collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<ListMongoClustersResponseDTO> listClusters(
            @Nullable
            List<UUID> projectIds,
            @Nullable
            Boolean isDeleted
    ) {
        var filter = ClusterFilter.builder()
                .withProjectIds(projectIds)
                .withIsDeleted(isDeleted)
                .withType(ClusterType.MONGODB)
                .build();
        List<Cluster> clusters = clusterService.listClusters(filter);

        var response = new ListMongoClustersResponseDTO(
                clusters.stream().map(clusterMapper::clusterToMongoClusterDTO).toList()
        );
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<MongoClusterDTO> getCluster(UUID clusterId) {
        Cluster cluster = clusterService.getClusterOrThrow(clusterId);
        return ResponseEntity.ok(clusterMapper.clusterToMongoClusterDTO(cluster));
    }

    @Override
    public ResponseEntity<CreateMongoClusterResponseDTO> createCluster(CreateMongoClusterRequestDTO rq) {
        Account account = SecurityContextUtils.getCurrentAccount();

        CreateCluster createCluster = clusterMapper.createMongoClusterRequestDTOtoCreateCluster(rq, account.id());
        CreateClusterResult result = clusterService.createCluster(createCluster);

        return ResponseEntity.ok(new CreateMongoClusterResponseDTO(
                result.clusterId(),
                result.operationId()
        ));
    }

    @Override
    public ResponseEntity<ScheduledOperationDTO> updateCluster(UUID clusterId, UpdateMongoClusterRequestDTO rq) {
        Account account = SecurityContextUtils.getCurrentAccount();
        UpdateCluster updateCluster = clusterMapper.updateMongoClusterRequestDTOtoUpdateCluster(
                clusterId,
                rq,
                account.id()
        );

        UUID operationId = clusterService.updateCluster(updateCluster);

        return ResponseEntity.ok(new ScheduledOperationDTO(operationId));
    }

    @Override
    public ResponseEntity<ScheduledOperationDTO> deleteCluster(UUID clusterId) {
        Account account = SecurityContextUtils.getCurrentAccount();
        UUID operationId = clusterService.deleteCluster(clusterId, account.id());

        return ResponseEntity.ok(new ScheduledOperationDTO(operationId));
    }

    @Override
    public ResponseEntity<ListMongoVersionsResponseDTO> listVersions() {
        var response = new ListMongoVersionsResponseDTO(List.of(
                ClusterVersion.MONGODB_7_0.getVersion(),
                ClusterVersion.MONGODB_8_0.getVersion()
        ));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ListStorageClassesResponseDTO> listStorageClasses() {
        return ResponseEntity.ok(new ListStorageClassesResponseDTO(storageClasses));
    }
}

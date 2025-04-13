package com.onyxdb.mdb.controllers.v1;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.core.clusters.ClusterMapper;
import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.models.CreateCluster;
import com.onyxdb.mdb.core.clusters.models.UpdateCluster;
import com.onyxdb.mdb.generated.openapi.apis.V1ManagedMongoDbApi;
import com.onyxdb.mdb.generated.openapi.models.V1ClusterResources;
import com.onyxdb.mdb.generated.openapi.models.V1ClusterStatusResponse;
import com.onyxdb.mdb.generated.openapi.models.V1CreateMongoClusterRequest;
import com.onyxdb.mdb.generated.openapi.models.V1CreateMongoClusterResponse;
import com.onyxdb.mdb.generated.openapi.models.V1DeleteMongoClusterResponse;
import com.onyxdb.mdb.generated.openapi.models.V1ListMongoClustersResponse;
import com.onyxdb.mdb.generated.openapi.models.V1MongoClusterResponse;
import com.onyxdb.mdb.generated.openapi.models.V1MongoConfig;
import com.onyxdb.mdb.generated.openapi.models.V1MongoHost;
import com.onyxdb.mdb.generated.openapi.models.V1MongoListHostsResponse;
import com.onyxdb.mdb.generated.openapi.models.V1MongoScaleHostsRequest;
import com.onyxdb.mdb.generated.openapi.models.V1MongoUpdateClusterRequest;
import com.onyxdb.mdb.generated.openapi.models.V1ScheduledOperationResponse;

/**
 * @author foxleren
 */
@RestController
public class V1ManagedMongoController implements V1ManagedMongoDbApi {
    private final ClusterMapper clusterMapper;
    private final ClusterService clusterService;

    private static final V1MongoClusterResponse clusterResponse = new V1MongoClusterResponse(
            UUID.randomUUID(),
            "demo-cluster",
            "Some desc",
            new V1ClusterStatusResponse(
                    "alive",
                    "Все хосты работают нормально, все запущенные операции были успешно выполнены."
            ),
            UUID.randomUUID(),
            new V1MongoConfig(
                    new V1ClusterResources(
                            UUID.randomUUID(),
                            "standard",
                            10737418240L
                    ),
                    3
            )
    );

    public V1ManagedMongoController(
            ClusterMapper clusterMapper,
            ClusterService clusterService
    ) {
        this.clusterMapper = clusterMapper;
        this.clusterService = clusterService;
    }

    @Override
    public ResponseEntity<V1ListMongoClustersResponse> listClusters() {
        return ResponseEntity.ok()
                .body(new V1ListMongoClustersResponse(
                        List.of(clusterResponse)
                ));
    }

    @Override
    public ResponseEntity<V1MongoClusterResponse> getCluster(UUID clusterId) {
        return ResponseEntity.ok()
                .body(clusterResponse);
    }

    @Override
    public ResponseEntity<V1CreateMongoClusterResponse> createCluster(V1CreateMongoClusterRequest r) {
        CreateCluster createCluster = clusterMapper.v1CreateMongoClusterRequestToCreateCluster(r);

        UUID clusterId = clusterService.createCluster(createCluster);

        var response = new V1CreateMongoClusterResponse(clusterId);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<V1DeleteMongoClusterResponse> deleteCluster(UUID clusterId) {
        UUID operationId = clusterService.deleteCluster(clusterId);

        var response = new V1DeleteMongoClusterResponse(operationId);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<V1MongoListHostsResponse> listHosts(UUID clusterId) {
        return ResponseEntity.ok()
                .body(new V1MongoListHostsResponse(
                        List.of(
                                new V1MongoHost(
                                        "host-0"
                                ),
                                new V1MongoHost(
                                        "host-1"
                                ),
                                new V1MongoHost(
                                        "host-2"
                                )
                        )
                ));
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> scaleHosts(UUID clusterId, V1MongoScaleHostsRequest rq) {
        UUID operationId = clusterService.scaleHosts(clusterId, rq.getReplicas());

        return ResponseEntity.ok()
                .body(new V1ScheduledOperationResponse(operationId));
    }

    @Override
    public ResponseEntity<V1ScheduledOperationResponse> updateCluster(
            UUID clusterId,
            V1MongoUpdateClusterRequest rq
    ) {
        UpdateCluster updateCluster = clusterMapper.v1MongoUpdateClusterRequestToUpdateCluster(
                clusterId,
                rq
        );
        UUID operationId = clusterService.updateCluster(updateCluster);

        return ResponseEntity.ok()
                .body(new V1ScheduledOperationResponse(operationId));
    }
}

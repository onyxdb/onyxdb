package com.onyxdb.mdb.controllers.v1;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.generated.openapi.apis.V1ManagedMongoDbApi;
import com.onyxdb.mdb.generated.openapi.models.V1ClusterResourcesResponse;
import com.onyxdb.mdb.generated.openapi.models.V1ClusterStatusResponse;
import com.onyxdb.mdb.generated.openapi.models.V1CreateMongoClusterRequest;
import com.onyxdb.mdb.generated.openapi.models.V1CreateMongoClusterResponse;
import com.onyxdb.mdb.generated.openapi.models.V1ListMongoClustersResponse;
import com.onyxdb.mdb.generated.openapi.models.V1MongoClusterResponse;
import com.onyxdb.mdb.generated.openapi.models.V1MongoConfig;
import com.onyxdb.mdb.generated.openapi.models.V1MongoConfigV8d0;
import com.onyxdb.mdb.generated.openapi.models.V1MongodV8d0;

/**
 * @author foxleren
 */
@RestController
public class V1ManagedMongoDbController implements V1ManagedMongoDbApi {
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
                    "8.0"
            ).mongodbV80(
                    new V1MongoConfigV8d0(
                            new V1MongodV8d0(
                                    new V1ClusterResourcesResponse(
                                            "c2-r4",
                                            "standard",
                                            10737418240L
                                    )
                            )
                    )
            )
    );

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
        return ResponseEntity.ok()
                .body(new V1CreateMongoClusterResponse(
                        UUID.randomUUID()
                ));
    }
}

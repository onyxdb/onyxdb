package com.onyxdb.mdb.controllers.v1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.core.clusters.HostService;
import com.onyxdb.mdb.core.clusters.mappers.HostMapper;
import com.onyxdb.mdb.core.clusters.models.MongoHost;
import com.onyxdb.mdb.generated.openapi.apis.ManagedMongoDbInternalsApi;
import com.onyxdb.mdb.generated.openapi.models.UpdateMongoHostsRequest;

@RestController
public class ManagedMongoInternalsController implements ManagedMongoDbInternalsApi {
    private final HostService hostService;
    private final HostMapper hostMapper;

    public ManagedMongoInternalsController(HostService hostService, HostMapper hostMapper) {
        this.hostService = hostService;
        this.hostMapper = hostMapper;
    }

    @Override
    public ResponseEntity<Void> updateHosts(UpdateMongoHostsRequest rq) {
        List<MongoHost> mongoHosts = hostMapper.map(rq);
        hostService.updateMongoHosts(mongoHosts);

        return ResponseEntity.ok().build();
    }
}

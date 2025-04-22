package com.onyxdb.platform.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.core.clusters.HostService;
import com.onyxdb.platform.core.clusters.mappers.HostMapper;
import com.onyxdb.platform.core.clusters.models.MongoHost;
import com.onyxdb.platform.generated.openapi.apis.ManagedMongoDbInternalsApi;
import com.onyxdb.platform.generated.openapi.models.UpdateMongoHostsRequest;

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

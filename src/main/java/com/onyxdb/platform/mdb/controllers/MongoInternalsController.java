package com.onyxdb.platform.mdb.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ManagedMongoDbInternalsApi;
import com.onyxdb.platform.generated.openapi.models.UpdateMongoHostsRequestDTO;
import com.onyxdb.platform.mdb.clusters.models.MongoHost;
import com.onyxdb.platform.mdb.hosts.HostMapper;
import com.onyxdb.platform.mdb.hosts.HostService;

@RestController
public class MongoInternalsController implements ManagedMongoDbInternalsApi {
    private final HostService hostService;
    private final HostMapper hostMapper;

    public MongoInternalsController(HostService hostService, HostMapper hostMapper) {
        this.hostService = hostService;
        this.hostMapper = hostMapper;
    }

    @Override
    public ResponseEntity<Void> updateHosts(UpdateMongoHostsRequestDTO rq) {
        List<MongoHost> mongoHosts = hostMapper.map(rq);
        hostService.updateMongoHosts(mongoHosts);

        return ResponseEntity.ok().build();
    }
}

package com.onyxdb.platform.mdb.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ManagedMongoDbHostsApi;
import com.onyxdb.platform.generated.openapi.models.ListMongoHostsResponseDTO;
import com.onyxdb.platform.mdb.clusters.models.MongoHost;
import com.onyxdb.platform.mdb.hosts.HostMapper;
import com.onyxdb.platform.mdb.hosts.HostService;

@RestController
public class MongoHostsController implements ManagedMongoDbHostsApi {
    private final HostService hostService;
    private final HostMapper hostMapper;

    public MongoHostsController(HostService hostService, HostMapper hostMapper) {
        this.hostService = hostService;
        this.hostMapper = hostMapper;
    }

    @Override
    public ResponseEntity<ListMongoHostsResponseDTO> listHosts(UUID clusterId) {
        List<MongoHost> hosts = hostService.listMongoHosts(clusterId);

        var response = new ListMongoHostsResponseDTO(
                hosts.stream().map(hostMapper::map).toList()
        );
        return ResponseEntity.ok(response);
    }
}

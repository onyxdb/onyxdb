package com.onyxdb.platform.idm.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class V1PingController {

    @GetMapping(path = "/ping")
    public ResponseEntity<String> v1ClustersCreateCluster() {
        return ResponseEntity.ok("OK");
    }
}

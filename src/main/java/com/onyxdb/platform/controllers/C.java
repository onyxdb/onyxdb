package com.onyxdb.platform.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.clients.k8s.psmdb.PsmdbClient;

@RestController
public class C {
    private final PsmdbClient psmdbClient;

    public C(PsmdbClient psmdbClient) {
        this.psmdbClient = psmdbClient;
    }

//    @GetMapping("/test")
//    public void hello() {
//        psmdbClient.createVectorConfig(
//                "onyxdb",
//                "some-project",
//                "some-cluster"
//        );
//    }
}

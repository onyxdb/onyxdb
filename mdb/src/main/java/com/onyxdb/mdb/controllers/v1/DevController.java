package com.onyxdb.mdb.controllers.v1;

import java.util.Random;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.services.ClusterOperationService;

/**
 * @author foxleren
 */
@RestController
@RequestMapping("/dev")
@RequiredArgsConstructor
public class DevController {
    private final ClusterOperationService clusterOperationService;

    @GetMapping("/")
    public double post() {
        return new Random().nextGaussian();
//        var o = new ClusterOperation.create()
//        clusterOperationService.create();
    }
}

package com.onyxdb.mdb.services;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.onyxdb.mdb.models.ClusterOperation;
import com.onyxdb.mdb.repositories.ClusterOperationRepository;

/**
 * @author foxleren
 */
@Service
@RequiredArgsConstructor
public class ClusterOperationService {
    private final ClusterOperationRepository clusterOperationRepository;

//    public void
//    public void create(ClusterOperation clusterOperation) {
//        clusterOperationRepository.createBulk(List.of(clusterOperation));
//    }
}

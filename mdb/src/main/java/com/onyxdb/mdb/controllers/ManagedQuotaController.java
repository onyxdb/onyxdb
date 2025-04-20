package com.onyxdb.mdb.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.generated.openapi.apis.ManagedQuotasApi;
import com.onyxdb.mdb.generated.openapi.models.Quota;
import com.onyxdb.mdb.generated.openapi.models.ResourceType;
import com.onyxdb.mdb.generated.openapi.models.SimulateMongoDBQuotaUsageRequest;
import com.onyxdb.mdb.generated.openapi.models.SimulateMongoDBQuotaUsageResponse;

@RestController
public class ManagedQuotaController implements ManagedQuotasApi {
    private static final Quota CPU_QUOTA = new Quota(
            UUID.randomUUID(),
            new ResourceType(
                    UUID.randomUUID(),
                    "vcpu",
                    "Количество vCPU"
            ),
            1L,
            0L,
            1L
    );
    private static final Quota RAM_QUOTA = new Quota(
            UUID.randomUUID(),
            new ResourceType(
                    UUID.randomUUID(),
                    "ram",
                    "Объем оперативной памяти"
            ),
            1L,
            0L,
            1L
    );

//    @Override
//    public ResponseEntity<ListQuotasResponse> listQuotas() {
//        return ResponseEntity.ok(new ListQuotasResponse(
//                List.of(CPU_QUOTA, RAM_QUOTA)
//        ));
//    }

    @Override
    public ResponseEntity<SimulateMongoDBQuotaUsageResponse> simulateMongoDbUsage(SimulateMongoDBQuotaUsageRequest rq) {
        return ResponseEntity.ok(new SimulateMongoDBQuotaUsageResponse(
                List.of(CPU_QUOTA, RAM_QUOTA)
        ));
    }

//    @Override
//    public ResponseEntity<Quota> getQuota(UUID quotaId) {
//        return ResponseEntity.ok(new Quota(
//                UUID.randomUUID(),
//                new ResourceType(
//                        UUID.randomUUID(),
//                        "ram",
//                        "Объем оперативной памяти"
//                ),
//                1L,
//                0L
//        ));
//    }
}

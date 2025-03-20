package com.onyxdb.mdb.controllers.v1;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.controllers.v1.models.ClusterRes;
import com.onyxdb.mdb.controllers.v1.models.CreatedClusterRes;
import com.onyxdb.mdb.controllers.v1.models.CreatedProjectRes;
import com.onyxdb.mdb.controllers.v1.models.OkRes;
import com.onyxdb.mdb.controllers.v1.models.OperationRes;
import com.onyxdb.mdb.controllers.v1.models.ProjectRes;
import com.onyxdb.mdb.controllers.v1.models.QuotaRes;
import com.onyxdb.mdb.exceptions.BadRequestException;

/**
 * @author foxleren
 */
@RestController
@RequestMapping("/api/v1/mdb")
public class FakeController {
    private static final UUID PROJECT_ID = UUID.fromString("30dc4f3a-d73d-4de3-8d12-a3c10995319c");
    private static final UUID PRODUCT_ID = UUID.fromString("f099c2be-1253-43be-9bbc-411ce060d4fb");
    private static final UUID CREATED_BY = UUID.fromString("490fdf97-824f-4173-9e92-1f94cf1dc701");
    private static final UUID CLUSTER_ID = UUID.fromString("7c778e78-e357-47de-b699-124761f61387");
    private static final UUID OPERATION_ID = UUID.fromString("a770c030-e717-402a-82bc-84cd9204fc19");

    private static final List<QuotaRes> PROJECT_QUOTAS = new ArrayList<>();
    private static final List<ProjectRes> PROJECTS = new ArrayList<>();
    private static final List<ClusterRes> CLUSTERS = new ArrayList<>();
    private static final OkRes OK_RES = new OkRes("OK");
    private static LocalDateTime CLUSTER_CREATED_AT = null;
    private static boolean IS_QUOTA_TRANSFERED = false;

    public FakeController() {
        PROJECT_QUOTAS.addAll(
                List.of(
                        new QuotaRes(
                                "VCPU",
                                5,
                                "vcores"
                        ),
                        new QuotaRes(
                                "RAM",
                                10,
                                "GB"
                        )
                )
        );
    }

    @GetMapping("/projects")
    public List<ProjectRes> getProjects() {
        return PROJECTS;
    }

    @PostMapping("/projects")
    public CreatedProjectRes createProject() {
        ProjectRes projectRes = new ProjectRes(
                PROJECT_ID,
                PRODUCT_ID,
                "demo-project",
                CREATED_BY,
                LocalDateTime.now()
        );
        if (PROJECTS.isEmpty()) {
            PROJECTS.add(projectRes);
        }
        return new CreatedProjectRes(PROJECT_ID);
    }

    @PostMapping("/clusters")
    public CreatedClusterRes createCluster() throws IOException {
        if (PROJECT_QUOTAS.size() == 2) {
            throw new BadRequestException("Requested 3 GB of resource STANDARD_STORAGE, but quota is 0 GB");
        }
        CLUSTER_CREATED_AT = LocalDateTime.now();
        CLUSTERS.add(new ClusterRes(
                CLUSTER_ID,
                "demo-cluster",
                PROJECT_ID,
                "mongodb"
        ));

        Runtime.getRuntime()
                .exec(String.format(" kubectl apply -f /Users/foxleren/dev/onyxdb-org/onyxdb/mongodb-cr.yaml -n onyxdb-demo"));

        return new CreatedClusterRes(CLUSTER_ID);
    }

    @GetMapping("/clusters")
    public List<ClusterRes> getClusters() {
        return CLUSTERS;
    }

    @GetMapping("/quotas/products/{id}")
    public List<QuotaRes> getProductQuotas(@PathVariable String id) {
        return List.of(
                new QuotaRes(
                        "VCPU",
                        10,
                        "vcores"
                ),
                new QuotaRes(
                        "RAM",
                        10,
                        "GB"
                ),
                new QuotaRes(
                        "STANDARD_STORAGE",
                        IS_QUOTA_TRANSFERED ? 5 : 10,
                        "GB"
                )
        );
    }

    @GetMapping("/quotas/projects/{id}")
    public List<QuotaRes> getProjectQuotas(@PathVariable String id) {
        return PROJECT_QUOTAS;
    }

    @PostMapping("/quotas/transfer/between-product-and-project")
    public OkRes transferQuotas() {
        PROJECT_QUOTAS.add(
                new QuotaRes(
                        "STANDARD_STORAGE",
                        5,
                        "GB"
                )
        );
        IS_QUOTA_TRANSFERED = true;

        return OK_RES;
    }

    @GetMapping("/clusters/{id}/operations")
    public List<OperationRes> getClusterOperations(@PathVariable String id) {
        String status = "in_progress";
        if (Duration.between(CLUSTER_CREATED_AT, LocalDateTime.now()).getSeconds() >= 30) {
            status = "finished";
        }

        return List.of(
                new OperationRes(
                        OPERATION_ID,
                        "create_cluster",
                        status
                )
        );
    }
}

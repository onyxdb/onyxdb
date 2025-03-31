package com.onyxdb.mdb.controllers.v1;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.clients.grafana.GrafanaClient;
import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.mdb.clients.k8s.victoriametrics.VmOperatorClient;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@RestController
@RequestMapping("/foxleren")
public class FakeController {
    private final KubernetesClient kubernetesClient;
    private final ObjectMapper objectMapper;
    private final PsmdbClient psmdbClient;
    private final VmOperatorClient vmOperatorClient;
    private final GrafanaClient grafanaClient;

    public FakeController(
            KubernetesClient kubernetesClient,
            ObjectMapper objectMapper,
            PsmdbClient psmdbClient, VmOperatorClient vmOperatorClient,
            GrafanaClient grafanaClient
    ) {
        this.kubernetesClient = kubernetesClient;
        this.objectMapper = objectMapper;
        this.psmdbClient = psmdbClient;
        this.vmOperatorClient = vmOperatorClient;
        this.grafanaClient = grafanaClient;
    }

    @GetMapping("/")
    public void do1() throws IOException {
        var r = new ClassPathResource("templates/mongodb_dashboard.json");
        var rr = new String(r.getContentAsByteArray());
        rr = rr.replace("${DASHBOARD_TITLE}", "Managed MongoDB demo cluster Dashboard");
        var rrr = objectMapper.readValue(rr, HashMap.class);
        grafanaClient.createDashboard(rrr);
//        System.err.println(rr);
//        var rr = new String(r.getInputStream().readAllBytes());
//        System.err.println(rr);
//        InputStream in = this.getClass().getClassLoader().getResourceAsStream("yourFile.json");
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode jsonNode = objectMapper.createObjectNode();
//        jsonNode.put("name", "Abul Hasan");
//        jsonNode.put("age", 23);
//        jsonNode.put("city", "Lucknow");
//        jsonNode.put("state", "Uttar Pradesh");
//        jsonNode.put("country", "India");
//        objectMapper.writeValue(new File("mydata.json"), jsonNode);
//        JsonNode jsonNode = objectMapper.readTree(new File("templates/mongodb_dashboard.json"));
//        System.err.println(jsonNode.toString());
//        ResourceDefinitionContext context = new ResourceDefinitionContext.Builder()
//                .withGroup("psmdb.percona.com")
//                .withVersion("v1")
//                .withKind("PerconaServerMongoDB")
//                .withPlural("perconaservermongodbs")
//                .withNamespaced(true)
//                .build();
//        vmOperatorClient.createServiceScrape("managed-mongodb-exporter",
//                Map.of("instance", "me"),
//                List.of(228));
//        GenericKubernetesResource resource = new GenericKubernetesResourceBuilder()
//                .withApiVersion("psmdb.percona.com/v1")
//                .withKind("PerconaServerMongoDB")
//                .withNewMetadata()
//                .withName("demo-cluster")
//                .endMetadata()
////                .addToAdditionalProperties("spec", buildSpec())
//                .build();

//        var r = kubernetesClient.genericKubernetesResources("psmdb.percona.com/v1", "PerconaServerMongoDB")
//                .inNamespace("onyxdb")
//                .resource(resource)
//                .create();
//
//        var genericKubernetesResource = kubernetesClient.genericKubernetesResources(context)
//                .inNamespace("onyxdb")
//                .withName("demo-cluster")
//                .get();
//
//        System.err.println(genericKubernetesResource);
//        System.err.println(genericKubernetesResource.getMetadata().getAdditionalProperties());
//        System.err.println(genericKubernetesResource.getAdditionalProperties().get("status"));

//        var statusObj = genericKubernetesResource.getAdditionalProperties().get("status");
//        var r =
//                psmdbClient.getClusterSecret("demo-cluster");
//        System.err.println(r);
//        var rr = objectMapper.convertValue(statusObj, new TypeReference<Map<String, Object>>() {});

//        System.err.println(rr);
    }
}

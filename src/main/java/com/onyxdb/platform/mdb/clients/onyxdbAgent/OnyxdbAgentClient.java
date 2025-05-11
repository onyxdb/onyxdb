package com.onyxdb.platform.mdb.clients.onyxdbAgent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.onyxdb.platform.mdb.clients.k8s.KubernetesAdapter;
import com.onyxdb.platform.mdb.clients.onyxdbAgent.models.CreateMongoDatabaseRequestDTO;
import com.onyxdb.platform.mdb.clients.onyxdbAgent.models.CreateMongoUserRequestDTO;
import com.onyxdb.platform.mdb.clients.onyxdbAgent.models.DeleteMongoDatabaseRequestDTO;
import com.onyxdb.platform.mdb.clients.onyxdbAgent.models.DeleteMongoUserRequestDTO;

public class OnyxdbAgentClient {
    private static final Logger logger = LoggerFactory.getLogger(OnyxdbAgentClient.class);

    private final WebClient webClient;

    public OnyxdbAgentClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public void createDatabase(
            String namespace,
            String projectName,
            String clusterName,
            CreateMongoDatabaseRequestDTO rq
    ) {
        String baseUrl = buildBaseUrl(namespace, projectName, clusterName);
        webClient.post()
                .uri(UriComponentsBuilder.fromHttpUrl(baseUrl).path("/api/mongodb/databases").build().toUri())
                .bodyValue(rq)
                .retrieve()
                .toBodilessEntity()
                .doOnError(e -> logger.error("Error creating database: ", e))
                .block();
    }

    public void deleteDatabase(
            String namespace,
            String projectName,
            String clusterName,
            DeleteMongoDatabaseRequestDTO rq
    ) {
        String baseUrl = buildBaseUrl(namespace, projectName, clusterName);
        webClient.method(HttpMethod.DELETE)
                .uri(UriComponentsBuilder.fromHttpUrl(baseUrl).path("/api/mongodb/databases").build().toUri())
                .bodyValue(rq)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void createUser(
            String namespace,
            String projectName,
            String clusterName,
            CreateMongoUserRequestDTO rq
    ) {
        String baseUrl = buildBaseUrl(namespace, projectName, clusterName);
        webClient.post()
                .uri(UriComponentsBuilder.fromHttpUrl(baseUrl).path("/api/mongodb/users").build().toUri())
                .bodyValue(rq)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void deleteUser(
            String namespace,
            String projectName,
            String clusterName,
            DeleteMongoUserRequestDTO rq
    ) {
        String baseUrl = buildBaseUrl(namespace, projectName, clusterName);
        webClient.method(HttpMethod.DELETE)
                .uri(UriComponentsBuilder.fromHttpUrl(baseUrl).path("/api/mongodb/users").build().toUri())
                .bodyValue(rq)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private String buildBaseUrl(
            String namespace,
            String projectName,
            String clusterName
    ) {
        String agentName = KubernetesAdapter.getOnyxdbAgentName(clusterName, projectName);
        return "http://%s.%s.svc.cluster.local:9002".formatted(agentName, namespace);
    }
}

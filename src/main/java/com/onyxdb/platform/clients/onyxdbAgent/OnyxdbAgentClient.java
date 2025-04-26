package com.onyxdb.platform.clients.onyxdbAgent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import com.onyxdb.platform.clients.onyxdbAgent.models.CreateMongoDatabaseRequestDTO;
import com.onyxdb.platform.clients.onyxdbAgent.models.CreateMongoUserRequestDTO;
import com.onyxdb.platform.clients.onyxdbAgent.models.DeleteMongoDatabaseRequestDTO;
import com.onyxdb.platform.clients.onyxdbAgent.models.DeleteMongoUserRequestDTO;

public class OnyxdbAgentClient {
    private static final Logger logger = LoggerFactory.getLogger(OnyxdbAgentClient.class);

    private final WebClient webClient;

    public OnyxdbAgentClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public void createDatabase(CreateMongoDatabaseRequestDTO rq) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/mongodb/databases").build())
                .bodyValue(rq)
                .retrieve()
                .toBodilessEntity()
                .doOnError(e -> logger.error("Error creating database: ", e))
                .block();
    }

    public void deleteDatabase(DeleteMongoDatabaseRequestDTO rq) {
        webClient.method(HttpMethod.DELETE)
                .uri(uriBuilder -> uriBuilder.path("/api/mongodb/databases").build())
                .bodyValue(rq)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void createUser(CreateMongoUserRequestDTO rq) {
        webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/mongodb/users").build())
                .bodyValue(rq)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void deleteUser(DeleteMongoUserRequestDTO rq) {
        webClient.method(HttpMethod.DELETE)
                .uri(uriBuilder -> uriBuilder.path("/api/mongodb/users").build())
                .bodyValue(rq)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}

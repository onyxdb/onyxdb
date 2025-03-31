package com.onyxdb.mdb.clients.grafana;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class GrafanaClient {
    private final WebClient webClient;

    public GrafanaClient() {
        // TODO add timeouts
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:55708")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer glsa_1xfJvI1rbI4HgveSr3ckifQiLuj34vWA_0c368145")
                .build();
    }

    public void createDashboard(HashMap rr) {
        String r = webClient.post()
                .uri("/api/dashboards/import")
                .bodyValue(Map.ofEntries(
                        Map.entry("dashboard", rr),
                        Map.entry("overwrite", true),
                        Map.entry("folderUid", ""),
                        Map.entry("inputs", List.of(Map.ofEntries(
                                Map.entry("name", "DS_PROMETHEUS"),
                                Map.entry("type", "datasource"),
                                Map.entry("pluginId", "prometheus"),
                                Map.entry("value", "PABDA7AB1AD2A1489")
                        )))
                ))
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> System.err.println("Error: " + e.getMessage()))
                .block();
        System.err.println(r);

//        String r = webClient.get()
//                .uri("/api/dashboards/home")
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
        System.err.println(r);
    }
}

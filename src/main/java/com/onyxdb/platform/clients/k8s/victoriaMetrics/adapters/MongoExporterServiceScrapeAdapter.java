package com.onyxdb.platform.clients.k8s.victoriaMetrics.adapters;

import java.util.List;

import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;

import com.onyxdb.platform.clients.k8s.psmdb.PsmdbExporterServiceClient;
import com.onyxdb.platform.clients.k8s.victoriaMetrics.VmEndpoint;
import com.onyxdb.platform.clients.k8s.victoriaMetrics.VmRelabelConfig;
import com.onyxdb.platform.clients.k8s.victoriaMetrics.VmServiceScrape;
import com.onyxdb.platform.clients.k8s.victoriaMetrics.VmServiceScrapeSpec;

public class MongoExporterServiceScrapeAdapter {
    public VmServiceScrape buildVmServiceScrape(
            String namespace,
            String project,
            String cluster
    ) {
        var vmServiceScrapeSpec = VmServiceScrapeSpec.builder()
                .withSelector(
                        new LabelSelectorBuilder()
                                .withMatchLabels(PsmdbExporterServiceClient.getLabels(cluster))
                                .build()
                )
                .withEndpoints(List.of(
                        new VmEndpoint(
                                PsmdbExporterServiceClient.PORT_NAME,
                                PsmdbExporterServiceClient.PATH,
                                List.of(
                                        new VmRelabelConfig(
                                                "project",
                                                project
                                        ),
                                        new VmRelabelConfig(
                                                "cluster",
                                                cluster
                                        )
                                )
                        )
                ))
                .build();

        return new VmServiceScrape(
                namespace,
                PsmdbExporterServiceClient.getPreparedName(cluster),
                vmServiceScrapeSpec
        );
    }
}

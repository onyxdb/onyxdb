package com.onyxdb.platform.clients.k8s.victoriaMetrics.adapters;

public class MongoExporterServiceScrapeAdapter {
//    public VmServiceScrape buildVmServiceScrape(
//            String namespace,
//            String project,
//            String cluster
//    ) {
//        var vmServiceScrapeSpec = VmServiceScrapeSpec.builder()
//                .withSelector(
//                        new LabelSelectorBuilder()
//                                .withMatchLabels(PsmdbExporterServiceClient.getLabels(cluster))
//                                .build()
//                )
//                .withEndpoints(List.of(
//                        new VmEndpoint(
//                                PsmdbExporterServiceClient.PORT_NAME,
//                                PsmdbExporterServiceClient.PATH,
//                                List.of(
//                                        new VmRelabelConfig(
//                                                "project",
//                                                project
//                                        ),
//                                        new VmRelabelConfig(
//                                                "cluster",
//                                                cluster
//                                        )
//                                )
//                        )
//                ))
//                .build();
//
//        return new VmServiceScrape(
//                namespace,
//                PsmdbExporterServiceClient.getPreparedName(cluster),
//                vmServiceScrapeSpec
//        );
//    }
}

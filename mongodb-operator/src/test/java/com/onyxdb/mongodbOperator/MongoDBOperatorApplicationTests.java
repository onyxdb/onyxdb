package com.onyxdb.mongodbOperator;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.springboot.starter.test.EnableMockOperator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@EnableMockOperator(crdPaths = "classpath:META-INF/fabric8/managed-mongodb-crd.yaml")
class MongoDBOperatorApplicationTests {
    @Autowired
    KubernetesClient client;

    @Test
    void contextLoads() {
    }

//    @Test
//    void whenContextLoaded_thenCrdRegistered() {
//        var r = client
//                .apiextensions()
//                .v1()
//                .customResourceDefinitions()
//                .withName("managed-mongodb.onyxdb.com")
//                .get();
//        assertThat(r).isNotNull();
//    }
}

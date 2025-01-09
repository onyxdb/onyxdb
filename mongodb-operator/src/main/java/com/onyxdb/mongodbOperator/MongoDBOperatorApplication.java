package com.onyxdb.mongodbOperator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.onyxdb.mongodbOperator.utils.K8sUtil;

@SpringBootApplication
public class MongoDBOperatorApplication {
    public static void main(String[] args) {
//        System.err.println("KEY=" + K8sUtil.generateBase64Key(768));
        SpringApplication.run(MongoDBOperatorApplication.class, args);
    }
}

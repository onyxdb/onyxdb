package com.onyxdb.mdb.context;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.processors.CompositeClusterOperationProcessor;
import com.onyxdb.mdb.processors.MongoClusterOperationProcessor;

/**
 * @author foxleren
 */
@Configuration
public class MdbContextConfiguration {
    @Bean
    public CompositeClusterOperationProcessor compositeClusterOperationProcessor(
            MongoClusterOperationProcessor mongoClusterOperationProcessor)
    {
        return new CompositeClusterOperationProcessor(
                List.of(mongoClusterOperationProcessor)
        );
    }
}

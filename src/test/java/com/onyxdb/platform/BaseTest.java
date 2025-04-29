package com.onyxdb.platform;

import org.jooq.DSLContext;
import org.jooq.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.onyxdb.platform.context.DatasourceContextConfiguration;
import com.onyxdb.platform.generated.jooq.Public;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BaseTest {
    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    @Qualifier(DatasourceContextConfiguration.PSQL_JOOQ_DSL_CONTEXT)
    private DSLContext psqlDslContext;

    protected void truncateTables() {
        for (Table<?> table : Public.PUBLIC.getTables()) {
            psqlDslContext.truncate(table).cascade().execute();
        }
    }
}

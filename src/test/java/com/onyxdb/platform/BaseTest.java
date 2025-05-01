package com.onyxdb.platform;

import org.jooq.DSLContext;
import org.jooq.Table;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.onyxdb.platform.generated.jooq.Public;
import com.onyxdb.platform.generated.jooq.Tables;
import com.onyxdb.platform.generated.openapi.models.AuthRequestDTO;
import com.onyxdb.platform.generated.openapi.models.JwtResponseDTO;
import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.context.DatasourceContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BaseTest {
    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    @Qualifier(DatasourceContextConfiguration.PSQL_JOOQ_DSL_CONTEXT)
    private DSLContext psqlDslContext;

    protected PsmdbClient psmdbClient = Mockito.mock(PsmdbClient.class);

    protected HttpHeaders getHeaders() {
        ResponseEntity<JwtResponseDTO> response = restTemplate.postForEntity(
                "/api/v1/auth/login",
                new AuthRequestDTO(
                        "admin",
                        "admin"
                ),
                JwtResponseDTO.class
        );

        if (response.getBody() == null) {
            throw new RuntimeException("Failed to get body after login");
        }

        var headers = new HttpHeaders();
        headers.setBearerAuth(response.getBody().getAccessToken());

        return headers;
    }

    protected void truncateTables() {
        for (Table<?> table : Public.PUBLIC.getTables()) {
            if (table.equals(Tables.ACCOUNT_TABLE)) {
                continue;
            }
            psqlDslContext.truncate(table).cascade().execute();
        }
    }
}

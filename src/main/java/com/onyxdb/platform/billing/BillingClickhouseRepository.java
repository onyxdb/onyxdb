package com.onyxdb.platform.billing;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;

import com.onyxdb.platform.quotas.EnrichedProductQuota;
import com.onyxdb.platform.utils.TimeUtils;

public class BillingClickhouseRepository implements BillingRepository {
    private static final String GET_USAGE_REPORT_QUERY = """
            SELECT
                toDate(created_at) AS day,
                resource_id,
                avg(limit) AS avg_limit,
                avg(usage) AS avg_usage,
                avg(free) AS avg_free
            FROM onyxdb.billing_quotas_usage
            WHERE created_at BETWEEN toDate(?) AND toDate(?)
            AND product_id = ?
            GROUP BY day, resource_id
            ORDER BY day, resource_id;
            """;
    private static final String INSERT_BILLING_PRODUCT_QUOTAS = """
            INSERT INTO onyxdb.billing_quotas_usage (product_id, resource_id, limit, usage, free, created_at) VALUES
            (?, ?, ?, ?, ?, ?);
            """;

    private final JdbcTemplate jdbcTemplate;

    public BillingClickhouseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ProductQuotaUsageReportItem> getUsageReportByProduct(
            UUID productId,
            LocalDate starDate,
            LocalDate endDate
    ) {
        return jdbcTemplate.query(
                GET_USAGE_REPORT_QUERY,
                (ResultSet rs, int rowNum) -> new ProductQuotaUsageReportItem(
                        productId,
                        UUID.fromString(rs.getString("resource_id")),
                        rs.getLong("avg_limit"),
                        rs.getLong("avg_usage"),
                        rs.getLong("avg_free"),
                        rs.getDate("day").toLocalDate()
                ),
                starDate,
                endDate,
                productId
        );
    }

    @Override
    public void saveProductQuotaUsageRecords(List<EnrichedProductQuota> quotas) {
        LocalDateTime now = TimeUtils.now();
        quotas.forEach(q -> {
            jdbcTemplate.update(
                    INSERT_BILLING_PRODUCT_QUOTAS,
                    q.productId(),
                    q.resource().id(),
                    q.limit(),
                    q.usage(),
                    q.free(),
                    now
            );
        });
    }
}

CREATE TABLE onyxdb.billing_quotas_usage
(
    product_id     UUID,
    quota_provider String,
    limit          Int64,
    usage          Int64,
    free           Int64,
    ts             DateTime
)
    ENGINE = MergeTree
        ORDER BY (product_id, quota_provider, ts);

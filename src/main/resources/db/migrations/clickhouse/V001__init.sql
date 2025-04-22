CREATE TABLE onyxdb.billing_quotas_usage
(
    product_id  UUID,
    resource_id UUID,
    limit       Int64,
    usage       Int64,
    free        Int64,
    created_at  DateTime
)
    ENGINE = MergeTree
        ORDER BY (product_id, resource_id, created_at);

CREATE TABLE account_roles_history
(
    record_id  UUID,
    account_id UUID,
    role_id    UUID,
    status     String,
    created_at DateTime
) ENGINE = MergeTree()
      PRIMARY KEY record_id
      ORDER BY (record_id, created_at);

CREATE TABLE roles_changing_history
(
    record_id  UUID,
    role_id    UUID,
    difference String,
    created_at DateTime
) ENGINE = MergeTree()
      PRIMARY KEY record_id
      ORDER BY (record_id, created_at);

CREATE TABLE business_role_changing_history
(
    record_id        UUID,
    business_role_id UUID,
    difference       String,
    created_at       DateTime
) ENGINE = MergeTree()
      PRIMARY KEY record_id
      ORDER BY (record_id, created_at);

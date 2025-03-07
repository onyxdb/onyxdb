-- Создание таблицы для Business Role
CREATE TABLE business_role_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    shop_name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    parent_id UUID,
    data JSONB NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES business_role_table(id)
);

CREATE INDEX IF NOT EXISTS business_role_table_index ON business_role_table(name);
CREATE INDEX IF NOT EXISTS business_role_table_parent_id_index ON business_role_table(parent_id);

-- Создание таблицы для связи Business Role и Role
CREATE TABLE business_role_role_table (
    business_role_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (business_role_id, role_id),
    FOREIGN KEY (business_role_id) REFERENCES business_role_table(id),
    FOREIGN KEY (role_id) REFERENCES role_table(id)
);

CREATE INDEX IF NOT EXISTS business_role_role_table_business_role_id_index
    ON business_role_role_table(business_role_id);
CREATE INDEX IF NOT EXISTS business_role_role_table_role_id_index
    ON business_role_role_table(role_id);

-- Создание таблицы для связи Account и Business Role
CREATE TABLE account_business_role_table (
    account_id UUID NOT NULL,
    business_role_id UUID NOT NULL,
    PRIMARY KEY (account_id, business_role_id),
    FOREIGN KEY (account_id) REFERENCES account_table(id),
    FOREIGN KEY (business_role_id) REFERENCES business_role_table(id)
);

CREATE INDEX IF NOT EXISTS account_business_role_table_account_id_index
    ON account_business_role_table(account_id);
CREATE INDEX IF NOT EXISTS account_business_role_table_business_role_id_index
    ON account_business_role_table(business_role_id);

-- Создание таблицы для связи Account и Role
CREATE TABLE account_role_table (
    account_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (account_id, role_id),
    FOREIGN KEY (account_id) REFERENCES account_table(id),
    FOREIGN KEY (role_id) REFERENCES role_table(id)
);

CREATE INDEX IF NOT EXISTS account_role_table_account_id_index ON account_role_table(account_id);
CREATE INDEX IF NOT EXISTS account_role_table_role_id_index ON account_role_table(role_id);

-- Создание таблицы для связи Account и Organization Unit
CREATE TABLE account_ou_table (
    account_id UUID NOT NULL,
    ou_id UUID NOT NULL,
    PRIMARY KEY (account_id, ou_id),
    FOREIGN KEY (account_id) REFERENCES account_table(id),
    FOREIGN KEY (ou_id) REFERENCES organization_unit_table(id)
);

CREATE INDEX IF NOT EXISTS account_ou_table_account_id_index ON account_ou_table(account_id);
CREATE INDEX IF NOT EXISTS account_ou_table_ou_id_index ON account_ou_table(ou_id);

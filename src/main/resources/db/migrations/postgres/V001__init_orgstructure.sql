-- Создание таблицы для Account
CREATE TABLE account_table (
    id UUID PRIMARY KEY,
    login VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Хранится зашифрованным
    email VARCHAR(255) UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    data JSONB NOT NULL,
    is_deleted bool NOT NULL DEFAULT false,
    deleted_at timestamp,
    deleted_by uuid,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS account_table_index ON account_table(login, email, last_name, first_name);

-- Создание таблицы для Domain Component (DC)
CREATE TABLE domain_component_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS domain_component_table_index ON domain_component_table(name);

-- Создание таблицы для Organization Unit (OU)
CREATE TABLE organization_unit_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    domain_component_id UUID NOT NULL,
    parent_id UUID,
    owner_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (domain_component_id) REFERENCES domain_component_table(id),
    FOREIGN KEY (parent_id) REFERENCES organization_unit_table(id),
    FOREIGN KEY (owner_id) REFERENCES account_table(id)
);

CREATE INDEX IF NOT EXISTS organization_unit_table_index ON organization_unit_table(name, domain_component_id);
CREATE INDEX IF NOT EXISTS organization_unit_table_parent_id_index ON organization_unit_table(parent_id);

-- Создание таблицы для Business Role
CREATE TABLE business_role_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    parent_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES business_role_table(id)
);

-- Создание таблицы для связи Business Role и Role
CREATE TABLE business_role_role_table (
    business_role_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (business_role_id, role_id),
    FOREIGN KEY (business_role_id) REFERENCES business_role_table(id),
    FOREIGN KEY (role_id) REFERENCES role_table(id)
);

-- Создание таблицы для связи Account и Business Role
CREATE TABLE account_business_role_table (
    account_id UUID NOT NULL,
    business_role_id UUID NOT NULL,
    PRIMARY KEY (account_id, business_role_id),
    FOREIGN KEY (account_id) REFERENCES account_table(id),
    FOREIGN KEY (business_role_id) REFERENCES business_role_table(id)
);

-- Создание таблицы для связи Account и Role
CREATE TABLE account_role_table (
    account_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (account_id, role_id),
    FOREIGN KEY (account_id) REFERENCES account_table(id),
    FOREIGN KEY (role_id) REFERENCES role_table(id)
);

-- Создание таблицы для связи Account и Organization Unit
CREATE TABLE account_ou_table (
    account_id UUID NOT NULL,
    ou_id UUID NOT NULL,
    PRIMARY KEY (account_id, ou_id),
    FOREIGN KEY (account_id) REFERENCES account_table(id),
    FOREIGN KEY (ou_id) REFERENCES organization_unit_table(id)
);

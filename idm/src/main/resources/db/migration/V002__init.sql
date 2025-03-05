
-- Создание таблицы для Product
CREATE TABLE product_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    parent_id UUID,
    owner_id UUID,
    data JSONB NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES product_table(id),
    FOREIGN KEY (owner_id) REFERENCES account_table(id)
);

CREATE INDEX IF NOT EXISTS product_table_parent_id_index ON product_table(parent_id);
CREATE INDEX IF NOT EXISTS product_table_owner_id_index ON product_table(owner_id);

-- Создание таблицы для Role
CREATE TABLE role_table (
    id UUID PRIMARY KEY,
    role_type VARCHAR(255) NOT NULL,    -- Auditor | Developer | Owner | Admin
    name VARCHAR(255) NOT NULL UNIQUE,
    shop_name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    product_id UUID,
    org_unit_id UUID,
    is_shop_hidden BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product_table(id),
    FOREIGN KEY (org_unit_id) REFERENCES organization_unit_table(id)
);

CREATE INDEX IF NOT EXISTS role_table_index ON role_table(name, shop_name);
CREATE INDEX IF NOT EXISTS role_table_product_id_index ON role_table(product_id);

-- Создание таблицы для Permission
CREATE TABLE permission_table (
    id UUID PRIMARY KEY,
    action_type VARCHAR(255) NOT NULL, -- Например: CREATE, READ, UPDATE, DELETE or any custom
    resource_type VARCHAR(255) NOT NULL, -- На кого распространяется, например: FRONT, MDB, IDM
    data JSONB NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы для связи Role и Action Permission
CREATE TABLE role_permission_table (
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES role_table(id),
    FOREIGN KEY (permission_id) REFERENCES permission_table(id)
);

CREATE INDEX IF NOT EXISTS role_permission_table_role_id_index ON role_permission_table(role_id);
CREATE INDEX IF NOT EXISTS role_permission_table_permission_id_index ON role_permission_table(permission_id);

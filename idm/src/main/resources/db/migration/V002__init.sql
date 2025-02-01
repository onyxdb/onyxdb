-- Создание таблицы для Project
CREATE TABLE project_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    parent_id UUID NOT NULL,
    owner_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES project_table(id),
    FOREIGN KEY (owner_id) REFERENCES account_table(id)
);

-- Создание таблицы для Role
CREATE TABLE role_table (
    id UUID PRIMARY KEY,
    role_type VARCHAR(255) NOT NULL,    -- Auditor | Developer | Owner | Admin
    name VARCHAR(255) NOT NULL UNIQUE,
    shop_name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    project_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES project_table(id)
);

-- Создание таблицы для Permission
CREATE TABLE permission_table (
    id UUID PRIMARY KEY,
    action_type VARCHAR(255) NOT NULL, -- Например: CREATE, READ, UPDATE, DELETE or any custom
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

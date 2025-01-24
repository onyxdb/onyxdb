-- Создание таблицы для Resource (для выдачи доступов)
CREATE TABLE resource_table (
    id UUID PRIMARY KEY,
    resource_type VARCHAR(255) NOT NULL, -- Например: PROJECT, SERVICE, ORGANIZATION
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы для Organization
CREATE TABLE organization_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resource_id UUID NOT NULL UNIQUE,
    owner_id UUID NOT NULL,
    FOREIGN KEY (resource_id) REFERENCES resource_table(id),
    FOREIGN KEY (owner_id) REFERENCES account_table(id)
);

-- Создание таблицы для Project
CREATE TABLE project_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resource_id UUID NOT NULL UNIQUE,
    organization_id UUID NOT NULL UNIQUE,
    owner_id UUID NOT NULL,
    FOREIGN KEY (resource_id) REFERENCES resource_table(id),
    FOREIGN KEY (organization_id) REFERENCES organization_table(id),
    FOREIGN KEY (owner_id) REFERENCES account_table(id)
);

-- Создание таблицы для Service
CREATE TABLE service_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL, -- Например: ManagedMongoDB, ManagedPostgreSQL
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resource_id UUID NOT NULL UNIQUE,
    project_id UUID NOT NULL UNIQUE,
    owner_id UUID NOT NULL,
    FOREIGN KEY (resource_id) REFERENCES resource_table(id),
    FOREIGN KEY (project_id) REFERENCES project_table(id),
    FOREIGN KEY (owner_id) REFERENCES account_table(id)
);

CREATE TABLE account_resource_role_table (
    account_id UUID NOT NULL,
    resource_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (account_id, resource_id, role_id),
    FOREIGN KEY (account_id) REFERENCES account_table(id),
    FOREIGN KEY (resource_id) REFERENCES resource_table(id),
    FOREIGN KEY (role_id) REFERENCES role_table(id)
);

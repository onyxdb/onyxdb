-- Создание таблицы для Resource (для выдачи доступов)
CREATE TABLE resource (
    id SERIAL PRIMARY KEY,
    resource_type VARCHAR(255) NOT NULL, -- Например: PROJECT, SERVICE, ORGANIZATION
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы для Organization
CREATE TABLE organization (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resource_id INT NOT NULL UNIQUE,
    FOREIGN KEY (resource_id) REFERENCES resource(id)
);

-- Создание таблицы для Project
CREATE TABLE project (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resource_id INT NOT NULL UNIQUE,
    organization_id INT NOT NULL UNIQUE,
    FOREIGN KEY (resource_id) REFERENCES resource(id),
    FOREIGN KEY (organization_id) REFERENCES organization(id)
);

-- Создание таблицы для Service
CREATE TABLE service (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL, -- Например: ManagedMongoDB, ManagedPostgreSQL
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resource_id INT NOT NULL UNIQUE,
    project_id INT NOT NULL UNIQUE,
    FOREIGN KEY (resource_id) REFERENCES resource(id),
    FOREIGN KEY (project_id) REFERENCES project(id)
);

CREATE TABLE account_resource_role (
    account_id INT NOT NULL,
    resource_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (account_id, resource_id, role_id),
    FOREIGN KEY (account_id) REFERENCES account(id),
    FOREIGN KEY (resource_id) REFERENCES resource(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

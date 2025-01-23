-- Создание таблицы для Role
CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы для Permission
CREATE TABLE permission (
    id SERIAL PRIMARY KEY,
    action_type VARCHAR(255) NOT NULL, -- Например: CREATE, READ, UPDATE, DELETE, READ_LOGS
    resource_type VARCHAR(255) NOT NULL, -- Например: PROJECT, SERVICE, ORGANIZATION
    resource_fields VARCHAR(255)[], -- Массив полей ресурса, к которым применимо действие
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы для связи Role и Permission
CREATE TABLE role_permission (
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES role(id),
    FOREIGN KEY (permission_id) REFERENCES permission(id)
);

-- Создание таблицы для Business Role
CREATE TABLE business_role (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    parent_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES business_role(id)
);

-- Создание таблицы для связи Business Role и Role
CREATE TABLE business_role_role (
    business_role_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (business_role_id, role_id),
    FOREIGN KEY (business_role_id) REFERENCES business_role(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- Создание таблицы для связи Account и Business Role
CREATE TABLE account_business_role (
    account_id INT NOT NULL,
    business_role_id INT NOT NULL,
    PRIMARY KEY (account_id, business_role_id),
    FOREIGN KEY (account_id) REFERENCES account(id),
    FOREIGN KEY (business_role_id) REFERENCES business_role(id)
);

-- Создание таблицы для связи Account и Role
CREATE TABLE account_role (
    account_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (account_id, role_id),
    FOREIGN KEY (account_id) REFERENCES account(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- Создание таблицы для связи Account и Organization Unit
CREATE TABLE account_ou (
    account_id INT NOT NULL,
    ou_id INT NOT NULL,
    PRIMARY KEY (account_id, ou_id),
    FOREIGN KEY (account_id) REFERENCES account(id),
    FOREIGN KEY (ou_id) REFERENCES organization_unit(id)
);

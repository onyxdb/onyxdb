CREATE TABLE account (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE role (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    permissions TEXT[] NOT NULL
);

CREATE TABLE service (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    project_id UUID NOT NULL,
    FOREIGN KEY (project_id) REFERENCES project(id)
);

CREATE TABLE project (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    organization_id UUID NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organization(id)
);

CREATE TABLE organization (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE group_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE account_group (
    account_id UUID NOT NULL,
    group_id UUID NOT NULL,
    PRIMARY KEY (account_id, group_id),
    FOREIGN KEY (account_id) REFERENCES account(id),
    FOREIGN KEY (group_id) REFERENCES group_table(id)
);

CREATE TABLE group_role (
    group_id UUID NOT NULL,
    role_id UUID NOT NULL,
    resource_id UUID NOT NULL,
    PRIMARY KEY (group_id, role_id, resource_id),
    FOREIGN KEY (group_id) REFERENCES group_table(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE account_role (
    account_id UUID NOT NULL,
    role_id UUID NOT NULL,
    resource_id UUID NOT NULL,
    PRIMARY KEY (account_id, role_id, resource_id),
    FOREIGN KEY (account_id) REFERENCES account(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);
CREATE TABLE account_table (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE group_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE account_group_table (
    account_id UUID NOT NULL,
    group_id UUID NOT NULL,
    PRIMARY KEY (account_id, group_id),
    FOREIGN KEY (account_id) REFERENCES account_table(id),
    FOREIGN KEY (group_id) REFERENCES group_table(id)
);

CREATE TABLE role_table (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    permissions TEXT[] NOT NULL
);

CREATE TABLE group_role_table (
    group_id UUID NOT NULL,
    role_id UUID NOT NULL,
    resource_id UUID NOT NULL,
    PRIMARY KEY (group_id, role_id, resource_id),
    FOREIGN KEY (group_id) REFERENCES group_table(id),
    FOREIGN KEY (role_id) REFERENCES role_table(id)
);

CREATE TABLE account_role_table (
    account_id UUID NOT NULL,
    role_id UUID NOT NULL,
    resource_id UUID NOT NULL,
    PRIMARY KEY (account_id, role_id, resource_id),
    FOREIGN KEY (account_id) REFERENCES account_table(id),
    FOREIGN KEY (role_id) REFERENCES role_table(id)
);

CREATE TABLE organization_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE project_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    organization_id UUID NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organization_table(id)
);

CREATE TABLE service_table (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    project_id UUID NOT NULL,
    FOREIGN KEY (project_id) REFERENCES project_table(id)
);

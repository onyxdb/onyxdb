CREATE TABLE public.projects
(
    id         uuid        NOT NULL,
    name       varchar(64) NOT NULL,
    product_id uuid        NOT NULL,
    created_at timestamp   NOT NULL,
    created_by uuid        NOT NULL,
    min_zones  int         NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TYPE public.cluster_type AS ENUM (
    'mongodb',
    'postgresql'
    );

CREATE TABLE public.clusters
(
    id         uuid                NOT NULL,
    name       varchar(64)         NOT NULL,
    project_id uuid                NOT NULL,
    type       public.cluster_type NOT NULL,
    spec       jsonb               NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (project_id) REFERENCES projects (id)
);

CREATE TABLE public.cluster_users
(
    id         uuid      NOT NULL,
    name       varchar   NOT NULL,
    cluster_id uuid      NOT NULL,
    data       jsonb     NOT NULL,
    created_at timestamp NOT NULL,
    created_by uuid      NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name, cluster_id),
    FOREIGN KEY (cluster_id) REFERENCES public.clusters (id)
);

CREATE TABLE public.cluster_databases
(
    id         uuid      NOT NULL,
    name       varchar   NOT NULL,
    cluster_id uuid      NOT NULL,
    data       jsonb     NOT NULL,
    created_at timestamp NOT NULL,
    created_by uuid      NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name, cluster_id),
    FOREIGN KEY (cluster_id) REFERENCES public.clusters (id)
);


CREATE TABLE public.cluster_database_permissions
(
    id          uuid      NOT NULL,
    user_id     uuid      NOT NULL,
    database_id uuid      NOT NULL,
    data        jsonb     NOT NULL,
    created_at  timestamp NOT NULL,
    created_by  uuid      NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (user_id, database_id),
    FOREIGN KEY (user_id) REFERENCES public.cluster_users (id),
    FOREIGN KEY (database_id) REFERENCES public.cluster_databases (id)
);

CREATE TYPE public.cluster_operation_type AS ENUM (
    'create_cluster'
    );

CREATE TYPE public.cluster_operation_status AS ENUM (
    'scheduled',
    'in_progress',
    'error',
    'success'
    );

CREATE TABLE public.cluster_operations
(
    id         uuid                            NOT NULL,
    cluster_id uuid                            NOT NULL,
    type       public.cluster_operation_type   NOT NULL,
    status     public.cluster_operation_status NOT NULL,
    created_at timestamp                       NOT NULL,
    created_by uuid                            NOT NULL,
    updated_at timestamp                       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cluster_id) REFERENCES clusters (id)
);

CREATE TYPE public.cluster_task_status AS ENUM (
    'scheduled',
    'in_progress',
    'error',
    'success'
    );

CREATE TYPE public.cluster_task_type AS ENUM (
    'mongodb_create_cluster_apply_manifest',
    'mongodb_create_cluster_save_hosts',
    'mongodb_create_cluster_generate_grafana_dashboard'
    );

CREATE TABLE public.cluster_tasks
(
    id            uuid                       NOT NULL,
    cluster_id    uuid                       NOT NULL,
    operation_id  uuid                       NOT NULL,
    cluster_type  public.cluster_type        NOT NULL,
    type          public.cluster_task_type   NOT NULL,
    status        public.cluster_task_status NOT NULL,
    created_at    timestamp                  NOT NULL,
    updated_at    timestamp                  NOT NULL,
    scheduled_at  timestamp                  NOT NULL,
    attempts_left int                        NOT NULL,
    is_first      boolean                    NOT NULL,
    is_last       boolean                    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cluster_id) REFERENCES clusters (id),
    FOREIGN KEY (operation_id) REFERENCES cluster_operations (id)
);

CREATE TABLE public.cluster_tasks_to_blocker_tasks
(
    task_id         uuid,
    blocker_task_id uuid,
    FOREIGN KEY (task_id) REFERENCES public.cluster_tasks (id),
    FOREIGN KEY (blocker_task_id) REFERENCES public.cluster_tasks (id),
    PRIMARY KEY (task_id, blocker_task_id)
);

CREATE TABLE public.zone
(
    key      varchar NOT NULL,
    name     varchar NOT NULL,
    selector varchar NOT NULL,
    PRIMARY KEY (key)
);

CREATE TYPE public.cluster_tip_type AS ENUM (
    'scheduled'
    );

CREATE TYPE public.cluster_tip_status AS ENUM (
    'scheduled'
    );


CREATE TABLE public.cluster_tips
(
    id         uuid                      NOT NULL,
    type       public.cluster_tip_type   NOT NULL,
    cluster_id uuid                      NOT NULL,
    data       jsonb                     NOT NULL,
    created_at timestamp                 NOT NULL,
    created_by uuid                      NOT NULL,
    status     public.cluster_tip_status NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cluster_id) REFERENCES clusters (id)
);

CREATE TABLE public.resource_types
(
    key          varchar          NOT NULL,
    name         varchar          NOT NULL,
    billing_coef double precision NOT NULL,
    PRIMARY KEY (key)
);

CREATE TABLE public.product_quotas
(
    id                uuid    NOT NULL,
    product_id        uuid,
    resource_type_key varchar NOT NULL,
    quoted            varchar NOT NULL,
    allocated         varchar NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (product_id, resource_type_key),
    FOREIGN KEY (resource_type_key) REFERENCES resource_types (key)
);

CREATE TABLE public.project_quotas
(
    id                uuid    NOT NULL,
    project_id        uuid,
    resource_type_key varchar NOT NULL,
    quoted            varchar NOT NULL,
    allocated         varchar NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (project_id, resource_type_key),
    FOREIGN KEY (project_id) REFERENCES projects (id),
    FOREIGN KEY (resource_type_key) REFERENCES resource_types (key)
);
-- INSERT INTO projects
-- VALUES ('5cb0ca1c-e6c1-47ab-b832-0074312490a3',
--         'test-project',
--         now(),
--         '66ee0ec9-1e19-4f1b-ba8e-4e817d4fa1f2',
--         '66ee0ec9-1e19-4f1b-ba8e-4e817d4fa1f2');
--
--
-- INSERT INTO clusters
-- VALUES ('062c4806-0248-4c5d-86da-e1244e172619',
--         'test-cluster',
--         '5cb0ca1c-e6c1-47ab-b832-0074312490a3',
--         'mongodb');

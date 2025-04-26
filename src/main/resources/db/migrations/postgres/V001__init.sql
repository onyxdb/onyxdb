CREATE TYPE public.resource_preset_type AS ENUM (
    'cpu_optimized',
    'standard',
    'ram_optimized'
    );

CREATE TABLE public.resource_presets
(
    id   uuid                        NOT NULL,
    name varchar                     NOT NULL,
    type public.resource_preset_type NOT NULL,
    vcpu double precision            NOT NULL,
    ram  bigint                      NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

INSERT INTO public.resource_presets (id, name, type, vcpu, ram)
VALUES ('4eaec494-f935-46eb-8a5e-c8e54afa9869', 'co-c2-r4', 'cpu_optimized', 2, 4294967296),
       ('00551fb9-f935-43e7-a597-565221818b79', 'co-c4-r8', 'cpu_optimized', 4, 8589934592),
       ('853ec99d-8b5e-469b-94eb-41a88d244223', 's-c2-r4', 'standard', 2, 4294967296),
       ('06445eb3-e1d9-4b0b-a567-fdff5cdf619a', 's-c4-r8', 'standard', 4, 8589934592),
       ('c42ba25e-8206-4395-9bf9-5835e6267dac', 'ro-c2-r16', 'ram_optimized', 2, 17179869184),
       ('05195d23-5385-4887-b1ec-5f7d5b315d5c', 'ro-c4-r32', 'ram_optimized', 4, 34359738368);

CREATE TABLE public.zones
(
    id          varchar NOT NULL,
    description varchar NOT NULL,
    selector    varchar NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (selector)
);

CREATE TABLE public.projects
(
    id          uuid    NOT NULL,
    name        varchar NOT NULL,
    description varchar NOT NULL,
    product_id  uuid    NOT NULL,
    is_archived boolean NOT NULL DEFAULT false,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TYPE public.cluster_type AS ENUM (
    'mongodb'
    );

CREATE TABLE public.clusters
(
    id          uuid                NOT NULL,
    name        varchar             NOT NULL,
    description varchar             NOT NULL,
    project_id  uuid                NOT NULL,
    type        public.cluster_type NOT NULL,
    config      jsonb               NOT NULL,
    is_deleted  bool                NOT NULL DEFAULT false,
    deleted_at  timestamp,
    PRIMARY KEY (id),
    FOREIGN KEY (project_id) REFERENCES public.projects (id),
    UNIQUE (name, project_id)
);

CREATE TABLE public.cluster_hosts
(
    name       varchar NOT NULL,
    cluster_id uuid    NOT NULL,
    PRIMARY KEY (name, cluster_id),
    FOREIGN KEY (cluster_id) REFERENCES clusters (id)
);

CREATE TABLE public.databases
(
    id         uuid      NOT NULL,
    name       varchar   NOT NULL,
    cluster_id uuid      NOT NULL,
    created_at timestamp NOT NULL,
    created_by uuid      NOT NULL,
    is_deleted bool      NOT NULL,
    deleted_at timestamp,
    deleted_by uuid,
    PRIMARY KEY (id),
    FOREIGN KEY (cluster_id) REFERENCES public.clusters (id),
    UNIQUE (name, cluster_id)
);

CREATE TABLE public.users
(
    id         uuid      NOT NULL,
    name       varchar   NOT NULL,
    password_secret varchar   NOT NULL,
    cluster_id uuid      NOT NULL,
    created_at timestamp NOT NULL,
    created_by uuid      NOT NULL,
    is_deleted bool      NOT NULL,
    deleted_at timestamp,
    deleted_by uuid,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX users_name_cluster_id_uniq_idx ON public.users (name, cluster_id) WHERE (users.is_deleted = false);

CREATE TABLE public.permissions
(
    id          uuid      NOT NULL,
    user_id     uuid      NOT NULL,
    database_id uuid      NOT NULL,
    created_at  timestamp NOT NULL,
    created_by  uuid      NOT NULL,
    is_deleted  bool      NOT NULL,
    deleted_at  timestamp,
    deleted_by  uuid,
    data        jsonb     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES public.users (id),
    FOREIGN KEY (database_id) REFERENCES public.databases (id),
    UNIQUE (user_id, database_id)
);

CREATE TABLE public.operations
(
    id         uuid      NOT NULL,
    type       varchar   NOT NULL,
    status     varchar   NOT NULL,
    created_at timestamp NOT NULL,
    created_by uuid      NOT NULL,
    updated_at timestamp NOT NULL,
    payload    jsonb     NOT NULL,
    cluster_id uuid,
    PRIMARY KEY (id),
    FOREIGN KEY (cluster_id) REFERENCES public.clusters (id)
);

CREATE UNIQUE INDEX operations_uniq_idx ON public.operations (cluster_id, status)
    WHERE public.operations.status = 'in_progress';

CREATE TYPE public.task_status AS ENUM (
    'scheduled',
    'in_progress',
    'error',
    'success'
    );

-- CREATE TYPE public.task_type AS ENUM (
--     'mongodb_create_vector_config',
--     'mongodb_apply_psmdb',
--     'mongodb_check_psmdb_readiness',
--     'mongodb_apply_onyxdb_agent',
--     'mongodb_check_onyxdb_agent_readiness',
--     'mongodb_create_exporter_service',
--     'mongodb_create_exporter_service_scrape',
--     'mongodb_delete_exporter_service_scrape',
--     'mongodb_delete_exporter_service',
--     'mongodb_delete_onyxdb_agent',
--     'mongodb_check_onyxdb_agent_is_deleted',
--     'mongodb_delete_psmdb',
--     'mongodb_check_psmdb_is_deleted',
--     'mongodb_delete_vector_config',
--     'mongodb_delete_secrets'
--     );

CREATE TABLE public.tasks
(
    id            uuid               NOT NULL,
    type          varchar            NOT NULL,
    status        public.task_status NOT NULL,
    operation_id  uuid               NOT NULL,
    created_at    timestamp          NOT NULL,
    updated_at    timestamp          NOT NULL,
    scheduled_at  timestamp          NOT NULL,
    attempts_left int                NOT NULL,
    is_first      boolean            NOT NULL,
    is_last       boolean            NOT NULL,
    payload       jsonb              NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (operation_id) REFERENCES operations (id)
);

CREATE TABLE public.tasks_to_blocker_tasks
(
    task_id         uuid,
    blocker_task_id uuid,
    FOREIGN KEY (task_id) REFERENCES public.tasks (id),
    FOREIGN KEY (blocker_task_id) REFERENCES public.tasks (id),
    PRIMARY KEY (task_id, blocker_task_id)
);

CREATE TYPE public.resource_type AS ENUM (
    'unknown',
    'vcpu',
    'ram'
    );

CREATE TYPE public.quota_provider AS ENUM (
    'mdb'
    );

CREATE TABLE public.resources
(
    id          uuid                  NOT NULL,
    name        varchar               NOT NULL,
    description varchar               NOT NULL,
    type        public.resource_type  NOT NULL,
    provider    public.quota_provider NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (type, provider)
);

INSERT INTO public.resources (id, name, description, type, provider)
VALUES ('a162cf17-0320-42be-b4e2-9b2e91070916',
        'vCPU',
        'Количество vCPU',
        'vcpu',
        'mdb'),
       ('070dc19a-4000-4035-94a7-fe389df8fb1b',
        'RAM',
        'Объем оперативной памяти',
        'ram',
        'mdb');

CREATE TABLE public.products
(
    id   uuid    NOT NULL,
    name varchar NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO public.products (id, name)
VALUES ('a4ea283e-b3aa-43dd-9e64-d6c68d0af96f', 'Product 1'),
       ('a3e1fcde-aa38-4029-9370-9b320d81d01e', 'Product 2');

CREATE TABLE public.product_quotas
(
    product_id  uuid   NOT NULL,
    resource_id uuid   NOT NULL,
    "limit"     bigint NOT NULL,
    allocation  bigint NOT NULL,
    free        bigint NOT NULL,
    PRIMARY KEY (product_id, resource_id)
--     FOREIGN KEY (product_id) REFERENCES public.products (id),
--     FOREIGN KEY (resource_id) REFERENCES public.resources (id)
);

INSERT INTO public.product_quotas(product_id, resource_id, "limit", allocation, free)
VALUES ('a4ea283e-b3aa-43dd-9e64-d6c68d0af96f',
        'a162cf17-0320-42be-b4e2-9b2e91070916',
        10,
        5,
        5),
       ('a3e1fcde-aa38-4029-9370-9b320d81d01e',
        'a162cf17-0320-42be-b4e2-9b2e91070916',
        5,
        2,
        3),
       ('a4ea283e-b3aa-43dd-9e64-d6c68d0af96f',
        '070dc19a-4000-4035-94a7-fe389df8fb1b',
        10,
        5,
        5)
--        ('a3e1fcde-aa38-4029-9370-9b320d81d01e',
--         '070dc19a-4000-4035-94a7-fe389df8fb1b',
--         5,
--         2,
--         3)
;

CREATE TABLE public.shedlock
(
    name       VARCHAR(64),
    lock_until TIMESTAMP(3) NULL,
    locked_at  TIMESTAMP(3) NULL,
    locked_by  VARCHAR(255),
    PRIMARY KEY (name)
);

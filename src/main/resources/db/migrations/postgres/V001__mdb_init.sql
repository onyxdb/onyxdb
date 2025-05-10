CREATE TABLE public.initialization
(
    is_initialized bool NOT NULL,
    initialized_at timestamp
);

INSERT INTO public.initialization(is_initialized)
VALUES (false);

CREATE TYPE public.resource_preset_type AS ENUM (
    'cpu_optimized',
    'standard',
    'ram_optimized'
    );

CREATE TABLE public.resource_presets
(
    id   varchar                     NOT NULL,
    type public.resource_preset_type NOT NULL,
    vcpu bigint                      NOT NULL,
    ram  bigint                      NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO public.resource_presets (id, type, vcpu, ram)
VALUES
    -- standard
    ('s-c2-r8', 'standard', 2000, 8589934592),
    ('s-c4-r16', 'standard', 4000, 17179869184),
    ('s-c8-r32', 'standard', 8000, 34359738368),
    ('s-c12-r48', 'standard', 12000, 51539607552),
    ('s-c16-r64', 'standard', 16000, 68719476736),
    ('s-c24-r96', 'standard', 24000, 103079215104),
    ('s-c32-r128', 'standard', 32000, 137438953472),
    ('s-c40-r160', 'standard', 40000, 171798691840),
    ('s-c48-r192', 'standard', 48000, 206158430208),
    ('s-c64-r256', 'standard', 64000, 274877906944),
    ('s-c80-r320', 'standard', 80000, 343597383680),
    ('s-c96-r576', 'standard', 96000, 618475290624),
    -- cpu_optimized
    ('co-c2-r4', 'cpu_optimized', 2000, 4294967296),
    ('co-c4-r8', 'cpu_optimized', 4000, 8589934592),
    ('co-c8-r16', 'cpu_optimized', 8000, 17179869184),
    ('co-c12-r24', 'cpu_optimized', 12000, 25769803776),
    ('co-c16-r32', 'cpu_optimized', 16000, 34359738368),
    ('co-c24-r48', 'cpu_optimized', 24000, 51539607552),
    ('co-c32-r64', 'cpu_optimized', 32000, 68719476736),
    ('co-c40-r80', 'cpu_optimized', 40000, 85899345920),
    ('co-c48-r96', 'cpu_optimized', 48000, 103079215104),
    ('co-c64-r128', 'cpu_optimized', 64000, 137438953472),
    ('co-c80-r160', 'cpu_optimized', 80000, 171798691840),
    ('co-c96-r192', 'cpu_optimized', 96000, 206158430208),
    -- ram_optimized
    ('ro-c2-r16', 'ram_optimized', 2000, 17179869184),
    ('ro-c4-r32', 'ram_optimized', 4000, 34359738368),
    ('ro-c6-r48', 'ram_optimized', 6000, 51539607552),
    ('ro-c8-r64', 'ram_optimized', 8000, 68719476736),
    ('ro-c12-r96', 'ram_optimized', 12000, 103079215104),
    ('ro-c16-r128', 'ram_optimized', 16000, 137438953472),
    ('ro-c24-r192', 'ram_optimized', 24000, 206158430208),
    ('ro-c32-r256', 'ram_optimized', 32000, 274877906944),
    ('ro-c40-r320', 'ram_optimized', 40000, 343597383680),
    ('ro-c48-r384', 'ram_optimized', 48000, 412316860416),
    ('ro-c56-r448', 'ram_optimized', 56000, 481036337152),
    ('ro-c64-r512', 'ram_optimized', 64000, 549755813888),
    ('ro-c80-r640', 'ram_optimized', 80000, 687194767360);

CREATE TABLE public.projects
(
    id          uuid      NOT NULL,
    name        varchar   NOT NULL,
    description varchar   NOT NULL,
    product_id  uuid      NOT NULL,
    namespace   varchar   NOT NULL,
    created_at  timestamp NOT NULL,
    created_by  uuid      NOT NULL,
    is_deleted  bool      NOT NULL DEFAULT false,
    deleted_at  timestamp,
    deleted_by  uuid,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX projects_project_name_is_deleted_uniq_idx ON public.projects (name, is_deleted)
    WHERE public.projects.is_deleted = false;

CREATE TYPE public.cluster_type AS ENUM (
    'mongodb'
    );

CREATE TABLE public.clusters
(
    id          uuid                NOT NULL,
    name        varchar             NOT NULL,
    description varchar             NOT NULL,
    status      varchar             NOT NULL,
    project_id  uuid                NOT NULL,
    namespace   varchar             NOT NULL,
    type        public.cluster_type NOT NULL,
    config      jsonb               NOT NULL,
    created_at  timestamp           NOT NULL,
    created_by  uuid                NOT NULL,
    is_deleted  bool                NOT NULL DEFAULT false,
    deleted_at  timestamp,
    deleted_by  uuid,
    PRIMARY KEY (id),
    FOREIGN KEY (project_id) REFERENCES public.projects (id)
);

CREATE UNIQUE INDEX clusters_cluster_name_is_deleted_uniq_idx ON public.clusters (name, is_deleted)
    WHERE public.clusters.is_deleted = false;

CREATE TABLE public.hosts
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
    is_deleted bool      NOT NULL DEFAULT false,
    deleted_at timestamp,
    deleted_by uuid,
    PRIMARY KEY (id),
    FOREIGN KEY (cluster_id) REFERENCES public.clusters (id)
);

CREATE UNIQUE INDEX databases_database_name_cluster_id_is_deleted_uniq_idx ON public.databases (name, cluster_id, is_deleted)
    WHERE public.databases.is_deleted = false;

CREATE TABLE public.users
(
    id              uuid      NOT NULL,
    name            varchar   NOT NULL,
    password_secret varchar   NOT NULL,
    cluster_id      uuid      NOT NULL,
    created_at      timestamp NOT NULL,
    created_by      uuid      NOT NULL,
    is_deleted      bool      NOT NULL DEFAULT false,
    deleted_at      timestamp,
    deleted_by      uuid,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX users_user_name_cluster_id_is_deleted_uniq_idx ON public.users (name, cluster_id, is_deleted)
    WHERE public.users.is_deleted = false;

CREATE TABLE public.permissions
(
    id            uuid      NOT NULL,
    user_name     varchar   NOT NULL,
    database_name varchar   NOT NULL,
    cluster_id    uuid      NOT NULL,
    created_at    timestamp NOT NULL,
    created_by    uuid      NOT NULL,
    is_deleted    bool      NOT NULL DEFAULT false,
    deleted_at    timestamp,
    deleted_by    uuid,
    data          jsonb     NOT NULL,
    PRIMARY KEY (id)
--     FOREIGN KEY (user_name) REFERENCES public.users (name),
--     FOREIGN KEY (database_name) REFERENCES public.databases (name)
);

CREATE UNIQUE INDEX permissions_u_name_db_name_cluster_id_is_deleted_uniq_idx
    ON public.permissions (user_name, database_name, cluster_id, is_deleted)
    WHERE public.permissions.is_deleted = false;

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

CREATE UNIQUE INDEX operations_scheduled_uniq_idx ON public.operations (cluster_id, status)
    WHERE public.operations.status in ('scheduled', 'in_progress');

CREATE TYPE public.task_status AS ENUM (
    'scheduled',
    'rescheduled',
    'in_progress',
    'error',
    'success'
    );

CREATE TABLE public.tasks
(
    id                  uuid               NOT NULL,
    type                varchar            NOT NULL,
    status              public.task_status NOT NULL,
    operation_id        uuid               NOT NULL,
    created_at          timestamp          NOT NULL,
    updated_at          timestamp          NOT NULL,
    attempts_left       int                NOT NULL,
    payload             jsonb              NOT NULL,
    blocker_ids         uuid[]             NOT NULL,
    post_delay_seconds  int                NOT NULL,
    retry_delay_seconds int                NOT NULL,
    started_at          timestamp,
    finished_at         timestamp,
    PRIMARY KEY (id),
    FOREIGN KEY (operation_id) REFERENCES public.operations (id)
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
VALUES ('3ec32ad8-db16-4955-8872-41404d483b9f', 'Connectify'),
       ('672fdd9c-fdef-49c2-a675-3c890e7316a3', 'Production');

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

CREATE TABLE public.shedlock
(
    name       VARCHAR(64),
    lock_until TIMESTAMP(3) NULL,
    locked_at  TIMESTAMP(3) NULL,
    locked_by  VARCHAR(255),
    PRIMARY KEY (name)
);

INSERT INTO public.product_quotas(product_id, resource_id, "limit", allocation, free)
VALUES ('3ec32ad8-db16-4955-8872-41404d483b9f',
        'a162cf17-0320-42be-b4e2-9b2e91070916',
        10,
        0,
        10),
       ('3ec32ad8-db16-4955-8872-41404d483b9f',
        '070dc19a-4000-4035-94a7-fe389df8fb1b',
        10737418240,
        0,
        10737418240);

INSERT INTO public.projects (id, name, description, product_id, namespace, created_at, created_by)
VALUES ('5cb0ca1c-e6c1-47ab-b832-0074312490a3',
        'sandbox',
        'This this sandbox project',
        '672fdd9c-fdef-49c2-a675-3c890e7316a3',
        'onyxdb',
        now(),
        'c1e0125f-61bb-421f-9f64-df0436506186');

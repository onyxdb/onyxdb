CREATE TYPE public.resource_preset_type AS ENUM (
    'cpu_optimized',
    'standard',
    'ram_optimized'
    );

CREATE TABLE public.resource_presets
(
    id   varchar                     NOT NULL,
    type public.resource_preset_type NOT NULL,
    vcpu double precision            NOT NULL,
    ram  bigint                      NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO public.resource_presets (id, type, vcpu, ram)
VALUES ('co-c2-r4', 'cpu_optimized', 2, 4294967296),
       ('co-c4-r8', 'cpu_optimized', 4, 8589934592),
       ('s-c2-r8', 'standard', 2, 4294967296),
       ('s-c4-r8', 'standard', 4, 8589934592),
       ('ro-c2-r16', 'ram_optimized', 2, 17179869184),
       ('ro-c4-r32', 'ram_optimized', 4, 34359738368);

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
    PRIMARY KEY (id),
    FOREIGN KEY (project_id) REFERENCES public.projects (id),
    UNIQUE (name, project_id)
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
--     created_by uuid                            NOT NULL,
--     updated_at timestamp                       NOT NULL,
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
    'mongodb_create_cluster',
    'mongodb_check_cluster_readiness',
    'mongodb_create_exporter_service',
    'mongodb_check_exporter_service_readiness',
    'mongodb_create_exporter_service_scrape',
    'mongodb_check_exporter_service_scrape_readiness'
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

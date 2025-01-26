CREATE TYPE public.cluster_type AS ENUM (
    'mongodb'
    );

CREATE TABLE public.clusters
(
    id          uuid                NOT NULL,
    name        varchar(64)         NOT NULL,
    description varchar(256)        NOT NULL,
    type        public.cluster_type NOT NULL,
    PRIMARY KEY (id)
);

CREATE TYPE public.cluster_operation_type AS ENUM (
    'create_cluster'
    );

CREATE TYPE public.cluster_operation_status AS ENUM (
    'in_progress',
    'failed',
    'done'
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
    'failed',
    'done'
    );

CREATE TYPE public.cluster_task_type AS ENUM (
    'mongodb_create_cluster_apply_manifest',
    'mongodb_create_cluster_check_cluster_readiness',
    'mongodb_create_cluster_generate_grafana_dashboard'
    );

CREATE TABLE public.cluster_tasks
(
    id                  uuid                       NOT NULL,
    cluster_id          uuid                       NOT NULL,
    operation_id        uuid                       NOT NULL,
    cluster_type        public.cluster_type        NOT NULL,
    type                public.cluster_task_type   NOT NULL,
    status              public.cluster_task_status NOT NULL,
    created_at          timestamp                  NOT NULL,
    updated_at          timestamp                  NOT NULL,
    scheduled_at        timestamp                  NOT NULL,
    retries_left        int                        NOT NULL,
    depends_on_task_ids uuid[]                     NOT NULL,
    is_last             boolean                    NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cluster_id) REFERENCES clusters (id),
    FOREIGN KEY (operation_id) REFERENCES cluster_operations (id)
);

-- CREATE TABLE public.cluster_tasks_queue
-- (
--     id uuid NOT NULL,
--     PRIMARY KEY (id)
-- );

-- CREATE TABLE public.cluster_operation
-- (
--     id         uuid                            NOT NULL,
--     cluster_id uuid                            NOT NULL,
--     type       cluster_operation_type          NOT NULL,
--     status     public.cluster_operation_status NOT NULL,
--     created_at timestamp                       NOT NULL,
--     retries    int                             NOT NULL,
--     execute_at timestamp                       NOT NULL,
--     PRIMARY KEY (id),
--     FOREIGN KEY (cluster_id) REFERENCES public.cluster (id)
-- );
--
-- CREATE TABLE public.cluster_operation_queue
-- (
--     id uuid NOT NULL,
--     FOREIGN KEY (id) REFERENCES public.cluster_operation (id)
-- );

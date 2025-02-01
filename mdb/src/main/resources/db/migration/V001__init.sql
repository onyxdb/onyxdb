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

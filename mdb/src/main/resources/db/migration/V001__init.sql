CREATE TYPE public.hardware_preset_type AS ENUM (
    'cpu_optimized',
    'standard',
    'ram_optimized'
    );

CREATE TABLE public.hardware_presets
(
    id   varchar                     NOT NULL,
    type public.hardware_preset_type NOT NULL,
    vcpu double precision            NOT NULL,
    ram  bigint                      NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO public.hardware_presets
    (id, type, vcpu, ram)
VALUES ('dev', 'standard', 0.3, 1073741824),
       ('co-c2-r4', 'cpu_optimized', 2, 4294967296),
       ('co-c4-r8', 'cpu_optimized', 4, 8589934592),
       ('s-c2-r8', 'standard', 2, 4294967296),
       ('s-c4-r8', 'standard', 4, 8589934592),
       ('ro-c2-r16', 'ram_optimized', 2, 17179869184),
       ('ro-c4-r32', 'ram_optimized', 4, 34359738368);

CREATE TABLE public.projects
(
    id         uuid        NOT NULL,
    name       varchar(64) NOT NULL,
    created_at timestamp   NOT NULL,
    created_by uuid        NOT NULL,
    owner_id   uuid        NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TYPE public.cluster_type AS ENUM (
    'mongodb'
    );

CREATE TABLE public.clusters
(
    id         uuid                NOT NULL,
    name       varchar(64)         NOT NULL,
    project_id uuid                NOT NULL,
    type       public.cluster_type NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (project_id) REFERENCES projects (id)
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

INSERT INTO projects
VALUES ('5cb0ca1c-e6c1-47ab-b832-0074312490a3',
        'test-project',
        now(),
        '66ee0ec9-1e19-4f1b-ba8e-4e817d4fa1f2',
        '66ee0ec9-1e19-4f1b-ba8e-4e817d4fa1f2');


INSERT INTO clusters
VALUES ('062c4806-0248-4c5d-86da-e1244e172619',
        'test-cluster',
        '5cb0ca1c-e6c1-47ab-b832-0074312490a3',
        'mongodb');

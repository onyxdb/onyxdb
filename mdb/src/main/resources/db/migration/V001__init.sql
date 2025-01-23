CREATE TABLE public.cluster
(
    id          uuid        NOT NULL,
    name        varchar(64) NOT NULL,
    description varchar(256),
    PRIMARY KEY (id)
);

CREATE TYPE cluster_operation_type AS ENUM
    (
        'mongodb_create_cluster_deploy_to_k8s',
        'mongodb_create_cluster_generate_dashboard'
        );

CREATE TYPE public.cluster_operation_status AS ENUM
    (
        'scheduled',
        'in_progress',
        'success',
        'error'
        );


CREATE TABLE public.cluster_operation
(
    id         uuid                            NOT NULL,
    cluster_id uuid                            NOT NULL,
    type       cluster_operation_type          NOT NULL,
    status     public.cluster_operation_status NOT NULL,
    created_at timestamp                       NOT NULL,
    retries    int                             NOT NULL,
    execute_at timestamp                       NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (cluster_id) REFERENCES public.cluster(id)
);

CREATE TABLE public.cluster_operation_queue
(
    id uuid NOT NULL,
    FOREIGN KEY (id) REFERENCES public.cluster_operation (id)
);

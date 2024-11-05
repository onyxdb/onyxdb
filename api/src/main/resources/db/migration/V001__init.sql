CREATE TYPE cluster_db_type AS ENUM ('mongodb');

CREATE TABLE public.clusters
(
    id          uuid UNIQUE,
    name        varchar(64) NOT NULL,
    description varchar(256),
    PRIMARY KEY (id)
);

CREATE TABLE public.clusters_to_storages
(
    cluster_id uuid UNIQUE,
    disk_size  bigint NOT NULL check (disk_size between 1073741824 and 10737418240), -- 1GB <= disk_size <= 10 GB
    FOREIGN KEY (cluster_id) REFERENCES public.clusters (id)
);

CREATE TABLE public.clusters_to_db_specs
(
    cluster_id uuid UNIQUE,
    type       cluster_db_type NOT NULL,
    spec       jsonb           NOT NULL,
    FOREIGN KEY (cluster_id) REFERENCES clusters (id)
);

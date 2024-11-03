CREATE TABLE public.clusters
(
    id   uuid UNIQUE,
    name varchar(64),
    PRIMARY KEY (id)
);

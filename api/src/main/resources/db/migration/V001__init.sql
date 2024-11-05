CREATE TABLE public.clusters
(
    id          uuid UNIQUE,
    name        varchar(64) NOT NULL,
    description varchar(256),
    PRIMARY KEY (id)
);

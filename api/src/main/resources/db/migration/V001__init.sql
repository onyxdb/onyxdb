CREATE TABLE public.cluster
(
    id          uuid        NOT NULL,
    name        varchar(64) NOT NULL,
    description varchar(256),
    PRIMARY KEY (id)
);

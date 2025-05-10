INSERT INTO public.resource_presets (id, type, vcpu, ram)
VALUES ('dev', 'standard', 500, 1073741824);

INSERT INTO public.products (id, name)
VALUES ('3ec32ad8-db16-4955-8872-41404d483b9f', 'Connectify'),
       ('672fdd9c-fdef-49c2-a675-3c890e7316a3', 'Production');

INSERT INTO public.product_quotas(product_id, resource_id, "limit", allocation, free)
VALUES ('3ec32ad8-db16-4955-8872-41404d483b9f',
        'a162cf17-0320-42be-b4e2-9b2e91070916',
        10000,
        0,
        10000),
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

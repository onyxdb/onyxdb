INSERT INTO public.zones (id, description, selector)
VALUES ('msc', 'Москва', 'msc'),
       ('spb', 'Санкт-Петербург', 'spb');

INSERT INTO public.resource_presets (id, name, type, vcpu, ram)
VALUES (gen_random_uuid(), 'dev', 'standard', 0.3, 1073741824);

INSERT INTO public.resource_presets (id, name, type, vcpu, ram)
VALUES (gen_random_uuid(), 'dev-2', 'standard', 0.35, 1299227607);


INSERT INTO public.projects (id, name, description)
VALUES ('5cb0ca1c-e6c1-47ab-b832-0074312490a3',
        'test-project',
        'Some description');

-- INSERT INTO public.clusters (id, name, description, project_id, type, version)
-- VALUES ('062c4806-0248-4c5d-86da-e1244e172619',
--         'test-cluster',
--         'some desc',
--         '5cb0ca1c-e6c1-47ab-b832-0074312490a3',
--         'mongodb',
--         'mongodb_8_0');
--
-- INSERT INTO public.mongo_8_0_config (cluster_id, mongod_resources_preset_id, mongod_storage_class)
-- VALUES ('062c4806-0248-4c5d-86da-e1244e172619',
--         'dev',
--         'standard');

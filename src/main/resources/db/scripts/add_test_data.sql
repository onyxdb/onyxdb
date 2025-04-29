-- INSERT INTO public.zones (id, description, selector)
-- VALUES ('msc', 'Москва', 'msc'),
--        ('spb', 'Санкт-Петербург', 'spb');

INSERT INTO public.resource_presets (id, name, type, vcpu, ram)
VALUES ('c92712a0-d344-4b1a-91fb-b27469b72bf5', 'dev', 'standard', 0.3, 536870912);

INSERT INTO public.resource_presets (id, name, type, vcpu, ram)
VALUES ('155df930-243d-4b57-b24b-607992f8c3d1', 'dev-2', 'standard', 0.35, 805306368);


INSERT INTO public.projects (id, name, description, product_id)
VALUES ('5cb0ca1c-e6c1-47ab-b832-0074312490a3',
        'sandbox',
        'This this sandbox project',
        'a3e1fcde-aa38-4029-9370-9b320d81d01e');

-- INSERT INTO public.clusters (id, databaseName, description, project_id, type, config, is_deleted)
-- VALUES ('062c4806-0248-4c5d-86da-e1244e172619',
--         'test-cluster',
--         'some desc',
--         '5cb0ca1c-e6c1-47ab-b832-0074312490a3',
--         'mongodb',
--         '{}',
--         false);
--
-- INSERT INTO public.mongo_8_0_config (cluster_id, mongod_resources_preset_id, mongod_storage_class)
-- VALUES ('062c4806-0248-4c5d-86da-e1244e172619',
--         'dev',
--         'standard');

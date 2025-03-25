INSERT INTO public.zones (id, description, selector)
VALUES ('msc', 'Москва', 'msc'),
       ('spb', 'Санкт-Петербург', 'spb');

INSERT INTO public.resource_presets (id, type, vcpu, ram)
VALUES ('dev', 'standard', 0.3, 1073741824);

INSERT INTO public.projects (id, name, description)
VALUES ('5cb0ca1c-e6c1-47ab-b832-0074312490a3',
        'test-project',
        'Some description');

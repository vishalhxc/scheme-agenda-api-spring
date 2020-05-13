create table agenda_api.group
(
    id uuid primary key not null,
    name text not null,
    user_id uuid not null,
    color text,
    unique(name, user_id)
);

create table agenda_api.scheme
(
    id uuid primary key not null,
    group_id uuid references groups(id),
    user_id uuid not null,
    name text not null,
    interval text not null,
    agendas_per_interval integer not null,
    is_countdown boolean default false,
    is_enabled boolean default true,
    unique(group_id, user_id, name)
);

create table agenda_api.agenda
(
    id uuid primary key not null,
    scheme_id uuid references schemes(id),
    name text not null,
    sequence integer not null,
    iterations integer not null,
    repetitions integer not null,
    target_value integer not null,
    target_description integer not null
);

grant select, insert, update, delete on all tables in schema agenda_api to agenda_app_user;
create schema if not exists scheme_agenda;

create database if not exists scheme_agenda_db with
    owner = scheme_agenda;

create table groups
(
    id uuid primary key not null,
    name text not null,
    user_id text not null,
    color text not null
);

create table schemes
(
    id uuid primary key not null,
    group_id uuid references group(id),
    user_id text not null,
    name text not null,
    interval text not null,
    routines_per_interval = integer not null,
    is_countdown = boolean default false,
    is_enabled = boolean default true
);

create table agendas
(
    id uuid primary key not null,
    scheme_id uuid references schemes(id)
    name text not null,
    sequence sequence not null,
    iterations integer not null,
    repetitions integer not null,
    target_value integer not null,
    target_description integer not null
);
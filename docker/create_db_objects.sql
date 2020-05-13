create schema agenda_api;
create role agenda_app_user with login password 'schemecrud';
grant connect on database scheme_agenda_db to agenda_app_user;
grant usage on schema agenda_api to agenda_app_user;
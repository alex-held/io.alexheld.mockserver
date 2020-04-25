create schema setup;

create table setup.actions
(
    id         int4,
    statuscode int4 default 400 not null,
    message    text
);

create table setup.requests
(
    id      int4,
    method  varchar(20),
    path    text,
    queries text[] default ARRAY []::text[] not null
);

create table setup.setups
(
    id        int4,
    action_id int4
);


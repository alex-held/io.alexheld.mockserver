create table setup.actions
(
    id         int4 default nextval('setup.actions_id_seq'::regclass) not null,
    statuscode int4 default 400                                       not null,
    message    text
);

create unique index actions_id_uindex
    on setup.actions (id);

create unique index actions_pk
    on setup.actions (id);

alter table setup.actions
    add constraint actions_pk
        primary key (id);

create table setup.requests
(
    request_id int4  default nextval('setup.requests_request_id_seq'::regclass) not null,
    method     varchar(20),
    path       text,
    queries    _text default ARRAY []::text[]                                   not null,
    constraint requests_pk
        primary key (request_id)
);

create index requests_method_index
    on setup.requests (method);

create unique index requests_request_pk_uindex
    on setup.requests (request_id);

create table setup.setups
(
    setup_id  int4 default nextval('setup.setups_setup_id_seq'::regclass) not null,
    action_fk int4
);



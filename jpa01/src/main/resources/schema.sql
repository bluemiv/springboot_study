drop table if exists notice;

create table notice
(
    id            bigint generated by default as identity,
    title         varchar(100),
    contents      varchar(1500),
    hits          bigint,
    likes         bigint,
    deleted       boolean,
    registered_at timestamp,
    updated_at    timestamp,
    deleted_at    timestamp,
    primary key (id)
);

drop table if exists user;

create table user
(
    id            bigint generated by default as identity,
    username      varchar(255),
    password      varchar(255),
    email         varchar(255),
    phone         varchar(255),
    registered_at timestamp,
    updated_at    timestamp,
    primary key (id)
);
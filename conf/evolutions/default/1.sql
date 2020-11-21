# --- First database schema

# --- !Ups

create table Person (
  id                        bigint not null,
  name                      varchar(255),
  password                      varchar(255),
  constraint pk_company primary key (id))
;

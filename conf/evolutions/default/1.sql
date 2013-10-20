# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table user (
  pseudo                    varchar(255) not null,
  mdp                       varchar(255),
  nom                       varchar(255),
  prenom                    varchar(255),
  email                     varchar(255),
  constraint pk_user primary key (pseudo))
;

create sequence user_seq;




# --- !Downs

drop table if exists user cascade;

drop sequence if exists user_seq;


# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table tweet (
  id                        bigint not null,
  commentaire               varchar(255),
  pseudo                    varchar(255),
  creation_date             timestamp,
  constraint pk_tweet primary key (id))
;

create table user (
  pseudo                    varchar(255) not null,
  mdp                       varchar(255),
  nom                       varchar(255),
  prenom                    varchar(255),
  email                     varchar(255),
  constraint pk_user primary key (pseudo))
;

create sequence tweet_seq;

create sequence user_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists tweet;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists tweet_seq;

drop sequence if exists user_seq;


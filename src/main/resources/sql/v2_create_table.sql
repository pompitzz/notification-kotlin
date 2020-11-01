use notification_service;


create table corona_event
(
    corona_event_id bigint not null auto_increment,
    is_enable       bit    not null,
    region_set      varchar(255),
    member_id       bigint,
    primary key (corona_event_id)
) engine = InnoDB;

create table corona_status
(
    corona_status_id          bigint  not null auto_increment,
    domestic_occurrence_count integer not null,
    foreign_inflow_count      integer not null,
    measurement_date_time     datetime,
    region                    varchar(255),
    primary key (corona_status_id)
) engine = InnoDB;

create table member
(
    member_id                          bigint not null auto_increment,
    access_token                       varchar(255),
    access_token_expiration_date_time  datetime,
    refresh_token                      varchar(255),
    refresh_token_expiration_date_time datetime,
    nickname                           varchar(255),
    oauth_id                           bigint not null,
    primary key (member_id)
) engine = InnoDB

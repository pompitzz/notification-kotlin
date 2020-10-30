use notification_service;

alter database notification_service
    character set = 'utf8mb4'
    collate = 'utf8mb4_general_ci';

show variables like 'c%';

select @@time_zone, now();

create table test (
                      id bigint(20) not null primary key auto_increment,
                      name varchar(256) default null
);

insert into test (id, name)
values (1, '안녕하세요');

select * from test;

drop table test;

--Mysql
create table email (
    id bigint not null,
    from_address varchar(255) not null,
    raw_data longtext not null,
    received_on datetime(6) not null,
    subject longtext not null,
    to_address varchar(255) not null,
    primary key (id)) engine=InnoDB;

create table email_attachment (
    id bigint not null,
    data longblob not null,
    filename varchar(1024) not null,
    email bigint not null,
    primary key (id)) engine=InnoDB;


create table email_content (
    id bigint not null,
    content_type varchar(255) not null,
    data longtext not null,
    email bigint not null,
    primary key (id)) engine=InnoDB;

create table email_attachment_sequence (next_val bigint) engine=InnoDB;
insert into email_attachment_sequence values ( 1 );

create table email_content_sequence (next_val bigint) engine=InnoDB;
insert into email_content_sequence values ( 1 );

create table email_sequence (next_val bigint) engine=InnoDB;
insert into email_sequence values ( 1 );

alter table email_attachment add constraint FK3mqbo7rnfaw1wlw99u08t0hbb foreign key (email) references email (id);
alter table email_content add constraint FK4t9way9926w2e9gd09dfc3v4v foreign key (email) references email (id);
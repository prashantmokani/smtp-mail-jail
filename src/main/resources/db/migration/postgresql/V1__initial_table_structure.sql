create sequence email_attachment_sequence start 1 increment 1;
create sequence email_content_sequence start 1 increment 1;
create sequence email_sequence start 1 increment 1;

create table email (id int8 not null, from_address varchar(255) not null, raw_data oid not null, received_on timestamp not null, subject oid not null, to_address varchar(255) not null, primary key (id));
create table email_attachment (id int8 not null, data oid not null, filename varchar(1024) not null, email int8 not null, primary key (id));
create table email_content (id int8 not null, content_type varchar(255) not null, data oid not null, email int8 not null, primary key (id));

alter table if exists email_attachment add constraint FK3mqbo7rnfaw1wlw99u08t0hbb foreign key (email) references email;
alter table if exists email_content add constraint FK4t9way9926w2e9gd09dfc3v4v foreign key (email) references email;
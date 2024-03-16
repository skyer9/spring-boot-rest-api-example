insert into user (USERNAME, PASSWORD, NICKNAME, ACTIVATED)
values ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 'admin', 1);

insert into AUTHORITY (AUTHORITY_NAME) values ('USER');
insert into AUTHORITY (AUTHORITY_NAME) values ('ADMIN');

insert into USER_AUTHORITY (USERNAME, AUTHORITY_NAME) values ('admin', 'USER');
insert into USER_AUTHORITY (USERNAME, AUTHORITY_NAME) values ('admin', 'ADMIN');
create table users
(
    id        bigserial,
    username  varchar(255) UNIQUE NOT NULL ,
    password  varchar(255) NOT NULL ,
    name      varchar(255),
    role      varchar(255) NOT NULL ,
    isactive  boolean NOT NULL ,
    primary key (id)
);

insert into users (username, password, name, role, isactive)
VALUES ('user', '{bcrypt}$2a$10$XBH9W6HVfegEp2gNM0xB8.Sq2QwLlSppcL0wOJrMSjU2v41WHZD5i', 'Обычный пользователь', 'USER', true ),
 ('editor', '{bcrypt}$2a$10$btqldBu9leIKRMm.UFWSMuUUhZFqPGKcKDDVu.k2RK7SHPkVqnLOW', 'Редактор', 'EDITOR', true )
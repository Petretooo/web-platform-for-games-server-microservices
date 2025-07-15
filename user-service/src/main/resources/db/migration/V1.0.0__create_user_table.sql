CREATE TABLE IF NOT EXISTS user(
    user_id varchar(36) primary key not null,
    username varchar(30) not null,
    email varchar(120) not null,
    password varchar (120) not null
);

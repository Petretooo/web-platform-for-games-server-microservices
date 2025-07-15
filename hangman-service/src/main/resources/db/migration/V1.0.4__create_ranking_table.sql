CREATE TABLE IF NOT EXISTS ranking(
    rank_id varchar(36) primary key not null,
    id_user_rank varchar(36),
    word varchar(30),
    score int,
    date_game date
);
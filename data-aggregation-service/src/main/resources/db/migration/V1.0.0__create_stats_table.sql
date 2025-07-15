CREATE table if not exists stats(
    stats_id varchar(36) PRIMARY KEY not null,
    game_title varchar(50),
    game_mode varchar(50),
    popularity_counter int
);
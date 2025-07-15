CREATE TABLE IF NOT EXISTS word(
    word_id VARCHAR(36) primary key not null,
    word VARCHAR(40) unique not null,
    level_difficulty int check(level_difficulty >= 1 and level_difficulty <= 6)
);
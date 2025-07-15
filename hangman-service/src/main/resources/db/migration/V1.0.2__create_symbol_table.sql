CREATE TABLE IF NOT EXISTS symbol(
    character_id varchar(36) primary key not null,
    letter char(1),
    game_id VARCHAR(36),
    FOREIGN KEY(game_id) REFERENCES hangman_game(game_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
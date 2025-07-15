CREATE TABLE IF NOT EXISTS hangman_game_stats(
    game_stats_id varchar(36) primary key not null,
    is_word_found BOOLEAN,
    wrong_tries int,
    game_start date,
    game_end date,
    game_result varchar(20),
    game_id varchar(36) UNIQUE,
    CONSTRAINT fk_hangman_game__stats_game FOREIGN KEY(game_id) REFERENCES hangman_game(game_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
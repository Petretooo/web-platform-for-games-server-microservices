CREATE table if not exists hangman_game(
    game_id varchar(36) PRIMARY KEY not null,
    number_tries int,
    current_word varchar(30) not null,
    hidden_word varchar(30) not null
);
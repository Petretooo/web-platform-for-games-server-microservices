CREATE TABLE IF NOT EXISTS hangman_game_room(
    game_room_id varchar(36) primary key not null,
    game_room_name varchar(36),
    game_title varchar(36),
    active boolean,
    word varchar(36),
    first_user_game varchar(36),
    second_user_game varchar(36),
    first_user_id varchar(36),
    second_user_id varchar(36),
    FOREIGN KEY(first_user_game) REFERENCES hangman_game(game_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(second_user_game) REFERENCES hangman_game(game_id) ON DELETE CASCADE ON UPDATE CASCADE
);

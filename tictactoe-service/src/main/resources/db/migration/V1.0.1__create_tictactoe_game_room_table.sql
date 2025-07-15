CREATE TABLE IF NOT EXISTS tictactoe_game_room(
    game_room_id varchar(36) primary key not null,
    game_room_name varchar(36),
    game_title varchar(36),
    active boolean,
    game_id varchar(36),
    first_user_id varchar(36),
    first_user_symbol varchar(1),
    second_user_id varchar(36),
    second_user_symbol varchar(1),
    FOREIGN KEY(game_id) REFERENCES tictactoe_game(game_id) ON DELETE CASCADE ON UPDATE CASCADE
);

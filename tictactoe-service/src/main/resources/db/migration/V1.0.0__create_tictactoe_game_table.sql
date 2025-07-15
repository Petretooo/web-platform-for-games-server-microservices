CREATE table if not exists tictactoe_game(
    game_id varchar(36) PRIMARY KEY not null,
    winner_username varchar(50)
);
CREATE TABLE IF NOT EXISTS tictactoe_game_user(
	game_id varchar(36) NOT NULL,
    user_id varchar(36) NOT NULL,
    PRIMARY KEY(game_id, user_id),
    FOREIGN KEY(game_id) REFERENCES tictactoe_game(game_id)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);
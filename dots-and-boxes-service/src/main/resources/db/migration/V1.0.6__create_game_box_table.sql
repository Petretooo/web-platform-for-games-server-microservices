CREATE table if not exists game_box(
	game_id varchar(36) NOT NULL,
	box_id varchar(36) NOT NULL,
	is_box_available boolean,
	user_id varchar(36),
	PRIMARY KEY(game_id, box_id),
    FOREIGN KEY(game_id) REFERENCES game(game_id),
    FOREIGN KEY(box_id) REFERENCES box(box_id)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);
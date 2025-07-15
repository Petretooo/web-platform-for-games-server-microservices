CREATE table if not exists game_room(
	game_room_id varchar(36) primary key not null,
	game_room_name varchar(36),
	game_title varchar(36),
	active boolean,
	game_id varchar(36),
	first_user_id varchar(36),
	first_user_symbol varchar(1),
	first_user_score int,
	second_user_id varchar(36),
	second_user_symbol varchar(1),
	second_user_score int,
	FOREIGN KEY(game_id) REFERENCES game(game_id) ON DELETE CASCADE ON UPDATE CASCADE
);
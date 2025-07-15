CREATE table if not exists edge(
    edge_id varchar(36) PRIMARY KEY NOT NULL,
    game_id varchar(36) NOT NULL,
	box_id varchar(36) NOT NULL,
	from_dot_id varchar(36) NOT NULL,
    to_dot_id varchar(36) NOT NULL,
    is_edge_available boolean,
    user_id varchar(36),
    order_edge int NOT NULL,
    FOREIGN KEY(box_id) REFERENCES box(box_id),
    FOREIGN KEY(from_dot_id) REFERENCES dot(dot_id),
    FOREIGN KEY(to_dot_id) REFERENCES dot(dot_id)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);
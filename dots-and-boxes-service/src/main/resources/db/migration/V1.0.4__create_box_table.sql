CREATE table if not exists box(
	box_id varchar(36) PRIMARY KEY not null,
	box_name varchar(36),
	order_box int NOT NULL
);
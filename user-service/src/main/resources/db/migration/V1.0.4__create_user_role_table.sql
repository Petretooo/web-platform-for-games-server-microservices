CREATE TABLE IF NOT EXISTS user_role(
	user_id varchar(36) NOT NULL,
    role_id varchar(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    PRIMARY KEY(user_id, role_id)
);
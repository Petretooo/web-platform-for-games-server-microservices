CREATE TABLE IF NOT EXISTS role_permission(
	role_id varchar(36) NOT NULL,
    permission_id varchar(36) NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permission(permission_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    PRIMARY KEY(role_id, permission_id)
);
package security.model;

import java.util.Set;

import lombok.Data;
import security.util.RoleEnum;

@Data
public class Role {


	private String roleId;

	private RoleEnum role;

	Set<Permission> permissions;

	public Role(RoleEnum role) {
		this.role = role;
	}
	
	public Role() {
		
	}
}
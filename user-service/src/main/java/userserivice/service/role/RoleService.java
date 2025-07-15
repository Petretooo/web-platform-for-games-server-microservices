package userserivice.service.role;

import java.util.Optional;

import userserivice.model.Role;
import userserivice.util.RoleEnum;

public interface RoleService {
	public Optional<Role> getRoleByName(RoleEnum roleName);

}

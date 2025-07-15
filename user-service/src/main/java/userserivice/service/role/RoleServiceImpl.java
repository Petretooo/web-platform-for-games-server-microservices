package userserivice.service.role;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import userserivice.model.Role;
import userserivice.repository.role.RoleRepository;
import userserivice.util.RoleEnum;

@Service
@AllArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Optional<Role> getRoleByName(RoleEnum roleName) {
		
		return roleRepository.findByRole(roleName);
	}

}

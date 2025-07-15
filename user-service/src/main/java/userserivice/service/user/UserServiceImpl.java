package userserivice.service.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import userserivice.model.Role;
import userserivice.model.User;
import userserivice.repository.user.UserRepository;
import userserivice.util.RoleEnum;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void remove(String uesrId) {
		userRepository.deleteById(uesrId);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User getUser(String uesrId) {
		return userRepository.findById(uesrId).get();
	}

	@Override
	public User getUserByName(String name) {
		return userRepository.findByUsername(name).get();
	}

	@Override
	public User createUser(User user) {

		Set<Role> roles = new HashSet<>();
		roles.add(new Role(RoleEnum.ROLE_USER));

		user.setRoles(roles);
		return userRepository.save(user);
	}

}

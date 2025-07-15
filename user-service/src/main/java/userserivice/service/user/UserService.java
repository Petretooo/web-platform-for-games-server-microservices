package userserivice.service.user;

import java.util.List;

import userserivice.model.User;

public interface UserService {
	
	public User createUser(User user);
	public void remove(String uesrId);
	public List<User> getAllUsers();
	public User getUser(String uesrId);
	public User getUserByName(String name);	
}

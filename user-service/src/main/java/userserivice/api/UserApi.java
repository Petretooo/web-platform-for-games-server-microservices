package userserivice.api;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import userserivice.model.User;
import userserivice.service.user.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserApi {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value="/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUser(@PathVariable("username") String username) {
		try{
			User user = userService.getUserByName(username);
			return ResponseEntity.ok(user);
		}catch(NoSuchElementException ex) {
			return ResponseEntity.ok(null);
		}
	}
	
	@GetMapping(value="/id/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
		try{
			User user = userService.getUser(id);
			return ResponseEntity.ok(user);
		}catch(NoSuchElementException ex) {
			return ResponseEntity.ok(null);
		}
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<User>> getAll() {
		List<User> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}
	
	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody User user) {
		
		User createdUser = userService.createUser(user);

		return ResponseEntity.created(URI.create("/api/v1/use")).body(createdUser);
	}
	
	@DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> deleteUser(@PathVariable("userId") String userId) {
		userService.remove(userId);
	    return ResponseEntity.noContent().build();
	}
}

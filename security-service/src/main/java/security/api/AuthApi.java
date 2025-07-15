package security.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import security.config.SessionSecurityContextRepository;
import security.dto.request.LoginForm;
import security.dto.request.SignUpForm;
import security.dto.response.JwtResponse;
import security.dto.response.ResponseMessage;
import security.model.Role;
import security.model.User;
import security.service.jwt.JwtProvider;
import security.util.RoleEnum;
import security.util.UserServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthApi {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtProvider jwtProvider;
	
    @Autowired
    private SessionSecurityContextRepository securityContextRepository;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginForm loginRequest, HttpServletRequest request, HttpServletResponse response) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
				
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);
		
		securityContextRepository.saveContext(context, request, response);
		
		String jwt = jwtProvider.generateJwtToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				
		User user = null;
		
		try {
			user = restTemplate.getForObject("http://user-service/api/v1/users/{username}", User.class, userDetails.getUsername());
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}

		return ResponseEntity.ok(new JwtResponse(jwt, user.getUserId(), userDetails.getUsername(), userDetails.getAuthorities()));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignUpForm signUpRequest) {
		
		User user = null;
		
		try {
			user = restTemplate.getForObject("http://user-service/api/v1/users/{username}", User.class, signUpRequest.getUsername());
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}
		
		if(user == null) {
			User newUser = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
					encoder.encode(signUpRequest.getPassword()));

			Set<Role> roles = new HashSet<>();
			
			roles.add(new Role(RoleEnum.ROLE_USER));
						
			try {
				restTemplate.postForObject("http://user-service/api/v1/users", newUser, User.class);
			} catch (Exception e) {
				throw new UserServiceException(e.getMessage());
			}
			
			return new ResponseEntity<>(
					new ResponseMessage("User " + signUpRequest.getUsername() + " is registered successfully!"),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
			HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/context/username")
	public ResponseEntity<String> getContextUser(HttpServletRequest request, HttpServletResponse response) {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			String username = ((UserDetails)principal).getUsername();
			System.out.println(username);
			return ResponseEntity.ok(username);
		} catch (ClassCastException e) {
			System.out.println("nullllllll");
			return ResponseEntity.ok(null);
		}
	}
}
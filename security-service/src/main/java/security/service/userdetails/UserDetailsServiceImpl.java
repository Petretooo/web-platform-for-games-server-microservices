package security.service.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import security.model.User;
import security.util.UserServiceException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	RestTemplate restTemplate;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = null;
		
		try {
			user = restTemplate.getForObject("http://user-service/api/v1/users/{username}", User.class, username);
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}
		
		if (user == null) {
			throw new UsernameNotFoundException("User Not Found with -> username or email : " + username);
		}

		return UserPrinciple.build(user);
	}
}
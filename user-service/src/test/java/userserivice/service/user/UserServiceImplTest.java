package userserivice.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import userserivice.model.User;
import userserivice.repository.user.UserRepository;

class UserServiceImplTest {

    private static final int SIZE_OF_RETRIEVED_LIST = 2;
	private static final String PASSWORD_JOHN = "123456789John";
	private static final String EMAIL_JOHN = "john@gmail.com";
	private static final String USERNAME_JOHN = "John";
	private static final String PASSWORD_PETER = "123456789";
	private static final String EMAIL_PETER = "peter@gmail.com";
	private static final String USERNAME_PETER = "Peter";
	private static final String USER_ID = "123";

	@Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRemoveUser() {
        String userId = USER_ID;
        
        userService.remove(userId);
        
        verify(userRepository).deleteById(userId);
    }

    @Test
    void shouldGetAllUsers() {
    	
        List<User> users = new ArrayList<>();
        users.add(new User(USERNAME_PETER, EMAIL_PETER, PASSWORD_PETER));
        users.add(new User(USERNAME_JOHN, EMAIL_JOHN, PASSWORD_JOHN));

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        verify(userRepository).findAll();
        assertNotNull(result);
        assertEquals(result.size(), SIZE_OF_RETRIEVED_LIST);
        assertEquals(users, result);
    }

    @Test
    void shouldGetUser() {
    	
        String userId = USER_ID;
        User user = new User(USERNAME_PETER, EMAIL_PETER, PASSWORD_PETER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUser(userId);

        verify(userRepository).findById(userId);
        assertEquals(user, result);
    }

    @Test
    void shouldGetUserByName() {
    	
        User user = new User(USERNAME_PETER, EMAIL_PETER, PASSWORD_PETER);

        when(userRepository.findByUsername(USERNAME_PETER)).thenReturn(Optional.of(user));

        User result = userService.getUserByName(USERNAME_PETER);

        verify(userRepository).findByUsername(USERNAME_PETER);
        assertEquals(user, result);
    }

    @Test
    void testCreateUser() {
    	
        User user = new User(USERNAME_PETER, EMAIL_PETER, PASSWORD_PETER);

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        verify(userRepository).save(user);
        assertEquals(user, result);
    }
}
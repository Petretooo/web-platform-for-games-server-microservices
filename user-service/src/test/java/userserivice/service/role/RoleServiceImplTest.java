package userserivice.service.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import userserivice.model.Role;
import userserivice.repository.role.RoleRepository;
import userserivice.util.RoleEnum;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetRoleByName() {
    	
        RoleEnum roleName = RoleEnum.ROLE_USER;
        Role role = new Role(roleName);

        when(roleRepository.findByRole(roleName)).thenReturn(Optional.of(role));

        Optional<Role> result = roleService.getRoleByName(roleName);
        Role retrievedRole = result.get();

        verify(roleRepository).findByRole(roleName);
        assertEquals(retrievedRole, role);
    }
}
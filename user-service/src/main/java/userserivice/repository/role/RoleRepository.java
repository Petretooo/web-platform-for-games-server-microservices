package userserivice.repository.role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import userserivice.model.Role;
import userserivice.util.RoleEnum;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(RoleEnum roleName);
}
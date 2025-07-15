package userserivice.model;

import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import userserivice.util.RoleEnum;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Role {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column
	private String roleId;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private RoleEnum role;

	@ManyToMany
	@JoinTable(name = "role_permission", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	Set<Permission> permissions;

	public Role(RoleEnum role) {
		this.role = role;
	}
}
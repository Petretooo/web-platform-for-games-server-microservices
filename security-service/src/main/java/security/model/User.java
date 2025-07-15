package security.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import security.dto.RankingDto;
import security.dto.UserStatsDto;

@Data
public class User {

	private String userId;

	private String username;

	private String email;

	private String password;

	private Set<Role> roles = new HashSet<>();

	private transient Set<UserStatsDto> userStats;

	private transient Set<RankingDto> rank;
	
	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
    @JsonCreator
    public User(@JsonProperty("userId") String userId,
                @JsonProperty("username") String username,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password,
                @JsonProperty("roles") Set<Role> roles,
                @JsonProperty("userStats") Set<UserStatsDto> userStats,
                @JsonProperty("rank") Set<RankingDto> rank) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.userStats = userStats;
        this.rank = rank;
    }

}

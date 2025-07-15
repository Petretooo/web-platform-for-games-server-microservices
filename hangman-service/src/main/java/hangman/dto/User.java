package hangman.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class User {

	private String userId;

	private String username;

	private String email;

	private String password;

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
                @JsonProperty("rank") Set<RankingDto> rank) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.rank = rank;
    }
}

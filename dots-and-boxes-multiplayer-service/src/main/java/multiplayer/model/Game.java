package multiplayer.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Game {

	@JsonProperty
	private String id;
	
	@JsonProperty
	private String winnerUsername;

	@JsonIgnore
	private Set<GameUser> users;

}

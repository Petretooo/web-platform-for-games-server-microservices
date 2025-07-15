package multiplayer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GameRoom {
	
	@JsonProperty
	private String gameRoomId;

	@JsonProperty
	private String gameRoomName;

	@JsonProperty
	private String gameTitle;

	@JsonProperty
	private boolean active;

	@JsonIgnore
	private Game game;

	@JsonProperty
	private String firstUser;

	@JsonProperty
	private String firstUserSymbol;

	@JsonProperty
	private String secondUser;

	@JsonProperty
	private String secondUserSymbol;
}

package multiplayer.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import multiplayer.model.HangmanGame;

@Setter
@Getter
@NoArgsConstructor
public class RoomUserDto {
	
	private String userId;
	private boolean userTurn;
	private HangmanGame gameInfo;
	private List<Character> unusedCharacters;
	
	@JsonCreator
	public RoomUserDto(@JsonProperty("userId") String userId,
			@JsonProperty("userTurn") boolean userTurn,
			@JsonProperty("gameInfo") HangmanGame gameInfo,
			@JsonProperty("unusedCharacters") List<Character> unusedCharacters) {
		
		this.userId = userId;
		this.userTurn = userTurn;
		this.gameInfo = gameInfo;
		this.unusedCharacters = unusedCharacters;
	}
	
}

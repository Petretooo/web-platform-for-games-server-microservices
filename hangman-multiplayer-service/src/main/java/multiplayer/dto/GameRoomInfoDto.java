package multiplayer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import multiplayer.util.GameStatus;

@Setter
@Getter
@NoArgsConstructor
public class GameRoomInfoDto {

	private String gameRoomId;
	private String gameRoomName;
	private String gameTitle;
	private String word;
	private RoomUserDto firstUser;
	private RoomUserDto secondUser;
	private String winnerId;
	private String loserId;
	private GameStatus gameStatus;

	@JsonCreator
	public GameRoomInfoDto(@JsonProperty("gameRoomId") String gameRoomId,
			@JsonProperty("gameRoomName") String gameRoomName, 
			@JsonProperty("gameTitle") String gameTitle,
			@JsonProperty("word") String word, 
			@JsonProperty("firstUser") RoomUserDto firstUser,
			@JsonProperty("secondUser") RoomUserDto secondUser, 
			@JsonProperty("winnerId") String winnerId,
			@JsonProperty("loserId") String loserId, 
			@JsonProperty("gameStatus") GameStatus gameStatus) {
		this.gameRoomId = gameRoomId;
		this.gameRoomName = gameRoomName;
		this.gameTitle = gameTitle;
		this.word = word;
		this.firstUser = firstUser;
		this.secondUser = secondUser;
		this.winnerId = winnerId;
		this.loserId = loserId;
		this.gameStatus = gameStatus;
	}
}

package hangman.dto;

import hangman.util.GameStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}

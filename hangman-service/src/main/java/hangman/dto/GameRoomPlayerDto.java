package hangman.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GameRoomPlayerDto {
	String gameRoomId;
	String userId;
}

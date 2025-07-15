package hangman.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GameRoomTryResponse {
	
	private GameRoomInfoDto infoDto;
	private GameRoomTryDto gameRoomTry;

}

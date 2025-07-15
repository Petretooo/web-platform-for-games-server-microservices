package hangman.service.multiplayer;

import hangman.dto.GameRoomInfoDto;
import hangman.dto.GameRoomResultsDto;
import hangman.dto.GameRoomTryDto;

public interface HangmanMultiplayerService {

	public GameRoomResultsDto checkGameRoom(GameRoomInfoDto infoDto, GameRoomTryDto message);

	public GameRoomInfoDto makeTry(String gameRoomId, GameRoomTryDto message);

}

package hangman.service.gameroom;

import java.util.List;

import hangman.dto.GameRoomInfoDto;
import hangman.dto.GameRoomPlayerDto;
import hangman.dto.GameRoomTryDto;
import hangman.model.HangmanGameRoom;

public interface HangmanGameRoomService {
	
	public List<HangmanGameRoom> getAllRooms();
	public HangmanGameRoom getGameRoom(String gameRoomId);
	public HangmanGameRoom createHangmanGameRoom(String gameRoomName);
	public void deleteGameRoom(String gameRoomId);
	public boolean addSecondPlayer(String gameRoomId, String secondPlayerId);
	public GameRoomInfoDto getGameRoomInfo(String gameRoomId);
	public GameRoomInfoDto makeTry(String gameRoomId, GameRoomTryDto message);
	public void joinGameRoom(GameRoomPlayerDto roomPlayerDto);
	public void leaveGameRoom(GameRoomPlayerDto roomPlayerDto);
	public GameRoomInfoDto mapGameRoomToDto(HangmanGameRoom room);
}

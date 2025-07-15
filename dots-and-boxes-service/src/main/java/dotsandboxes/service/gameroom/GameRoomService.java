package dotsandboxes.service.gameroom;

import java.util.List;

import dotsandboxes.dto.DotsAndBoxesPlayerDto;
import dotsandboxes.dto.GameDetailsDto;
import dotsandboxes.model.GameRoom;

public interface GameRoomService {
	
	public List<GameRoom> getAllRooms();
	public GameRoom getGameRoom(String gameRoomId);
	public GameRoom createTicTacToeGameRoom(String gameRoomName);
	public void deleteGameRoom(String gameRoomId);
	public void updateGameRoom(String roomId, String username);
	public void joinGameRoom(DotsAndBoxesPlayerDto roomPlayerDto);
	public void leaveGameRoom(DotsAndBoxesPlayerDto roomPlayerDto);
	public GameDetailsDto getGameDetails(GameRoom room);

}

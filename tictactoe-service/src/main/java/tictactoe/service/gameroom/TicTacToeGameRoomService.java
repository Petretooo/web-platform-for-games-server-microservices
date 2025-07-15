package tictactoe.service.gameroom;

import java.util.List;

import tictactoe.dto.TicTacToePlayerDto;
import tictactoe.model.TicTacToeGameRoom;

public interface TicTacToeGameRoomService {
	
	public List<TicTacToeGameRoom> getAllRooms();
	public TicTacToeGameRoom getGameRoom(String gameRoomId);
	public TicTacToeGameRoom createTicTacToeGameRoom(String gameRoomName);
	public void deleteGameRoom(String gameRoomId);
	public void updateGameRoom(String roomId, String username);
	public void joinGameRoom(TicTacToePlayerDto roomPlayerDto);
	public void leaveGameRoom(TicTacToePlayerDto roomPlayerDto);

}

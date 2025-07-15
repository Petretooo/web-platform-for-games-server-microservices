package tictactoe.service.multiplayer;

import tictactoe.dto.TicTacToeDto;
import tictactoe.dto.TicTacToeResultDto;
import tictactoe.model.TicTacToeGameRoom;

public interface TicTacToeMultiplayerService {

	public TicTacToeResultDto checkGame(TicTacToeGameRoom room, TicTacToeDto message);

}

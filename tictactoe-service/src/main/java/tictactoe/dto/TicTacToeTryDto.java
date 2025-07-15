package tictactoe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import tictactoe.model.TicTacToeGameRoom;

@Data
@NoArgsConstructor
public class TicTacToeTryDto {
	
	private TicTacToeGameRoom room;
	private TicTacToeDto message;

}

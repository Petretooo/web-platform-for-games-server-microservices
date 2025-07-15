package multiplayer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import multiplayer.model.TicTacToeGameRoom;

@Data
@NoArgsConstructor
public class TicTacToeTryDto {
	
	private TicTacToeGameRoom room;
	private TicTacToeDto message;
}

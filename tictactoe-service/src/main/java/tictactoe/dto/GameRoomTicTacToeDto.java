package tictactoe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameRoomTicTacToeDto {
	private String gameRoomName;
	private String firstUserId;
}

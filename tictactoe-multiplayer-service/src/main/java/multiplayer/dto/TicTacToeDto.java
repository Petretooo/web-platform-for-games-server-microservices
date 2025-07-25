package multiplayer.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import multiplayer.util.GameRoomStatus;

@Setter
@Getter
@NoArgsConstructor
public class TicTacToeDto {
	
	private String senderId;
	private String message;
	private List<String> squares;
	private GameRoomStatus status;
}

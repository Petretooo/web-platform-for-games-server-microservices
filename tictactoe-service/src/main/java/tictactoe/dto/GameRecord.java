package tictactoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tictactoe.utils.GameMode;
import tictactoe.utils.GameTitle;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRecord {
	
	private GameTitle gameTitle;
	private GameMode gameMode;
	private String userId;
	private String endpoint;
	private String status;
	
}

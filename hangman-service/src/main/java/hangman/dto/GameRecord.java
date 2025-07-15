package hangman.dto;

import hangman.util.GameMode;
import hangman.util.GameTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

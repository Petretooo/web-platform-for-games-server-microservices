package dotsandboxes.dto;

import dotsandboxes.utils.GameMode;
import dotsandboxes.utils.GameTitle;
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

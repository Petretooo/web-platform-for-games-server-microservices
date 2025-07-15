package hangman.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GameRoomResultsDto {
	
	private String userOutOfTriesId;
	private String winnerId;
	private String loserId;
	private boolean isDraw;
	
	public GameRoomResultsDto(boolean isDraw) {
		this.isDraw = isDraw;
	}
	
	public GameRoomResultsDto(String userOutOfTriesId) {
		this.userOutOfTriesId = userOutOfTriesId;
	}
	
	public GameRoomResultsDto(String winnerId, String loserId) {
		this.winnerId = winnerId;
		this.loserId = loserId;
		this.isDraw = false;
	}
}

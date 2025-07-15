package multiplayer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DotsAndBoxesResultDto {
	
	private String senderId;
	private String winnerId;
	private String loserId;
	private GameDetailsDto gameDetails;
	private boolean tryAgain;
	private boolean isDraw;

}

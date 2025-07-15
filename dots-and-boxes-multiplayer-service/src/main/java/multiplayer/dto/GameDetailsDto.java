package multiplayer.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDetailsDto {
	
	private String gameId;
	private String winnerUsername;
	private List<BoxDetailsDto> boxDetails;
	private List<EdgeDetailsDto> edges;
	private int gameSize;

}

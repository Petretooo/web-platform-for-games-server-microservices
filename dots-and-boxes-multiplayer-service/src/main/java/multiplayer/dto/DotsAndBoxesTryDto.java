package multiplayer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import multiplayer.model.GameRoom;

@Data
@NoArgsConstructor
public class DotsAndBoxesTryDto {
	
	private GameRoom room;
	private DotsAndBoxesDto message;

}

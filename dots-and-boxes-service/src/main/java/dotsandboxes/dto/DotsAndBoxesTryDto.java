package dotsandboxes.dto;

import dotsandboxes.model.GameRoom;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DotsAndBoxesTryDto {
	
	private GameRoom room;
	private DotsAndBoxesDto message;

}

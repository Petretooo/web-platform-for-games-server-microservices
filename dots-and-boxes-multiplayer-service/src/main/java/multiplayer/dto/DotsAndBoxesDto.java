package multiplayer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import multiplayer.util.GameRoomStatus;

@Setter
@Getter
@NoArgsConstructor
public class DotsAndBoxesDto {
	
	private String senderId;
	private String message;
	private GameRoomStatus status;
}

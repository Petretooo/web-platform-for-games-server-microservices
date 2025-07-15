package dotsandboxes.dto;

import dotsandboxes.utils.GameRoomStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DotsAndBoxesDto {
	
	private String senderId;
	private String message;
	private GameRoomStatus status;
}

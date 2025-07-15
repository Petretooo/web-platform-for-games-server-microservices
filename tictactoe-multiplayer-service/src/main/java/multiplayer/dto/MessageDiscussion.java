package multiplayer.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import multiplayer.util.GameRoomStatus;

@Getter
@Setter
public class MessageDiscussion {
	
	private GameRoomStatus status;
	private List<Message> messages;

}

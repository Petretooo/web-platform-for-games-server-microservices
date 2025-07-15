package chat.dto;

import java.util.List;

import chat.util.GameRoomStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDiscussion {
	
	private GameRoomStatus status;
	private List<Message> messages;

}

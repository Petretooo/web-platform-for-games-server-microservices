package chat.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LobbyDto {
	
	String senderUsername;
	String senderStatus;
	String firstUserUsername;
	String secondUserUsername;
	String firstUserStatus;
	String secondUserStatus;

}

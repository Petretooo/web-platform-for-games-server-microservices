package multiplayer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import multiplayer.dto.User;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class HangmanGameRoom {
	
	private String gameRoomId;
	
	private String gameRoomName;
	
	private String gameTitle;
	
	private boolean active;

	private String word;
	
	private HangmanGame firstUserGame;
	
	private HangmanGame secondUserGame;

	private String firstUserId;

	private String secondUserId;
	
	private transient User firstUser;
	
	private transient User secondUser;
}

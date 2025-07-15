package hangman.dto;

import java.util.List;

import hangman.model.HangmanGame;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomUserDto {
	
	private String userId;
	private boolean userTurn;
	private HangmanGame gameInfo;
	private List<Character> unusedCharacters;
}

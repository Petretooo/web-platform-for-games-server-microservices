package hangman.dto;

import java.util.List;
import java.util.stream.Collectors;

import hangman.model.HangmanGame;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GameInfoDto {
	
	private String id;
	private int numberTries;
	private String currentWord;
	private char[] hiddenWord;
	private List<String> characters;
	
	public GameInfoDto(HangmanGame game) {
		this.setId(game.getId());
		this.setNumberTries(game.getNumberTries());
		this.setCurrentWord(game.getCurrentWord());
		this.setHiddenWord(game.getHiddenWord());
		this.setCharacters(game.getCharacters().stream().map(e -> String.valueOf(e.getLetter())).collect(Collectors.toList()));
	}

}

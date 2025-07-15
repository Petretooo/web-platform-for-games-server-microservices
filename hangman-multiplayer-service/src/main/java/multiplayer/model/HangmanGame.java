package multiplayer.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class HangmanGame {

	@JsonProperty
	private String id;

	@JsonProperty
	private int numberTries;

	@JsonProperty
	private String currentWord;

	@JsonProperty
	private char[] hiddenWord;

	@JsonProperty
	private Set<Symbol> characters;

	@JsonProperty
	private Set<HangmanGameUser> users;

	@JsonProperty
	private HangmanGameStats gameStats;

	@JsonCreator
	public HangmanGame(@JsonProperty("id") String id, 
			@JsonProperty("numberTries") int numberTries,
			@JsonProperty("currentWord") String currentWord, 
			@JsonProperty("hiddenWord") char[] hiddenWord,
			@JsonProperty("Characters") Set<Symbol> characters, 
			@JsonProperty("users") Set<HangmanGameUser> users,
			@JsonProperty("gameStats") HangmanGameStats gameStats) {

		this.id = id;
		this.numberTries = numberTries;
		this.currentWord = currentWord;
		this.hiddenWord = hiddenWord;
		this.characters = characters;
		this.users = users;
		this.gameStats = gameStats;
	}
}

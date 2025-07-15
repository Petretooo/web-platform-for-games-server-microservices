package multiplayer.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HangmanGameStats {

	private String id;

	private boolean isWordFound;

	private int wrongTries;

	private LocalDate gameStart;

	private LocalDate gameEnd;

	private String gameResult;

	private String gameId;

	private HangmanGame game;
	
	@JsonCreator
	public HangmanGameStats(@JsonProperty("id") String id, 
			@JsonProperty("isWordFound") boolean isWordFound,
			@JsonProperty("wrongTries") int wrongTries, 
			@JsonProperty("gameStart") LocalDate gameStart,
			@JsonProperty("gameEnd") LocalDate gameEnd, 
			@JsonProperty("gameResult") String gameResult,
			@JsonProperty("gameId") String gameId,
			@JsonProperty("game") HangmanGame game) {

		this.id = id;
		this.isWordFound = isWordFound;
		this.wrongTries = wrongTries;
		this.gameStart = gameStart;
		this.gameEnd = gameEnd;
		this.gameResult = gameResult;
		this.gameId = gameId;
		this.game = game;
	}
	
}

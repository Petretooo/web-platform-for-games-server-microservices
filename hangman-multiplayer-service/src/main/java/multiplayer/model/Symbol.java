package multiplayer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Symbol {

	private String characterId;

	private char letter;

	@JsonCreator
	public Symbol(@JsonProperty("characterId") String characterId, 
			@JsonProperty("letter") char letter) {

		this.characterId = characterId;
		this.letter = letter;
	}
}

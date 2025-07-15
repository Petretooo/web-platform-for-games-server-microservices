package multiplayer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class HangmanGameUser {

	private HangmanGameUserId hangmanGameUserId;

	@JsonCreator
	public HangmanGameUser(@JsonProperty("hangmanGameUserId") HangmanGameUserId hangmanGameUserId) {
		this.hangmanGameUserId = hangmanGameUserId;
	}

}

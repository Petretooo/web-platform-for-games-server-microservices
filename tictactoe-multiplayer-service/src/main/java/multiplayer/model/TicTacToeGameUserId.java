package multiplayer.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TicTacToeGameUserId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String gameId;

	private String userId;

	@JsonCreator
	public TicTacToeGameUserId(@JsonProperty("gameId") String gameId, @JsonProperty("userId") String userId) {
		this.gameId = gameId;
		this.userId = userId;
	}

}

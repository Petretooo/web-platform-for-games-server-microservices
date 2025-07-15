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
public class TicTacToeGame {

	private String id;
	
	private String winnerUsername;

	private transient Set<TicTacToeGameUser> users;
	
    @JsonCreator
    public TicTacToeGame(@JsonProperty("id") String id,
                @JsonProperty("winnerUsername") String winnerUsername,
                @JsonProperty("users") Set<TicTacToeGameUser> users) {
    	
        this.id = id;
        this.winnerUsername = winnerUsername;
        this.users = users;
    }

}

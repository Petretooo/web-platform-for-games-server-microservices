package multiplayer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicTacToeGameRoom {
	

	private String gameRoomId;
	
	private String gameRoomName;
	
	private String gameTitle;
	
	private boolean active;
	
	private TicTacToeGame game;

	private String firstUser;
	
	private String firstUserSymbol;
	
	private String secondUser;
	
	private String secondUserSymbol;
	
    @JsonCreator
    public TicTacToeGameRoom(@JsonProperty("gameRoomId") String gameRoomId,
                @JsonProperty("gameRoomName") String gameRoomName,
                @JsonProperty("gameTitle") String gameTitle,
                @JsonProperty("active") boolean active,
                @JsonProperty("game") TicTacToeGame game,
                @JsonProperty("firstUser") String firstUser,
                @JsonProperty("firstUserSymbol") String firstUserSymbol,
                @JsonProperty("secondUser") String secondUser,
                @JsonProperty("secondUserSymbol") String secondUserSymbol) {
        this.gameRoomId = gameRoomId;
        this.gameRoomName = gameRoomName;
        this.gameTitle = gameTitle;
        this.active = active;
        this.game = game;
        this.firstUser = firstUser;
        this.firstUserSymbol = firstUserSymbol;
        this.secondUser = secondUser;
        this.secondUserSymbol = secondUserSymbol;
    }

}

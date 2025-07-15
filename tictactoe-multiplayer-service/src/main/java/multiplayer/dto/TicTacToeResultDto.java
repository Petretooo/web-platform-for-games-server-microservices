package multiplayer.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TicTacToeResultDto {
	
	private String senderId;
	private String winnerId;
	private String loserId;
	private List<String> squares;
	private boolean tryAgain;
	private boolean isDraw;
	
    @JsonCreator
    public TicTacToeResultDto(@JsonProperty("senderId") String senderId,
                @JsonProperty("winnerId") String winnerId,
                @JsonProperty("loserId") String loserId,
                @JsonProperty("squares") List<String> squares,
                @JsonProperty("tryAgain") boolean tryAgain,
                @JsonProperty("isDraw") boolean isDraw) {
        this.senderId = senderId;
        this.winnerId = winnerId;
        this.loserId = loserId;
        this.squares = squares;
        this.tryAgain = tryAgain;
        this.isDraw = isDraw;
    }

}

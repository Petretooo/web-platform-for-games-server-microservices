package tictactoe.dto;

import java.util.List;

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

}

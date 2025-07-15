package hangman.dto;

import hangman.util.GameRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GameRoomTryDto {
	private String userId;
	private String senderName;
	private String message;
	private String date;
	private GameRoomStatus status;
}

package multiplayer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import multiplayer.util.GameRoomStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Message {
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private GameRoomStatus status;
}

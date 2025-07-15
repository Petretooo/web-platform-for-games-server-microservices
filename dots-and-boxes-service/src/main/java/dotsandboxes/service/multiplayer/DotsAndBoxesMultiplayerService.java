package dotsandboxes.service.multiplayer;

import dotsandboxes.dto.DotsAndBoxesDto;
import dotsandboxes.dto.DotsAndBoxesResultDto;
import dotsandboxes.model.GameRoom;

public interface DotsAndBoxesMultiplayerService {

	DotsAndBoxesResultDto checkGame(GameRoom room, DotsAndBoxesDto message);

}

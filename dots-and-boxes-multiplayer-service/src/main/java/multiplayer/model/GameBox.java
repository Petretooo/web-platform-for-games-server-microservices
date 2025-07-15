package multiplayer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GameBox {
	
	@JsonProperty
	private GameBoxId gameBoxId;
	
	@JsonProperty
	Game gameId;
	
	@JsonProperty
	Box boxId;
	
	@JsonProperty
	boolean isBoxAvailable;
}

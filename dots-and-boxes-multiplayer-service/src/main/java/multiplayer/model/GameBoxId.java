package multiplayer.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class GameBoxId implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8211037076506847219L;

	private String gameId;
	private String boxId;

}

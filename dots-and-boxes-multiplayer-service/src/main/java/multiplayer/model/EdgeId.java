package multiplayer.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class EdgeId implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2812504767759320665L;

	private String boxId;
	private String fromDotId;
	private String toDotId;

}

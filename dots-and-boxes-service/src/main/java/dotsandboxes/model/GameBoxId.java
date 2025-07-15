package dotsandboxes.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class GameBoxId implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8211037076506847219L;

	private String gameId;
	private String boxId;

}

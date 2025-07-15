package hangman.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Embeddable
@Data
public class HangmanGameUserId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "game_id")
	@JsonProperty
	private String gameId;
	
	private String userId;

}

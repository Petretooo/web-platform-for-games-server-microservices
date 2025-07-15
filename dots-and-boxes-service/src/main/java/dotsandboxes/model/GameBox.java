package dotsandboxes.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameBox {
	
	@EmbeddedId
	private GameBoxId gameBoxId;
	
	@ManyToOne
	@MapsId("gameId")
	@JoinColumn(name="game_id")
	@JsonProperty
	Game game;
	
	@ManyToOne
	@MapsId("boxId")
	@JoinColumn(name="box_id")
	@JsonProperty
	Box box;
	
	@Column
	@JsonProperty
	boolean isBoxAvailable;
	
	@Column
	@JsonProperty
	private String userId;
}

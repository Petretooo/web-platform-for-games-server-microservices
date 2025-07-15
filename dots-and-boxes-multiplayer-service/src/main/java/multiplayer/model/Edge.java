package multiplayer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Edge {
	
	@JsonProperty
	private EdgeId edgeId;
	
	@JsonProperty
	private Box box;
	
	@JsonProperty
	private Dot fromDot;
	
	@JsonProperty
	private Dot toDot;
	
	@JsonProperty
	private boolean isEdgeAvailable;
}

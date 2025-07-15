package multiplayer.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Box {
	
	@JsonProperty
	private String boxId;
	
	@JsonProperty
	private String boxName;
	
	@JsonProperty
	private List<Edge> edges;

}

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
public class Dot {
	
	@JsonProperty
	private String dotId;
	
	@JsonProperty
	private String xAxis;
	
	@JsonProperty
	private String yAxis;
}

package aggregation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import aggregation.util.GameMode;
import aggregation.util.GameTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRecord {
	
	@JsonProperty
	private GameTitle gameTitle;
	@JsonProperty
	private GameMode gameMode;
	@JsonProperty
	private String userId;
	@JsonProperty
	private String endpoint;
	@JsonProperty
	private String status;
	
}

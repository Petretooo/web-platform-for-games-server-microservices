package aggregation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsDto {

	private String gameTitle;
	private String gameMode;
	private int popularityCounter;
}

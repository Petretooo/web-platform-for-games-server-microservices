package multiplayer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoxDetailsDto {
	
	private String boxName;
	private boolean isBoxAvailable;
	private String boxColor;
	private String userId;
	private int orderBox;
}

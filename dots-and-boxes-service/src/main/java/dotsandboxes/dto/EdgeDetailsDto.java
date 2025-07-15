package dotsandboxes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EdgeDetailsDto {
	
	private String fromDot;
	private String toDot;
	private boolean isEdgeAvailable;
	private String edgeColor;
	private String userId;
	private int orderEdge;
}

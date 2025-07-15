package multiplayer.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserStats {

	private String id;

	private int score;

	private String word;

	private String idUserStats;

	private String userId;
}

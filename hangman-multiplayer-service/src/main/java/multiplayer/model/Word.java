package multiplayer.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Word {

	private String wordId;

	private String wordName;

	private int levelDifficulty;
}

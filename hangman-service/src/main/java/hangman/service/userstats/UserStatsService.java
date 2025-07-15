package hangman.service.userstats;

import hangman.dto.User;
import hangman.model.HangmanGame;
import hangman.model.UserStats;

public interface UserStatsService {
	
	public UserStats get(String gameStatsId);
	public UserStats save(User user, String word);
	public UserStats update(HangmanGame game);
	public void remove(String gameStatsId);

}

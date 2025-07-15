package hangman.service.gamestats;

import hangman.model.HangmanGame;
import hangman.model.HangmanGameStats;
import hangman.util.GameStatus;

public interface GameStatsService {
	
	public HangmanGameStats saveGameStats(HangmanGame game);
	public void deleteGameStats(String gameStatsId);
	public HangmanGameStats updateGameStats(String gameId);
	HangmanGameStats updateGameStatsBuStatus(String gameId, GameStatus status);
	HangmanGameStats getGameStats(String gameId);
}

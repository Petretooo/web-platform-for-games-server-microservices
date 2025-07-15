package hangman.repository.game;

import java.util.List;

import hangman.model.HangmanGame;

public interface HangmanGameDao {
	public HangmanGame get(String id);
	public void remove(String gameId);
	public List<HangmanGame> getAll();
	public void save(HangmanGame entity);
	public void updateGame(HangmanGame game, String gameId);
	public List<HangmanGame> getRunningGames();
}

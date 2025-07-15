package hangman.service.game;

import java.util.List;

import hangman.dto.User;
import hangman.model.HangmanGame;

public interface HangmanGameService {

  public HangmanGame createGame(User user);
  
  public HangmanGame getGame(String id);
  
  public void deleteGame(String id);

  public HangmanGame enterCharacter(String id, String letter);

  public String resultWord(String id);

  public String getUsedLetters(String id);
  
  public List<HangmanGame> getRunningGame();	

  public List<String> getusedLettersArray(String id);

  public HangmanGame createGameWithWord(User user, String word);

  public List<Character> getUnusedCharacters(String gameId);

}

package hangman.service.alphabet;

import java.util.List;
import java.util.Map;
import java.util.Set;

import hangman.model.Symbol;

public interface AlphabetService {
  
  public void setGameAlphabet(String gameId);
  
  public Map<Character, Boolean> getCurrentGameAlphabet(String gameId);
  
  public void saveCharacter(String gameId, Symbol character);
  
  public List<Character> getUnusedCharacters(String gameId);

  public List<Character> getUnusedCharacters(String gameId, Set<Symbol> usedLetters);

  void saveCharacterWithUsed(String gameId, Symbol character, Set<Symbol> usedLetters);
}
	
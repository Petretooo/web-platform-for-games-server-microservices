package hangman.service.game;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hangman.dto.User;
import hangman.model.HangmanGame;
import hangman.model.HangmanGameUser;
import hangman.model.HangmanGameUserId;
import hangman.model.Symbol;
import hangman.repository.game.GameRepository;
import hangman.repository.game.HangmanGameDao;
import hangman.service.alphabet.AlphabetService;
import hangman.service.word.WordService;
import hangman.util.CharacterNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class HangmanGameServiceImpl implements HangmanGameService {

	@Autowired
	private WordService wordService;
	
	@Autowired
	private AlphabetService alphabetService;

	@Autowired
	private HangmanGameDao gameDao;
	
	@Autowired
	private GameRepository gameRepository;
	
	@Override
	public HangmanGame enterCharacter(String id, String letter) {

		HangmanGame game = gameDao.get(id);
		
		if (letter.charAt(0) >= 'A' && letter.charAt(0) <= 'Z') {
			Symbol character = new Symbol();
			character.setLetter(letter.charAt(0));
			game.getCharacters().add(character);
			alphabetService.saveCharacterWithUsed(game.getId(), character, game.getCharacters());

			checkCharacterInWord(game, letter);
		}
		
		return game;
	}

	private void checkCharacterInWord(HangmanGame game, String letter) {
		
		if (game.getCurrentWord().contains(letter)) {
			int index = game.getCurrentWord().indexOf(letter);
			char[] hidden = game.getHiddenWord();
			checkCharacterInRange(index, hidden, game, letter);
			game.setHiddenWord(hidden);
		} else {
			int numberRemaining = game.getNumberTries();
			game.setNumberTries(--numberRemaining);
		}
	}

	private void checkCharacterInRange(int index, char[] hidden, HangmanGame game, String letter) {

		while (index >= 0) {
			try {
				hidden[index] = letter.charAt(0);
			} catch (Exception ex) {
				throw new CharacterNotFoundException("Character not found");
			}
			index = game.getCurrentWord().indexOf(letter, index + 1);
		}
	}

	@Override
	public HangmanGame createGame(User user) {
		
		HangmanGame game = permitGame();
				
		HangmanGameUserId id = new HangmanGameUserId();
		id.setGameId(game.getId());
		id.setUserId(user.getUserId());

		game.getUsers().add(new HangmanGameUser(id));

		alphabetService.setGameAlphabet(game.getId());

		String firstLetter = game.getCurrentWord().substring(0, 1);
		String lastLetter = game.getCurrentWord().substring(game.getCurrentWord().length() - 1);
		enterCharacter(game.getId(), firstLetter);
		enterCharacter(game.getId(), lastLetter);

		return game;
	}

	private HangmanGame permitGame() {
		HangmanGame game = new HangmanGame();
		game.setCharacters(new HashSet<Symbol>());
		game.setNumberTries(5);
		game.setCurrentWord(wordService.randomWordGenerator().getWordName());

		char[] hidden = new char[game.getCurrentWord().length()];
		for (int i = 0; i < hidden.length; i++) {
			hidden[i] = '_';
		}
		game.setHiddenWord(hidden);
		game.setUsers(new HashSet<HangmanGameUser>());
		return gameRepository.save(game);
	}
	
	@Override
	public HangmanGame createGameWithWord(User user, String word) {
		
		HangmanGame game = persistGame(word);
		
		HangmanGameUserId id = new HangmanGameUserId();
		id.setGameId(game.getId());
		id.setUserId(user.getUserId());

		game.getUsers().add(new HangmanGameUser(id));

		alphabetService.setGameAlphabet(game.getId());

		String firstLetter = game.getCurrentWord().substring(0, 1);
		String lastLetter = game.getCurrentWord().substring(game.getCurrentWord().length() - 1);
		enterCharacter(game.getId(), firstLetter);
		enterCharacter(game.getId(), lastLetter);

		return game;
	}

	private HangmanGame persistGame(String word) {
		HangmanGame game = new HangmanGame();
		game.setCharacters(new HashSet<Symbol>());
		game.setNumberTries(5);
		game.setCurrentWord(word);

		char[] hidden = new char[game.getCurrentWord().length()];
		for (int i = 0; i < hidden.length; i++) {
			hidden[i] = '_';
		}
		game.setHiddenWord(hidden);
		game.setUsers(new HashSet<HangmanGameUser>());
		return gameRepository.save(game);
	}	

	@Override
	public String resultWord(String id) {
		
		HangmanGame game = gameDao.get(id);
		if (game.getNumberTries() <= 0) {
			return "gameover";
		}
		if (game.getCurrentWord().contentEquals(new String(game.getHiddenWord()))) {
			return "win";
		}
		return null;
	}

	@Override
	public String getUsedLetters(String id) {
		
		HangmanGame game = gameDao.get(id);
		String words = "";
		for (Symbol letter : game.getCharacters()) {
			words += letter.getLetter() + ", ";
		}
		return words;
	}
	
	public List<String> getusedLettersArray(String id) {
		
		HangmanGame game = gameDao.get(id);
		
		return game.getCharacters().stream()
				.filter(distinctByKey(Symbol::getLetter))
				.map(e -> String.valueOf(e.getLetter()))
				.collect(Collectors.toList());
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	@Override
	public List<Character> getUnusedCharacters(String gameId){
		
		HangmanGame game = this.gameDao.get(gameId);
		
		return this.alphabetService.getUnusedCharacters(gameId, game.getCharacters());
		
	}

	@Override
	public HangmanGame getGame(String id) {
		return gameDao.get(id);
	}

	@Override
	public void deleteGame(String id) {
		gameDao.remove(id);
	}

	@Override
	public List<HangmanGame> getRunningGame() {
		List<HangmanGame> games = gameDao.getRunningGames();
		return games;
	}

}

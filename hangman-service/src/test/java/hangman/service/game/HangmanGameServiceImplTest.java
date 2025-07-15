package hangman.service.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hangman.model.HangmanGame;
import hangman.model.Symbol;
import hangman.repository.game.GameRepository;
import hangman.repository.game.HangmanGameDao;
import hangman.service.alphabet.AlphabetService;
import hangman.service.word.WordService;

class HangmanGameServiceImplTest {
	
    private static final String FIRST_USER_ID = "firstUserId";
    private static final String FIRST_USER = "firstUser";
    private static final String FIRST_USER_EMAIL = "firstUserEmail";
    private static final String FIRST_USER_PASSWORD = "firstUserPassword";
    
    private static final String SECOND_USER_ID = "secondUserId";
    private static final String SECOND_USER = "secondUser";
    private static final String SECOND_USER_EMAIL = "secondUserEmail";
    private static final String SECOND_USER_PASSWORD = "secondUserPassword";

    @Mock
    private WordService wordService;

    @Mock
    private AlphabetService alphabetService;

    @Mock
    private HangmanGameDao gameDao;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private HangmanGameServiceImpl gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldEnterCharacterValidCharacter() {
    	
        String gameId = "gameId";
        String letter = "P";

        HangmanGame game = new HangmanGame();
        game.setId(gameId);
        game.setCharacters(new HashSet<>());
        game.setCurrentWord("APPLE");
        game.setHiddenWord("_____".toCharArray());
        game.setNumberTries(5);

        when(gameDao.get(gameId)).thenReturn(game);

        HangmanGame result = gameService.enterCharacter(gameId, letter);

        assertNotNull(result);
        assertEquals("_PP__", new String(game.getHiddenWord()));
    }

    @Test
    void shouldEnterCharacterInvalidCharacter() {
    	
        String gameId = "gameId";
        String letter = "Q";

        HangmanGame game = new HangmanGame();
        game.setId(gameId);
        game.setCharacters(new HashSet<>());
        game.setCurrentWord("APPLE");
        game.setHiddenWord("_____".toCharArray());
        game.setNumberTries(5);

        when(gameDao.get(gameId)).thenReturn(game);

        HangmanGame result = gameService.enterCharacter(gameId, letter);

        assertNotNull(result);
        assertEquals("_____", new String(game.getHiddenWord()));
    }

    @Test
    void shouldResultGameover() {
    	
        String gameId = "gameId";

        HangmanGame game = new HangmanGame();
        game.setId(gameId);
        game.setCurrentWord("APPLE");
        game.setHiddenWord("AP___".toCharArray());

        when(gameDao.get(gameId)).thenReturn(game);

        String result = gameService.resultWord(gameId);

        assertEquals(result, "gameover");
    }

    @Test
    void shouldGetUsedLetters() {
    	
        String gameId = "gameId";

        Symbol symbolA = new Symbol();
        symbolA.setLetter('A');
        Symbol symbolB = new Symbol();
        symbolB.setLetter('B');
        Symbol symbolC = new Symbol();
        symbolC.setLetter('C');
        
        HangmanGame game = new HangmanGame();
        game.setId(gameId);
        game.setCharacters(new HashSet<>());
        game.getCharacters().add(symbolA);
        game.getCharacters().add(symbolB);
        game.getCharacters().add(symbolC);

        when(gameDao.get(gameId)).thenReturn(game);

        String result = gameService.getUsedLetters(gameId);

        assertEquals("C, B, A, ", result);
    }

    @Test
    void shouldGetusedLettersArray() {
    	
        String gameId = "gameId";
        
        Symbol symbolA = new Symbol();
        symbolA.setLetter('A');
        Symbol symbolB = new Symbol();
        symbolB.setLetter('B');
        Symbol symbolC = new Symbol();
        symbolC.setLetter('C');

        HangmanGame game = new HangmanGame();
        game.setId(gameId);
        game.setCharacters(new HashSet<>());
        game.getCharacters().add(symbolA);
        game.getCharacters().add(symbolB);
        game.getCharacters().add(symbolC);

        when(gameDao.get(gameId)).thenReturn(game);

        List<String> result = gameService.getusedLettersArray(gameId);

        assertEquals(3, result.size());
        assertTrue(result.contains("A"));
        assertTrue(result.contains("B"));
        assertTrue(result.contains("C"));
    }

    @Test
    void shouldGetUnusedCharacters() {
    	
        String gameId = "gameId";
        
        Symbol symbolA = new Symbol();
        symbolA.setLetter('A');
        Symbol symbolB = new Symbol();
        symbolB.setLetter('B');
        Symbol symbolC = new Symbol();
        symbolC.setLetter('C');

        HangmanGame game = new HangmanGame();
        game.setId(gameId);
        game.setCharacters(new HashSet<>());
        game.getCharacters().add(symbolA);
        game.getCharacters().add(symbolB);
        game.getCharacters().add(symbolC);

        when(gameDao.get(gameId)).thenReturn(game);
        when(alphabetService.getUnusedCharacters(eq(gameId), anySet())).thenReturn(List.of('D', 'E', 'F'));

        List<Character> result = gameService.getUnusedCharacters(gameId);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void shouldGetGame() {
    	
        String gameId = "gameId";

        HangmanGame game = new HangmanGame();
        game.setId(gameId);

        when(gameDao.get(gameId)).thenReturn(game);

        HangmanGame result = gameService.getGame(gameId);

        assertNotNull(result);
        assertEquals(game, result);
    }

    @Test
    void shouldDeleteGame() {
    	
        String gameId = "gameId";

        gameService.deleteGame(gameId);

        verify(gameDao).remove(gameId);
    }

    @Test
    void shouldGetRunningGame() {
    	
        List<HangmanGame> games = new ArrayList<>();
        games.add(new HangmanGame());
        games.add(new HangmanGame());

        when(gameDao.getRunningGames()).thenReturn(games);

        List<HangmanGame> result = gameService.getRunningGame();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(games));
    }
}
package hangman.service.gamestats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hangman.model.HangmanGame;
import hangman.model.HangmanGameStats;
import hangman.repository.gamestats.GameStatsDao;
import hangman.service.game.HangmanGameService;
import hangman.util.GameStatus;

class GameStatsServiceImplTest {
	
    @Mock
    private GameStatsDao gameStatsDao;

    @Mock
    private HangmanGameService gameService;

    @InjectMocks
    private GameStatsServiceImpl gameStatsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetGameStats() {

        String gameId = "gameId";
        HangmanGameStats expectedStats = new HangmanGameStats();

        when(gameStatsDao.findByGameId(gameId)).thenReturn(expectedStats);

        HangmanGameStats result = gameStatsService.getGameStats(gameId);

        assertNotNull(result);
        assertEquals(expectedStats, result);
    }

    @Test
    void shouldSaveGameStats() {

        HangmanGame game = new HangmanGame();
        HangmanGameStats expectedStats = new HangmanGameStats();
        expectedStats.setGameStart(LocalDate.now());
        expectedStats.setGame(game);

        HangmanGameStats result = gameStatsService.saveGameStats(game);

        verify(gameStatsDao, times(1)).save(expectedStats);

        assertNotNull(result);
        assertEquals(expectedStats, result);
    }

    @Test
    void shouldDeleteGameStats() {

        String gameStatsId = "gameStatsId";
        HangmanGameStats stat = new HangmanGameStats();
        when(gameStatsDao.getOne(gameStatsId)).thenReturn(stat);

        gameStatsService.deleteGameStats(gameStatsId);

        verify(gameStatsDao, times(1)).delete(stat);
    }

    @Test
    void shouldUpdateGameStats() {

        String gameId = "gameId";
        HangmanGameStats gameStat = new HangmanGameStats();
        HangmanGame game = new HangmanGame();
        game.setNumberTries(3);

        when(gameService.resultWord(gameId)).thenReturn("win");
        when(gameStatsDao.findByGameId(gameId)).thenReturn(gameStat);
        when(gameService.getGame(gameId)).thenReturn(game);

        HangmanGameStats result = gameStatsService.updateGameStats(gameId);

        assertNotNull(result);
        assertEquals(LocalDate.now(), result.getGameEnd());
        assertEquals(2, result.getWrongTries());
        assertEquals(true, result.isWordFound());
    }

    @Test
    void shouldUpdateGameStatsByStatus() {
    	
        String gameId = "gameId";
        GameStatus status = GameStatus.START;
        HangmanGameStats gameStat = new HangmanGameStats();
        HangmanGame game = new HangmanGame();
        game.setNumberTries(2);

        when(gameStatsDao.findByGameId(gameId)).thenReturn(gameStat);
        when(gameService.getGame(gameId)).thenReturn(game);

        HangmanGameStats result = gameStatsService.updateGameStatsBuStatus(gameId, status);

        assertNotNull(result);
        assertEquals(LocalDate.now(), result.getGameEnd());
        assertEquals(3, result.getWrongTries());
        assertEquals(false, result.isWordFound());
        assertEquals(GameStatus.START.name(), result.getGameResult());
    }
}
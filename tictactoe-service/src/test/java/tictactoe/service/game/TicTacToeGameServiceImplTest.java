package tictactoe.service.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tictactoe.model.TicTacToeGame;
import tictactoe.repository.game.TicTacToeGameRepository;

class TicTacToeGameServiceImplTest {

    private static final String WINNER_OF_GAME = "Peter";
	private static final String GAME_TEST_ID = "gameTestId";

	@Mock
    private TicTacToeGameRepository gameRepository;

    @InjectMocks
    private TicTacToeGameServiceImpl gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateGame() {
    	
        TicTacToeGame game = new TicTacToeGame();
        game.setId(GAME_TEST_ID);
        game.setWinnerUsername(WINNER_OF_GAME);
        
        when(gameRepository.save(game)).thenReturn(game);

        TicTacToeGame result = gameService.createGame();

        verify(gameRepository).save(game);
        assertEquals(game, result);
    }

    @SuppressWarnings("deprecation")
	@Test
    void shouldGetGame() {

    	TicTacToeGame game = new TicTacToeGame();

        when(gameRepository.getById(GAME_TEST_ID)).thenReturn(game);

        TicTacToeGame result = gameService.getGame(GAME_TEST_ID);

        verify(gameRepository).getById(GAME_TEST_ID);
        assertEquals(game, result);
    }

    @Test
    void shouldDeleteGame() {
    	
        gameService.deleteGame(GAME_TEST_ID);

        verify(gameRepository).deleteById(GAME_TEST_ID);
    }

    @Test
    void shouldUpdateGameWinner() {

        TicTacToeGame game = new TicTacToeGame();

        when(gameRepository.getById(GAME_TEST_ID)).thenReturn(game);

        gameService.updateGameWinner(GAME_TEST_ID, WINNER_OF_GAME);

        verify(gameRepository).getById(GAME_TEST_ID);
        assertEquals(WINNER_OF_GAME, game.getWinnerUsername());
    }

    @Test
    void shouldGetAllGames() {
    	
        List<TicTacToeGame> games = new ArrayList<>();
        games.add(new TicTacToeGame());
        games.add(new TicTacToeGame());

        when(gameRepository.findAll()).thenReturn(games);

        List<TicTacToeGame> result = gameService.getAllGames();

        verify(gameRepository).findAll();
        assertEquals(games, result);
    }
}
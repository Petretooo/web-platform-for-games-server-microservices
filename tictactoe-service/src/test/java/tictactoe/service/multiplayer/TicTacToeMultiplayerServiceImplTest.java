package tictactoe.service.multiplayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import tictactoe.dto.TicTacToeDto;
import tictactoe.dto.TicTacToeResultDto;
import tictactoe.dto.User;
import tictactoe.model.TicTacToeGame;
import tictactoe.model.TicTacToeGameRoom;
import tictactoe.service.gameroom.TicTacToeGameRoomService;
import tictactoe.utils.GameTitle;

class TicTacToeMultiplayerServiceImplTest {

	private static final String SECOND_PLAYER_SYMBOL_O = "O";
	private static final String FIRST_PLAYER_SYMBOL_X = "X";
	private static final String POSITION_SEVEN = "7";
	private static final String FIRST_USER_PASSWORD = "123456789";
	private static final String FIRST_USER_EMAIL = "first.user@gmail.com";
	private static final String SECOND_USER_ID = "second-user-id";
	private static final String FIRST_USER_ID = "first-user-id";
	private static final String SECOND_USER_SYMBOL_O = SECOND_PLAYER_SYMBOL_O;
	private static final String ROOM_NAME = "room-name";
	private static final String TIC_TAC_TOE_GAME_ROOM_ONE_ID = "tic-tac-toe-game-room-one";
	private static final String FIRST_USER_SYMBOL_X = FIRST_PLAYER_SYMBOL_X;
	private static final String FIRST_USER = "Peter";
	private static final String TIC_TAC_TOE_GAME__FIRST_ID = "tic-tac-toe-first-game-id";

	@Mock
	private TicTacToeGameRoomService gameRoomService;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private TicTacToeMultiplayerServiceImpl ticTacToeMultiplayerService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void shouldCheckGame() {

		User firstUser = new User(FIRST_USER_ID, FIRST_USER, FIRST_USER_EMAIL, FIRST_USER_PASSWORD);

		TicTacToeGame firstGame = new TicTacToeGame();
		firstGame.setId(TIC_TAC_TOE_GAME__FIRST_ID);

		TicTacToeGameRoom gameRoomOne = new TicTacToeGameRoom();
		gameRoomOne.setActive(true);
		gameRoomOne.setFirstUser(FIRST_USER_ID);
		gameRoomOne.setFirstUserSymbol(FIRST_USER_SYMBOL_X);
		gameRoomOne.setGame(firstGame);
		gameRoomOne.setGameRoomId(TIC_TAC_TOE_GAME_ROOM_ONE_ID);
		gameRoomOne.setGameRoomName(ROOM_NAME);
		gameRoomOne.setGameTitle(GameTitle.TICTACTOE.name());
		gameRoomOne.setSecondUser(SECOND_USER_ID);
		gameRoomOne.setSecondUserSymbol(SECOND_USER_SYMBOL_O);

		List<String> board = Arrays.asList(
				FIRST_PLAYER_SYMBOL_X, 
				SECOND_PLAYER_SYMBOL_O, 
				FIRST_PLAYER_SYMBOL_X, 
				SECOND_PLAYER_SYMBOL_O, 
				null,
				null, 
				null, 
				null, 
				null);

		TicTacToeDto tacToeDto = new TicTacToeDto();
		tacToeDto.setMessage(POSITION_SEVEN);
		tacToeDto.setSenderId(FIRST_USER_ID);
		tacToeDto.setSquares(board);

		TicTacToeResultDto ticTacToeResultDto = new TicTacToeResultDto();
		ticTacToeResultDto.setSenderId(FIRST_USER_ID);
		ticTacToeResultDto.setSquares(board);

        when(restTemplate.getForObject(anyString(), eq(User.class), eq("senderId"))).thenReturn(firstUser);

		TicTacToeResultDto result = ticTacToeMultiplayerService.checkGame(gameRoomOne, tacToeDto);

		List<String> checkedSquare = result.getSquares();
		
		assertEquals(checkedSquare.get(7), FIRST_PLAYER_SYMBOL_X);
	}

	@Test
	void shouldCheckWinner() {

		TicTacToeResultDto tictactoe = new TicTacToeResultDto();

		tictactoe.setSquares(Arrays.asList(
				FIRST_PLAYER_SYMBOL_X, 
				FIRST_PLAYER_SYMBOL_X, 
				FIRST_PLAYER_SYMBOL_X, 
				SECOND_PLAYER_SYMBOL_O, 
				SECOND_PLAYER_SYMBOL_O, 
				null, 
				null, 
				null, 
				null));

		TicTacToeResultDto result = ticTacToeMultiplayerService.checkWinner(tictactoe, FIRST_USER_ID, SECOND_USER_ID);

		assertEquals(FIRST_USER_ID, result.getWinnerId());
		assertEquals(SECOND_USER_ID, result.getLoserId());
	}

	@Test
	void shouldCheckLine() {

		List<String> board = Arrays.asList(
				FIRST_PLAYER_SYMBOL_X, 
				FIRST_PLAYER_SYMBOL_X,
				FIRST_PLAYER_SYMBOL_X, 
				null, 
				null, 
				null, 
				null, 
				null, 
				null);

		int[] winnerPositions = { 0, 1, 2 };

		boolean result = ticTacToeMultiplayerService.checkLine(board, winnerPositions, 0, 1);

		assertTrue(result);
	}

	@Test
	void shouldCheckDraw() {

		List<String> board = Arrays.asList(
				FIRST_PLAYER_SYMBOL_X, 
				SECOND_PLAYER_SYMBOL_O, 
				FIRST_PLAYER_SYMBOL_X, 
				SECOND_PLAYER_SYMBOL_O, 
				FIRST_PLAYER_SYMBOL_X, 
				SECOND_PLAYER_SYMBOL_O, 
				SECOND_PLAYER_SYMBOL_O, 
				FIRST_PLAYER_SYMBOL_X, 
				SECOND_PLAYER_SYMBOL_O);

		boolean isDraw = ticTacToeMultiplayerService.checkDraw(board);

		assertTrue(isDraw);
	}
}
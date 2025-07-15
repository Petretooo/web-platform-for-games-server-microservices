package tictactoe.service.gameroom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import tictactoe.dto.TicTacToePlayerDto;
import tictactoe.dto.User;
import tictactoe.model.TicTacToeGame;
import tictactoe.model.TicTacToeGameRoom;
import tictactoe.model.TicTacToeGameUser;
import tictactoe.model.TicTacToeGameUserId;
import tictactoe.repository.gameroom.TicTacToeGameRoomRepository;
import tictactoe.service.game.TicTacToeGameService;
import tictactoe.utils.GameTitle;

class TicTacToeGameRoomServiceImplTest {

    private static final String SECOND_USER_PASSWORD = "12345679";
	private static final String SECOND_USER_EMAIL = "second.user@gmail.com";
	private static final String FIRST_USER_PASSWORD = "123456789";
	private static final String FIRST_USER_EMAIL = "first.user@gmail.com";
	private static final String SECOND_USER_ID = "second-user-id";
	private static final String FIRST_USER_ID = "first-user-id";
	private static final String SECOND_USER_SYMBOL_O = "O";
	private static final String SECOND_USER = "John";
	private static final String ROOM_NAME = "room-name";
	private static final String TIC_TAC_TOE_GAME_ROOM_ONE_ID = "tic-tac-toe-game-room-one";
	private static final String FIRST_USER_SYMBOL_X = "X";
	private static final String FIRST_USER = "Peter";
	private static final String TIC_TAC_TOE_GAME__FIRST_ID = "tic-tac-toe-first-game-id";
	private static final String TIC_TAC_TOE_GAME_SECOND_ID = "tic-tac-toe-second-game-id";
	
	@Mock
    private TicTacToeGameRoomRepository gameRoomRepository;

    @Mock
    private TicTacToeGameService gameService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TicTacToeGameRoomServiceImpl gameRoomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllRooms() {
    	
    	TicTacToeGame firstGame = new TicTacToeGame();
    	firstGame.setId(TIC_TAC_TOE_GAME__FIRST_ID);
    	TicTacToeGame secondGame = new TicTacToeGame();
    	firstGame.setId(TIC_TAC_TOE_GAME_SECOND_ID);
    	
    	TicTacToeGameRoom gameRoomOne = new TicTacToeGameRoom();
    	gameRoomOne.setActive(true);
    	gameRoomOne.setFirstUser(FIRST_USER);
    	gameRoomOne.setFirstUserSymbol(FIRST_USER_SYMBOL_X);
    	gameRoomOne.setGame(firstGame);
    	gameRoomOne.setGameRoomId(TIC_TAC_TOE_GAME_ROOM_ONE_ID);
    	gameRoomOne.setGameRoomName(ROOM_NAME);
    	gameRoomOne.setGameTitle(GameTitle.TICTACTOE.name());
    	gameRoomOne.setSecondUser(SECOND_USER);
    	gameRoomOne.setSecondUserSymbol(SECOND_USER_SYMBOL_O);
    	
    	TicTacToeGameRoom gameRoomTwo = new TicTacToeGameRoom();
    	gameRoomOne.setActive(true);
    	gameRoomOne.setFirstUser(FIRST_USER);
    	gameRoomOne.setFirstUserSymbol(FIRST_USER_SYMBOL_X);
    	gameRoomOne.setGame(secondGame);
    	gameRoomOne.setGameRoomId("tic-tac-toe-game-room-two");
    	gameRoomOne.setGameRoomName(ROOM_NAME);
    	gameRoomOne.setGameTitle("TICTACTOE");
    	gameRoomOne.setSecondUser(SECOND_USER);
    	gameRoomOne.setSecondUserSymbol(SECOND_USER_SYMBOL_O);
    	
    	
        List<TicTacToeGameRoom> gameRooms = new ArrayList<>();
        gameRooms.add(gameRoomOne);
        gameRooms.add(gameRoomTwo);

        when(gameRoomRepository.findByActiveTrue()).thenReturn(gameRooms);

        List<TicTacToeGameRoom> result = gameRoomService.getAllRooms();

        verify(gameRoomRepository).findByActiveTrue();
        assertEquals(gameRooms, result);
    }

    @Test
    void shouldGetGameRoom() {
    	
        TicTacToeGameRoom gameRoom = new TicTacToeGameRoom();

        when(gameRoomRepository.findByGameRoomId(TIC_TAC_TOE_GAME_ROOM_ONE_ID)).thenReturn(gameRoom);

        TicTacToeGameRoom result = gameRoomService.getGameRoom(TIC_TAC_TOE_GAME_ROOM_ONE_ID);

        verify(gameRoomRepository).findByGameRoomId(TIC_TAC_TOE_GAME_ROOM_ONE_ID);
        assertEquals(gameRoom, result);
    }

    @Test
    void shouldCreateTicTacToeGameRoom() {
    	
    	TicTacToeGame firstGame = new TicTacToeGame();
    	firstGame.setId(TIC_TAC_TOE_GAME__FIRST_ID);
    	
    	TicTacToeGameRoom gameRoomOne = new TicTacToeGameRoom();
    	gameRoomOne.setActive(true);
    	gameRoomOne.setFirstUser(null);
    	gameRoomOne.setFirstUserSymbol(FIRST_USER_SYMBOL_X);
    	gameRoomOne.setGame(firstGame);
    	gameRoomOne.setGameRoomId(null);
    	gameRoomOne.setGameRoomName(ROOM_NAME);
    	gameRoomOne.setGameTitle(GameTitle.TICTACTOE.name());
    	gameRoomOne.setSecondUser(null);
    	gameRoomOne.setSecondUserSymbol(SECOND_USER_SYMBOL_O);
    	
        when(gameService.createGame()).thenReturn(firstGame);
        when(gameRoomRepository.save(gameRoomOne)).thenReturn(gameRoomOne);

        TicTacToeGameRoom result = gameRoomService.createTicTacToeGameRoom(ROOM_NAME);

        verify(gameService).createGame();

        assertNull(result.getFirstUser());
        assertNull(result.getSecondUser());
        assertNotNull(result.getGame());
    }

    @Test
    void shouldDeleteGameRoom() {
    	
        gameRoomService.deleteGameRoom(TIC_TAC_TOE_GAME_ROOM_ONE_ID);

        verify(gameRoomRepository).deleteById(TIC_TAC_TOE_GAME_ROOM_ONE_ID);
    }

    @Test
    void shouldJoinGameRoom() {
    	
        TicTacToePlayerDto firstUserDto = new TicTacToePlayerDto();
        firstUserDto.setUserId(FIRST_USER_ID);
        firstUserDto.setGameRoomId(TIC_TAC_TOE_GAME__FIRST_ID);
        
        TicTacToePlayerDto secondUserDto = new TicTacToePlayerDto();
        secondUserDto.setUserId(SECOND_USER_ID);
        secondUserDto.setGameRoomId(TIC_TAC_TOE_GAME__FIRST_ID);
        
    	TicTacToeGame firstGame = new TicTacToeGame();
    	firstGame.setId(TIC_TAC_TOE_GAME__FIRST_ID);
    	
    	TicTacToeGameRoom gameRoomOne = new TicTacToeGameRoom();
    	gameRoomOne.setActive(true);
    	gameRoomOne.setFirstUser(FIRST_USER);
    	gameRoomOne.setFirstUserSymbol(FIRST_USER_SYMBOL_X);
    	gameRoomOne.setGame(firstGame);
    	gameRoomOne.setGameRoomId(TIC_TAC_TOE_GAME_ROOM_ONE_ID);
    	gameRoomOne.setGameRoomName(ROOM_NAME);
    	gameRoomOne.setGameTitle(GameTitle.TICTACTOE.name());
    	gameRoomOne.setSecondUser(SECOND_USER);
    	gameRoomOne.setSecondUserSymbol(SECOND_USER_SYMBOL_O);
        
        User firstUser = new User(FIRST_USER_ID, FIRST_USER, FIRST_USER_EMAIL, FIRST_USER_PASSWORD);
        User secondUser = new User(SECOND_USER_ID, SECOND_USER, SECOND_USER_EMAIL, SECOND_USER_PASSWORD);

        when(gameRoomRepository.findByGameRoomId(TIC_TAC_TOE_GAME__FIRST_ID)).thenReturn(gameRoomOne);
        when(gameRoomService.getGameRoom(TIC_TAC_TOE_GAME_ROOM_ONE_ID)).thenReturn(gameRoomOne);
        when(restTemplate.getForObject(anyString(), eq(User.class), eq(FIRST_USER_ID))).thenReturn(firstUser);
        when(restTemplate.getForObject(anyString(), eq(User.class), eq(SECOND_USER_ID))).thenReturn(secondUser);

        gameRoomService.joinGameRoom(firstUserDto);
        gameRoomService.joinGameRoom(secondUserDto);


        verify(restTemplate).getForObject(anyString(), eq(User.class), eq(FIRST_USER_ID));

        assertEquals(2, gameRoomOne.getGame().getUsers().size());
        assertEquals(FIRST_USER, gameRoomOne.getFirstUser());
        assertEquals(SECOND_USER, gameRoomOne.getSecondUser());
    }

    @SuppressWarnings("deprecation")
	@Test
    void shouldLeaveGameRoom() {
    	
        User firstUser = new User(FIRST_USER_ID, FIRST_USER, FIRST_USER_EMAIL, FIRST_USER_PASSWORD);
    	        
        TicTacToePlayerDto firstUserDto = new TicTacToePlayerDto();
        firstUserDto.setUserId(FIRST_USER_ID);
        firstUserDto.setGameRoomId(TIC_TAC_TOE_GAME_ROOM_ONE_ID);
        
        TicTacToePlayerDto secondUserDto = new TicTacToePlayerDto();
        secondUserDto.setUserId(SECOND_USER_ID);
        secondUserDto.setGameRoomId(TIC_TAC_TOE_GAME_ROOM_ONE_ID);
        
        TicTacToeGameUser ticTacToeGameUser = new TicTacToeGameUser();
        TicTacToeGameUserId gameUserId = new TicTacToeGameUserId();
        gameUserId.setUserId(FIRST_USER_ID);
        gameUserId.setGameId(TIC_TAC_TOE_GAME__FIRST_ID);
        ticTacToeGameUser.setTictactoeGameUserId(gameUserId);
        
    	TicTacToeGame firstGame = new TicTacToeGame();
    	firstGame.setId(TIC_TAC_TOE_GAME__FIRST_ID);
    	
    	Set<TicTacToeGameUser> setGameUsers = new HashSet<TicTacToeGameUser>();
    	setGameUsers.add(ticTacToeGameUser);
    	firstGame.setUsers(setGameUsers);
    	
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

    	
        when(gameRoomRepository.getById(TIC_TAC_TOE_GAME_ROOM_ONE_ID)).thenReturn(gameRoomOne);
        when(restTemplate.getForObject(anyString(), eq(User.class), eq(FIRST_USER_ID))).thenReturn(firstUser);

        gameRoomService.leaveGameRoom(firstUserDto);

        verify(restTemplate).getForObject(anyString(), eq(User.class), eq(FIRST_USER_ID));

        assertNull(gameRoomOne.getFirstUser());
    }

    @Test
    void shouldUpdateGameRoom() {
    	
    	TicTacToeGame firstGame = new TicTacToeGame();
    	firstGame.setId(TIC_TAC_TOE_GAME__FIRST_ID);
    	
    	TicTacToeGameRoom gameRoomOne = new TicTacToeGameRoom();
    	gameRoomOne.setActive(true);
    	gameRoomOne.setFirstUser(FIRST_USER);
    	gameRoomOne.setFirstUserSymbol(FIRST_USER_SYMBOL_X);
    	gameRoomOne.setGame(firstGame);
    	gameRoomOne.setGameRoomId(TIC_TAC_TOE_GAME_ROOM_ONE_ID);
    	gameRoomOne.setGameRoomName(ROOM_NAME);
    	gameRoomOne.setGameTitle(GameTitle.TICTACTOE.name());
    	gameRoomOne.setSecondUser(SECOND_USER);
    	gameRoomOne.setSecondUserSymbol(SECOND_USER_SYMBOL_O);

        when(gameRoomRepository.getById(TIC_TAC_TOE_GAME_ROOM_ONE_ID)).thenReturn(gameRoomOne);
        when(gameService.getGame(firstGame.getId())).thenReturn(firstGame);

        gameRoomService.updateGameRoom(TIC_TAC_TOE_GAME_ROOM_ONE_ID, FIRST_USER);

        verify(gameRoomRepository).getById(TIC_TAC_TOE_GAME_ROOM_ONE_ID);

        verify(gameService).getGame(firstGame.getId());

        assertFalse(gameRoomOne.isActive());
        assertEquals(FIRST_USER, firstGame.getWinnerUsername());
    }
}
package dotsandboxes.service.gameroom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import dotsandboxes.dto.DotsAndBoxesPlayerDto;
import dotsandboxes.dto.User;
import dotsandboxes.model.Game;
import dotsandboxes.model.GameRoom;
import dotsandboxes.repository.edge.EdgeRepository;
import dotsandboxes.repository.gamebox.GameBoxRepository;
import dotsandboxes.repository.gameroom.DotsAndBoxesGameRoomRepository;
import dotsandboxes.service.game.GameService;
import dotsandboxes.utils.GameTitle;

public class GameRoomServiceImplTest {
	
    private static final String FIRST_USER_ID = "firstUserId";
    private static final String FIRST_USER = "firstUser";
    private static final String FIRST_USER_EMAIL = "firstUserEmail";
    private static final String FIRST_USER_PASSWORD = "firstUserPassword";

	@Mock
	private DotsAndBoxesGameRoomRepository gameRoomRepository;

	@Mock
	private GameBoxRepository gameBoxRepository;

	@Mock
	private EdgeRepository edgeRepository;

	@Mock
	private GameService gameService;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private GameRoomServiceImpl service;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void shouldGetAllRooms() {

		List<GameRoom> rooms = new ArrayList<>();
		when(gameRoomRepository.findByActiveTrue()).thenReturn(rooms);

		List<GameRoom> result = service.getAllRooms();

		assertEquals(rooms, result);
		verify(gameRoomRepository).findByActiveTrue();
	}

	@Test
	public void shouldGetGameRoom() {

		String gameRoomId = "room1";
		GameRoom gameRoom = new GameRoom();
		when(gameRoomRepository.findByGameRoomId(gameRoomId)).thenReturn(gameRoom);

		GameRoom result = service.getGameRoom(gameRoomId);

		assertEquals(gameRoom, result);
		verify(gameRoomRepository).findByGameRoomId(gameRoomId);
	}

	@Test
	public void shouldCreateTicTacToeGameRoom() {

		String gameRoomName = "Room 1";
		GameRoom gameRoom = new GameRoom();
		Game game = new Game();
		when(gameService.createGame()).thenReturn(game);
		when(gameRoomRepository.save(any(GameRoom.class))).thenReturn(gameRoom);

		GameRoom result = service.createTicTacToeGameRoom(gameRoomName);

		assertEquals(gameRoomName, result.getGameRoomName());
		assertEquals(GameTitle.DOTSANDBOXES.name(), result.getGameTitle());
		assertEquals(game, result.getGame());
		assertTrue(result.isActive());
		assertEquals("X", result.getFirstUserSymbol());
		assertEquals("O", result.getSecondUserSymbol());
		verify(gameService).createGame();
		verify(gameRoomRepository).save(any(GameRoom.class));
	}

	@Test
	public void shouldDeleteGameRoom() {

		String gameRoomId = "room1";

		service.deleteGameRoom(gameRoomId);

		verify(gameRoomRepository).deleteById(gameRoomId);
	}

	@Test
	public void shouldJoinGameRoomPlayerNotJoined() {

		String gameRoomId = "room1";
		
		GameRoom gameRoom = new GameRoom();
		gameRoom.setGame(new Game());
		gameRoom.setGameRoomId(gameRoomId);
		gameRoom.setFirstUser(null);
		gameRoom.setSecondUser(null);
		when(gameRoomRepository.findByGameRoomId(gameRoomId)).thenReturn(gameRoom);

		User firstUser = new User(FIRST_USER, FIRST_USER_EMAIL, FIRST_USER_PASSWORD);
		firstUser.setUserId(FIRST_USER_ID);
		
		when(restTemplate.getForObject(anyString(), eq(User.class), eq(FIRST_USER_ID))).thenReturn(firstUser);

		DotsAndBoxesPlayerDto roomPlayerDto = new DotsAndBoxesPlayerDto();
		roomPlayerDto.setGameRoomId(gameRoomId);
		roomPlayerDto.setUserId(FIRST_USER_ID);

		service.joinGameRoom(roomPlayerDto);

		verify(gameRoomRepository).findByGameRoomId(gameRoomId);
		verify(restTemplate).getForObject(anyString(), eq(User.class), eq(FIRST_USER_ID));
	}

	@Test
	public void shouldLeaveGameRoomFirstUserLeaves() {

		String gameRoomId = "room1";
		
		GameRoom gameRoom = new GameRoom();
		gameRoom.setGame(new Game());
		gameRoom.setGameRoomId(gameRoomId);
		gameRoom.setFirstUser(null);
		gameRoom.setSecondUser(null);
		when(gameRoomRepository.findByGameRoomId(gameRoomId)).thenReturn(gameRoom);

		User firstUser = new User(FIRST_USER, FIRST_USER_EMAIL, FIRST_USER_PASSWORD);
		firstUser.setUserId(FIRST_USER_ID);

		when(gameRoomRepository.getById(gameRoomId)).thenReturn(gameRoom);
		when(restTemplate.getForObject(anyString(), eq(User.class), eq(FIRST_USER_ID))).thenReturn(firstUser);

		DotsAndBoxesPlayerDto roomPlayerDto = new DotsAndBoxesPlayerDto();
		roomPlayerDto.setGameRoomId(gameRoomId);
		roomPlayerDto.setUserId(FIRST_USER_ID);

		service.leaveGameRoom(roomPlayerDto);

		verify(gameRoomRepository).getById(gameRoomId);
		verify(restTemplate).getForObject(anyString(), eq(User.class), eq(FIRST_USER_ID));
		assertNull(gameRoom.getFirstUser());
	}
}
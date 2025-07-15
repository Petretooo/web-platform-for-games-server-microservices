package dotsandboxes.service.multiplayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import dotsandboxes.dto.DotsAndBoxesDto;
import dotsandboxes.dto.DotsAndBoxesResultDto;
import dotsandboxes.dto.User;
import dotsandboxes.model.Box;
import dotsandboxes.model.Dot;
import dotsandboxes.model.Edge;
import dotsandboxes.model.Game;
import dotsandboxes.model.GameBox;
import dotsandboxes.model.GameRoom;
import dotsandboxes.repository.edge.EdgeRepository;
import dotsandboxes.repository.gamebox.GameBoxRepository;
import dotsandboxes.repository.gameroom.DotsAndBoxesGameRoomRepository;
@Disabled
public class DotsAndBoxesMultiplayerServiceImplTest {
	
    private static final String FIRST_USER_ID = "firstUserId";
    private static final String FIRST_USER = "firstUser";
    private static final String FIRST_USER_EMAIL = "firstUserEmail";
    private static final String FIRST_USER_PASSWORD = "firstUserPassword";
    
    private static final String SECOND_USER_ID = "secondUserId";
    private static final String SECOND_USER = "secondUser";
    private static final String SECOND_USER_EMAIL = "secondUserEmail";
    private static final String SECOND_USER_PASSWORD = "secondUserPassword";
	
	@Mock
	private DotsAndBoxesGameRoomRepository gameRoomRepository;

	@Mock
	private GameBoxRepository gameBoxRepository;

	@Mock
	private EdgeRepository edgeRepository;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private DotsAndBoxesMultiplayerServiceImpl service;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCheckGame_AllBoxesUsed_FirstUserWins() {
		
		User firstUser = new User(FIRST_USER, FIRST_USER_EMAIL, FIRST_USER_PASSWORD);
		firstUser.setUserId(FIRST_USER_ID);
		
		User secondUser = new User(SECOND_USER, SECOND_USER_EMAIL, SECOND_USER_PASSWORD);
		secondUser.setUserId(SECOND_USER_ID);

		String gameRoomId = "room1";
		String senderId = "user1";
		String edgePosition = "A1-A2";

		Game game = new Game();
		game.setId("1L");

		GameRoom gameRoom = new GameRoom();
		gameRoom.setGameRoomId(gameRoomId);
		gameRoom.setFirstUser(FIRST_USER_ID);
		gameRoom.setSecondUser(SECOND_USER_ID);
		gameRoom.setGame(game);
		
        Dot dotA1 = new Dot();
        dotA1.setXAxis("A");
        dotA1.setYAxis("1");
        
        Dot dotA2 = new Dot();
        dotA2.setXAxis("A");
        dotA2.setYAxis("2");
        
		List<Edge> edges = new ArrayList<>();
        
    	Box box1 = new Box();
    	box1.setBoxName("A1A2B1B2");
    	box1.setOrderBox(1);

		Edge edge1 = new Edge();
		edge1.setBox(box1);
		edge1.setEdgeAvailable(true);
		edge1.setFromDot(dotA1);
		edge1.setToDot(dotA2);
		edge1.setGameId(game.getId());
		edge1.setOrderEdge(1);
		
		Edge edge2 = new Edge();
		edge2.setBox(box1);
		edge2.setEdgeAvailable(true);
		edge2.setFromDot(dotA1);
		edge2.setToDot(dotA2);
		edge2.setGameId(game.getId());
		edge2.setOrderEdge(3);
		
		edges.add(edge1);
		edges.add(edge2);
		
    	box1.setEdges(edges);

		List<GameBox> gameBoxes = new ArrayList<>();
		GameBox gameBox1 = new GameBox();
		gameBox1.setBoxAvailable(true);
		gameBox1.setUserId(FIRST_USER_ID);
		gameBox1.setBox(box1);
		gameBox1.setGame(game);
		gameBoxes.add(gameBox1);

		when(gameRoomRepository.findByGameRoomId(gameRoomId)).thenReturn(gameRoom);
		when(edgeRepository.findAllByGameId(game.getId())).thenReturn(edges);
		when(gameBoxRepository.findAllByGameId(game.getId())).thenReturn(gameBoxes);
        when(restTemplate.getForObject(anyString(), any(), eq(FIRST_USER_ID))).thenReturn(firstUser);
        when(restTemplate.getForObject(anyString(), any(), eq(SECOND_USER_ID))).thenReturn(secondUser);

		DotsAndBoxesDto message = new DotsAndBoxesDto();
		message.setSenderId(senderId);
		message.setMessage(edgePosition);

		DotsAndBoxesResultDto resultDto = service.checkGame(gameRoom, message);

		verify(edge1).setEdgeAvailable(false);
		verify(gameBox1).setBoxAvailable(false);

		assertEquals(FIRST_USER_ID, resultDto.getWinnerId());
		assertEquals(SECOND_USER_ID, resultDto.getLoserId());
		assertFalse(resultDto.isDraw());
	}

}

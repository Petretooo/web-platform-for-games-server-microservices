package dotsandboxes.service.gameroom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import dotsandboxes.dto.BoxDetailsDto;
import dotsandboxes.dto.DotsAndBoxesPlayerDto;
import dotsandboxes.dto.EdgeDetailsDto;
import dotsandboxes.dto.GameDetailsDto;
import dotsandboxes.dto.User;
import dotsandboxes.model.Edge;
import dotsandboxes.model.Game;
import dotsandboxes.model.GameBox;
import dotsandboxes.model.GameRoom;
import dotsandboxes.model.GameUser;
import dotsandboxes.model.GameUserId;
import dotsandboxes.repository.edge.EdgeRepository;
import dotsandboxes.repository.gamebox.GameBoxRepository;
import dotsandboxes.repository.gameroom.DotsAndBoxesGameRoomRepository;
import dotsandboxes.service.game.GameService;
import dotsandboxes.utils.GameTitle;
import dotsandboxes.utils.UserServiceException;

@Service
@Transactional
public class GameRoomServiceImpl implements GameRoomService{
	
	private final static String EDGE_WHITE_SMOKE_COLOR_DEFAULT = "whitesmoke";
	private final static String EDGE_RED_COLOR_FIRST_PLAYER = "red";
	private final static String EDGE_BLUE_COLOR_SECOND_PLAYER = "blue";
	
	private final static String BOX_TRANSPERENT_GREY_DEFAULT_COLOR = "#ffffff46";
	private final static String BOX_TRANSPERENT_RED_COLOR_FIRST_PLAYER = "#e74a4a6c";
	private final static String BOX_TRANSPERENT_BLUE_COLOR_SECOND_PLAYER = "#4a4de76c";
	
	@Autowired
	private DotsAndBoxesGameRoomRepository gameRoomRepository;
	
	@Autowired
	private GameBoxRepository gameBoxRepository;
	
	@Autowired
	private EdgeRepository edgeRepository;
		
	@Autowired
	private GameService gameService;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<GameRoom> getAllRooms() {
		return this.gameRoomRepository.findByActiveTrue();
	}

	@Override
	public GameRoom getGameRoom(String gameRoomId) {
		return this.gameRoomRepository.findByGameRoomId(gameRoomId);
	}

	@Override
	public GameRoom createTicTacToeGameRoom(String gameRoomName) {
		
		GameRoom gameRoom = new GameRoom();
		
		gameRoom.setGameRoomName(gameRoomName);
		gameRoom.setGameTitle(GameTitle.DOTSANDBOXES.name());
		gameRoom.setActive(true);
		gameRoom.setFirstUserSymbol("X");
		gameRoom.setSecondUserSymbol("O");
		
		Game game = this.gameService.createGame();
		gameRoom.setGame(game);
			
		this.gameRoomRepository.save(gameRoom);
		return gameRoom;
	}

	@Override
	public void deleteGameRoom(String gameRoomId) {
		this.gameRoomRepository.deleteById(gameRoomId);
	}

	@Override
	public void joinGameRoom(DotsAndBoxesPlayerDto roomPlayerDto) {
		
		GameRoom gameRoom = getGameRoom(roomPlayerDto.getGameRoomId());
		
		User user = null;
		
		try {
			user = restTemplate.getForObject("http://user-service/api/v1/users/id/{id}", User.class, roomPlayerDto.getUserId());
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}
		
		List<String> users = Arrays.asList(gameRoom.getFirstUser(), gameRoom.getSecondUser());
		
		for (String currentUser : users) {
			if(currentUser != null && currentUser.equalsIgnoreCase(user.getUserId())) {
				return;
			}
		}
		
		Game game = gameRoom.getGame();
		
		GameUserId id = new GameUserId();
		id.setGameId(game.getId());
		id.setUserId(user.getUserId());
		
		if(game.getUsers() == null) {
			game.setUsers(new HashSet<GameUser>());
		}
		
		gameRoom.getGame().getUsers().add(new GameUser(id));
		
		if(gameRoom.getFirstUser() == null) {
			gameRoom.setFirstUser(user.getUserId());
		}else if(gameRoom.getSecondUser() == null) {
			gameRoom.setSecondUser(user.getUserId());
		}else {
			return;
		}
		
	}

	@Override
	public void leaveGameRoom(DotsAndBoxesPlayerDto roomPlayerDto) {
		
		GameRoom gameRoom = this.gameRoomRepository.getById(roomPlayerDto.getGameRoomId());
		
		User user = null;
		
		try {
			user = restTemplate.getForObject("http://user-service/api/v1/users/id/{id}", User.class, roomPlayerDto.getUserId());
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}
		
		if(gameRoom.getFirstUser() != null && gameRoom.getFirstUser().equalsIgnoreCase(user.getUserId())) {
			gameRoom.setFirstUser(null);
			gameRoom.getGame().getUsers().remove(user);
		}else if(gameRoom.getSecondUser() != null && gameRoom.getSecondUser().equalsIgnoreCase(user.getUserId())) {
			gameRoom.setSecondUser(null);
			gameRoom.getGame().getUsers().remove(user);
		}else {
			return;
		}
		
	}

	@Override
	public void updateGameRoom(String roomId, String username) {
		GameRoom room = this.gameRoomRepository.getById(roomId);
		room.setActive(false);
		Game game = this.gameService.getGame(room.getGame().getId());
		game.setWinnerUsername(username);
		
	}

	@Override
	public GameDetailsDto getGameDetails(GameRoom room) {
		
		Game game = room.getGame();
		List<GameBox> gameBoxes = this.gameBoxRepository.findAllByGameId(game.getId());
//		int firstUserScore = room.getFirstUserScore();
//		int secondUserScore = room.getSecondUserScore();
		
		List<Edge> edgesByGame = this.edgeRepository.findAllByGameId(game.getId());
		
		List<BoxDetailsDto> boxDetailsDtos = gameBoxes.stream()
				.map(gameBox -> mapToBoxDetails(gameBox, room.getFirstUser(), room.getSecondUser()))
				.sorted(Comparator.comparingInt(BoxDetailsDto::getOrderBox))
				.toList();
		
		List<EdgeDetailsDto> mappedEdges = mapEdges(edgesByGame, boxDetailsDtos, room.getFirstUser(), room.getSecondUser());
		
		GameDetailsDto gameDetailsDto = new GameDetailsDto();
		gameDetailsDto.setGameId(game.getId());
		gameDetailsDto.setWinnerUsername(game.getWinnerUsername());
		gameDetailsDto.setBoxDetails(boxDetailsDtos);
		gameDetailsDto.setEdges(mappedEdges);
		gameDetailsDto.setGameSize(7);

		return gameDetailsDto;
	}
	
	private List<EdgeDetailsDto> mapEdges(List<Edge> edgesByGame, List<BoxDetailsDto> boxDetailsDtos, String firstPlayerId, String secondPlayerId) {
		
		List<EdgeDetailsDto> edges = new ArrayList<>();
		
		int orderEdge = 0;
		
		int orderBox = 1;
		
		List<Edge> lastLines = new ArrayList<Edge>();
		
		for (int i = 1 ; i <= 6 ; i++) {
			for (int j = 1 ; j <= 6 ; j++) {
				
				final int order = orderBox;
				
				BoxDetailsDto box = boxDetailsDtos.stream()
						.filter(e -> e.getOrderBox() == order)
						.findFirst()
						.orElseGet(null);
						
				List<Edge> boxEdges = edgesByGame.stream().filter(e -> e.getBox().getBoxName().equals(box.getBoxName())).toList();
				
				Edge topEdge = boxEdges.stream().filter(e -> e.getOrderEdge() == 1).findFirst().orElse(null);
				Edge leftEdge = boxEdges.stream().filter(e -> e.getOrderEdge() == 3).findFirst().orElse(null);
				
				edges.add(mapToEdgeDetailsDto(topEdge, orderEdge, firstPlayerId, secondPlayerId));
				orderEdge++;
				edges.add(mapToEdgeDetailsDto(leftEdge, orderEdge, firstPlayerId, secondPlayerId));
				orderEdge++;

				if(j == 6) {
					Edge rightEdge = boxEdges.stream().filter(e -> e.getOrderEdge() == 4).findFirst().orElse(null);
					edges.add(mapToEdgeDetailsDto(rightEdge, orderEdge, firstPlayerId, secondPlayerId));
					orderEdge++;
				}
				
				if(i == 6) {
					Edge bottomEdge = boxEdges.stream().filter(e -> e.getOrderEdge() == 2).findFirst().orElse(null);
					lastLines.add(bottomEdge);
				}
				
				orderBox++;
			}
			
			if(i == 6) {
				for (Edge bottomEdge : lastLines) {
					edges.add(mapToEdgeDetailsDto(bottomEdge, orderEdge, firstPlayerId, secondPlayerId));
					orderBox++;
				}
			}
		}
		
		return edges.stream().sorted(Comparator.comparingInt(EdgeDetailsDto::getOrderEdge)).toList();
	}

	private EdgeDetailsDto mapToEdgeDetailsDto(Edge edge, int orderEdge, String firstPlayerId, String secondPlayerId) {
		
		EdgeDetailsDto edgeDetailsDto = new EdgeDetailsDto();
		edgeDetailsDto.setEdgeAvailable(edge.isEdgeAvailable());
		edgeDetailsDto.setFromDot(edge.getFromDot().getXAxis() + edge.getFromDot().getYAxis());
		edgeDetailsDto.setToDot(edge.getToDot().getXAxis() + edge.getToDot().getYAxis());
		edgeDetailsDto.setUserId(edge.getUserId());
		edgeDetailsDto.setOrderEdge(orderEdge);
		
		if(edgeDetailsDto.getUserId() != null && edge.getUserId().equalsIgnoreCase(firstPlayerId)) {
			edgeDetailsDto.setEdgeColor(EDGE_RED_COLOR_FIRST_PLAYER);
		} else if (edgeDetailsDto.getUserId() != null && edge.getUserId().equalsIgnoreCase(secondPlayerId)) {
			edgeDetailsDto.setEdgeColor(EDGE_BLUE_COLOR_SECOND_PLAYER);
		} else {
			edgeDetailsDto.setEdgeColor(EDGE_WHITE_SMOKE_COLOR_DEFAULT);
		}		
		return edgeDetailsDto;
	}

	private BoxDetailsDto mapToBoxDetails(GameBox gameBox, String firstPlayerId, String secondPlayerId) {
		BoxDetailsDto boxDetailsDto = new BoxDetailsDto();
		boxDetailsDto.setBoxName(gameBox.getBox().getBoxName());
		boxDetailsDto.setBoxAvailable(gameBox.isBoxAvailable());
		boxDetailsDto.setOrderBox(gameBox.getBox().getOrderBox());
		boxDetailsDto.setUserId(gameBox.getUserId());
		
		if (gameBox.getUserId() != null && gameBox.getUserId().equalsIgnoreCase(firstPlayerId)) {
			boxDetailsDto.setBoxColor(BOX_TRANSPERENT_RED_COLOR_FIRST_PLAYER);
		} else if (gameBox.getUserId() != null && gameBox.getUserId().equalsIgnoreCase(secondPlayerId)) {
			boxDetailsDto.setBoxColor(BOX_TRANSPERENT_BLUE_COLOR_SECOND_PLAYER);
		} else {
			boxDetailsDto.setBoxColor(BOX_TRANSPERENT_GREY_DEFAULT_COLOR);
		}		
		return boxDetailsDto;
	}

}

package dotsandboxes.service.multiplayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import dotsandboxes.dto.BoxDetailsDto;
import dotsandboxes.dto.DotsAndBoxesDto;
import dotsandboxes.dto.DotsAndBoxesResultDto;
import dotsandboxes.dto.EdgeDetailsDto;
import dotsandboxes.dto.GameDetailsDto;
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
import dotsandboxes.utils.UserServiceException;

@Service
@Transactional
public class DotsAndBoxesMultiplayerServiceImpl implements DotsAndBoxesMultiplayerService{
	
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
	private RestTemplate restTemplate;

	@Override
	public DotsAndBoxesResultDto checkGame(GameRoom room, DotsAndBoxesDto message) {
		
		String gameRoomId = room.getGameRoomId();
		GameRoom gameRoom = gameRoomRepository.findByGameRoomId(gameRoomId);
		
		DotsAndBoxesResultDto resultDto = new DotsAndBoxesResultDto();
		resultDto.setSenderId(message.getSenderId());

		GameDetailsDto gameDetailsDto = getGameDetails(gameRoom, message, resultDto);
		resultDto.setGameDetails(gameDetailsDto);
		
		return resultDto;
	}
	
	
	private GameDetailsDto getGameDetails(GameRoom room, DotsAndBoxesDto message, DotsAndBoxesResultDto resultDto) {
		
		GameDetailsDto gameDetailsDto = new GameDetailsDto();
		
		String senderId = message.getSenderId();
		String edgePossition = message.getMessage(); 
		
		Game game = room.getGame();
		String firstPlayerId = room.getFirstUser();
		String secondPlayerId = room.getSecondUser();
		
		List<Edge> edgesByGame = this.edgeRepository.findAllByGameId(game.getId());
		boolean isItNecessaryToCheckForPlayAgain = checkEdge(edgesByGame, senderId, edgePossition);
		
		List<GameBox> gameBoxes = this.gameBoxRepository.findAllByGameId(game.getId());
		boolean playAgain = checkBox(gameBoxes, senderId, edgePossition, isItNecessaryToCheckForPlayAgain);
		
		boolean allBoxesUsed = gameBoxes.stream().allMatch(e -> !e.isBoxAvailable());
		
		User winnerUser = null;

		if(allBoxesUsed) {
			Long firstUserScore = gameBoxes.stream().filter(gb -> gb.getUserId().equalsIgnoreCase(room.getFirstUser())).count();
			Long secondUserScore = gameBoxes.stream().filter(gb -> gb.getUserId().equalsIgnoreCase(room.getSecondUser())).count();
			
			room.setFirstUserScore(firstUserScore.intValue());
			room.setSecondUserScore(secondUserScore.intValue());
			
			if (firstUserScore > secondUserScore) {
								
				try {
					winnerUser = restTemplate.getForObject("http://user-service/api/v1/users/id/{id}", User.class, room.getFirstUser());
				} catch (Exception e) {
					throw new UserServiceException(e.getMessage());
				}
				
				if (winnerUser != null) {
					gameDetailsDto.setWinnerUsername(winnerUser.getUsername());
					game.setWinnerUsername(winnerUser.getUsername());
					resultDto.setWinnerId(room.getFirstUser());
					resultDto.setLoserId(room.getSecondUser());
				}

			} else if (secondUserScore > firstUserScore) {
				
				try {
					winnerUser = restTemplate.getForObject("http://user-service/api/v1/users/id/{id}", User.class, room.getSecondUser());
				} catch (Exception e) {
					throw new UserServiceException(e.getMessage());
				}
				
				if (winnerUser != null) {
					gameDetailsDto.setWinnerUsername(winnerUser.getUsername());
					game.setWinnerUsername(winnerUser.getUsername());
					resultDto.setWinnerId(room.getSecondUser());
					resultDto.setLoserId(room.getFirstUser());
				}
				
			} else if (firstUserScore == secondUserScore) {
				resultDto.setDraw(true);
			}
			
		}
		
				
		List<BoxDetailsDto> boxDetailsDtos = gameBoxes.stream()
				.map(gameBox -> mapToBoxDetails(gameBox, firstPlayerId, secondPlayerId))
				.sorted(Comparator.comparingInt(BoxDetailsDto::getOrderBox))
				.toList();
		
		List<EdgeDetailsDto> mappedEdges = mapEdges(edgesByGame, boxDetailsDtos, firstPlayerId, secondPlayerId);
		
		gameDetailsDto.setGameId(game.getId());
		gameDetailsDto.setWinnerUsername(game.getWinnerUsername());
		gameDetailsDto.setBoxDetails(boxDetailsDtos);
		gameDetailsDto.setEdges(mappedEdges);
		gameDetailsDto.setGameSize(7);
		
		resultDto.setTryAgain(playAgain);

		return gameDetailsDto;
	}
	
	private boolean checkBox(List<GameBox> gameBoxes, String senderId, String edgePossition, boolean isItNecessaryToCheckForPlayAgain) {
		
		boolean playAgain = false;
		
		String fromDotXaxis = String.valueOf(edgePossition.charAt(0));
		String fromDotYaxis = String.valueOf(edgePossition.charAt(1));
		
		String toDotXaxis = String.valueOf(edgePossition.charAt(3));
		String toDotYaxis = String.valueOf(edgePossition.charAt(4));
				
		for (GameBox gameBox : gameBoxes) {
						
			if (gameBox.isBoxAvailable()) {
				
				Box box = gameBox.getBox();
				
				List<Edge> edges = box.getEdges().stream()
						.filter(e -> e.getGameId().equalsIgnoreCase(gameBox.getGame().getId()))
						.toList();
				
				boolean allBoxEdgesUsed = edges.stream().allMatch(e -> !e.isEdgeAvailable());
				
				if (allBoxEdgesUsed) {
					gameBox.setBoxAvailable(false);
					gameBox.setUserId(senderId);
					
					if(!playAgain && isItNecessaryToCheckForPlayAgain) {
						for (Edge e : edges) {	
							if(isCorrectEdge(fromDotXaxis, fromDotYaxis, toDotXaxis, toDotYaxis, e.getFromDot(), e.getToDot())) {
								playAgain = true;
							}
						}
					}
				}
			}
		}
		
		return playAgain;
	}


	private boolean checkEdge(List<Edge> edgesByGame, String senderId, String edgePossition) {
		
		boolean isItNecessaryToCheckForPlayAgain = false;
		
		String fromDotXaxis = String.valueOf(edgePossition.charAt(0));
		String fromDotYaxis = String.valueOf(edgePossition.charAt(1));
		
		String toDotXaxis = String.valueOf(edgePossition.charAt(3));
		String toDotYaxis = String.valueOf(edgePossition.charAt(4));
		
		for (Edge edge : edgesByGame) {
			
			Dot fromDot = edge.getFromDot();
			Dot toDot = edge.getToDot();
			
			if(isCorrectEdge(fromDotXaxis, fromDotYaxis, toDotXaxis, toDotYaxis, fromDot, toDot)) {
				edge.setEdgeAvailable(false);
				edge.setUserId(senderId);
				isItNecessaryToCheckForPlayAgain = true;
			}
		}
		
		return isItNecessaryToCheckForPlayAgain;
	}


	private boolean isCorrectEdge(String fromDotXaxis, String fromDotYaxis, String toDotXaxis, String toDotYaxis,
			Dot fromDot, Dot toDot) {
		return (fromDot.getXAxis().equalsIgnoreCase(fromDotXaxis) && fromDot.getYAxis().equalsIgnoreCase(fromDotYaxis)) &&
			(toDot.getXAxis().equalsIgnoreCase(toDotXaxis) && toDot.getYAxis().equalsIgnoreCase(toDotYaxis));
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

package tictactoe.service.gameroom;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import tictactoe.dto.TicTacToePlayerDto;
import tictactoe.dto.User;
import tictactoe.model.TicTacToeGame;
import tictactoe.model.TicTacToeGameRoom;
import tictactoe.model.TicTacToeGameUser;
import tictactoe.model.TicTacToeGameUserId;
import tictactoe.repository.gameroom.TicTacToeGameRoomRepository;
import tictactoe.service.game.TicTacToeGameService;
import tictactoe.utils.GameTitle;
import tictactoe.utils.UserServiceException;

@Service
@AllArgsConstructor
@Transactional
public class TicTacToeGameRoomServiceImpl implements TicTacToeGameRoomService{
	
	@Autowired
	private TicTacToeGameRoomRepository gameRoomRepository;
		
	@Autowired
	private TicTacToeGameService gameService;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<TicTacToeGameRoom> getAllRooms() {
		return this.gameRoomRepository.findByActiveTrue();
	}

	@Override
	public TicTacToeGameRoom getGameRoom(String gameRoomId) {
		return this.gameRoomRepository.findByGameRoomId(gameRoomId);
	}

	@Override
	public TicTacToeGameRoom createTicTacToeGameRoom(String gameRoomName) {
		
		TicTacToeGameRoom gameRoom = new TicTacToeGameRoom();
		
		gameRoom.setGameRoomName(gameRoomName);
		gameRoom.setGameTitle(GameTitle.TICTACTOE.name());
		gameRoom.setActive(true);
		gameRoom.setFirstUserSymbol("X");
		gameRoom.setSecondUserSymbol("O");
		
		TicTacToeGame game = this.gameService.createGame();
		gameRoom.setGame(game);
			
		this.gameRoomRepository.save(gameRoom);
		return gameRoom;
	}

	@Override
	public void deleteGameRoom(String gameRoomId) {
		this.gameRoomRepository.deleteById(gameRoomId);
	}

	@Override
	public void joinGameRoom(TicTacToePlayerDto roomPlayerDto) {
		
		TicTacToeGameRoom gameRoom = getGameRoom(roomPlayerDto.getGameRoomId());
		
		User user = restTemplate.getForObject("http://user-service/api/v1/users/id/{id}", User.class, roomPlayerDto.getUserId());
		
		List<String> users = Arrays.asList(gameRoom.getFirstUser(), gameRoom.getSecondUser());
		
		for (String currentUser : users) {
			if(currentUser != null && currentUser.equalsIgnoreCase(user.getUserId())) {
				return;
			}
		}
		
		TicTacToeGame game = gameRoom.getGame();
		
		TicTacToeGameUserId id = new TicTacToeGameUserId();
		id.setGameId(game.getId());
		id.setUserId(user.getUserId());
		
		if(game.getUsers() == null) {
			game.setUsers(new HashSet<TicTacToeGameUser>());
		}
		
		gameRoom.getGame().getUsers().add(new TicTacToeGameUser(id));
		
		if(gameRoom.getFirstUser() == null) {
			gameRoom.setFirstUser(user.getUserId());
		}else if(gameRoom.getSecondUser() == null) {
			gameRoom.setSecondUser(user.getUserId());
		}else {
			return;
		}
		
	}

	@Override
	public void leaveGameRoom(TicTacToePlayerDto roomPlayerDto) {
		
		TicTacToeGameRoom gameRoom = this.gameRoomRepository.getById(roomPlayerDto.getGameRoomId());
		
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
		TicTacToeGameRoom room = this.gameRoomRepository.getById(roomId);
		room.setActive(false);
		TicTacToeGame game = this.gameService.getGame(room.getGame().getId());
		game.setWinnerUsername(username);
		
	}

}

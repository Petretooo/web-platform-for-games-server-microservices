package hangman.service.gameroom;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import hangman.dto.GameRoomInfoDto;
import hangman.dto.GameRoomPlayerDto;
import hangman.dto.GameRoomTryDto;
import hangman.dto.RoomUserDto;
import hangman.dto.User;
import hangman.model.HangmanGame;
import hangman.model.HangmanGameRoom;
import hangman.repository.gameroom.HangmanGameRoomRepository;
import hangman.service.alphabet.AlphabetService;
import hangman.service.game.HangmanGameService;
import hangman.service.gamestats.GameStatsService;
import hangman.service.userstats.UserStatsService;
import hangman.service.word.WordService;
import hangman.util.GameTitle;
import hangman.util.UserServiceException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class HangmanGameRoomServiceImpl implements HangmanGameRoomService {

	@Autowired
	private HangmanGameRoomRepository gameRoomRepository;

	@Autowired
	private UserStatsService userStatsService;

	@Autowired
	private HangmanGameService gameService;

	@Autowired
	private AlphabetService alphabetService;
	
	@Autowired
	private WordService wordService;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private GameStatsService gameStatsService;

	@Override
	public List<HangmanGameRoom> getAllRooms() {
		return this.gameRoomRepository.findByActiveTrue();
	}

	@Override
	public HangmanGameRoom getGameRoom(String gameRoomId) {
		return gameRoomRepository.findByGameRoomId(gameRoomId);
	}

	@Override
	public HangmanGameRoom createHangmanGameRoom(String gameRoomName) {

		HangmanGameRoom room = new HangmanGameRoom();
		room.setGameRoomName(gameRoomName);
		room.setGameTitle(GameTitle.HANGMAN.name());
		room.setWord(wordService.randomWordGenerator().getWordName());
		room.setActive(true);

		this.gameRoomRepository.save(room);
		return room;
	}

	@Override
	public void deleteGameRoom(String gameRoomId) {
		this.gameRoomRepository.deleteById(gameRoomId);
	}

	@Override
	public boolean addSecondPlayer(String gameRoomId, String secondPlayerId) {

		Optional<HangmanGameRoom> roomOptional = this.gameRoomRepository.findById(gameRoomId);
		HangmanGameRoom room = roomOptional.get();
		
		User user = null;
		
		try {
			user = restTemplate.getForObject("http://user-service/api/v1/users/id/{id}", User.class, secondPlayerId);
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}

		if (room != null && user != null) {
			room.setSecondUserId(secondPlayerId);
			return true;
		}

		return false;
	}

	@Override
	public GameRoomInfoDto getGameRoomInfo(String gameRoomId) {

		HangmanGameRoom room = gameRoomRepository.findByGameRoomId(gameRoomId);

		if (room == null) {
			throw new NullPointerException();
		}

		GameRoomInfoDto roomDto = mapGameRoomToDto(room);

		return roomDto;
	}

	@Override
	public GameRoomInfoDto mapGameRoomToDto(HangmanGameRoom room) {

		GameRoomInfoDto dto = new GameRoomInfoDto();
		dto.setGameRoomId(room.getGameRoomId());

		dto.setGameRoomId(room.getGameRoomId());
		dto.setGameRoomName(room.getGameRoomName());
		
		// ADDED LATER
		dto.setGameTitle(room.getGameTitle());
		dto.setWord(room.getWord());
		// END

		String firstUserId = room.getFirstUserId();
		if (firstUserId != null) {
			
			User user = null;
			
			try {
				user = restTemplate.getForObject("http://user-service/api/v1/users/id/{id}", User.class, firstUserId);
			} catch (Exception e) {
				throw new UserServiceException(e.getMessage());
			}
			
			dto.setFirstUser(mapUserToDto(user, room.getFirstUserGame(), room.getWord()));
			room.setFirstUserGame(dto.getFirstUser().getGameInfo());
		}

		String secondUserId = room.getSecondUserId();
		if (secondUserId != null) {
			
			User user = null;
			
			try {
				user = restTemplate.getForObject("http://user-service/api/v1/users/id/{id}", User.class, secondUserId);
			} catch (Exception e) {
				throw new UserServiceException(e.getMessage());
			}
			
			dto.setSecondUser(mapUserToDto(user, room.getSecondUserGame(), room.getWord()));
			room.setSecondUserGame(dto.getSecondUser().getGameInfo());
		}

		return dto;
	}

	private RoomUserDto mapUserToDto(User user, HangmanGame game, String word) {

		RoomUserDto roomUserDto = new RoomUserDto();
		roomUserDto.setUserId(user.getUserId());
		roomUserDto.setUserTurn(false);
		roomUserDto.setGameInfo(game);
		
		if (game == null) {
			game = this.gameService.createGameWithWord(user, word);
			userStatsService.save(user, game.getCurrentWord());
			gameStatsService.saveGameStats(game);
			roomUserDto.setGameInfo(game);
			roomUserDto.setUnusedCharacters(alphabetService.getUnusedCharacters(roomUserDto.getGameInfo().getId()));
		}

		return roomUserDto;
	}

	@Override
	public GameRoomInfoDto makeTry(String gameRoomId, GameRoomTryDto message) {

		GameRoomInfoDto roomInfo = getGameRoomInfo(gameRoomId);

		RoomUserDto userDto = Arrays.asList(roomInfo.getFirstUser(), roomInfo.getSecondUser()).stream()
				.filter(user -> user.getUserId().equalsIgnoreCase(message.getUserId())).findFirst().get();

		changeGameForUser(userDto, message.getMessage());

		return roomInfo;
	}

	public void changeGameForUser(RoomUserDto roomUserDto, String letter) {

		this.gameService.enterCharacter(roomUserDto.getGameInfo().getId(), letter);
		roomUserDto.setUnusedCharacters(this.alphabetService.getUnusedCharacters(roomUserDto.getGameInfo().getId()));
	}

	@Override
	public void joinGameRoom(GameRoomPlayerDto roomPlayerDto) {
		
		HangmanGameRoom room = this.gameRoomRepository.findByGameRoomId(roomPlayerDto.getGameRoomId());
		User user = null;
		
		try {
			user = restTemplate.getForObject("http://user-service/api/v1/users/id/{id}", User.class, roomPlayerDto.getUserId());
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}
		
		List<String> userIds = Arrays.asList(room.getFirstUserId(), room.getSecondUserId());
		
		for (String currentUserId : userIds) {
			
			User currentUser = null;
			
			try {
				currentUser = restTemplate.getForObject("http://user-service/api/v1/users/id/{id}", User.class, currentUserId);
			} catch (Exception e) {
				throw new UserServiceException(e.getMessage());
			}
			
			if(currentUser != null && currentUser.getUserId().equalsIgnoreCase(roomPlayerDto.getUserId())) {
				return;
			}
		}
		
		if(room.getFirstUserId() == null) {
			room.setFirstUser(user);
			room.setFirstUserId(user.getUserId());
		}else if(room.getSecondUserId() == null) {
			room.setSecondUser(user);
			room.setSecondUserId(user.getUserId());
		}else {
			return;
		}
	}

	@Override
	public void leaveGameRoom(GameRoomPlayerDto roomPlayerDto) {
		
		HangmanGameRoom room = this.gameRoomRepository.findByGameRoomId(roomPlayerDto.getGameRoomId());
				
		String firstUserId = room.getFirstUserId();
		User firstUser = null;
		
		if(firstUserId != null) {
			try {
				firstUser = restTemplate.getForObject("http://user-service/api/v1/users/id/{id}", User.class, firstUserId);
			} catch (Exception e) {
				throw new UserServiceException(e.getMessage());
			}
		}
		
		String secondUserId = room.getSecondUserId();
		User secondUser = null;
		
		if(secondUserId != null) {			
			try {
				secondUser = restTemplate.getForObject("http://user-service/api/v1/users/id/{id}", User.class, secondUserId);
			} catch (Exception e) {
				throw new UserServiceException(e.getMessage());
			}
		}
				
		if(firstUser != null && firstUserId.equalsIgnoreCase(roomPlayerDto.getUserId())) {
			room.setFirstUserId(null);
		}else if(secondUser != null && secondUserId.equalsIgnoreCase(roomPlayerDto.getUserId())) {
			room.setSecondUserId(null);
		}else {
			return;
		}
	}
}

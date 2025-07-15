package hangman.api;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import hangman.dto.GameRoomDto;
import hangman.dto.GameRoomHangmanDto;
import hangman.dto.GameRoomInfoDto;
import hangman.dto.GameRoomPlayerDto;
import hangman.dto.GameRoomResultsDto;
import hangman.dto.GameRoomTryDto;
import hangman.dto.GameRoomTryInfoDto;
import hangman.dto.RoomUserDto;
import hangman.dto.User;
import hangman.kafka.GameRecordProducerService;
import hangman.model.HangmanGame;
import hangman.model.HangmanGameRoom;
import hangman.service.game.HangmanGameService;
import hangman.service.gameroom.HangmanGameRoomService;
import hangman.service.multiplayer.HangmanMultiplayerService;
import hangman.util.GameMode;
import hangman.util.UserServiceException;

@RestController
@RequestMapping("/api/v1/multiplayer/hangman")
public class MultiplayerHangmanApi {
	
	private static final String ENDPOINT = "/api/v1/multiplayer/hangman/";

	@Autowired
	private HangmanGameRoomService gameRoomService;
	
	@Autowired
	private HangmanGameService gameService;
	
	@Autowired
	private HangmanMultiplayerService hangmanMultiplayerService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private GameRecordProducerService gameRecordProducerService;
	
	@GetMapping
	public ResponseEntity<List<GameRoomInfoDto>> getAllGameRoom() {
		
		List<HangmanGameRoom> game = this.gameRoomService.getAllRooms();
		
		List<GameRoomInfoDto> infoDtos = game.stream().map(room -> this.gameRoomService.mapGameRoomToDto(room)).collect(Collectors.toList());
		
//		gameRecordProducerService.sendGameRecord(ENDPOINT, GameMode.MULTIPLAYER);
		return ResponseEntity.ok(infoDtos);
	}
	
	@GetMapping(value = "/{gameRoomId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameRoomInfoDto> getGameRoom(@PathVariable String gameRoomId) {
		
		GameRoomInfoDto info = this.gameRoomService.getGameRoomInfo(gameRoomId);
		
//		String endpointGetGameRoom = ENDPOINT + "/{gameRoomId}";
//		gameRecordProducerService.sendGameRecord(endpointGetGameRoom, GameMode.MULTIPLAYER);
		return ResponseEntity.ok(info);
	}
	
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameRoomInfoDto> createGameRoomHangman(@RequestBody GameRoomHangmanDto dto){
		
		HangmanGameRoom room = null;
		
		if(dto != null) {
			room = this.gameRoomService.createHangmanGameRoom(dto.getGameRoomName());
		}
		
		GameRoomInfoDto info = this.gameRoomService.mapGameRoomToDto(room);
		
		String endpointCreateGameRoom = ENDPOINT + "/create";
		gameRecordProducerService.sendGameRecord(endpointCreateGameRoom, GameMode.MULTIPLAYER);
		return new ResponseEntity<>(info, HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{gameRoomId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteGameRoom(@PathVariable String gameRoomId) {
		
//		String endpointDeleteGameRoom = ENDPOINT + "/{gameRoomId}";
//		gameRecordProducerService.sendGameRecord(endpointDeleteGameRoom, GameMode.MULTIPLAYER);
		this.gameRoomService.deleteGameRoom(gameRoomId);
	}
	
	
	@GetMapping(value = "/room/{roomId}/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HangmanGame> getGameByRoom(@PathVariable String roomId, @PathVariable String username) {
		
		GameRoomInfoDto infoDto = this.gameRoomService.getGameRoomInfo(roomId);
		
		User user = null;
		
		try {
			user = restTemplate.getForObject("http://user-service/api/v1/users/{username}", User.class, username);
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}
		
		RoomUserDto userDto = null;
		String userId = user.getUserId();
		
		if(infoDto != null) { 
			userDto = Arrays.asList(infoDto.getFirstUser(), infoDto.getSecondUser()).stream()
					.filter(player -> player.getUserId().equalsIgnoreCase(userId)).findFirst().get();
		}
		
//		String endpointGetGameRoomByParams = ENDPOINT + "/room/{roomId}/username/{username}";
//		gameRecordProducerService.sendGameRecord(endpointGetGameRoomByParams, GameMode.MULTIPLAYER);
		return ResponseEntity.ok(userDto.getGameInfo());
	}
	
	@GetMapping(value = "/room/letters/{roomId}/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getGameUsedLetters(@PathVariable String roomId, @PathVariable String username) {
		
		GameRoomInfoDto infoDto = this.gameRoomService.getGameRoomInfo(roomId);
				
		User user = null;
		
		try {
			user = restTemplate.getForObject("http://user-service/api/v1/users/{username}", User.class, username);
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}
		
		RoomUserDto userDto = null;
		String userId = user.getUserId();
		
		if(infoDto != null) { 
			userDto = Arrays.asList(infoDto.getFirstUser(), infoDto.getSecondUser()).stream()
					.filter(player -> player.getUserId().equalsIgnoreCase(userId)).findFirst().get();
		}
		List<String> letters = gameService.getusedLettersArray(userDto.getGameInfo().getId());
		
//		String endpointGetGameRoomByParams = ENDPOINT + "/room/letters/{roomId}/username/{username}";
//		gameRecordProducerService.sendGameRecord(endpointGetGameRoomByParams, GameMode.MULTIPLAYER);
		return ResponseEntity.ok(letters);
	}
	
	@PutMapping(value = "/room/try/{roomId}/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void makeTry(@PathVariable String roomId, @PathVariable String username, @RequestParam("letter") String letter) {
		
		GameRoomInfoDto infoDto = this.gameRoomService.getGameRoomInfo(roomId);
				
		User user = null;
		
		try {
			user = restTemplate.getForObject("http://user-service/api/v1/users/{username}", User.class, username);
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}
		
		RoomUserDto userDto = null;
		String userId = user.getUserId();
		
		if(infoDto != null) { 
			userDto = Arrays.asList(infoDto.getFirstUser(), infoDto.getSecondUser()).stream()
					.filter(player -> player.getUserId().equalsIgnoreCase(userId)).findFirst().get();
		}
		
		gameService.enterCharacter(userDto.getGameInfo().getId(), letter);
		
//		String endpointUpdateGameRoomByParams = ENDPOINT + "/room/try/{roomId}/username/{username}";
//		gameRecordProducerService.sendGameRecord(endpointUpdateGameRoomByParams, GameMode.MULTIPLAYER);
		ResponseEntity.noContent();
	}
	
	@GetMapping(value = "/room/alphabet/{roomId}/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Character>> getGameRoomAlphabet(@PathVariable String roomId, @PathVariable String username) {
		
		GameRoomInfoDto infoDto = this.gameRoomService.getGameRoomInfo(roomId);
				
		User user = null;
		
		try {
			user = restTemplate.getForObject("http://user-service/api/v1/users/{username}", User.class, username);
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}
		
		RoomUserDto userDto = null;
		String userId = user.getUserId();
		
		if(infoDto != null) { 
			userDto = Arrays.asList(infoDto.getFirstUser(), infoDto.getSecondUser()).stream()
					.filter(player -> player.getUserId().equalsIgnoreCase(userId)).findFirst().get();
		}
		
//		String endpointGetGameRoomByParams = ENDPOINT + "/room/alphabet/{roomId}/username/{username}";
//		gameRecordProducerService.sendGameRecord(endpointGetGameRoomByParams, GameMode.MULTIPLAYER);
		return ResponseEntity.ok(this.gameService.getUnusedCharacters(userDto.getGameInfo().getId()));
	}

	
	@PutMapping(value = "/join", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameRoomInfoDto> joinGameRoom(@RequestBody GameRoomPlayerDto roomPlayerDto) {
		
		this.gameRoomService.joinGameRoom(roomPlayerDto);
		GameRoomInfoDto infoDto = this.gameRoomService.getGameRoomInfo(roomPlayerDto.getGameRoomId());
		
//		String endpointJoinGameRoomByParams = ENDPOINT + "/join";
//		gameRecordProducerService.sendGameRecord(endpointJoinGameRoomByParams, GameMode.MULTIPLAYER);
		return new ResponseEntity<>(infoDto, HttpStatus.OK);
	}
	
	@PutMapping(value = "/leave", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameRoomInfoDto> leaveGameRoom(@RequestBody GameRoomPlayerDto roomPlayerDto) {
		
		this.gameRoomService.leaveGameRoom(roomPlayerDto);
		GameRoomInfoDto infoDto = this.gameRoomService.getGameRoomInfo(roomPlayerDto.getGameRoomId());
		
//		String endpointLeaveGameRoomByParams = ENDPOINT + "/leave";
//		gameRecordProducerService.sendGameRecord(endpointLeaveGameRoomByParams, GameMode.MULTIPLAYER);
		return new ResponseEntity<>(infoDto, HttpStatus.OK);
	}
	
	@PostMapping(value = "/check/game")
	public ResponseEntity<GameRoomResultsDto> checkGameRoom(@RequestBody GameRoomDto gameRoomDto) {
		
		GameRoomInfoDto gameRoomInfoDto = gameRoomDto.getGameRoomInfoDto();
		GameRoomTryDto gameRoomTryDto = gameRoomDto.getGameRoomTryDto();
		
		GameRoomResultsDto gameRoomResultsDto = this.hangmanMultiplayerService.checkGameRoom(gameRoomInfoDto, gameRoomTryDto);
		
//		String endpointCheckGameRoomByParams = ENDPOINT + "/check/game";
//		gameRecordProducerService.sendGameRecord(endpointCheckGameRoomByParams, GameMode.MULTIPLAYER);
		return new ResponseEntity<>(gameRoomResultsDto, HttpStatus.OK);
	}
	
	@PostMapping(value = "/make/try")
	public ResponseEntity<GameRoomInfoDto> makeTryGame(@RequestBody GameRoomTryInfoDto gameRoomTryInfoDto) {
		
		String gameRoomId = gameRoomTryInfoDto.getGameRoomId();
		GameRoomTryDto gameRoomTryDto = gameRoomTryInfoDto.getGameRoomTryDto();
		
		GameRoomInfoDto gameRoomResultsDto = this.hangmanMultiplayerService.makeTry(gameRoomId, gameRoomTryDto);
		
//		String endpointMakeTryGameRoomByParams = ENDPOINT + "/make/try";
//		gameRecordProducerService.sendGameRecord(endpointMakeTryGameRoomByParams, GameMode.MULTIPLAYER);
		return new ResponseEntity<>(gameRoomResultsDto, HttpStatus.OK);
	}
	
}

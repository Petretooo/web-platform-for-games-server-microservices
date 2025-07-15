package dotsandboxes.api;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import dotsandboxes.dto.DotsAndBoxesPlayerDto;
import dotsandboxes.dto.DotsAndBoxesResultDto;
import dotsandboxes.dto.DotsAndBoxesTryDto;
import dotsandboxes.dto.GameDetailsDto;
import dotsandboxes.dto.GameRoomDotsAndBoxesDto;
import dotsandboxes.kafka.GameRecordProducerService;
import dotsandboxes.model.Game;
import dotsandboxes.model.GameRoom;
import dotsandboxes.service.gameroom.GameRoomService;
import dotsandboxes.service.multiplayer.DotsAndBoxesMultiplayerService;

@RestController
@RequestMapping("/api/v1/multiplayer/dotsandboxes")
public class MultiplayerApi {
	
	private static final String ENDPOINT = "/api/v1/multiplayer/dotsandboxes/";
	
	@Autowired
	private GameRoomService gameRoomService;
	
	@Autowired
	private DotsAndBoxesMultiplayerService multiplayerService;
	
	@Autowired
	private GameRecordProducerService gameRecordProducerService;
	
	@GetMapping
	public ResponseEntity<List<GameRoom>> getAllGameRoom() {
		
		List<GameRoom> games = this.gameRoomService.getAllRooms();
		
//		gameRecordProducerService.sendGameRecord(ENDPOINT);
		return ResponseEntity.ok(games);
	}
	
	@GetMapping(value = "/{gameRoomId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameRoom> getGameRoom(@PathVariable String gameRoomId) {
		
		GameRoom room = this.gameRoomService.getGameRoom(gameRoomId);
		
//		String getGameRoomEndpoint = ENDPOINT + gameRoomId;
		
//		gameRecordProducerService.sendGameRecord(getGameRoomEndpoint);
		return ResponseEntity.ok(room);
	}
	
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameRoom> createGameRoomTicTacToe(@RequestBody GameRoomDotsAndBoxesDto dto){
		
		GameRoom room = null;
		
		if(dto != null) {
			room = this.gameRoomService.createTicTacToeGameRoom(dto.getGameRoomName());
		}
		
		String createGameRoomEndpoint = ENDPOINT + "create";
		
		gameRecordProducerService.sendGameRecord(createGameRoomEndpoint);
				
		return new ResponseEntity<>(room, HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{gameRoomId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteGameRoom(@PathVariable String gameRoomId) {
		
//		String deleteGameRoomEndpoint = ENDPOINT + gameRoomId;
		
//		gameRecordProducerService.sendGameRecord(deleteGameRoomEndpoint);
		this.gameRoomService.deleteGameRoom(gameRoomId);
	}
	
	@GetMapping(value = "/room/{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameDetailsDto> getGameByRoom(@PathVariable String roomId) {
		
		GameRoom room = this.gameRoomService.getGameRoom(roomId);
		
		GameDetailsDto gameDetails = this.gameRoomService.getGameDetails(room);
		
//		String getGameRoomEndpoint = ENDPOINT + "/room/" + roomId;
		
//		gameRecordProducerService.sendGameRecord(getGameRoomEndpoint);
		return ResponseEntity.ok(gameDetails);
	}
	
	@PutMapping(value = "/join", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameRoom> joinGameRoom(@RequestBody DotsAndBoxesPlayerDto roomPlayerDto) {
		
		this.gameRoomService.joinGameRoom(roomPlayerDto);
		GameRoom room = this.gameRoomService.getGameRoom(roomPlayerDto.getGameRoomId());
		
//		String joinGameRoomEndpoint = ENDPOINT + "/join";
		
//		gameRecordProducerService.sendGameRecord(joinGameRoomEndpoint);
		return new ResponseEntity<>(room, HttpStatus.OK);
	}
	
	@PutMapping(value = "/leave", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GameRoom> leaveGameRoom(@RequestBody DotsAndBoxesPlayerDto roomPlayerDto) {
		
		this.gameRoomService.leaveGameRoom(roomPlayerDto);
		GameRoom room = this.gameRoomService.getGameRoom(roomPlayerDto.getGameRoomId());
		
//		String leaveGameRoomEndpoint = ENDPOINT + "/leave";
		
//		gameRecordProducerService.sendGameRecord(leaveGameRoomEndpoint);
		return new ResponseEntity<>(room, HttpStatus.OK);
	}
	
	@GetMapping(value = "/gameroom/{gameRoomId}/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateGameRoom(@PathVariable String roomId, @PathVariable String username) {
		
//		String updateGameRoomEndpoint = ENDPOINT + String.format("/gameroom/%s/username/%s", roomId, username);
		
//		gameRecordProducerService.sendGameRecord(updateGameRoomEndpoint);
		this.gameRoomService.updateGameRoom(roomId, username);
	}
	
	@PostMapping(value = "/checkgame", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DotsAndBoxesResultDto> checkGame(@RequestBody DotsAndBoxesTryDto tryDto) {
		
		DotsAndBoxesResultDto dto = this.multiplayerService.checkGame(tryDto.getRoom(), tryDto.getMessage());
		
//		String checkGameRoomEndpoint = ENDPOINT + "/checkgame";
		
//		gameRecordProducerService.sendGameRecord(checkGameRoomEndpoint);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

}

package tictactoe.api;

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

import tictactoe.dto.GameRoomTicTacToeDto;
import tictactoe.dto.TicTacToePlayerDto;
import tictactoe.dto.TicTacToeResultDto;
import tictactoe.dto.TicTacToeTryDto;
import tictactoe.kafka.GameRecordProducerService;
import tictactoe.model.TicTacToeGame;
import tictactoe.model.TicTacToeGameRoom;
import tictactoe.service.gameroom.TicTacToeGameRoomService;
import tictactoe.service.multiplayer.TicTacToeMultiplayerService;

@RestController
@RequestMapping("/api/v1/multiplayer/tictactoe")
public class MultiplayerTicTacToeApi {
	
	String ENDPOINT = "/api/v1/multiplayer/tictactoe/";
	
	@Autowired
	private TicTacToeGameRoomService gameRoomService;
	
	@Autowired
	private TicTacToeMultiplayerService multiplayerService;
	
	@Autowired
	private GameRecordProducerService gameRecordProducerService;
	
	@GetMapping
	public ResponseEntity<List<TicTacToeGameRoom>> getAllGameRoom() {
		
		List<TicTacToeGameRoom> games = this.gameRoomService.getAllRooms();
		
//		gameRecordProducerService.sendGameRecord(ENDPOINT);
		return ResponseEntity.ok(games);
	}
	
	@GetMapping(value = "/{gameRoomId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TicTacToeGameRoom> getGameRoom(@PathVariable String gameRoomId) {
		
		TicTacToeGameRoom room = this.gameRoomService.getGameRoom(gameRoomId);
		
//		String getGameRoomEndpoint = ENDPOINT + gameRoomId;
//		
//		gameRecordProducerService.sendGameRecord(getGameRoomEndpoint);
		return ResponseEntity.ok(room);
	}
	
	@PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TicTacToeGameRoom> createGameRoomTicTacToe(@RequestBody GameRoomTicTacToeDto dto){
		
		TicTacToeGameRoom room = null;
		
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
//		
//		gameRecordProducerService.sendGameRecord(deleteGameRoomEndpoint);
		this.gameRoomService.deleteGameRoom(gameRoomId);
	}
	
	@GetMapping(value = "/room/{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TicTacToeGame> getGameByRoom(@PathVariable String roomId) {
		
		TicTacToeGameRoom room = this.gameRoomService.getGameRoom(roomId);
		
//		String getGameRoomEndpoint = ENDPOINT + "/room/" + roomId;
//		
//		gameRecordProducerService.sendGameRecord(getGameRoomEndpoint);
		return ResponseEntity.ok(room.getGame());
	}
	
	@PutMapping(value = "/join", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TicTacToeGameRoom> joinGameRoom(@RequestBody TicTacToePlayerDto roomPlayerDto) {
		
		this.gameRoomService.joinGameRoom(roomPlayerDto);
		TicTacToeGameRoom room = this.gameRoomService.getGameRoom(roomPlayerDto.getGameRoomId());
		
//		String joinGameRoomEndpoint = ENDPOINT + "/join";
//		
//		gameRecordProducerService.sendGameRecord(joinGameRoomEndpoint);
		return new ResponseEntity<>(room, HttpStatus.OK);
	}
	
	@PutMapping(value = "/leave", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TicTacToeGameRoom> leaveGameRoom(@RequestBody TicTacToePlayerDto roomPlayerDto) {
		
		this.gameRoomService.leaveGameRoom(roomPlayerDto);
		TicTacToeGameRoom room = this.gameRoomService.getGameRoom(roomPlayerDto.getGameRoomId());
		
//		String leaveGameRoomEndpoint = ENDPOINT + "/leave";
//		
//		gameRecordProducerService.sendGameRecord(leaveGameRoomEndpoint);
		return new ResponseEntity<>(room, HttpStatus.OK);
	}
	
	@GetMapping(value = "/gameroom/{gameRoomId}/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateGameRoom(@PathVariable String roomId, @PathVariable String username) {
		
//		String updateGameRoomEndpoint = ENDPOINT + String.format("/gameroom/%s/username/%s", roomId, username);
//		
//		gameRecordProducerService.sendGameRecord(updateGameRoomEndpoint);
		this.gameRoomService.updateGameRoom(roomId, username);
	}
	
	@PostMapping(value = "/checkgame", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TicTacToeResultDto> checkGame(@RequestBody TicTacToeTryDto tryDto) {
		
		TicTacToeResultDto dto = this.multiplayerService.checkGame(tryDto.getRoom(), tryDto.getMessage());
		
//		String checkGameRoomEndpoint = ENDPOINT + "/checkgame";
//		
//		gameRecordProducerService.sendGameRecord(checkGameRoomEndpoint);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}
}

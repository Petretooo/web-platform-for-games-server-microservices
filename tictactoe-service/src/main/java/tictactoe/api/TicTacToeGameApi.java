package tictactoe.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tictactoe.kafka.GameRecordProducerService;
import tictactoe.model.TicTacToeGame;
import tictactoe.service.game.TicTacToeGameService;

@RestController
@RequestMapping("/api/v1/games/tictactoe")
public class TicTacToeGameApi {
	
	String ENDPOINT = "/api/v1/games/tictactoe/";

	@Autowired
	private TicTacToeGameService gameService;
	
//	@Autowired
//	private GameRecordProducerService gameRecordProducerService;

	@GetMapping(value = "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TicTacToeGame> getGame(@PathVariable String gameId) {
		
		TicTacToeGame game = this.gameService.getGame(gameId);
		
//		String endpointGet = ENDPOINT + gameId;
//		gameRecordProducerService.sendGameRecord(endpointGet);
		
		return ResponseEntity.ok(game);
	}

	@PostMapping
	public ResponseEntity<String> createGame() {
		
		TicTacToeGame game = this.gameService.createGame();
		
//		gameRecordProducerService.sendGameRecord(ENDPOINT);
		return new ResponseEntity<>(game.getId(), HttpStatus.CREATED);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TicTacToeGame>> getRunningGames() {

		List<TicTacToeGame> games = this.gameService.getAllGames();
		
//		gameRecordProducerService.sendGameRecord(ENDPOINT);
		return ResponseEntity.ok(games);
	}

}

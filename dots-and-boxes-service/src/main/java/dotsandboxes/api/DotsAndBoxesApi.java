package dotsandboxes.api;

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

import dotsandboxes.model.Game;
import dotsandboxes.service.game.GameService;

@RestController
@RequestMapping("/api/v1/games/dotsandboxes")
public class DotsAndBoxesApi {
	
	
	@Autowired
	private GameService gameService;

	@GetMapping(value = "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Game> getGame(@PathVariable String gameId) {
		
		Game game = this.gameService.getGame(gameId);
		
//		String endpointGet = ENDPOINT + gameId;
//		gameRecordProducerService.sendGameRecord(endpointGet);
		
		return ResponseEntity.ok(game);
	}

	@PostMapping
	public ResponseEntity<String> createGame() {

		Game game = this.gameService.createGame();
		
//		gameRecordProducerService.sendGameRecord(ENDPOINT);
		return new ResponseEntity<>(game.getId(), HttpStatus.CREATED);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Game>> getRunningGames() {

		List<Game> games = this.gameService.getAllGames();
		
//		gameRecordProducerService.sendGameRecord(ENDPOINT);
		return ResponseEntity.ok(games);
	}

}

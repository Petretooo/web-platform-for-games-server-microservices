package hangman.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hangman.model.HangmanGameStats;
import hangman.service.gamestats.GameStatsService;

@RestController
@RequestMapping("/api/v1/game/stats")
public class GameStatsApi {
	
	@Autowired
	private GameStatsService gameStatsService;
	
	@GetMapping(value ="/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HangmanGameStats> getGameStats(@PathVariable String gameId) {
		HangmanGameStats gameStats = this.gameStatsService.getGameStats(gameId);
		return ResponseEntity.ok(gameStats);
	}

}

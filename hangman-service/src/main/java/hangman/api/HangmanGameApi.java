package hangman.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import hangman.dto.User;
import hangman.kafka.GameRecordProducerService;
import hangman.model.HangmanGame;
import hangman.model.HangmanGameStats;
import hangman.model.UserStats;
import hangman.service.game.HangmanGameService;
import hangman.service.gamestats.GameStatsService;
import hangman.service.rank.RankService;
import hangman.service.userstats.UserStatsService;
import hangman.util.GameMode;
import hangman.util.UserServiceException;

@RestController
@RequestMapping("/api/v1/games/hangman")
public class HangmanGameApi {
	
	private static final String ENDPOINT = "/api/v1/games/hangman/";
	
	@Autowired
	private HangmanGameService gameService;

	@Autowired
	private UserStatsService userStatsService;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private GameStatsService gameStatsService;
	
	@Autowired
	private RankService rankService;
	
	@Autowired
	private GameRecordProducerService gameRecordProducerService;

	private final static String URL_GAME_SELF_REF = "http://localhost:8080/hangman/api/v1/games/%s";

	@GetMapping(value = "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HangmanGame> getGame(@PathVariable String gameId) {
		
		HangmanGame game = gameService.getGame(gameId);
		Link link = Link.of(String.format(URL_GAME_SELF_REF, gameId)).withSelfRel();
		game.add(link);
		
//		String concreteEndpoint = ENDPOINT + "/{gameId}";
//		gameRecordProducerService.sendGameRecord(concreteEndpoint, GameMode.SINGLEPLAYER);
		return ResponseEntity.ok(game);
	}

	@PutMapping(value = "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void makeTry(@PathVariable String gameId, @RequestParam("letter") String letter) {
		
		HangmanGame game = gameService.getGame(gameId);
		gameService.enterCharacter(gameId, letter);
		ResponseEntity.noContent();

		if (gameService.resultWord(gameId) != null) {
			HangmanGameStats gameStat = gameStatsService.updateGameStats(gameId);
			UserStats stat = userStatsService.update(game);
			rankService.saveRank(stat, gameStat);
		}
		
//		String concreteEndpoint = ENDPOINT + "/{gameId}";
//		gameRecordProducerService.sendGameRecord(concreteEndpoint, GameMode.SINGLEPLAYER);
	}

	@PostMapping(value = "/create/{username}")
	public ResponseEntity<String> createGame(@PathVariable String username) {
						
		User user = null;
		
		try {
			user = restTemplate.getForObject("http://user-service/api/v1/users/{username}", User.class, username);
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}
		
		if (user != null) {
			HangmanGame game = gameService.createGame(user);

			userStatsService.save(user, game.getCurrentWord());
			gameStatsService.saveGameStats(game);
			return new ResponseEntity<>(game.getId(), HttpStatus.CREATED);
		}

		String concreteEndpoint = ENDPOINT + "/create/{username}";
		gameRecordProducerService.sendGameRecord(concreteEndpoint, GameMode.SINGLEPLAYER);
		return null;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<HangmanGame>> getRunningGames() {
		
		List<HangmanGame> games = gameService.getRunningGame();
		games.stream().forEach(e -> e.add(Link.of(String.format(URL_GAME_SELF_REF, e.getId())).withSelfRel()));
		
//		gameRecordProducerService.sendGameRecord(ENDPOINT, GameMode.SINGLEPLAYER);
		return ResponseEntity.ok(games);
	}

	@GetMapping(value = "/letters/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> getGameUsedLetters(@PathVariable String gameId) {
		
		List<String> letters = gameService.getusedLettersArray(gameId);
		
//		String concreteEndpoint = ENDPOINT + "/letters/{gameId}";
//		gameRecordProducerService.sendGameRecord(concreteEndpoint, GameMode.SINGLEPLAYER);
		return ResponseEntity.ok(letters);
	}

}

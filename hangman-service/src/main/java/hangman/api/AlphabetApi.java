package hangman.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hangman.service.game.HangmanGameService;


@RestController
@RequestMapping("/api/v1/alphabet")
public class AlphabetApi {

	@Autowired
	private HangmanGameService hangmanGameService;	
	
	@GetMapping(value = "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Character>> getAlphabet(@PathVariable String gameId) {
		return ResponseEntity.ok(hangmanGameService.getUnusedCharacters(gameId));
	}

}
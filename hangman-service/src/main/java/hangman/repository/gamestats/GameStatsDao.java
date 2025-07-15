package hangman.repository.gamestats;

import org.springframework.data.jpa.repository.JpaRepository;

import hangman.model.HangmanGameStats;

public interface GameStatsDao extends JpaRepository<HangmanGameStats, String>{
	
	HangmanGameStats findByGameId(String gameId);
	
}

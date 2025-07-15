package hangman.service.gamestats;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hangman.model.HangmanGame;
import hangman.model.HangmanGameStats;
import hangman.repository.gamestats.GameStatsDao;
import hangman.service.game.HangmanGameService;
import hangman.util.GameStatus;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class GameStatsServiceImpl implements GameStatsService {

	@Autowired
	private GameStatsDao gameStatsDao;

	@Autowired
	private HangmanGameService gameService;
	
	@Override
	public HangmanGameStats getGameStats(String gameId) {
		return this.gameStatsDao.findByGameId(gameId);
	}

	@Override
	public HangmanGameStats saveGameStats(HangmanGame game) {
		HangmanGameStats stats = new HangmanGameStats();
		stats.setGameStart(LocalDate.now());
		stats.setGame(game);
		gameStatsDao.save(stats);
		return stats;
	}

	@Override
	public void deleteGameStats(String gameStatsId) {
		HangmanGameStats stat = gameStatsDao.getOne(gameStatsId);
		gameStatsDao.delete(stat);
	}

	@Override
	public HangmanGameStats updateGameStats(String gameId) {
		HangmanGameStats gameStat = gameStatsDao.findByGameId(gameId);
		HangmanGame game = gameService.getGame(gameId);
		gameStat.setGameEnd(LocalDate.now());
		gameStat.setWrongTries(5 - game.getNumberTries());
		gameStat.setWordFound(gameService.resultWord(gameId).equals("win"));
		if(gameStat.isWordFound()) {
			gameStat.setGameResult(GameStatus.WON.name());
		}else {
			gameStat.setGameResult(GameStatus.LOST.name());
		}
		return gameStat;
	}
	
	@Override
	public HangmanGameStats updateGameStatsBuStatus(String gameId, GameStatus status) {
		HangmanGameStats gameStat = gameStatsDao.findByGameId(gameId);
		HangmanGame game = gameService.getGame(gameId);
		gameStat.setGameEnd(LocalDate.now());
		gameStat.setWrongTries(5 - game.getNumberTries());
		gameStat.setGameResult(status.name());
		return gameStat;
	}

}

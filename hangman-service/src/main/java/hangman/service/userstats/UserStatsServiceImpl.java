package hangman.service.userstats;

import java.util.List;
import java.util.Set;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hangman.dto.User;
import hangman.model.HangmanGame;
import hangman.model.HangmanGameStats;
import hangman.model.HangmanGameUser;
import hangman.model.UserStats;
import hangman.repository.gamestats.GameStatsDao;
import hangman.repository.userstats.UserStatsDao;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class UserStatsServiceImpl implements UserStatsService {

	@Autowired
	private UserStatsDao userStatsDao;

	@Autowired
	private GameStatsDao gameStatsDao;

	@Override
	public UserStats get(String gameStatsId) {
		return userStatsDao.getOne(gameStatsId);
	}

	@Override
	public UserStats save(User user, String word) {
		UserStats userStat = new UserStats();
		userStat.setUserId(user.getUserId());;
		userStat.setWord(word);
		return userStatsDao.save(userStat);
	}

	@Override
	public UserStats update(HangmanGame game) {
		HangmanGameStats gameStat = gameStatsDao.findByGameId(game.getId());
		Set<HangmanGameUser> users = game.getUsers();
		
		for (HangmanGameUser user : users) {
			
			List<UserStats> userStats = userStatsDao.findByIdUserStats(user.getHangmanGameUserId().getUserId());
			UserStats stat;
			for (UserStats userStats2 : userStats) {
				if (userStats2.getScore() == 0 && userStats2.getWord().equals(game.getCurrentWord())) {
					stat = userStats2;
					stat.setScore(scoreCalc(gameStat, game.getHiddenWord()));

					return stat;
				}
			}
		}

		return null;
	}

	private int scoreCalc(HangmanGameStats gameStats, char[] hiddenWord) {
		int score = 0;
		for (char c : hiddenWord) {
			if (c == '_') {
				continue;
			} else {
				score += 25;
			}
		}
		switch (gameStats.getWrongTries()) {
		case 0:
			score += 550;
			break;
		case 1:
			score += 450;
			break;
		case 2:
			score += 350;
			break;
		case 3:
			score += 250;
			break;
		case 4:
			score += 150;
			break;
		default:
			break;
		}

		score += hiddenWord.length * 3;

		return score;
	}

	@Override
	public void remove(String gameStatsId) {
		UserStats userStat = userStatsDao.getOne(gameStatsId);
		userStatsDao.delete(userStat);

	}

}

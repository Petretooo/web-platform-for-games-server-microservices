package hangman.service.rank;

import hangman.dto.RankingDtoList;
import hangman.model.HangmanGameStats;
import hangman.model.Ranking;
import hangman.model.UserStats;

public interface RankService {
	
	public Ranking saveRank(UserStats stat, HangmanGameStats gameStat);
	public void removeFromRank(String rankId);
	public RankingDtoList getRankList();
	public RankingDtoList getRankByMonth();
}

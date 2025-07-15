package hangman.service.rank;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import hangman.dto.RankingDto;
import hangman.dto.RankingDtoList;
import hangman.dto.User;
import hangman.model.HangmanGameStats;
import hangman.model.Ranking;
import hangman.model.UserStats;
import hangman.repository.rank.RankingDao;
import hangman.repository.rank.RankingSpecification;
import hangman.repository.rank.SearchCriteria;
import hangman.repository.rank.SearchOperation;
import hangman.util.UserServiceException;

@Service
@Transactional
public class RankServiceImpl implements RankService {

	@Autowired
	private RankingDao rankDao;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Ranking saveRank(UserStats stat, HangmanGameStats gameStat) {
		
		if(stat == null) {
			return null;
		}
		
		Ranking rank = new Ranking();
		rank.setIdUserRank(stat.getUserId());
		rank.setScore(stat.getScore());
		rank.setWord(gameStat.getGame().getCurrentWord());
		rank.setDate(gameStat.getGameStart());
		return rankDao.save(rank);
	}

	@Override
	public void removeFromRank(String rankId) {
		Ranking rank = rankDao.getOne(rankId);
		rankDao.delete(rank);
	}

	@Override
	public RankingDtoList getRankList() {
				
		ResponseEntity<User[]> response = null;
		
		try {
			response = restTemplate.getForEntity("http://user-service/api/v1/users", User[].class);
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}
		
		List<User> users = Arrays.asList(response.getBody());
		
		List<Ranking> ranks = rankDao.findTop10ByOrderByScoreDesc();
		
		for (Ranking rank : ranks) {
			
			User user = users.stream().filter(u -> u.getUserId().equals(rank.getIdUserRank())).findFirst().orElse(null);
			
			if(user != null) {
				rank.setUser(user);
			}
		}
		
		ranks.removeIf(r -> r.getUser() == null);
		
		List<RankingDto> rankingDtos = ranks.stream()
				.map(RankingDto::fromRanking)
				.collect(Collectors.toList());

		RankingDtoList dtoList = new RankingDtoList();
		dtoList.setDtoList(rankingDtos);
		return dtoList;
	}

	@Override
	public RankingDtoList getRankByMonth() {
		
		LocalDate ld = LocalDate.now();
		LocalDate start = ld.withDayOfMonth(1);
		LocalDate end = ld.withDayOfMonth(ld.lengthOfMonth());
		Pageable page = PageRequest.of(0, 10, Sort.by("score").descending());

		RankingSpecification specification = new RankingSpecification();
		specification.add(new SearchCriteria("date", start, SearchOperation.GREATER_THAN_EQUAL));
		specification.add(new SearchCriteria("date", end, SearchOperation.LESS_THAN_EQUAL));

		ResponseEntity<User[]> response = null;
				
		try {
			response = restTemplate.getForEntity("http://user-service/api/v1/users", User[].class);
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}
		
		List<User> users = Arrays.asList(response.getBody());

		List<Ranking> ranks = rankDao.findAll(specification);
		
		for (Ranking rank : ranks) {
			
			User user = users.stream().filter(u -> u.getUserId().equals(rank.getIdUserRank())).findFirst().orElse(null);
			
			if(user != null) {
				rank.setUser(user);
			}		
		}
		
		ranks.removeIf(r -> r.getUser() == null);
		
		List<RankingDto> rankingDtos = ranks.stream()
				.map(RankingDto::fromRanking)
				.sorted(Comparator.comparing(RankingDto::getScore).reversed())
				.limit(10)
				.collect(Collectors.toList());

		RankingDtoList dtoList = new RankingDtoList();
		dtoList.setDtoList(rankingDtos);
		return dtoList;

	}

}
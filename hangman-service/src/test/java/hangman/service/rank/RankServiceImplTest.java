package hangman.service.rank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import hangman.dto.RankingDtoList;
import hangman.dto.User;
import hangman.model.HangmanGame;
import hangman.model.HangmanGameStats;
import hangman.model.Ranking;
import hangman.model.UserStats;
import hangman.repository.rank.RankingDao;
import hangman.repository.rank.RankingSpecification;

class RankServiceImplTest {
	
    private static final String RANK_ID = "rankId";
	private static final int SCORE = 437;
	private static final int USER_SCORE = 0;
	private static final String SECOND_GAME_ID = "firstGameId";
    private static final String FIRST_GAME_ID = "secondGameId";
	private static final String WORD = "word";
	private static final String GAME_STATS_ID = "gameStatsId";
    private static final String FIRST_USER_ID = "firstUserId";
    private static final String SECOND_USER_ID = "secondUserId";
    private static final String SECOND_USER = "secondUser";
    private static final String SECOND_USER_EMAIL = "secondUserEmail";
    private static final String SECOND_USER_PASSWORD = "secondUserPassword";
    private static final String FIRST_USER = "firstUser";
    private static final String FIRST_USER_EMAIL = "firstUserEmail";
    private static final String FIRST_USER_PASSWORD = "firstUserPassword";
	
    @Mock
    private RankingDao rankDao;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RankServiceImpl rankService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveRank() {

        UserStats stat = new UserStats();
        stat.setUserId(SECOND_GAME_ID);
        stat.setScore(SCORE);

        HangmanGameStats gameStat = new HangmanGameStats();
        gameStat.setGame(new HangmanGame());
        gameStat.getGame().setCurrentWord(WORD);

        Ranking expectedRank = new Ranking();
        expectedRank.setIdUserRank(stat.getUserId());
        expectedRank.setScore(stat.getScore());
        expectedRank.setWord(gameStat.getGame().getCurrentWord());
        expectedRank.setDate(gameStat.getGameStart());

        when(rankDao.save(any(Ranking.class))).thenReturn(expectedRank);

        Ranking result = rankService.saveRank(stat, gameStat);

        assertNotNull(result);
        assertEquals(expectedRank, result);
    }

    @Test
    void shouldRemoveFromRank() {

        Ranking rank = new Ranking();
        when(rankDao.getOne(RANK_ID)).thenReturn(rank);

        rankService.removeFromRank(RANK_ID);

        verify(rankDao, times(1)).delete(rank);
    }

    @Test
    void shouldGetRankList() {
    	
        User user1 = new User(FIRST_USER, FIRST_USER_EMAIL, FIRST_USER_PASSWORD);
        user1.setUserId(FIRST_USER_ID);
        
        User user2 = new User(SECOND_USER, SECOND_USER_EMAIL, SECOND_USER_PASSWORD);
        user2.setUserId(SECOND_USER_ID);
        
        User[] users = {user1, user2};

        Ranking ranking1 = new Ranking();
        ranking1.setIdUserRank(FIRST_USER_ID);
        
        Ranking ranking2 = new Ranking();
        ranking2.setIdUserRank(SECOND_USER_ID);
        
        List<Ranking> ranks = Arrays.asList(ranking1, ranking2);

        when(restTemplate.getForEntity(anyString(), eq(User[].class))).thenReturn(ResponseEntity.ok(users));
        when(rankDao.findTop10ByOrderByScoreDesc()).thenReturn(ranks);

        RankingDtoList result = rankService.getRankList();

        assertNotNull(result);
        assertNotNull(result.getDtoList());
        assertEquals(2, result.getDtoList().size());
    }

    @Test
    void shouldGetRankByMonth() {
    	
        User user1 = new User(FIRST_USER, FIRST_USER_EMAIL, FIRST_USER_PASSWORD);
        user1.setUserId(FIRST_USER_ID);
        
        User user2 = new User(SECOND_USER, SECOND_USER_EMAIL, SECOND_USER_PASSWORD);
        user2.setUserId(SECOND_USER_ID);
        
        User[] users = {user1, user2};

        Ranking ranking1 = new Ranking();
        ranking1.setIdUserRank(FIRST_USER_ID);
        ranking1.setScore(SCORE);
        
        Ranking ranking2 = new Ranking();
        ranking2.setIdUserRank(SECOND_USER_ID);
        ranking2.setScore(SCORE);
        
        List<Ranking> ranks = Arrays.asList(ranking1, ranking2);

        when(restTemplate.getForEntity(anyString(), eq(User[].class))).thenReturn(ResponseEntity.ok(users));
        when(rankDao.findAll(any(RankingSpecification.class))).thenReturn(ranks);

        RankingDtoList result = rankService.getRankByMonth();

        assertNotNull(result);
        assertNotNull(result.getDtoList());
        assertEquals(2, result.getDtoList().size());
    }
}

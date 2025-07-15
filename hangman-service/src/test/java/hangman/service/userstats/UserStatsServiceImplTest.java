package hangman.service.userstats;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hangman.dto.User;
import hangman.model.HangmanGame;
import hangman.model.HangmanGameStats;
import hangman.model.HangmanGameUser;
import hangman.model.HangmanGameUserId;
import hangman.model.UserStats;
import hangman.repository.gamestats.GameStatsDao;
import hangman.repository.userstats.UserStatsDao;

class UserStatsServiceImplTest {
	
    private static final int EXPECTED_SCORE = 437;
	private static final int USER_SCORE = 0;
	private static final String SECOND_GAME_ID = "firstGameId";
    private static final String FIRST_GAME_ID = "secondGameId";
	private static final String WORD = "word";
	private static final String GAME_STATS_ID = "gameStatsId";
    private static final String FIRST_USER_ID = "firstUserId";
    private static final String SECOND_USER_ID = "secondUserId";

    private static final String FIRST_USER = "firstUser";
    private static final String FIRST_USER_EMAIL = "firstUserEmail";
    private static final String FIRST_USER_PASSWORD = "firstUserPassword";


	@Mock
    private UserStatsDao userStatsDao;

    @Mock
    private GameStatsDao gameStatsDao;

    @InjectMocks
    private UserStatsServiceImpl userStatsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetUserStats() {

        UserStats expectedUserStats = new UserStats();
        
        when(userStatsDao.getOne(GAME_STATS_ID)).thenReturn(expectedUserStats);

        UserStats result = userStatsService.get(GAME_STATS_ID);

        assertNotNull(result);
        assertEquals(expectedUserStats, result);
    }

    @Test
    void shouldSaveUserStats() {
    	
        User user = new User(FIRST_USER, FIRST_USER_EMAIL, FIRST_USER_PASSWORD);
        user.setUserId(FIRST_USER_ID);
        
        UserStats expectedUserStats = new UserStats();
        expectedUserStats.setUserId(user.getUserId());
        expectedUserStats.setWord(WORD);

        when(userStatsDao.save(any(UserStats.class))).thenReturn(expectedUserStats);

        UserStats result = userStatsService.save(user, WORD);

        assertNotNull(result);
        assertEquals(expectedUserStats, result);
    }

    @Test
    void shouldUpdateUserStats() {

        HangmanGame game = new HangmanGame();
        game.setId(FIRST_GAME_ID);
        game.setCurrentWord(WORD);
        
        char[] hiddenWord = {'w', '_', 'r', 'd'};
        game.setHiddenWord(hiddenWord);
        
        HangmanGameStats gameStats = new HangmanGameStats();
        gameStats.setWrongTries(2);

        HangmanGameUserId firstGameUserId = new HangmanGameUserId();
        firstGameUserId.setUserId(FIRST_USER_ID);
        firstGameUserId.setGameId(FIRST_GAME_ID);
        
        HangmanGameUser firstHangmaGameUser = new HangmanGameUser();
        firstHangmaGameUser.setHangmanGameUserId(firstGameUserId);
        
        HangmanGameUserId secondGameUserId = new HangmanGameUserId();
        secondGameUserId.setUserId(SECOND_USER_ID);
        secondGameUserId.setGameId(FIRST_GAME_ID);
        
        HangmanGameUser secondHangmaGameUser = new HangmanGameUser();
        secondHangmaGameUser.setHangmanGameUserId(firstGameUserId);
        
        Set<HangmanGameUser> users = Set.of(firstHangmaGameUser, secondHangmaGameUser);

        List<UserStats> userStatsList = new ArrayList<>();
        UserStats userStats1 = new UserStats();
        userStats1.setScore(USER_SCORE);
        userStats1.setWord(WORD);
        userStatsList.add(userStats1);
        
        game.setUsers(users);

        when(gameStatsDao.findByGameId(game.getId())).thenReturn(gameStats);
        when(userStatsDao.findByIdUserStats(FIRST_USER_ID)).thenReturn(userStatsList);
        when(userStatsDao.findByIdUserStats(SECOND_USER_ID)).thenReturn(new ArrayList<>());

        UserStats result = userStatsService.update(game);

        assertNotNull(result);
        assertEquals(userStats1, result);
        assertEquals(EXPECTED_SCORE, result.getScore());
    }

    @Test
    void shouldRemove() {

        UserStats userStats = new UserStats();
        when(userStatsDao.getOne(GAME_STATS_ID)).thenReturn(userStats);

        userStatsService.remove(GAME_STATS_ID);

        verify(userStatsDao, times(1)).delete(userStats);
    }
}
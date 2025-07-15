package hangman.service.multiplayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import hangman.dto.GameRoomInfoDto;
import hangman.dto.GameRoomResultsDto;
import hangman.dto.GameRoomTryDto;
import hangman.dto.RoomUserDto;
import hangman.dto.User;
import hangman.model.HangmanGame;
import hangman.model.HangmanGameRoom;
import hangman.model.HangmanGameStats;
import hangman.model.UserStats;
import hangman.service.game.HangmanGameService;
import hangman.service.gameroom.HangmanGameRoomService;
import hangman.service.gamestats.GameStatsService;
import hangman.service.rank.RankService;
import hangman.service.userstats.UserStatsService;
import hangman.util.GameStatus;

class HangmanMultiplayerServiceImplTest {
    @Mock
    private UserStatsService userStatsService;

    @Mock
    private HangmanGameService gameService;

    @Mock
    private HangmanGameRoomService gameRoomService;

    @Mock
    private GameStatsService gameStatsService;

    @Mock
    private RankService rankService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private HangmanMultiplayerServiceImpl hangmanMultiplayerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCheckGameRoomIsDraw() {

        char[] hiddenWord = {'w', '_', 'r', 'd'};
        String gameRoomId = "gameRoomId";
        String userId = "userId";
        
        RoomUserDto userDto = new RoomUserDto();
        userDto.setUserId(userId);
        
        HangmanGame userGame = new HangmanGame();
        userGame.setNumberTries(0);
        userGame.setHiddenWord(hiddenWord);
        userGame.setCurrentWord("Word");
        
        userDto.setGameInfo(userGame);

        RoomUserDto competitorDto = new RoomUserDto();
        competitorDto.setUserId("competitorId");
        
        HangmanGame competitorGame = new HangmanGame();
        competitorGame.setNumberTries(0);
        competitorGame.setHiddenWord(hiddenWord);
        competitorGame.setCurrentWord("Word");
        
        competitorDto.setGameInfo(competitorGame);

        GameRoomInfoDto infoDto = new GameRoomInfoDto();
        infoDto.setGameRoomId(gameRoomId);
        infoDto.setFirstUser(userDto);
        infoDto.setSecondUser(competitorDto);

        when(gameRoomService.getGameRoom(gameRoomId)).thenReturn(new HangmanGameRoom());
        when(gameStatsService.updateGameStatsBuStatus(anyString(), any())).thenReturn(new HangmanGameStats());
        when(userStatsService.update(any())).thenReturn(new UserStats());
        
        GameRoomTryDto roomTryDto = new GameRoomTryDto();
        roomTryDto.setUserId(userId);
        
        GameRoomResultsDto result = hangmanMultiplayerService.checkGameRoom(infoDto, roomTryDto);

        assertNotNull(result);
        assertTrue(result.isDraw());
    }

    @Test
    void testCheckGameRoomUserHasWinner() {

        char[] hiddenWordOne = {'w', 'o', 'r', 'd'};
        char[] hiddenWordTwo = {'w', '_', 'r', 'd'};

        String gameRoomId = "gameRoomId";
        String userId = "userId";
        
        RoomUserDto userDto = new RoomUserDto();
        userDto.setUserId(userId);
        
        HangmanGame userGame = new HangmanGame();
        userGame.setNumberTries(2);
        userGame.setHiddenWord(hiddenWordOne);
        userGame.setCurrentWord("word");
        
        userDto.setGameInfo(userGame);

        RoomUserDto competitorDto = new RoomUserDto();
        competitorDto.setUserId("competitorId");
        
        HangmanGame competitorGame = new HangmanGame();
        competitorGame.setNumberTries(4);
        competitorGame.setHiddenWord(hiddenWordTwo);
        competitorGame.setCurrentWord("Word");
        
        competitorDto.setGameInfo(competitorGame);

        GameRoomInfoDto infoDto = new GameRoomInfoDto();
        infoDto.setGameRoomId(gameRoomId);
        infoDto.setFirstUser(userDto);
        infoDto.setSecondUser(competitorDto);

        when(gameRoomService.getGameRoom(gameRoomId)).thenReturn(new HangmanGameRoom());
        when(gameStatsService.updateGameStatsBuStatus(anyString(), any())).thenReturn(new HangmanGameStats());
        when(userStatsService.update(any())).thenReturn(new UserStats());
        
        GameRoomTryDto roomTryDto = new GameRoomTryDto();
        roomTryDto.setUserId(userId);
        
        GameRoomResultsDto result = hangmanMultiplayerService.checkGameRoom(infoDto, roomTryDto);

        assertNotNull(result);
        assertEquals(result.getWinnerId(), userId);
    }
}
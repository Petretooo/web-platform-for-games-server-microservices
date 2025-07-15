package hangman.service.gameroom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import hangman.dto.GameRoomInfoDto;
import hangman.dto.GameRoomPlayerDto;
import hangman.dto.GameRoomTryDto;
import hangman.dto.RoomUserDto;
import hangman.dto.User;
import hangman.model.HangmanGame;
import hangman.model.HangmanGameRoom;
import hangman.model.Word;
import hangman.repository.gameroom.HangmanGameRoomRepository;
import hangman.service.alphabet.AlphabetService;
import hangman.service.game.HangmanGameService;
import hangman.service.gamestats.GameStatsService;
import hangman.service.userstats.UserStatsService;
import hangman.service.word.WordService;

class HangmanGameRoomServiceImplTest {
	
    private static final String FIRST_USER_ID = "firstUserId";
    private static final String FIRST_USER = "firstUser";
    private static final String FIRST_USER_EMAIL = "firstUserEmail";
    private static final String FIRST_USER_PASSWORD = "firstUserPassword";
    
    private static final String SECOND_USER_ID = "secondUserId";
    private static final String SECOND_USER = "secondUser";
    private static final String SECOND_USER_EMAIL = "secondUserEmail";
    private static final String SECOND_USER_PASSWORD = "secondUserPassword";

    @Mock
    private HangmanGameRoomRepository gameRoomRepository;

    @Mock
    private UserStatsService userStatsService;

    @Mock
    private HangmanGameService gameService;

    @Mock
    private AlphabetService alphabetService;

    @Mock
    private WordService wordService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GameStatsService gameStatsService;

    @InjectMocks
    private HangmanGameRoomServiceImpl gameRoomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllRooms() {
    	
        HangmanGameRoom room1 = new HangmanGameRoom();
        HangmanGameRoom room2 = new HangmanGameRoom();
        List<HangmanGameRoom> expectedRooms = Arrays.asList(room1, room2);

        when(gameRoomRepository.findByActiveTrue()).thenReturn(expectedRooms);

        List<HangmanGameRoom> result = gameRoomService.getAllRooms();

        assertEquals(expectedRooms, result);
        verify(gameRoomRepository).findByActiveTrue();
    }

    @Test
    void shouldGetGameRoom() {
    	
        String gameRoomId = "gameRoomId";
        HangmanGameRoom expectedRoom = new HangmanGameRoom();

        when(gameRoomRepository.findByGameRoomId(gameRoomId)).thenReturn(expectedRoom);

        HangmanGameRoom result = gameRoomService.getGameRoom(gameRoomId);

        assertEquals(expectedRoom, result);
        verify(gameRoomRepository).findByGameRoomId(gameRoomId);
    }

    @Test
    void shouldCreateHangmanGameRoom() {
    	
        String gameRoomName = "gameRoomName";
        HangmanGameRoom expectedRoom = new HangmanGameRoom();

        when(wordService.randomWordGenerator()).thenReturn(new Word());
        when(gameRoomRepository.save(any(HangmanGameRoom.class))).thenReturn(expectedRoom);

        HangmanGameRoom result = gameRoomService.createHangmanGameRoom(gameRoomName);

        assertEquals(gameRoomName, result.getGameRoomName());
        assertTrue(result.isActive());
        verify(wordService).randomWordGenerator();
        verify(gameRoomRepository).save(any(HangmanGameRoom.class));
    }

    @Test
    void shouldDeleteGameRoom() {
    	
        String gameRoomId = "gameRoomId";

        gameRoomService.deleteGameRoom(gameRoomId);

        verify(gameRoomRepository).deleteById(gameRoomId);
    }

    @Test
    void shouldAddSecondPlayer() {
    	
        String gameRoomId = "gameRoomId";
        String secondPlayerId = "secondPlayerId";
        HangmanGameRoom room = new HangmanGameRoom();
        
        User user = new User(FIRST_USER, FIRST_USER_EMAIL, FIRST_USER_PASSWORD);
        user.setUserId(FIRST_USER_ID);
        
        when(gameRoomRepository.findById(gameRoomId)).thenReturn(Optional.of(room));
        when(restTemplate.getForObject(anyString(), any(), anyString())).thenReturn(user);

        boolean result = gameRoomService.addSecondPlayer(gameRoomId, secondPlayerId);

        assertTrue(result);
        assertEquals(secondPlayerId, room.getSecondUserId());
        verify(gameRoomRepository).findById(gameRoomId);
        verify(restTemplate).getForObject(anyString(), any(), anyString());
    }

//    @Test
//    void shouldAddSecondPlayer_InvalidRoomOrUser() {
//    	
//        String gameRoomId = "gameRoomId";
//        String secondPlayerId = "secondPlayerId";
//        HangmanGameRoom room = null;
//        User user = null;
//
//        when(gameRoomRepository.findById(gameRoomId)).thenReturn(Optional.ofNullable(room));
//        when(restTemplate.getForObject(anyString(), any(), anyString())).thenReturn(user);
//
//        boolean result = gameRoomService.addSecondPlayer(gameRoomId, secondPlayerId);
//
//        assertFalse(result);
//        verify(gameRoomRepository).findById(gameRoomId);
//        verify(restTemplate).getForObject(anyString(), any(), anyString());
//    }

//    @Test
//    void shouldGetGameRoomInfo() {
//    	
//        String gameRoomId = "gameRoomId";
//        HangmanGameRoom room = new HangmanGameRoom();
//        GameRoomInfoDto expectedDto = new GameRoomInfoDto();
//
//        when(gameRoomRepository.findByGameRoomId(gameRoomId)).thenReturn(room);
//        when(gameRoomService.mapGameRoomToDto(room)).thenReturn(expectedDto);
//
//        GameRoomInfoDto result = gameRoomService.getGameRoomInfo(gameRoomId);
//
//        assertEquals(expectedDto, result);
//        verify(gameRoomRepository).findByGameRoomId(gameRoomId);
//        verify(gameRoomService).mapGameRoomToDto(room);
//    }

    @Test
    void shouldMapGameRoomToDto() {
    	
        HangmanGameRoom room = new HangmanGameRoom();
        room.setGameRoomId("gameRoomId");
        room.setGameRoomName("gameRoomName");
        room.setGameTitle("HANGMAN");
        room.setWord("word");
        room.setFirstUserId(FIRST_USER_ID);
        room.setSecondUserId(SECOND_USER_ID);
        
        HangmanGame game = new HangmanGame();
        room.setFirstUserGame(game);
        room.setSecondUserGame(game);

        User firstUser = new User(FIRST_USER, FIRST_USER_EMAIL, FIRST_USER_PASSWORD);
        firstUser.setUserId(FIRST_USER_ID);

        User secondUser = new User(SECOND_USER, SECOND_USER_EMAIL, SECOND_USER_PASSWORD);
        secondUser.setUserId(SECOND_USER_ID);

        when(restTemplate.getForObject(anyString(), any(), eq(FIRST_USER_ID))).thenReturn(firstUser);
        when(restTemplate.getForObject(anyString(), any(), eq(SECOND_USER_ID))).thenReturn(secondUser);

        GameRoomInfoDto result = gameRoomService.mapGameRoomToDto(room);

        assertEquals(room.getGameRoomId(), result.getGameRoomId());
        assertEquals(room.getGameRoomName(), result.getGameRoomName());
        assertEquals(room.getGameTitle(), result.getGameTitle());
        assertEquals(room.getWord(), result.getWord());
        assertNotNull(result.getFirstUser());
        assertNotNull(result.getSecondUser());
        verify(restTemplate).getForObject(anyString(), any(), eq(FIRST_USER_ID));
        verify(restTemplate).getForObject(anyString(), any(), eq(SECOND_USER_ID));
    }

//    @Test
//    void testMakeTry() {
//    	
//        HangmanGameRoom room = new HangmanGameRoom();
//        room.setGameRoomId("gameRoomId");
//        room.setGameRoomName("gameRoomName");
//        room.setGameTitle("HANGMAN");
//        room.setWord("word");
//        room.setFirstUserId(FIRST_USER_ID);
//        room.setSecondUserId(SECOND_USER_ID);
//        
//        HangmanGame game = new HangmanGame();
//        room.setFirstUserGame(game);
//        room.setSecondUserGame(game);
//
//        User firstUser = new User(FIRST_USER, FIRST_USER_EMAIL, FIRST_USER_PASSWORD);
//        firstUser.setUserId(FIRST_USER_ID);
//
//        User secondUser = new User(SECOND_USER, SECOND_USER_EMAIL, SECOND_USER_PASSWORD);
//        secondUser.setUserId(SECOND_USER_ID);
//    	
//        String gameRoomId = "gameRoomId";
//        GameRoomTryDto message = new GameRoomTryDto();
//        message.setUserId(FIRST_USER);
//
//        GameRoomInfoDto roomInfo = new GameRoomInfoDto();
//        roomInfo.setGameRoomId(gameRoomId);
//
//        RoomUserDto userDto = new RoomUserDto();
//        userDto.setUserId(FIRST_USER);
//
//        when(restTemplate.getForObject(anyString(), any(), FIRST_USER_ID)).thenReturn(firstUser);
//        when(restTemplate.getForObject(anyString(), any(), SECOND_USER_ID)).thenReturn(secondUser);
//        when(gameRoomRepository.findByGameRoomId(gameRoomId)).thenReturn(room);
//        when(gameRoomService.getGameRoomInfo(gameRoomId)).thenReturn(roomInfo);
//
//        GameRoomInfoDto result = gameRoomService.makeTry(gameRoomId, message);
//
//        assertEquals(roomInfo, result);
//        verify(gameRoomService).getGameRoomInfo(gameRoomId);
//        verify(gameRoomService).changeGameForUser(eq(userDto), eq(message.getMessage()));
//    }

    @Test
    void shouldChangeGameForUser() {
        RoomUserDto roomUserDto = new RoomUserDto();
        roomUserDto.setGameInfo(new HangmanGame());
        String letter = "A";

        gameRoomService.changeGameForUser(roomUserDto, letter);

        verify(gameService).enterCharacter(eq(roomUserDto.getGameInfo().getId()), eq(letter));
        verify(alphabetService).getUnusedCharacters(eq(roomUserDto.getGameInfo().getId()));
    }

//    @Test
//    void testJoinGameRoom() {
//        GameRoomPlayerDto roomPlayerDto = new GameRoomPlayerDto();
//        roomPlayerDto.setGameRoomId("gameRoomId");
//        roomPlayerDto.setUserId(FIRST_USER_ID);
//
//        HangmanGameRoom room = new HangmanGameRoom();
//        room.setGameRoomId("gameRoomId");
//        room.setFirstUserId(FIRST_USER_ID);
//
//        User firstUser = new User(FIRST_USER, FIRST_USER_EMAIL, FIRST_USER_PASSWORD);
//        firstUser.setUserId(FIRST_USER_ID);
//
//        when(gameRoomRepository.findByGameRoomId(roomPlayerDto.getGameRoomId())).thenReturn(room);
//        when(restTemplate.getForObject(anyString(), any(), eq(roomPlayerDto.getUserId()))).thenReturn(firstUser);
//
//        gameRoomService.joinGameRoom(roomPlayerDto);
//
//        assertEquals(firstUser, room.getFirstUser());
//        assertEquals(firstUser.getUserId(), room.getFirstUserId());
//        verify(gameRoomRepository).findByGameRoomId(roomPlayerDto.getGameRoomId());
//        verify(restTemplate).getForObject(anyString(), any(), eq(roomPlayerDto.getUserId()));
//    }
//
//    @Test
//    void testJoinGameRoom_UserAlreadyInRoom() {
//        GameRoomPlayerDto roomPlayerDto = new GameRoomPlayerDto();
//        roomPlayerDto.setGameRoomId("gameRoomId");
//        roomPlayerDto.setUserId("userId");
//
//        HangmanGameRoom room = new HangmanGameRoom();
//        room.setGameRoomId("gameRoomId");
//        room.setFirstUserId("userId");
//
//        User user = new User();
//        user.setUserId("userId");
//
//        when(gameRoomRepository.findByGameRoomId(roomPlayerDto.getGameRoomId())).thenReturn(room);
//        when(restTemplate.getForObject(anyString(), any(), eq(roomPlayerDto.getUserId()))).thenReturn(user);
//
//        gameRoomService.joinGameRoom(roomPlayerDto);
//
//        verify(gameRoomRepository).findByGameRoomId(roomPlayerDto.getGameRoomId());
//        verify(restTemplate).getForObject(anyString(), any(), eq(roomPlayerDto.getUserId()));
//        verifyNoMoreInteractions(gameRoomRepository, restTemplate);
//    }
//
//    @Test
//    void testLeaveGameRoom() {
//        GameRoomPlayerDto roomPlayerDto = new GameRoomPlayerDto();
//        roomPlayerDto.setGameRoomId("gameRoomId");
//        roomPlayerDto.setUserId("userId");
//
//        HangmanGameRoom room = new HangmanGameRoom();
//        room.setGameRoomId("gameRoomId");
//        room.setFirstUserId("userId");
//
//        User user = new User();
//        user.setUserId("userId");
//
//        when(gameRoomRepository.findByGameRoomId(roomPlayerDto.getGameRoomId())).thenReturn(room);
//        when(restTemplate.getForObject(anyString(), any(), eq(room.getFirstUserId()))).thenReturn(user);
//
//        gameRoomService.leaveGameRoom(roomPlayerDto);
//
//        assertNull(room.getFirstUserId());
//        verify(gameRoomRepository).findByGameRoomId(roomPlayerDto.getGameRoomId());
//        verify(restTemplate).getForObject(anyString(), any(), eq(room.getFirstUserId()));
//    }
//
//    @Test
//    void testLeaveGameRoom_UserNotInRoom() {
//        GameRoomPlayerDto roomPlayerDto = new GameRoomPlayerDto();
//        roomPlayerDto.setGameRoomId("gameRoomId");
//        roomPlayerDto.setUserId("userId");
//
//        HangmanGameRoom room = new HangmanGameRoom();
//        room.setGameRoomId("gameRoomId");
//        room.setFirstUserId("otherUserId");
//
//        User user = new User();
//        user.setUserId("userId");
//
//        when(gameRoomRepository.findByGameRoomId(roomPlayerDto.getGameRoomId())).thenReturn(room);
//        when(restTemplate.getForObject(anyString(), any(), eq(room.getFirstUserId()))).thenReturn(user);
//
//        gameRoomService.leaveGameRoom(roomPlayerDto);
//
//        verify(gameRoomRepository).findByGameRoomId(roomPlayerDto.getGameRoomId());
//        verify(restTemplate).getForObject(anyString(), any(), eq(room.getFirstUserId()));
//        verifyNoMoreInteractions(gameRoomRepository, restTemplate);
//    }
}
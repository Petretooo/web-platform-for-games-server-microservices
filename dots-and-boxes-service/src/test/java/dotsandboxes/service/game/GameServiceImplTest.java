package dotsandboxes.service.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import dotsandboxes.model.Box;
import dotsandboxes.model.Dot;
import dotsandboxes.model.Game;
import dotsandboxes.model.GameBox;
import dotsandboxes.repository.box.BoxRepository;
import dotsandboxes.repository.dot.DotRepository;
import dotsandboxes.repository.edge.EdgeRepository;
import dotsandboxes.repository.game.DotsAndBoxesGameRepository;
import dotsandboxes.repository.gamebox.GameBoxRepository;

public class GameServiceImplTest {

    @Mock
    private BoxRepository boxRepository;

    @Mock
    private GameBoxRepository gameBoxRepository;

    @Mock
    private EdgeRepository edgeRepository;

    @Mock
    private DotRepository dotRepository;

    @Mock
    private DotsAndBoxesGameRepository gameRepository;

    @InjectMocks
    private GameServiceImpl gameService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateGame() {
    	
    	Box box1 = new Box();
    	box1.setBoxName("A1A2B1B2");
    	
        List<Box> boxes = new ArrayList<>();
        boxes.add(box1);

        Dot dotA1 = new Dot();
        dotA1.setXAxis("A");
        dotA1.setYAxis("1");
        
        Dot dotA2 = new Dot();
        dotA2.setXAxis("A");
        dotA2.setYAxis("2");
        
        Dot dotB1 = new Dot();
        dotB1.setXAxis("B");
        dotB1.setYAxis("1");
        
        Dot dotB2 = new Dot();
        dotB2.setXAxis("B");
        dotB2.setYAxis("2");
        
        List<Dot> dots = new ArrayList<>();
        dots.add(dotA1);
        dots.add(dotA2);
        dots.add(dotB1);
        dots.add(dotB2);

        Game game = new Game();
        
        List<GameBox> gameBoxes = new ArrayList<>();
        
        GameBox gameBox1 = new GameBox();
        gameBox1.setBox(box1);
        gameBox1.setGame(game);
        
        gameBoxes.add(gameBox1);
        
        
        when(boxRepository.findAll()).thenReturn(boxes);
        when(dotRepository.findAll()).thenReturn(dots);
        when(gameBoxRepository.saveAll(anyList())).thenReturn(gameBoxes);
        when(edgeRepository.saveAll(anyList())).thenReturn(new ArrayList<>());
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        
        Game result = gameService.createGame();
        
        verify(gameRepository).save(any(Game.class));
        verify(boxRepository).findAll();
        verify(dotRepository).findAll();
        verify(gameBoxRepository).saveAll(anyList());
        verify(edgeRepository).saveAll(anyList());

        assertNotNull(result);
    }

    @Test
    public void shouldGetGame() {
    	
        String gameId = "gameId";
        Game game = new Game();
        when(gameRepository.getById(gameId)).thenReturn(game);

        Game result = gameService.getGame(gameId);

        verify(gameRepository).getById(gameId);
        assertEquals(game, result);
    }

    @Test
    public void shouldDeleteGame() {
    	
        String gameId = "gameId";

        gameService.deleteGame(gameId);

        verify(gameRepository).deleteById(gameId);
    }

    @Test
    public void shouldUpdateGameWinner() {
    	
        String gameId = "gameId";
        String username = "winner";

        Game game = new Game();
        when(gameRepository.getById(gameId)).thenReturn(game);

        gameService.updateGameWinner(gameId, username);

        verify(gameRepository).getById(gameId);
        assertEquals(username, game.getWinnerUsername());
    }

    @Test
    public void shouldGetAllGames() {
    	
        List<Game> games = new ArrayList<>();
        games.add(new Game());
        games.add(new Game());
        when(gameRepository.findAll()).thenReturn(games);

        List<Game> result = gameService.getAllGames();

        verify(gameRepository).findAll();
        assertEquals(games, result);
    }
}
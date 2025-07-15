package dotsandboxes.service.game;

import java.util.List;

import dotsandboxes.model.Game;

public interface GameService {
	
	  public Game createGame();
	  
	  public Game getGame(String id);
	  
	  public void deleteGame(String id);
	  
	  public void updateGameWinner(String id, String username);
	  
	  public List<Game> getAllGames();

}

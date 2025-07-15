package tictactoe.service.game;

import java.util.List;

import tictactoe.model.TicTacToeGame;

public interface TicTacToeGameService {
	
	  public TicTacToeGame createGame();
	  
	  public TicTacToeGame getGame(String id);
	  
	  public void deleteGame(String id);
	  
	  public void updateGameWinner(String id, String username);
	  
	  public List<TicTacToeGame> getAllGames();
	  
}

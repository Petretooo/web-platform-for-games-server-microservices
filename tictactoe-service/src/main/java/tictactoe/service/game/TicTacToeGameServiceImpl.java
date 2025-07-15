package tictactoe.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import tictactoe.model.TicTacToeGame;
import tictactoe.repository.game.TicTacToeGameRepository;

@Service
@AllArgsConstructor
@Transactional
public class TicTacToeGameServiceImpl implements TicTacToeGameService {
	
	@Autowired
	private TicTacToeGameRepository gameRepository;	
	
	@Override
	public TicTacToeGame createGame() {
		
		TicTacToeGame game = new TicTacToeGame();
		this.gameRepository.save(game);
		
		return game;
	}
	
	@Override
	public TicTacToeGame getGame(String id) {
		return this.gameRepository.getById(id);
	}

	@Override
	public void deleteGame(String id) {
		this.gameRepository.deleteById(id);
	}

	@Override
	public void updateGameWinner(String id, String username) {
		
		TicTacToeGame game = this.gameRepository.getById(id);
		game.setWinnerUsername(username);		
	}

	@Override
	public List<TicTacToeGame> getAllGames() {
		return this.gameRepository.findAll();
	}
}
	
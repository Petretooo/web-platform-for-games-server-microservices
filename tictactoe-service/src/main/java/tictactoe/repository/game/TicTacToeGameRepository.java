package tictactoe.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tictactoe.model.TicTacToeGame;

@Repository
public interface TicTacToeGameRepository extends JpaRepository<TicTacToeGame, String> {

}

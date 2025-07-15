package tictactoe.repository.gameroom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tictactoe.model.TicTacToeGameRoom;

@Repository
public interface TicTacToeGameRoomRepository extends JpaRepository<TicTacToeGameRoom, String>{
	
	TicTacToeGameRoom findByGameRoomId(String gameRoomId);
	
	List<TicTacToeGameRoom> findByActiveTrue();

}

package hangman.repository.gameroom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hangman.model.HangmanGameRoom;

public interface HangmanGameRoomRepository extends JpaRepository<HangmanGameRoom, String>{
	
	HangmanGameRoom findByGameRoomId(String gameRoomId);
	
	List<HangmanGameRoom> findByActiveTrue();

}

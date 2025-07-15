package dotsandboxes.repository.gameroom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dotsandboxes.model.GameRoom;

@Repository
public interface DotsAndBoxesGameRoomRepository extends JpaRepository<GameRoom, String>{
	
	GameRoom findByGameRoomId(String gameRoomId);
	
	List<GameRoom> findByActiveTrue();

}

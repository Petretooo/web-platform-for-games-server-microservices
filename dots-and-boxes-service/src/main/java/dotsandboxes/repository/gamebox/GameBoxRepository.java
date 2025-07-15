package dotsandboxes.repository.gamebox;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dotsandboxes.model.GameBox;
import dotsandboxes.model.GameBoxId;

public interface GameBoxRepository extends JpaRepository<GameBox, GameBoxId> {

	List<GameBox> findAllByGameId(String gameId);

}

package dotsandboxes.repository.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dotsandboxes.model.Game;

@Repository
public interface DotsAndBoxesGameRepository extends JpaRepository<Game, String>{

}

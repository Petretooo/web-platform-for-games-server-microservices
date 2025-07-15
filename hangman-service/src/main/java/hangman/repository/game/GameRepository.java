package hangman.repository.game;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hangman.model.HangmanGame;

@Repository
public interface GameRepository extends CrudRepository<HangmanGame, String>{

}

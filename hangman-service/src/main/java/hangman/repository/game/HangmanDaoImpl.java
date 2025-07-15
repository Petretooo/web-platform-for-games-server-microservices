package hangman.repository.game;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import hangman.model.HangmanGame;
import hangman.model.HangmanGameStats;

@Repository
public class HangmanDaoImpl implements HangmanGameDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public HangmanGame get(String id) {
		return entityManager.find(HangmanGame.class, id);
	}

	@Override
	public void remove(String gameId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaDelete<HangmanGame> criteriaDelete = cb.createCriteriaDelete(HangmanGame.class);
		Root<HangmanGame> root = criteriaDelete.from(HangmanGame.class);
		criteriaDelete.where(cb.equal(root.get("id"), gameId));
		entityManager.createQuery(criteriaDelete).executeUpdate();
	}

	@Override
	public List<HangmanGame> getAll() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<HangmanGame> cq = cb.createQuery(HangmanGame.class);
		Root<HangmanGame> rootGame = cq.from(HangmanGame.class);
		CriteriaQuery<HangmanGame> all = cq.select(rootGame);
		TypedQuery<HangmanGame> allQuery = entityManager.createQuery(all);
		return allQuery.getResultList();
	}

	@Override
	public void save(HangmanGame entity) {
		entityManager.persist(entity);
	}

	@Override
	public void updateGame(HangmanGame game, String gameId) {
		HangmanGame currentGame = entityManager.find(HangmanGame.class, gameId);
		currentGame.setHiddenWord(game.getHiddenWord());
		entityManager.persist(currentGame);
	}

	@Override
	public List<HangmanGame> getRunningGames() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<HangmanGame> queryCriteria = cb.createQuery(HangmanGame.class);
		Root<HangmanGame> games = queryCriteria.from(HangmanGame.class);
		Join<HangmanGame,HangmanGameStats> joinStats = games.join("game.gameStats");
		queryCriteria.select(games);
		queryCriteria.where(cb.equal(games.get("game.id"), joinStats.get("gameStats.gameId")));
		queryCriteria.where(cb.isNull(joinStats.get("gameStats.gameEnd")));
		TypedQuery<HangmanGame> joinedResult = entityManager.createQuery(queryCriteria);
		return joinedResult.getResultList();
	}
	

}

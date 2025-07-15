package hangman.repository.symbol;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import hangman.model.Symbol;

@Repository
public class SymbolDaoImpl implements SymbolDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Symbol get(String id) {
		return entityManager.find(Symbol.class, id);
	}

	@Override
	public void remove(String id) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaDelete<Symbol> criteriaDelete = cb.createCriteriaDelete(Symbol.class);
		Root<Symbol> root = criteriaDelete.from(Symbol.class);
		criteriaDelete.where(cb.equal(root.get("characterId"), id));
		entityManager.createQuery(criteriaDelete).executeUpdate();
	}

	@Override
	public List<Symbol> getAll() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Symbol> cq = cb.createQuery(Symbol.class);
		Root<Symbol> rootCharacter = cq.from(Symbol.class);
		CriteriaQuery<Symbol> all = cq.select(rootCharacter);
		TypedQuery<Symbol> allQuery = entityManager.createQuery(all);
		return allQuery.getResultList();
	}

	@Override
	public void save(Symbol entity) {
		entityManager.persist(entity);
		
	}

}

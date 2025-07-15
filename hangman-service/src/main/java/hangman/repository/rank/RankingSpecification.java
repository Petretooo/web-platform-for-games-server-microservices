package hangman.repository.rank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import hangman.model.Ranking;

public class RankingSpecification implements Specification<Ranking> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<SearchCriteria> list;

	public RankingSpecification() {
		this.list = new ArrayList<>();
	}

	public void add(SearchCriteria criteria) {
		list.add(criteria);
	}

	@Override
	public Predicate toPredicate(Root<Ranking> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

		List<Predicate> predicates = new ArrayList<>();

		for (SearchCriteria criteria : list) {
			if (criteria.getOperation().equals(SearchOperation.GREATER_THAN)) {
				predicates
						.add(criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString()));
			} else if (criteria.getOperation().equals(SearchOperation.LESS_THAN)) {
				predicates.add(criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString()));
			} else if (criteria.getOperation().equals(SearchOperation.GREATER_THAN_EQUAL)) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()),
						LocalDate.parse(criteria.getValue().toString())));
			} else if (criteria.getOperation().equals(SearchOperation.LESS_THAN_EQUAL)) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()),
						LocalDate.parse(criteria.getValue().toString())));
			} else if (criteria.getOperation().equals(SearchOperation.NOT_EQUAL)) {
				predicates.add(criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue()));
			} else if (criteria.getOperation().equals(SearchOperation.EQUAL)) {
				predicates.add(criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue()));
			} else if (criteria.getOperation().equals(SearchOperation.MATCH)) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(criteria.getKey())),
						"%" + criteria.getValue().toString().toLowerCase() + "%"));
			} else if (criteria.getOperation().equals(SearchOperation.MATCH_END)) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(criteria.getKey())),
						criteria.getValue().toString().toLowerCase() + "%"));
			} else if (criteria.getOperation().equals(SearchOperation.MATCH_START)) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(criteria.getKey())),
						"%" + criteria.getValue().toString().toLowerCase()));
			} else if (criteria.getOperation().equals(SearchOperation.IN)) {
				predicates.add(criteriaBuilder.in(root.get(criteria.getKey())).value(criteria.getValue()));
			} else if (criteria.getOperation().equals(SearchOperation.NOT_IN)) {
				predicates.add(criteriaBuilder.not(root.get(criteria.getKey())).in(criteria.getValue()));
			}
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}

}

package aggregation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import aggregation.model.Stats;

public interface StatsRepository extends JpaRepository<Stats, String>{
	
	public List<Stats> findAllByGameTitle(String gameTitle);

}

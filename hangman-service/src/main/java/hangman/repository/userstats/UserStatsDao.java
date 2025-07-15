package hangman.repository.userstats;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hangman.model.UserStats;

public interface UserStatsDao extends JpaRepository<UserStats, String> {
	
	List<UserStats> findByIdUserStats(String userId);

}

package aggregation.service.consumer;

import java.util.List;

import aggregation.dto.StatsDto;
import aggregation.model.Stats;

public interface StatsService {
	
	public void saveStats(Stats stats);
	
	public List<StatsDto> findAll();
	
	public List<StatsDto> findAllByGameTtitle(String gameTitle);

}

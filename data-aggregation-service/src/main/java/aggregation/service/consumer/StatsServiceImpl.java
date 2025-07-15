package aggregation.service.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import aggregation.dto.StatsDto;
import aggregation.model.Stats;
import aggregation.repository.StatsRepository;

@Service
@Transactional
public class StatsServiceImpl implements StatsService {

	@Autowired
	private StatsRepository statsRepository;

	@Override
	public void saveStats(Stats stats) {
		this.statsRepository.save(stats);
	}

	@Override
	public List<StatsDto> findAll() {

		List<StatsDto> retrievedStats = this.statsRepository.findAll().stream().map(stats -> mapToStatsDto(stats))
				.toList();

		List<StatsDto> calculatedStats = new ArrayList<>();

		Map<String, List<StatsDto>> groupedByTitle = retrievedStats.stream()
				.collect(Collectors.groupingBy(StatsDto::getGameTitle));

		groupedByTitle.entrySet().forEach(entry -> groupedByMode(entry.getValue(), calculatedStats));

		return calculatedStats;
	}

	@Override
	public List<StatsDto> findAllByGameTtitle(String gameTitle) {

		List<StatsDto> retrievedStats = this.statsRepository.findAllByGameTitle(gameTitle).stream()
				.map(stats -> mapToStatsDto(stats)).toList();

		List<StatsDto> calculatedStats = new ArrayList<>();

		Map<String, List<StatsDto>> groupedByTitle = retrievedStats.stream()
				.collect(Collectors.groupingBy(StatsDto::getGameTitle));

		groupedByTitle.entrySet().forEach(entry -> groupedByMode(entry.getValue(), calculatedStats));

		return calculatedStats;

	}

	private void groupedByMode(List<StatsDto> groupedByTitle, List<StatsDto> calculatedStats) {

		Map<String, List<StatsDto>> groupedByMode = groupedByTitle.stream()
				.collect(Collectors.groupingBy(StatsDto::getGameMode));

		groupedByMode.entrySet().forEach(entry -> calculateStats(entry.getValue(), calculatedStats));

	}

	private void calculateStats(List<StatsDto> groupedByMode, List<StatsDto> calculatedStats) {

		int popularitySum = groupedByMode.stream().reduce(0,
				(partialAgeResult, user) -> partialAgeResult + user.getPopularityCounter(), Integer::sum);

		if (!groupedByMode.isEmpty()) {

			StatsDto dto = groupedByMode.get(0);
			dto.setPopularityCounter(popularitySum);

			calculatedStats.add(dto);
		}
	}

	private StatsDto mapToStatsDto(Stats stats) {

		StatsDto dto = new StatsDto();
		dto.setGameMode(stats.getGameMode());
		dto.setGameTitle(stats.getGameTitle());
		dto.setPopularityCounter(stats.getPopularityCounter());

		return dto;
	}

}

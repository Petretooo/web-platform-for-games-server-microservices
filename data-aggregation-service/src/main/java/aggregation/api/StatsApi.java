package aggregation.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aggregation.dto.StatsDto;
import aggregation.service.consumer.StatsService;

@RestController
@RequestMapping("/api/v1/stats")
public class StatsApi {

	@Autowired
	private StatsService statsService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<StatsDto>> getAllStats() {

		List<StatsDto> statsDtos = statsService.findAll();

		return ResponseEntity.ok(statsDtos);
	}
	
	@GetMapping(value = "/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<StatsDto>> getStatsByTitle(@PathVariable("title") String titile) {

		List<StatsDto> statsDtos = statsService.findAllByGameTtitle(titile);

		return ResponseEntity.ok(statsDtos);
	}

}

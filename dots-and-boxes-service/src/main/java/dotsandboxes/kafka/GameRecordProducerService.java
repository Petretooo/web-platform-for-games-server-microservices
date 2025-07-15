package dotsandboxes.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import dotsandboxes.dto.GameRecord;
import dotsandboxes.utils.GameMode;
import dotsandboxes.utils.GameTitle;

@Component
public class GameRecordProducerService {

	private static final String GAME_STATS_DATA_TOPIC = "dotsAndBoxesStatsDataTopic";
	private static final String SUCCESS_STATUS = "SUCCESS";

	@Autowired
	private KafkaTemplate<String, GameRecord> kafkaTemplate;

	public void sendGameRecord(String endpoint) {
		
		String dotsAndBoxesKey = GameTitle.DOTSANDBOXES.name();
		
		GameRecord gameRecord = new GameRecord(
				GameTitle.DOTSANDBOXES, 
				GameMode.MULTIPLAYER, 
				null, 
				endpoint,
				SUCCESS_STATUS);
		
		kafkaTemplate.send(GAME_STATS_DATA_TOPIC, dotsAndBoxesKey, gameRecord);
	}

}

package hangman.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import hangman.dto.GameRecord;
import hangman.util.GameMode;
import hangman.util.GameTitle;

@Component
public class GameRecordProducerService {

	private static final String GAME_STATS_DATA_TOPIC = "hangmanStatsDataTopic";
	private static final String SUCCESS_STATUS = "SUCCESS";
	
	@Autowired
	private KafkaTemplate<String, GameRecord> kafkaTemplate;

	public void sendGameRecord(String endpoint, GameMode gameMode) {
		
		String hangmanKey = GameTitle.HANGMAN.name();
		
		GameRecord gameRecord = new GameRecord(
				GameTitle.HANGMAN, 
				gameMode, 
				null, 
				endpoint,
				SUCCESS_STATUS);
		
		kafkaTemplate.send(GAME_STATS_DATA_TOPIC, hangmanKey, gameRecord);
	}

}

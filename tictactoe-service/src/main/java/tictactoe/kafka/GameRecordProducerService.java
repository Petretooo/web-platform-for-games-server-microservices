package tictactoe.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import tictactoe.dto.GameRecord;
import tictactoe.utils.GameMode;
import tictactoe.utils.GameTitle;

@Component
public class GameRecordProducerService {

	private static final String GAME_STATS_DATA_TOPIC = "ticTacToeStatsDataTopic";
	private static final String SUCCESS_STATUS = "SUCCESS";
	
	@Autowired
	private KafkaTemplate<String, GameRecord> kafkaTemplate;

	public void sendGameRecord(String endpoint) {
		
		String ticTacToeKey = GameTitle.TICTACTOE.name();
		
		GameRecord gameRecord = new GameRecord(
				GameTitle.TICTACTOE, 
				GameMode.MULTIPLAYER, 
				null, 
				endpoint,
				SUCCESS_STATUS);
		
		kafkaTemplate.send(GAME_STATS_DATA_TOPIC, ticTacToeKey, gameRecord);
	}

}

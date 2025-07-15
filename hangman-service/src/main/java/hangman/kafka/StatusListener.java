package hangman.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import hangman.dto.GameRecord;

@Component
public class StatusListener {
	
	private static final String STATUS_SUCCESS = "SUCCESS";
	private static final String STATUS_FAILURE = "FAILURE";
	
    @KafkaListener(groupId = "hangmanId", topics = "dataAggregationDataTopic")
    public void receiveStatusUpdate(ConsumerRecord<String, GameRecord> record) {
    	
        String status = record.value().getStatus();
        
        switch (status) {
		case STATUS_SUCCESS: 
			System.out.println("The data is successfully processed!");
			break;
		case STATUS_FAILURE: 
			throw new IllegalAccessError("Data processing failed!");
		default:
			break;
		}
    }
}

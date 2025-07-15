package aggregation.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic ticTacToeDataTopic() {
		return TopicBuilder.name("ticTacToeStatsDataTopic").build();
	}
	
	@Bean
	public NewTopic hangmanDataTopic() {
		return TopicBuilder.name("hangmanStatsDataTopic").build();
	}
	
	@Bean
	public NewTopic dotsAndBoxesDataTopic() {
		return TopicBuilder.name("dotsAndBoxesStatsDataTopic").build();
	}
	
	@Bean
	public NewTopic dataAggregationDataTopic() {
		return TopicBuilder.name("dataAggregationDataTopic").build();
	}
}

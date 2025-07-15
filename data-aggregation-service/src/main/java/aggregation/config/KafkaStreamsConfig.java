package aggregation.config;

import java.time.Duration;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;

import aggregation.dto.GameRecord;
import aggregation.model.Stats;
import aggregation.service.consumer.StatsService;
import aggregation.util.GameMode;
import aggregation.util.StatSerde;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {
	
	private static final String DATA_AGGREGATION_SERVICE = "dataAggregationDataTopic";
	private static final String DATA_AGGREGATION_SERVICE_KEY = "dataAggregationService";
	
	private static final String STATUS_SUCCESS = "SUCCESS";
	private static final String STATUS_FAILURE = "FAILURE";
	
	@Autowired
	private KafkaTemplate<String, GameRecord> kafkaTemplate;

	@Autowired
	private StatsService statsService;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Bean
	public KStream<String, Stats> kStream(StreamsBuilder builder) {

		KStream<String, GameRecord> ticTactoeStream = builder.stream("ticTacToeStatsDataTopic");
		
		KStream<String, GameRecord> dotsAndBoxesStream = builder.stream("dotsAndBoxesStatsDataTopic");

		KStream<String, GameRecord> hangmanStream = builder.stream("hangmanStatsDataTopic");

		Duration duration = Duration.ofMinutes(3L);
		TimeWindows timeWindows = TimeWindows.of(duration);

		// TICTACTOR
		KStream<String, Stats> ticTacToeStats = ticTactoeStream.mapValues(gameRecord -> {
			return mapToStats(gameRecord);
		}).groupByKey().windowedBy(timeWindows).aggregate(() -> new Stats(), (key, value, aggregate) -> {

			int popularityCounter = aggregate.getPopularityCounter() + 1;
			value.setPopularityCounter(popularityCounter);

			return value;

		}, Materialized.<String, Stats, WindowStore<Bytes, byte[]>>as("materialized-store-tictactoe")
				.withKeySerde(Serdes.String()).withValueSerde(new StatSerde())).toStream()
				.peek((k, v) -> System.out.println(k + " --peek1-- " + v))
				.map((key, value) -> new KeyValue<>(key.key(), value));
		
		// DOTS AND BOXES
		KStream<String, Stats> dotsAndBoxesStats = dotsAndBoxesStream.mapValues(gameRecord -> {
			return mapToStats(gameRecord);
		}).groupByKey().windowedBy(timeWindows).aggregate(() -> new Stats(), (key, value, aggregate) -> {

			int popularityCounter = aggregate.getPopularityCounter() + 1;
			value.setPopularityCounter(popularityCounter);

			return value;

		}, Materialized.<String, Stats, WindowStore<Bytes, byte[]>>as("materialized-store-dotsandboxes")
				.withKeySerde(Serdes.String()).withValueSerde(new StatSerde())).toStream()
				.peek((k, v) -> System.out.println(k + " --peek1-- " + v))
				.map((key, value) -> new KeyValue<>(key.key(), value));

		// HANGMAN
		KStream<String, Stats>[] hangmanBatches = hangmanStream
				.map((key, gameRecord) -> KeyValue.pair(gameRecord.getGameMode().name(), mapToStats(gameRecord)))
				.peek((k, v) -> System.out.println(k + " --peek2-- " + v))
				.branch((gameTitle, gameStats) -> gameStats.getGameMode().equals(GameMode.MULTIPLAYER.name()),
						(gameTitle, gameStats) -> gameStats.getGameMode().equals(GameMode.SINGLEPLAYER.name()));

		KStream<String, Stats> multiplayerStats = hangmanBatches[0]
				.groupBy((key, value) -> key, Grouped.with(Serdes.String(), new JsonSerde<>(Stats.class)))
				.windowedBy(timeWindows).aggregate(() -> new Stats(), (key, value, aggregate) -> {

					int popularityCounter = aggregate.getPopularityCounter() + 1;
					value.setPopularityCounter(popularityCounter);

					return value;

				}, Materialized.<String, Stats, WindowStore<Bytes, byte[]>>as("materialized-store-hangman-multiplayer")
						.withKeySerde(Serdes.String()).withValueSerde(new StatSerde()))
				.toStream().peek((k, v) -> System.out.println(k + " --peek3-- " + v))
				.map((key, value) -> new KeyValue<>(key.key(), value));

		KStream<String, Stats> singleplayerStats = hangmanBatches[1]
				.groupBy((key, value) -> key, Grouped.with(Serdes.String(), new JsonSerde<>(Stats.class)))
				.windowedBy(timeWindows).aggregate(() -> new Stats(), (key, value, aggregate) -> {

					int popularityCounter = aggregate.getPopularityCounter() + 1;
					value.setPopularityCounter(popularityCounter);

					return value;

				}, Materialized.<String, Stats, WindowStore<Bytes, byte[]>>as("materialized-store-hangman-singleplayer")
						.withKeySerde(Serdes.String()).withValueSerde(new StatSerde()))
				.toStream().peek((k, v) -> System.out.println(k + " --peek4-- " + v))
				.map((key, value) -> new KeyValue<>(key.key(), value));

		KStream<String, Stats> combinedStreams = ticTacToeStats.merge(dotsAndBoxesStats).merge(multiplayerStats).merge(singleplayerStats)
				.peek((key, value) -> System.out.print(key + " --peek5-- " + value))
				.peek((key, value) -> {
		            try {
		                statsService.saveStats(value);
		                GameRecord gameRecord = new GameRecord();
		                gameRecord.setStatus(STATUS_SUCCESS);
		                sendGameRecord(gameRecord);
		            } catch (Exception e) {
		                System.err.println("Error saving stats: " + e.getMessage());
		                GameRecord gameRecord = new GameRecord();
		                gameRecord.setStatus(STATUS_FAILURE);
		                sendGameRecord(gameRecord);
		            }
		        });
				
		return combinedStreams;
	}

	private Stats mapToStats(GameRecord gameRecord) {

		Stats stats = new Stats();
		stats.setGameTitle(gameRecord.getGameTitle().toString());
		stats.setGameMode(gameRecord.getGameMode().toString());
		stats.setPopularityCounter(1);
		return stats;
	}
	
	public void sendGameRecord(GameRecord status) {
		kafkaTemplate.send(DATA_AGGREGATION_SERVICE, DATA_AGGREGATION_SERVICE_KEY, status);
	}

//	private TransformerSupplier<String, GameRecord, KeyValue<String, Stats>> transformerGameRecord() {
//
//		return new TransformerSupplier<String, GameRecord, KeyValue<String, Stats>>() {
//			@Override
//			public Transformer<String, GameRecord, KeyValue<String, Stats>> get() {
//
//				return new Transformer<String, GameRecord, KeyValue<String, Stats>>() {
//
//					@Override
//					public KeyValue<String, Stats> transform(String key, GameRecord value) {
//
//						String gameTitle = value.getGameTitle().name();
//
//						System.out.println(key + " --transform-record-- " + value);
//
//						if (gameTitle.equals(GameTitle.TICTACTOE.name())) {
//
//							Stats ticTacToeStats = new Stats();
//							ticTacToeStats.setGameTitle(gameTitle);
//							ticTacToeStats.setGameMode(value.getGameMode().name());
//
//							System.out.println(key + " --transform-- " + ticTacToeStats);
//							return KeyValue.pair(key, ticTacToeStats);
//
//						} else if (gameTitle.equals(GameTitle.HANGMAN.name())) {
//
//							// TODO ADD ADDITIONAL LOGIC BY GAME MODE
//
//							Stats hangmanStats = new Stats();
//							hangmanStats.setGameTitle(gameTitle);
//							hangmanStats.setGameTitle(gameTitle);
//							hangmanStats.setGameMode(value.getGameMode().name());
//
//							System.out.println(key + " --transform-- " + hangmanStats);
//							return KeyValue.pair(key, hangmanStats);
//						} else {
//							throw new NullPointerException("No game is found!");
//						}
//					}
//
//					@Override
//					public void close() {
//						// Cleanup resources
//					}
//
//					@Override
//					public void init(ProcessorContext context) {
//						// TODO Auto-generated method stub
//
//					}
//				};
//			}
//		};
//	}
}

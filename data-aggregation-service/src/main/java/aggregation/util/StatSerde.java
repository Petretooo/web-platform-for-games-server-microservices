package aggregation.util;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import aggregation.model.Stats;

public class StatSerde implements Serde<Stats> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// Nothing to configure
	}

	@Override
	public Serializer<Stats> serializer() {
		return new StatsSerializer();
	}

	@Override
	public Deserializer<Stats> deserializer() {
		return new StatsDeserializer();
	}
	
    @Override
    public void close() {
        // Nothing to do
    }

	private class StatsSerializer implements Serializer<Stats> {
		@Override
		public byte[] serialize(String topic, Stats data) {
			try {
				return objectMapper.writeValueAsBytes(data);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private class StatsDeserializer implements Deserializer<Stats> {
		@Override
		public Stats deserialize(String topic, byte[] data) {
			if (data == null) {
				return null;
			}
			try {
				return objectMapper.readValue(data, Stats.class);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}

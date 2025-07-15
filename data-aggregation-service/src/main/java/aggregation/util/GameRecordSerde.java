package aggregation.util;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import aggregation.dto.GameRecord;

public class GameRecordSerde implements Serde<GameRecord> {
	
	private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Nothing to configure
    }

    @Override
    public Serializer<GameRecord> serializer() {
        return new GameRecordSerializer();
    }

    @Override
    public Deserializer<GameRecord> deserializer() {
        return new GameRecordDeserializer();
    }

    @Override
    public void close() {
        // Nothing to do
    }

    private class GameRecordSerializer implements Serializer<GameRecord> {
        @Override
        public byte[] serialize(String topic, GameRecord data) {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class GameRecordDeserializer implements Deserializer<GameRecord> {
        @Override
        public GameRecord deserialize(String topic, byte[] data) {
            if (data == null) {
                return null;
            }
            try {
                return objectMapper.readValue(data, GameRecord.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

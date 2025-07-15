package hangman.util;

import java.io.IOException;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import hangman.dto.GameRecord;

public class GameRecordDeserializer implements Deserializer<GameRecord> {

	@Override
	public GameRecord deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(data, GameRecord.class);
		} catch (IOException e) {
			throw new SerializationException("Error deserializing GameRecord", e);
		}
	}

}

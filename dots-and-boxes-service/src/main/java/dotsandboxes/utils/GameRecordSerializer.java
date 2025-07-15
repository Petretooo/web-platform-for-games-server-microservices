package dotsandboxes.utils;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dotsandboxes.dto.GameRecord;

public class GameRecordSerializer implements Serializer<GameRecord> {

	@Override
	public byte[] serialize(String topic, GameRecord data) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsBytes(data);
		} catch (JsonProcessingException e) {
			throw new SerializationException("Error serializing GameRecord", e);
		}
	}

}

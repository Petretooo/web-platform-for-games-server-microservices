package chat.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import chat.model.Message;

public interface MessageRepository extends MongoRepository<Message, String>{
	
	public List<Message> findAllByRoomId(String roomId);

}

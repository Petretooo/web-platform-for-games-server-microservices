package chat.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

	@Id
	String id;
	String roomId;
	String sender;
	String recipient;
	String message;
	
}

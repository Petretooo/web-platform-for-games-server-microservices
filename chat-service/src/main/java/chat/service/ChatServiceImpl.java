package chat.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chat.dto.Message;
import chat.repository.MessageRepository;

@Service
public class ChatServiceImpl implements ChatService{
	
	@Autowired
	private MessageRepository messageRepository;
	
	private Map<String, List<Message>> chat;
	
	public ChatServiceImpl() {
		this.chat = new HashMap<>();
	}

	@Override
	@Deprecated
	public void creatNewChat(String roomId) {
		boolean isFound = this.chat.containsKey(roomId);
		if(!isFound) {
			this.chat.put(roomId, new LinkedList<Message>());
		}
	}

	@Override
	public List<Message> getMessagesByGameRoom(String roomId) {
		
		List<chat.model.Message> messages = messageRepository.findAllByRoomId(roomId);
		
		return messages.stream()
				.map(m -> mapToResponseMessages(m))
				.toList();
	}

	@Override
	public void addMessageToGameRoomChat(String roomId, Message message) {
		
		chat.model.Message m = new chat.model.Message();
		m.setRoomId(roomId);
		m.setSender(message.getSenderName());
		m.setRecipient(message.getReceiverName());
		m.setMessage(message.getMessage());
		
		messageRepository.save(m);
		
//		this.chat.get(roomId).add(message);
//		
//		
	}

	@Override
	@Deprecated
	public void deleteChat(String roomId) {
		this.chat.remove(roomId);
	}
	
	private Message mapToResponseMessages(chat.model.Message message) {
		
		Message responseMessage = new Message();
		responseMessage.setMessage(message.getMessage());
		responseMessage.setSenderName(message.getSender());
		responseMessage.setReceiverName(message.getRecipient());
		
		return responseMessage;
		
	}

}

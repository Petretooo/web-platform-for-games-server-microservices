package chat.service;

import java.util.List;

import chat.dto.Message;

public interface ChatService {
	
	public void creatNewChat(String roomId);
	public List<Message> getMessagesByGameRoom(String roomId);
	public void addMessageToGameRoomChat(String roomId, Message message);
	public void deleteChat(String roomId);
}

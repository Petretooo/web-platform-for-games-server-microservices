package chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import chat.dto.Message;
import chat.dto.MessageDiscussion;
import chat.service.ChatService;
import chat.util.GameRoomStatus;

@Controller
public class ChatController {
	
	@Autowired
	private ChatService chatService;

    @MessageMapping("/message/{roomId}")
    @SendTo("/chatroom/{roomId}")
    public MessageDiscussion receiveMessage(@DestinationVariable String roomId, @Payload Message message){
    	
    	if(message.getStatus().equals(GameRoomStatus.JOIN)) {
			chatService.creatNewChat(roomId);
    	}else if(message.getStatus().equals(GameRoomStatus.MESSAGE)) {
    		chatService.addMessageToGameRoomChat(roomId, message);
    	}else {
    		chatService.deleteChat(roomId);
    	}
    	
    	MessageDiscussion discussion = new MessageDiscussion();
    	discussion.setStatus(message.getStatus());
    	discussion.setMessages(chatService.getMessagesByGameRoom(roomId));
    	
        return discussion;
    }
}

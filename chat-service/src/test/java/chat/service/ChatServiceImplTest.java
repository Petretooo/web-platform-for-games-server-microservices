package chat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import chat.dto.Message;
import chat.repository.MessageRepository;

class ChatServiceImplTest {

    private static final String SENDER = "sender";

	private static final String RECIPIENT = "recipient";

	private static final String MESSSAGE_ID = "messsage-id";

	private static final String MESSAGE = "message";

	private static final String SENDER_ID = "sender-id";

	private static final String ROOM_ID = "roomId";

	@Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private ChatServiceImpl chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMessagesByGameRoom() {
    	
        List<chat.model.Message> messages = new ArrayList<>();
        
        chat.model.Message m = new chat.model.Message();
        m.setId(MESSSAGE_ID);
        m.setMessage(MESSAGE);
        m.setRecipient(RECIPIENT);
        m.setSender(SENDER);
        m.setRoomId(ROOM_ID);
        
        messages.add(m);

        when(messageRepository.findAllByRoomId(eq(ROOM_ID))).thenReturn(messages);

        List<Message> expectedMessages = new ArrayList<>();
        
        Message expectedMessage = new Message();
        expectedMessage.setSenderName(SENDER_ID);
        expectedMessage.setReceiverName(RECIPIENT);
        expectedMessage.setSenderName(SENDER);
        expectedMessage.setMessage(MESSAGE);
        
        expectedMessages.add(expectedMessage);
        
        when(messageRepository.findAllByRoomId(ROOM_ID)).thenReturn(messages);

        List<Message> result = chatService.getMessagesByGameRoom(ROOM_ID);

        verify(messageRepository).findAllByRoomId(eq(ROOM_ID));
        
        Message resultMessage = result.get(0);

        assertEquals(m.getMessage(), resultMessage.getMessage());
        assertEquals(m.getRecipient(), resultMessage.getReceiverName());
        assertEquals(m.getSender(), resultMessage.getSenderName());
    }

    @Test
    void testAddMessageToGameRoomChat() {
    	        
        chat.model.Message m = new chat.model.Message();
        m.setMessage(MESSAGE);
        m.setRecipient(RECIPIENT);
        m.setSender(SENDER);
        m.setRoomId(ROOM_ID);
        
        Message expectedMessage = new Message();
        expectedMessage.setSenderName(SENDER_ID);
        expectedMessage.setReceiverName(RECIPIENT);
        expectedMessage.setSenderName(SENDER);
        expectedMessage.setMessage(MESSAGE);
                
        chatService.addMessageToGameRoomChat(ROOM_ID, expectedMessage);

        verify(messageRepository).save(m);
    }

}
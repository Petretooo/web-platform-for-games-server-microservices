package multiplayer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import multiplayer.dto.DotsAndBoxesDto;
import multiplayer.dto.DotsAndBoxesResultDto;
import multiplayer.dto.DotsAndBoxesTryDto;
import multiplayer.dto.LobbyDto;
import multiplayer.model.GameRoom;
import multiplayer.util.DotsAndBoxesServiceException;

@Controller
public class MultiplayerDotsAndBoxesController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@MessageMapping("/message/dotsandboxes/{gameRoomId}")
	@SendTo("/dotsandboxes/{gameRoomId}")
	public DotsAndBoxesResultDto receiveHangmanTry(@DestinationVariable String gameRoomId, @Payload DotsAndBoxesDto message) {
		
		GameRoom room = null;
				
		
		try {
			room = 
					restTemplate.getForObject("http://localhost:8089/api/v1/multiplayer/dotsandboxes/{gameRoomId}", GameRoom.class, gameRoomId);
		} catch (Exception e) {
			throw new DotsAndBoxesServiceException(e.getMessage());
		}

		DotsAndBoxesResultDto resultsDto = new DotsAndBoxesResultDto();
		
		if(message.getMessage() == null) {
					
			resultsDto.setSenderId(room.getFirstUser());
			return  resultsDto;
		}
		
		DotsAndBoxesTryDto tryDto = new DotsAndBoxesTryDto();
		tryDto.setRoom(room);
		tryDto.setMessage(message);
		
		try {
			resultsDto = 
					restTemplate.postForEntity("http://localhost:8089/api/v1/multiplayer/dotsandboxes/checkgame", tryDto, DotsAndBoxesResultDto.class).getBody();
		} catch (Exception e) {
			throw new DotsAndBoxesServiceException(e.getMessage());
		}
		
		return resultsDto;
	}
	
	@MessageMapping("/message/dotsandboxes/lobby/{gameRoomId}")
	@SendTo("/dotsandboxes/public/lobby/{gameRoomId}")
	public LobbyDto lobby(@DestinationVariable String gameRoomId, @Payload LobbyDto message) {
		
		if(message.getSenderStatus().equalsIgnoreCase("JOIN")) {
			if(message.getFirstUserUsername() == null || message.getFirstUserUsername().isBlank()) {
				message.setFirstUserUsername(message.getSenderUsername());
				message.setFirstUserStatus(message.getSenderStatus());
			}else if(message.getSecondUserStatus() == null || message.getSecondUserUsername().isBlank()) {
				message.setSecondUserUsername(message.getSenderUsername());
				message.setSecondUserStatus(message.getSenderStatus());
			}
		}else if(message.getSenderStatus().equalsIgnoreCase("READY")) {
			if(message.getSecondUserStatus() == null || message.getFirstUserUsername().equalsIgnoreCase(message.getFirstUserUsername())) {
				message.setFirstUserStatus(message.getSenderStatus());
			}else if(message.getSecondUserStatus() == null || message.getSecondUserStatus().equalsIgnoreCase(message.getSecondUserUsername())) {
				message.setSecondUserStatus(message.getSenderStatus());
			}
		}

		return message;
	}

}

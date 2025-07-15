package multiplayer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import multiplayer.dto.GameRoomDto;
import multiplayer.dto.GameRoomInfoDto;
import multiplayer.dto.GameRoomResultsDto;
import multiplayer.dto.GameRoomTryDto;
import multiplayer.dto.LobbyDto;
import multiplayer.util.HangmanServiceException;

@Controller
public class MultiplayerHangmanController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@MessageMapping("/message/hangman/{gameRoomId}")
	@SendTo("/hangman/{gameRoomId}")
	public GameRoomResultsDto receiveHangmanTry(@DestinationVariable String gameRoomId, @Payload GameRoomTryDto message) {
		
		if(message.getMessage() == null) {
			return new GameRoomResultsDto();
		}
		
		GameRoomInfoDto room = null;
		
		try {
			room = this.restTemplate
					.getForEntity("http://localhost:8082/api/v1/multiplayer/hangman/{gameRoomId}", GameRoomInfoDto.class, gameRoomId).getBody();
		} catch (Exception e) {
			throw new HangmanServiceException(e.getMessage());
		}
		
		GameRoomDto gameRoomDto = new GameRoomDto();
		gameRoomDto.setGameRoomInfoDto(room);
		gameRoomDto.setGameRoomTryDto(message);
		
		GameRoomResultsDto resultsDto = null;
		
		try {
			resultsDto = this.restTemplate
					.postForEntity("http://localhost:8082/api/v1/multiplayer/hangman/check/game", gameRoomDto, GameRoomResultsDto.class).getBody();
		} catch (Exception e) {
			throw new HangmanServiceException(e.getMessage());
		}
		


		return resultsDto;
	}
	
	@MessageMapping("/message/hangman/lobby/{gameRoomId}")
	@SendTo("/hangman/public/lobby/{gameRoomId}")
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

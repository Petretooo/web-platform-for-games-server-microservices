package multiplayer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import multiplayer.dto.LobbyDto;
import multiplayer.dto.TicTacToeDto;
import multiplayer.dto.TicTacToeResultDto;
import multiplayer.dto.TicTacToeTryDto;
import multiplayer.model.TicTacToeGameRoom;
import multiplayer.util.TicTacToeServiceException;

@Controller
public class MultiplayerTicTacToeController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@MessageMapping("/message/tictactoe/{gameRoomId}")
	@SendTo("/tictactoe/{gameRoomId}")
	public TicTacToeResultDto receiveHangmanTry(@DestinationVariable String gameRoomId, @Payload TicTacToeDto message) {
		
		TicTacToeGameRoom room = null;
		
		try {
			room = restTemplate.getForObject("http://localhost:8083/api/v1/multiplayer/tictactoe/{gameRoomId}", TicTacToeGameRoom.class, gameRoomId);
		} catch (Exception e) {
			throw new TicTacToeServiceException(e.getMessage());
		}

		TicTacToeResultDto resultsDto = new TicTacToeResultDto();
		
		if(message.getMessage() == null) {
					
			resultsDto.setSenderId(room.getFirstUser());
			return  resultsDto;
		}
		
		TicTacToeTryDto tryDto = new TicTacToeTryDto();
		tryDto.setRoom(room);
		tryDto.setMessage(message);
		
		try {
			resultsDto = 
					restTemplate.postForEntity("http://localhost:8083/api/v1/multiplayer/tictactoe/checkgame", tryDto, TicTacToeResultDto.class).getBody();
		} catch (Exception e) {
			throw new TicTacToeServiceException(e.getMessage());
		}
		
		return resultsDto;
	}
	
	@MessageMapping("/message/tictactoe/lobby/{gameRoomId}")
	@SendTo("/tictactoe/public/lobby/{gameRoomId}")
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

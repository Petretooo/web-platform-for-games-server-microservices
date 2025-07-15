package tictactoe.service.multiplayer;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import tictactoe.dto.TicTacToeDto;
import tictactoe.dto.TicTacToeResultDto;
import tictactoe.dto.User;
import tictactoe.model.TicTacToeGameRoom;
import tictactoe.service.gameroom.TicTacToeGameRoomService;
import tictactoe.utils.UserServiceException;

@Service
@AllArgsConstructor
@Transactional
public class TicTacToeMultiplayerServiceImpl implements TicTacToeMultiplayerService{
	
	static int[][] winnerGames = { 
			{ 0, 1, 2 },
			{ 3, 4, 5 },
			{ 6, 7, 8 },
			{ 0, 3, 6 },
			{ 1, 4, 7 },
			{ 2, 5, 8 },
			{ 0, 4, 8 },
			{ 2, 4, 6 }
			
	};

	@Autowired
	private TicTacToeGameRoomService gameRoomService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public TicTacToeResultDto checkGame(TicTacToeGameRoom room, TicTacToeDto message) {
		
		TicTacToeResultDto tictactoe = new TicTacToeResultDto();
		
		tictactoe.setSenderId(message.getSenderId());
		tictactoe.setSquares(message.getSquares());
		
		String user = Arrays.asList(room.getFirstUser(), room.getSecondUser()).stream()
				.filter(player -> player.equalsIgnoreCase(message.getSenderId())).findFirst().get();
		
		String competitorId = Arrays.asList(room.getFirstUser(), room.getSecondUser()).stream()
				.filter(player -> !player.equalsIgnoreCase(message.getSenderId())).findFirst().get();

		
		if(message.getSenderId().equalsIgnoreCase(room.getFirstUser())) {
			if(tictactoe.getSquares().get(Integer.parseInt(message.getMessage())) == null) {
				tictactoe.getSquares().set(Integer.parseInt(message.getMessage()), "X");
				tictactoe.setTryAgain(false);
			}else {
				tictactoe.setTryAgain(true);
			}
		}else if(message.getSenderId().equalsIgnoreCase(room.getSecondUser())) {
			if(tictactoe.getSquares().get(Integer.parseInt(message.getMessage())) == null) {
				tictactoe.getSquares().set(Integer.parseInt(message.getMessage()), "O");
				tictactoe.setTryAgain(false);
			}else {
				tictactoe.setTryAgain(true);
			}
		}
		
		tictactoe = checkWinner(tictactoe, message.getSenderId(), competitorId);
		
		if(tictactoe.getWinnerId() != null && tictactoe.getLoserId() != null) {
			
			User winnerUser = null;
						
			try {
				winnerUser = restTemplate.getForObject("http://user-service/api/v1/users/id/{id}", User.class, user);
			} catch (Exception e) {
				throw new UserServiceException(e.getMessage());
			}
			
			this.gameRoomService.updateGameRoom(room.getGameRoomId(), winnerUser.getUsername());
		}
		
		if(tictactoe.isDraw()) {
			this.gameRoomService.updateGameRoom(room.getGameRoomId(), null);
		}
		
		return tictactoe;
	}
	
	TicTacToeResultDto checkWinner(TicTacToeResultDto tictactoe, String senderId, String competitorId) {
		
		List<String> board = tictactoe.getSquares();
		
		 for (int i = 0; i < winnerGames.length; i++) {
			 int[] winnerPositions = winnerGames[i];
			 if(checkLine(board, winnerPositions, 0, 1) && checkLine(board, winnerPositions, 0, 2) && checkLine(board, winnerPositions, 1, 2)) {
				 tictactoe.setWinnerId(senderId);
				 tictactoe.setLoserId(competitorId);
			 }
		 }
		 
		 if(checkDraw(board)) {
			 tictactoe.setDraw(true);
		 }
		 
		return tictactoe;
		
	}

	boolean checkLine(List<String> board, int[] winnerPositions, int firstPosition, int secondPosition) {
		return (board.get(winnerPositions[firstPosition]) != null && board.get(winnerPositions[secondPosition]) != null) && 
				(board.get(winnerPositions[firstPosition]).equalsIgnoreCase(board.get(winnerPositions[secondPosition])));
	}
	
	boolean checkDraw(List<String> board) {
		return board.stream().allMatch(x -> x != null);
	}

}

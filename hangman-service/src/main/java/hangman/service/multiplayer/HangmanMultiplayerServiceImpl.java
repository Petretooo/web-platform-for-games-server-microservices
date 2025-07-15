package hangman.service.multiplayer;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import hangman.dto.GameRoomInfoDto;
import hangman.dto.GameRoomResultsDto;
import hangman.dto.GameRoomTryDto;
import hangman.dto.RoomUserDto;
import hangman.dto.User;
import hangman.model.HangmanGame;
import hangman.model.HangmanGameRoom;
import hangman.model.HangmanGameStats;
import hangman.model.UserStats;
import hangman.service.game.HangmanGameService;
import hangman.service.gameroom.HangmanGameRoomService;
import hangman.service.gamestats.GameStatsService;
import hangman.service.rank.RankService;
import hangman.service.userstats.UserStatsService;
import hangman.util.GameStatus;
import hangman.util.UserServiceException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class HangmanMultiplayerServiceImpl implements HangmanMultiplayerService {

	@Autowired
	private UserStatsService userStatsService;
	
	@Autowired
	private HangmanGameService gameService;
	
	@Autowired
	private HangmanGameRoomService gameRoomService;
	
	@Autowired
	private GameStatsService gameStatsService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private RankService rankService;


	@Override
	public GameRoomResultsDto checkGameRoom(GameRoomInfoDto infoDto, GameRoomTryDto message) {
				
		String userId = message.getUserId();
		
		RoomUserDto userDto = null;
		RoomUserDto competitorDto = null;
		
		if(infoDto != null) {
			
			userDto = Arrays.asList(infoDto.getFirstUser(), infoDto.getSecondUser()).stream()
					.filter(player -> player.getUserId().equalsIgnoreCase(userId)).findFirst().get();
			competitorDto = Arrays.asList(infoDto.getFirstUser(), infoDto.getSecondUser()).stream()
					.filter(player -> !player.getUserId().equalsIgnoreCase(userId)).findFirst().get();
		}
				
		GameRoomResultsDto resultsDto = resultGame(infoDto.getGameRoomId(), userDto, competitorDto);

		return resultsDto;
	}

	private void saveMatchResults(HangmanGame game, GameStatus status) {
		
		HangmanGameStats gameStat = gameStatsService.updateGameStatsBuStatus(game.getId(), status);
		UserStats stat = userStatsService.update(game);
		rankService.saveRank(stat, gameStat);
	}
	
	public GameRoomResultsDto resultGame(String gameRoomId, RoomUserDto userDto, RoomUserDto competitorDto) {
		
		HangmanGameRoom room = this.gameRoomService.getGameRoom(gameRoomId);
		HangmanGame userGame = userDto.getGameInfo();
		HangmanGame competitorGame = competitorDto.getGameInfo();
		
		boolean checkIsDraw = userGame.getNumberTries() <= 0 && competitorGame.getNumberTries() <= 0;
		boolean checkForMoreTries = userGame.getNumberTries() <= 0 && competitorGame.getNumberTries() >= 0;
		boolean checkWon = userGame.getCurrentWord().contentEquals(new String(userGame.getHiddenWord()));
		
		if (checkIsDraw) {
			saveMatchResults(userDto.getGameInfo(), GameStatus.DRAW);
			saveMatchResults(competitorDto.getGameInfo(), GameStatus.DRAW);
			room.setActive(false);
			return new GameRoomResultsDto(checkIsDraw);
		}else if(checkForMoreTries) {
			return new GameRoomResultsDto(userDto.getUserId());
		}else if (checkWon) {
			saveMatchResults(userDto.getGameInfo(), GameStatus.WON);
			saveMatchResults(competitorDto.getGameInfo() ,GameStatus.LOST);
			room.setActive(false);
			return new GameRoomResultsDto(userDto.getUserId(), competitorDto.getUserId());
		}else {
			return null;
		}
	}
	
	@Override
	public GameRoomInfoDto makeTry(String gameRoomId, GameRoomTryDto message) {
		
		GameRoomInfoDto infoDto = this.gameRoomService.getGameRoomInfo(gameRoomId);
		
		User user = null;
		
		try {
			user = restTemplate.getForObject("http://user-service/api/v1/users/id/{id}", User.class, message.getUserId());
		} catch (Exception e) {
			throw new UserServiceException(e.getMessage());
		}
		
		RoomUserDto userDto = null;
		
		String userId = user.getUserId();
		
		if(infoDto != null) { 
			userDto = Arrays.asList(infoDto.getFirstUser(), infoDto.getSecondUser()).stream()
					.filter(player -> player.getUserId().equalsIgnoreCase(userId)).findFirst().get();
		}
		HangmanGame game = gameService.getGame(userDto.getGameInfo().getId());
		gameService.enterCharacter(game.getId(), message.getMessage());
		return infoDto;

	}

}

package dotsandboxes.service.game;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dotsandboxes.model.Box;
import dotsandboxes.model.Dot;
import dotsandboxes.model.Edge;
import dotsandboxes.model.Game;
import dotsandboxes.model.GameBox;
import dotsandboxes.model.GameBoxId;
import dotsandboxes.repository.box.BoxRepository;
import dotsandboxes.repository.dot.DotRepository;
import dotsandboxes.repository.edge.EdgeRepository;
import dotsandboxes.repository.game.DotsAndBoxesGameRepository;
import dotsandboxes.repository.gamebox.GameBoxRepository;

@Service
@Transactional
public class GameServiceImpl implements GameService {

	@Autowired
	private BoxRepository boxRepository;

	@Autowired
	private GameBoxRepository gameBoxRepository;

	@Autowired
	private EdgeRepository edgeRepository;

	@Autowired
	private DotRepository dotRepository;

	@Autowired
	private DotsAndBoxesGameRepository gameRepository;

	@Override
	public Game createGame() {

		Game game = saveGame();

		List<GameBox> gameBoxes = createBoxes(game);

		createEdges(gameBoxes, game.getId());

		return game;
	}

	private void createEdges(List<GameBox> gameBoxes, String gameId) {

		List<Dot> dots = dotRepository.findAll();

		List<Box> boxes = gameBoxes.stream().map(gameBox -> gameBox.getBox()).toList();

		List<Edge> edges = new ArrayList<Edge>();

		for (Box box : boxes) {

			String gameBoxName = box.getBoxName();

			String firstXAxisLetter = String.valueOf(gameBoxName.charAt(0));
			String firstYAxisNumber = String.valueOf(gameBoxName.charAt(1));

			Dot firstDot = dots.stream()
					.filter(d -> d.getXAxis().equals(firstXAxisLetter) && d.getYAxis().equals(firstYAxisNumber))
					.findFirst().orElseThrow();

			String secondXAxisLetter = String.valueOf(gameBoxName.charAt(2));
			String secondYAxisNumber = String.valueOf(gameBoxName.charAt(3));

			Dot secondDot = dots.stream()
					.filter(d -> d.getXAxis().equals(secondXAxisLetter) && d.getYAxis().equals(secondYAxisNumber))
					.findFirst().orElseThrow();

			String thirdXAxisLetter = String.valueOf(gameBoxName.charAt(4));
			String thirdYAxisNumber = String.valueOf(gameBoxName.charAt(5));

			Dot thirdDot = dots.stream()
					.filter(d -> d.getXAxis().equals(thirdXAxisLetter) && d.getYAxis().equals(thirdYAxisNumber))
					.findFirst().orElseThrow();

			String fourthXAxisLetter = String.valueOf(gameBoxName.charAt(6));
			String fourthYAxisNumber = String.valueOf(gameBoxName.charAt(7));

			Dot fourthDot = dots.stream()
					.filter(d -> d.getXAxis().equals(fourthXAxisLetter) && d.getYAxis().equals(fourthYAxisNumber))
					.findFirst().orElseThrow();


			Edge topEdge = new Edge();
			topEdge.setBox(box);
			topEdge.setEdgeAvailable(true);
			topEdge.setFromDot(firstDot);
			topEdge.setToDot(secondDot);
			topEdge.setGameId(gameId);
			topEdge.setOrderEdge(1);

			Edge bottomEdge = new Edge();
			bottomEdge.setBox(box);
			bottomEdge.setEdgeAvailable(true);
			bottomEdge.setFromDot(thirdDot);
			bottomEdge.setToDot(fourthDot);
			bottomEdge.setGameId(gameId);
			bottomEdge.setOrderEdge(2);

			Edge leftEdge = new Edge();
			leftEdge.setBox(box);
			leftEdge.setEdgeAvailable(true);
			leftEdge.setFromDot(firstDot);
			leftEdge.setToDot(thirdDot);
			leftEdge.setGameId(gameId);
			leftEdge.setOrderEdge(3);

			Edge rightEdge = new Edge();
			rightEdge.setBox(box);
			rightEdge.setEdgeAvailable(true);
			rightEdge.setFromDot(secondDot);
			rightEdge.setToDot(fourthDot);
			rightEdge.setGameId(gameId);
			rightEdge.setOrderEdge(4);

			edges.add(topEdge);
			edges.add(bottomEdge);
			edges.add(leftEdge);
			edges.add(rightEdge);
		}

		edgeRepository.saveAll(edges);
	}

	private List<GameBox> createBoxes(Game game) {

		List<Box> boxes = boxRepository.findAll();

		List<GameBox> gameBoxes = new ArrayList<GameBox>();

		for (Box b : boxes) {

			GameBoxId gameBoxId = new GameBoxId();
			gameBoxId.setBoxId(b.getBoxId());
			gameBoxId.setGameId(game.getId());

			GameBox gameBox = new GameBox();
			gameBox.setGameBoxId(gameBoxId);
			gameBox.setBoxAvailable(true);
			gameBox.setGame(game);
			gameBox.setBox(b);

			gameBoxes.add(gameBox);
		}

		return gameBoxRepository.saveAll(gameBoxes);
	}

	private Game saveGame() {
		Game game = new Game();
		this.gameRepository.save(game);
		return game;
	}

	@Override
	public Game getGame(String id) {
		return this.gameRepository.getById(id);
	}

	@Override
	public void deleteGame(String id) {
		this.gameRepository.deleteById(id);
	}

	@Override
	public void updateGameWinner(String id, String username) {

		Game game = this.gameRepository.getById(id);
		game.setWinnerUsername(username);
	}

	@Override
	public List<Game> getAllGames() {
		return this.gameRepository.findAll();
	}

}

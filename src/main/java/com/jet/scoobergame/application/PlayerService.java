package com.jet.scoobergame.application;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.jet.scoobergame.domain.Game;
import com.jet.scoobergame.domain.GameService;
import com.jet.scoobergame.domain.Player;
import com.jet.scoobergame.infrastructure.GameEventProducer;
import com.jet.scoobergame.domain.GameState;

/**
 * Manages the player lifecycle and tracks the current game state.
 *
 * @author Nikhil
 */
@Service
public class PlayerService {

	private final GameService gameService;
	private final GameEventProducer gameEventProducer;
	private final ConcurrentHashMap<String, Player> registeredPlayers = new ConcurrentHashMap<>();
	private Game currentGame; // Track the current game state

	public PlayerService(GameService gameService, GameEventProducer gameEventProducer) {
		this.gameService = gameService;
		this.gameEventProducer = gameEventProducer;
	}

	/**
	 * Registers the initial players and starts the game.
	 *
	 * @param player1Name Name of the first player
	 * @param player2Name Name of the second player
	 */
	// Register initial players and start the game
	public void registerPlayers(String player1Name, String player2Name) {
		Player player1 = new Player(player1Name);
		Player player2 = new Player(player2Name);
		registeredPlayers.putIfAbsent(player1Name, player1);
		registeredPlayers.putIfAbsent(player2Name, player2);

		if (registeredPlayers.size() == 2) {
			startGame(player1, List.copyOf(registeredPlayers.values()));
		}
	}
	/**
	 * Calls GameService to initialize the game and then publishes the initial state
	 *
	 * @param firstPlayer The first player to start the game
	 * @param players The list of players in the game
	 */
	private void startGame(Player firstPlayer, List<Player> players) {
		if (currentGame != null) {
			System.out.println("Game has already been started. Skipping initialization.");
			return; // Prevents re-initialization
		}

		currentGame = gameService.startGame(firstPlayer, players);
		gameEventProducer.publishGameEvent(currentGame); // Ensure only one event is published
	}

	/**
	 * Starts the game with the registered players. This method is used to restart the game.
	 */
	public void startGame() {
		int startNumber = new Random().nextInt(Integer.MAX_VALUE) + 1;
		currentGame = new Game(UUID.randomUUID(), registeredPlayers.values().stream().findFirst().get(), new GameState(startNumber),
				List.copyOf(registeredPlayers.values()), 1);
		gameEventProducer.publishGameEvent(currentGame);
		System.out.println(currentGame.getCurrentPlayer().getName() + " started the game with number: " + startNumber);
	}

	/**
	 * Add a new player to the game during gameplay
	 * @param playerName The name of the new player
	 */
	public void addNewPlayer(String playerName) {
		Player newPlayer = new Player(playerName);
		if (currentGame != null && !registeredPlayers.containsKey(playerName)) {
			registeredPlayers.put(playerName, newPlayer);
			currentGame = gameService.addPlayer(currentGame, newPlayer);
			System.out.println(playerName + " has been added to the game.");
		} else {
			System.out.println("Player is already registered or game not started.");
		}
	}

}

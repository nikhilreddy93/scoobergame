/**
 * 
 */
package com.jet.scoobergame.infrastructure;

import java.util.concurrent.TimeUnit;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jet.scoobergame.domain.Game;
import com.jet.scoobergame.domain.GameService;
import com.jet.scoobergame.domain.Player;

/**
 * 
 */
@Component
public class GameEventListener {

	private static final String TOPIC = "player-move";
    private static final String GROUPID = "scoober-game";
    
    private final GameService gameService;
    private final GameEventProducer gameEventProducer;
    private long latestVersion = 0; // Track the latest processed game version

    public GameEventListener(GameService gameService, GameEventProducer gameEventProducer) {
        this.gameService = gameService;
        this.gameEventProducer = gameEventProducer;
    }

    @KafkaListener(topics = TOPIC, groupId = GROUPID)
    public void handleGameEvent(Game game) {
        System.out.println("Consumed game event: " + game.getGameId() + " with number: " + game.getGameState().getNumber());
        if(game.getGameState().isGameOver()) {
        	System.out.println("Game Over. " + game.getCurrentPlayer().getName()+" is the Winner.");
        	return;
        }
        // Check if the event has already been processed or if it's outdated
        if (!gameService.isLatestVersion(game, latestVersion)) {
            System.out.println("Ignoring outdated game version: " + game.getVersion());
            return;
        }

        latestVersion = game.getVersion();  // Update to the latest version

        // Check if the next player is online; if not, wait and retry
        if (!isNextPlayerOnline(game)) {
            System.out.println("Next player " + game.getCurrentPlayer().getName() + " is not responding. Waiting...");
            Game retryGame = gameService.waitAndRetry(game);
            gameEventProducer.publishGameEvent(retryGame);
            return;
        }

        // Process the move using GameService
        Game updatedGame = gameService.processMove(game);
        gameEventProducer.publishGameEvent(updatedGame);  // Publish the updated game state
        System.out.println("Processed game event: " + updatedGame.getGameId() + " with new number: " + updatedGame.getGameState().getNumber());
        System.out.println("**********************************************************");
        try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    // Placeholder method to check if the next player is online
    private boolean isNextPlayerOnline(Game game) {
    	Player nextPlayer = game.getPlayers()
				.get((game.getPlayers().indexOf(game.getCurrentPlayer()) + 1) % game.getPlayers().size()); // getting the next player from the list
        return nextPlayer.isOnline();  
    }
}

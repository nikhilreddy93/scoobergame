/**
 * 
 */
package com.jet.scoobergame.infrastructure;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.jet.scoobergame.domain.Game;

/**
 * 
 */
@Component
public class GameEventProducer {
	
	private final KafkaTemplate<String, Game> kafkaTemplate;
    private static final String TOPIC = "player-move";

    public GameEventProducer(KafkaTemplate<String, Game> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishGameEvent(Game game) {
    	System.out.println("Produced gameId :"+ game.getGameId());
        kafkaTemplate.send(TOPIC, game);
        System.out.println("Published game event: " + game);
    }
}

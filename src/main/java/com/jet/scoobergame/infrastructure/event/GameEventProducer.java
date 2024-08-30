/**
 * 
 */
package com.jet.scoobergame.infrastructure.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.jet.scoobergame.domain.model.Game;

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
        kafkaTemplate.send(TOPIC, game);
    }
}

/**
 * 
 */
package com.jet.scoobergame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jet.scoobergame.application.PlayerService;

/**
 * 
 */
@RestController
@RequestMapping("/game")
public class GameController {
	
	@Autowired
    private PlayerService playerService;
	
	@GetMapping("/start")
	public void start() {
		playerService.startGame();
	}
	
}

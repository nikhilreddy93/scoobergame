package com.jet.scoobergame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jet.scoobergame.application.PlayerService;

@SpringBootApplication
public class ScoobergameApplication implements CommandLineRunner {

    @Autowired
    private PlayerService playerService;

    /**
     * This is the entry point of the Spring Boot application.
     *
     * @param args The command line arguments passed to the application.
     */
    public static void main(String[] args) {
        // Run the Spring Boot application
        SpringApplication.run(ScoobergameApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        playerService.registerPlayers("Nikhil", "Akhil");
    }
}

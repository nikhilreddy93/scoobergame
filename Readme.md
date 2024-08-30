# Game of Three - Spring Boot Implementation

## Introduction

This project implements the "Game of Three" using a **Spring Boot** application with **Kafka** for communication between players. The application follows **Domain-Driven Design (DDD)** principles to organize the code into different layers: **Domain**, **Application**, **Infrastructure**, and **Interface (Controllers)**.

## Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture and Design](#architecture-and-design)
3. [Game Logic](#game-logic)
4. [Classes Overview](#classes-overview)
5. [Error Handling](#error-handling)
6. [Setting Up Kafka Using Docker](#setting-up-kafka-server)
7. [Setting Up and Running the Application](#setting-up-and-running-the-application)

## Project Overview

The "Game of Three" is a simple game where players take turns modifying a given number (chosen randomly at the start). Each player can add, subtract, or do nothing with the number to make it divisible by three. The result is then divided by three to produce a new number for the next player. The game continues until the number reaches `1`.

## Architecture and Design

This project follows **Domain-Driven Design (DDD)** principles and is divided into three main layers:

1. **Domain Layer**: Contains the core business logic and domain models.
2. **Application Layer**: Coordinates the game flow by interacting with domain services and managing communication between players.
3. **Infrastructure Layer**: Manages external dependencies like Kafka, including configuration and event publishing/consumption.
4. **Interface Layer**: Contains REST controllers to interact with the application via HTTP requests.

### **Package Structure**

plaintext
com.example.gameofthree
├── application              # Application Layer
│   ├── controller           # Controllers (GameController, PlayerController)
│   ├── exception            # Custom Exceptions (GameAlreadyStartedException, etc.)
│   ├── service              # Application Services (PlayerService)
├── domain                   # Domain Layer
│   ├── model                # Entities and Value Objects (Game, Player, GameState)
│   ├── service              # Domain Services (GameService)
├── infrastructure           # Infrastructure Layer
│   ├── event                # Kafka Producer/Consumer (GameEventProducer, GameEventListener)
│   ├── config               # Kafka Configuration (KafkaConfig)
└── integration              # Integration Tests (KafkaIntegrationTest)


##Game Logic::
**Game Start**:
	The game is initialized with two players and game gets started automatically with the application start.
	A random number between 1 and Integer max value is generated as the starting number.
	
**Player Moves**:
	Each player receives a number and modifies it by adding, subtracting, or doing nothing to make it divisible by three.
	The resulting number is divided by three and sent to the next player.
	
**Game Over**:
	The game continues until the number reaches 1, at which point the game is over.

**Adding a Player**:
	Players can be added during the game using the /addPlayer API endpoint but they won't be participating in the ongoing game.
	They can be part of a next game which can be triggered using /start API endpoint.
	
##Classes Overview
**Domain Layer**::
	Game: Represents the game entity with details like current player, game state, list of players, and version.
	GameState: Represents the state of the game, mainly the current number being played.
	Player: Represents a player in the game.
	GameService: Contains the core business logic for starting the game, processing moves, and managing the game state.
	
**Application Layer**::
	PlayerService: Manages player registration, game initialization, and player moves. Interacts with GameService to handle game logic 				 and uses GameEventProducer to publish events to Kafka.
	GameController: REST controller for managing game operations (start game, add player).
	Exception Classes: Custom exceptions (GameAlreadyStartedException, PlayerAlreadyExistsException, GameNotFoundException) to handle 					different error scenarios.

**Infrastructure Layer**::
	GameEventProducer: Publishes game events to a Kafka topic (player-move).
	GameEventListener: Listens to Kafka topic (player-move) for game events and processes them.
	KafkaConfig: Configures Kafka producer and consumer settings.
	
**Integration Tests**::
	KafkaIntegrationTest: Tests the end-to-end flow of Kafka message production and consumption using an embedded Kafka broker.
	
##Error Handling
The application has robust error handling using custom exceptions and a global exception handler (GlobalExceptionHandler). It ensures that appropriate HTTP status codes and messages are returned for different scenarios:

409 Conflict: When a game has already started and players attempt to register again.
400 Bad Request: When a player already exists.
404 Not Found: When trying to add a player to a non-existent game.
500 Internal Server Error: For any other unexpected errors.

##Setting Up Kafka Using Docker
	1. Create docker-compose.yaml: Use the provided docker-compose.yaml to set up Kafka and Zookeeper.
	2. Start Kafka and Zookeeper: docker-compose up -d

##Setting Up and Running the Application
**Prerequisites**:
	-- Java 17 or later
	-- Gradle
	-- Docker for Kafka setup
**Running the Application Locally**
	1. Clone the Repository: 
		git clone https://github.com/nikhilreddy93/scoobergame.git
		cd scoobergame
	2. Build the Application:
		./gradlew clean build
	3. Run the Application:
		./gradlew bootRun
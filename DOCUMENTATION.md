# Player Communication System Documentation

## Table of Contents
1. [Introduction](#introduction)
2. [Architecture](#architecture)
3. [Class Descriptions](#class-descriptions)
   - [Server](#server)
   - [Player](#player)
   - [PlayerSameProcess](#playersameprocess)
4. [Usage Instructions](#usage-instructions)


## Introduction

The **Player Communication System** is a Java application that facilitates messaging between two players using both a server-client architecture and a synchronous model within a single process. The project demonstrates fundamental concepts of networking, multithreading, and inter-process communication in Java.

## Architecture

The application consists of three main components:
- A **Server** that manages player connections and facilitates communication.
- **Player** classes that represent individual players, capable of sending and receiving messages.
- A synchronized implementation where players communicate within the same process.

## Class Descriptions

### Server

The `Server` class is responsible for:
- Creating a `ServerSocket` to listen for incoming player connections on port 10903.
- Launching two instances of the `Player` class using `ProcessBuilder`.
- Handling message exchanges between the players using threads.

**Key Methods:**
- `main(String[] args)`: The entry point that initializes the server and player processes.
- `handlePlayer(Socket playerSocket, Socket otherPlayerSocket)`: Manages communication between two connected players, forwarding messages accordingly.

### Player

The `Player` class represents an individual player in the server-client model. It handles the sending and receiving of messages between the server and the player.

**Key Attributes:**
- `name`: The name of the player.
- `isPlayerInitiator`: A boolean flag to indicate if the player is the initiator of the messaging.
- `messageCounter`: Counts the number of messages sent by the player.

**Key Methods:**
- `messaging()`: Manages the messaging loop, sending and receiving messages until a limit is reached.
- `sendMessage(String message)`: Sends a message to the other player, appending the message counter.
- `receiveMessage()`: Receives a message from the other player.

### PlayerSameProcess

The `PlayerSameProcess` class implements the `Runnable` interface to enable communication between players within the same process. This model uses thread synchronization for message handling.

**Key Attributes:**
- `name`: The player's name.
- `isInitiator`: A flag to identify the initiator.
- `messageCounter`: Tracks the number of messages exchanged.

**Key Methods:**
- `sendMessage(String senderMsg)`: Sends a message to the other player and increments the message counter.
- `receiveMessage()`: Receives a message from the other player.
- `run()`: The method that executes the player's messaging logic in a separate thread.

## Usage Instructions

1. **Set Up the Project**:
   - Ensure you have JDK 21 or higher installed.
   - Navigate to the project directory.
   - Required Apache Maven
   - Required Git Bash (for running the provided Bash script on Windows)

2. **Build the Project**:
   - Open the Git Bash at project directory
   - Run this command : `./start_communication.sh`

   

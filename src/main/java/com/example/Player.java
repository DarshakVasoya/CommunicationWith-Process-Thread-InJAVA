package com.example;

import java.io.*;
import java.net.*;

public class Player {
    final int MAX_MESSAGES = 10; // Maximum number of messages that each player can send
    String name; // Name of the player
    boolean isPlayerInitiator; // Indicates if this player is the initiator of the messaging
    Socket playerSocket; // Socket to handle communication with the server
    BufferedReader in; // Reader for incoming messages
    PrintWriter out; // Writer for outgoing messages
    int messageCounter; // Counter to track the number of messages sent

    // Constructor to initialize player with a name and initiator status
    Player(String name, String isInitiator) {
        this.name = name;
        this.isPlayerInitiator = Boolean.parseBoolean(isInitiator);
        this.messageCounter = 0; // Initialize message counter to zero
    }

    // Method to handle messaging between players
    void messaging() {
        try {
            if (isPlayerInitiator) {
                // If this player is the initiator, send the first message
                sendMessage("Hello, Good Morning!!");
            }

            // Loop to manage the sending and receiving of messages
            while (messageCounter < MAX_MESSAGES) {
                String receivedMessage = receiveMessage(); // Wait to receive a message
                if (receivedMessage != null) { // Check for null to avoid NullPointerException
                    sendMessage(receivedMessage); // send received message back
                }
            }
        } catch (IOException e) {
            System.err.println("Error during communication: " + e.getMessage());
        } finally {
            // Output the final count of messages sent and received
            System.out.println(name + " has sent and received " + messageCounter + " messages. Game over.");
            cleanup(); // Cleanup resources at the end of messaging
        }
    }

    // Method to send a message to the other player
    void sendMessage(String message) {
        messageCounter++; // Increment message counter
        message= message + " ("+name+": " +messageCounter+")"; // Concating the message with counter
        out.println(message); // Send message through the PrintWriter
        System.out.println(name + " sent: " + message); // print sent message
    }

    // Method to receive a message from the other player
    String receiveMessage() throws IOException {
        String message = in.readLine(); // Read the incoming message
        System.out.println(name + " received: " + message); // print received message
        return message; // Return the received message, may return null if the stream is closed
    }

    // Method to cleanup resources such as streams and sockets
    void cleanup() {
        try {
            if (out != null) out.close(); // Close the output stream
            if (in != null) in.close(); // Close the input stream
            if (playerSocket != null && !playerSocket.isClosed()) playerSocket.close(); // Close the socket if open
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage()); // Log any cleanup errors
        }
    }

    // Method to initialize the socket connection to the server
    void initializeSocket() throws IOException {
        playerSocket = new Socket("localhost", 10903); // Connect to the server on the specified port
        in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream())); // Initialize input stream
        out = new PrintWriter(playerSocket.getOutputStream(), true); // Initialize output stream with auto-flush
    }

    // Main method to run the Player application
    public static void main(String[] args) {
        Player player = new Player(args[0], args[1]); // Create a new player with command-line arguments

        try {
            player.initializeSocket(); // Initialize socket and streams for communication
            player.messaging(); // Start messaging with the other player
        } catch (IOException e) {
            e.printStackTrace(); // Print any IO exceptions that occur during socket initialization or messaging
        } finally {
            player.cleanup(); // Ensure resources are cleaned up even if an exception occurs
        }
    }
}

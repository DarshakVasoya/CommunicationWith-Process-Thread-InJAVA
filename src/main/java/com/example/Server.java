package com.example;


import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
    try {
         // Create a ServerSocket to listen on port 10903
         System.out.println("\n Message formate : \"Message (Player name: Message counter) \"");
        
         ServerSocket serverSocket = new ServerSocket(10903);
         System.out.println("\n Server started.");
 
         // Start Player 1 Process
         // Set up the ProcessBuilder to launch the Player process
         ProcessBuilder player1Process = new ProcessBuilder("java", "-cp", "src/main/java",
                 "com.example.Player", "Player 1", "true");
         // Inherit IO allows the player process to use the server's standard input/output
         player1Process.inheritIO();
         Process player1 = player1Process.start(); // Start the player process
         System.out.println("PID for player 1: " + player1.pid()); // Print Player 1's PID
 
         // Accept Player 1 Connection
         Socket player1Socket = serverSocket.accept(); // Wait for Player 1 to connect
         System.out.println("Player 1 connected to server.");
 
         // Start Player 2 Process
         // Set up the ProcessBuilder for Player 2
         ProcessBuilder player2Process = new ProcessBuilder("java", "-cp", "src/main/java",
                 "com.example.Player", "Player 2", "false");
         player2Process.inheritIO(); // Inherit IO for Player 2 as well
         Process player2 = player2Process.start(); // Start Player 2 process
         System.out.println("PID for player 2: " + player2.pid()); // Print Player 2's PID
 
         // Accept Player 2 Connection
         Socket player2Socket = serverSocket.accept(); // Wait for Player 2 to connect
         System.out.println("Player 2 connected to server.");
 
         // Create threads to handle communication between players
         Thread t1 = new Thread(() -> handlePlayer(player1Socket, player2Socket));
         t1.start(); // Start the thread for Player 1
 
         Thread t2 = new Thread(() -> handlePlayer(player2Socket, player1Socket));
         t2.start(); // Start the thread for Player 2
 
         // Wait for both player threads to complete and for both processes to finish
         try {
             t1.join(); // Wait for thread t1 to complete
             t2.join(); // Wait for thread t2 to complete
             player1.waitFor(); // Wait for Player 1 process to complete
             player2.waitFor(); // Wait for Player 2 process to complete
         } catch (Exception e) {
             e.printStackTrace(); // Handle any exceptions during waiting
         }
         
         
         player1Socket.close(); // Close the player socket
         player2Socket.close();
         serverSocket.close(); // Close the server socket
         System.out.println("Server closed.");
     
    } catch (Exception e) {
        
    }   
    }

    // Method to handle communication between two players
    static void handlePlayer(Socket playerSocket, Socket otherPlayerSocket) {
        BufferedReader in = null; // Input stream for the current player
        PrintWriter out = null;   // Output stream for the other player
        try {
            // Create input and output streams for player communication
            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            out = new PrintWriter(otherPlayerSocket.getOutputStream(), true);

            String message;
            // Read messages from the current player and forward them to the other player
            while ((message = in.readLine()) != null) {
                out.println(message); // Forward message to the other player
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle any I/O exceptions
        } finally {
            // Cleanup: Ensure streams and sockets are closed after communication
            try {
                if (in != null)
                    in.close(); // Close the input stream
                if (out != null)
                    out.close(); // Close the output stream
            } catch (IOException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}

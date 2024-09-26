package com.example;
/**
 * PlayerSameProcess class implements the Runnable interface.
 * 
 * This class represents a player in a messaging game where two players send
 * and receive messages in a synchronized manner. The initiator player starts
 * the communication by sending a message, while the responder player waits
 * for messages and responds back until a maximum number of messages is exchanged.
 * Both player's thread will run in same process.
 */
public class PlayerSameProcess implements Runnable {
    final int MAX_MESSAGES = 10; // Maximum number of messages to send/receive
    final String name; // Name of the player
    int messageCounter; // Counter for tracking the number of messages sent/received
    final boolean isInitiator; // Flag to identify the initiator of the messaging
    final Object lock; // Lock object for synchronization
    static String message; // Store the message to be sent/received

    // Constructor to initialize player attributes
    public PlayerSameProcess(String name, boolean isInitiator, Object lock) {
        this.name = name; // Set the player's name
        this.isInitiator = isInitiator; // Set whether this player is the initiator
        this.messageCounter = 0; // Initialize message counter
        this.lock = lock; // Set the lock object for synchronization
        message = ""; // Initialize the message
    }

    // Method to send a message
    public void sendMessage(String senderMsg) {
        synchronized (lock) { // Ensure only one thread can execute this block at a time
            messageCounter++; // Increment the message counter
            // Construct the message with the sender's name and counter
            message = senderMsg + " (" + name + ": " + messageCounter + ")"; 
            System.out.println(name + " sending: " + message); // Print the sending message
            lock.notify(); // Notify the other thread to proceed
        }
    }

    // Method to receive a message
    public String receiveMessage() {
        synchronized (lock) { // Ensure thread-safe access to the message
            String msg = message; // Read the incoming message
            System.out.println(name + " received: " + msg); // Print received message
            message = ""; // Clear the message after receiving
            return msg; 
        }
    }

    @Override
    public void run() {
        try {
            // Print the process ID for the current thread
            System.out.println("\nProcess Id of " + name + " (PID): " + ProcessHandle.current().pid() + "\n");
            if (isInitiator) { // If this player is the initiator
                synchronized (lock) {
                    sendMessage("Hello, Good Morning!!"); // Send the first message
                    lock.wait(); // Wait for the response
                }
            }
          
            // Loop to send and receive messages until the maximum limit is reached
            while (messageCounter < MAX_MESSAGES) {
                synchronized (lock) {
                    // Check if there is a message to receive
                    if (!message.isEmpty()) { 
                        sendMessage(receiveMessage()); // Receive and send back the message
                    }
                    
                    // If there are still messages to be exchanged, wait for notification
                    if (messageCounter < MAX_MESSAGES) { 
                        lock.wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            // Handle interruption of the thread
            System.err.println(name + " interrupted: " + e.getMessage());
        } finally {
            // Print the total messages sent and received by the player
            System.out.println("\n" + name + " has sent and received " + messageCounter + " messages");
        }
    }

    public static void main(String[] args) {
        System.out.println("\nMessage format: \"Message (Player name: Message counter) \"");

        Object lock = new Object(); // Create a shared lock object for synchronization

        // Create two player instances, one as initiator and the other as responder
        PlayerSameProcess player1 = new PlayerSameProcess("Player 1", true, lock);
        PlayerSameProcess player2 = new PlayerSameProcess("Player 2", false, lock);

        // Create threads for each player
        Thread t1 = new Thread(player1);
        Thread t2 = new Thread(player2);
        
        // Start both threads
        t1.start(); // Start initiator thread
        t2.start(); // Start responder thread 
       
        
        // Wait for both threads to finish before exiting main thread
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            // Handle interruption of the main thread
            System.err.println("Main thread interrupted: " + e.getMessage());
        }
    }
}

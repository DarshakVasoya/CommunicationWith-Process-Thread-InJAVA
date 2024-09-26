#!/bin/bash
echo "Compiling Java files..."

javac ./src/main/java/com/example/*.java
while true; do
    

    if [ $? -eq 0 ]; then
        echo "Compilation successful."

        # Menu to choose communication mode
        echo "Choose communication mode: "
        echo "1. Same Process"
        echo "2. Different Processes"
        echo "3. Exit"
        read mode

        if [ "$mode" -eq 1 ]; then
            echo "Starting players in the same process..."
            java -cp ./src/main/java com.example.PlayerSameProcess
            echo "Operation completed. Returning to the menu..."
        elif [ "$mode" -eq 2 ]; then
            echo "Starting players in different processes..."
            java -cp ./src/main/java com.example.Server
            echo "Operation completed. Returning to the menu..."
        elif [ "$mode" -eq 3 ]; then
            echo "Exiting the script. Goodbye!"
            exit 0
        else
            echo "Invalid option selected. Please choose a valid option."
        fi
    else
        echo "Compilation failed. Please check your code."
    fi

    echo "--------------------------------------"
done

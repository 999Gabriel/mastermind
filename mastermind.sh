#!/bin/bash

# Path to the JAR file
JAR_PATH="$HOME/Developer/Mastermind/target/Mastermind-1.0-SNAPSHOT.jar"

# Check if the JAR exists
if [ ! -f "$JAR_PATH" ]; then
    echo "Error: Mastermind JAR not found at $JAR_PATH"
    echo "Building the project now..."
    cd "$HOME/Developer/Mastermind" && mvn clean package

    if [ ! -f "$JAR_PATH" ]; then
        echo "Build failed. Please check your project for errors."
        exit 1
    fi
fi

# Launch the application with JavaFX modules
java --module-path "$HOME/.m2/repository/org/openjfx" \
     --add-modules javafx.controls,javafx.fxml \
     -jar "$JAR_PATH"
# Mastermind Game

A modern implementation of the classic code-breaking game Mastermind built with JavaFX.

## Overview

This application is a digital version of the popular board game Mastermind. Players attempt to guess a secret color code within a limited number of attempts. After each guess, feedback is provided on how many colors are correct and in the right position, and how many colors are correct but in the wrong position.

## Features

- Modern, intuitive user interface built with JavaFX
- Customizable game settings (difficulty levels)
- Score tracking and game statistics
- Multiple color schemes

## Technology Stack

- Java 22
- JavaFX 22.0.1
- Maven for build and dependency management
- JUnit 5 for testing

## Installation

### Prerequisites

- Java 22 or higher
- Maven 3.6 or higher

### Build from Source

1. Clone the repository:
   git clone https://github.com/999Gabriel/Mastermind.git cd Mastermind
2. Build the project:
   mvn clean package
### Running the Game

#### Using Maven
mvn javafx:run
#### Using the JAR file
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -jar target/Mastermind-1.0-SNAPSHOT.jar
#### Using the Launcher Script

For convenience, you can install the launcher script:

```bash
#!/bin/bash

# Go to the Mastermind directory
cd "$HOME/Developer/Mastermind" || exit

# Run the application
mvn javafx:run
```
Save it to /usr/local/bin/mastermind and make it executable:
sudo mv mastermind.sh /usr/local/bin/mastermind
sudo chmod +x /usr/local/bin/mastermind

Launch the game by typing mastermind in your terminal.

How to Play
The computer randomly generates a secret code of four colors.
You have 10 attempts to guess this code.
After each guess, feedback is provided:
Black peg: correct color in the correct position
White peg: correct color but in the wrong position
Use logic and deduction to narrow down the possibilities.
Game Rules
The secret code consists of 4 colors from a set of 6 possible colors
Colors can be repeated in the code
You have 10 attempts to guess the correct code
To win, you must correctly identify all colors and their positions
Development
Project Structure
src/main/java/swp/com/mastermind - Source code
src/main/resources - UI resources, CSS, and images
src/test/java - Unit tests
Running Tests
mvn test

License
This project is open source, licensed under the MIT License.
Acknowledgments
Original Mastermind game by Mordecai Meirowitz
package swp.com.mastermind;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.*;
import java.util.logging.Logger;

public class MastermindController {
    private static final Logger LOGGER = Logger.getLogger(MastermindController.class.getName());

    @FXML private GridPane guessGrid;
    @FXML private HBox colorSelector;
    @FXML private HBox currentGuess;
    @FXML private Button submitButton;
    @FXML private Button newGameButton;
    @FXML private Label messageLabel;
    @FXML private Label timerLabel;

    private MastermindGame game;
    private HighScoreManager highScoreManager;
    private String selectedColor;
    private int selectedPosition = -1;
    private List<String> currentGuessColors;
    private ObservableList<HBox> guessRows = FXCollections.observableArrayList();

    private javafx.animation.Timeline timeline;

    // In your MastermindController class
    private static final Map<String, String> COLOR_STYLES = Map.of(
            "RED", "-fx-background-color: #ff5555;",
            "GREEN", "-fx-background-color: #50fa7b;",
            "BLUE", "-fx-background-color: #8be9fd;",
            "YELLOW", "-fx-background-color: #f1fa8c;",
            "ORANGE", "-fx-background-color: #ffb86c;",
            "PURPLE", "-fx-background-color: #bd93f9;",
            "WHITE", "-fx-background-color: #f8f8f2;",
            "BLACK", "-fx-background-color: #282a36;"
    );

    private static final String BLACK_PEG_STYLE = "-fx-background-color: #f8f8f2;"; // Exact match
    private static final String WHITE_PEG_STYLE = "-fx-background-color: #6272a4;"; // Color match

    // When creating UI elements:
    private void createColorPeg(String color) {
        Button peg = new Button();
        peg.getStyleClass().add("color-peg");
        peg.setStyle(COLOR_STYLES.get(color));
        // Other initialization code
    }

    public void initialize() {
        highScoreManager = new HighScoreManager();
        initializeColorSelector();
        initializeCurrentGuess();
        startNewGame();
        startTimer();
    }

    private void initializeColorSelector() {
        colorSelector.setSpacing(10);
        for (String color : MastermindGame.COLORS) {
            Circle circle = createColorCircle(color);
            circle.setOnMouseClicked(e -> {
                selectedColor = color;
                if (selectedPosition != -1) {
                    updateCurrentGuess(selectedPosition, color);
                    selectedPosition = -1;
                }
            });
            colorSelector.getChildren().add(circle);
        }
    }

    private void initializeCurrentGuess() {
        currentGuess.setSpacing(10);
        currentGuessColors = new ArrayList<>(Arrays.asList(new String[MastermindGame.CODE_LENGTH]));

        for (int i = 0; i < MastermindGame.CODE_LENGTH; i++) {
            final int position = i;
            Circle circle = createEmptyCircle();
            circle.setOnMouseClicked(e -> selectedPosition = position);
            currentGuess.getChildren().add(circle);
        }

        submitButton.setDisable(true);
        submitButton.setOnAction(e -> submitGuess());
        newGameButton.setOnAction(e -> startNewGame());
    }

    private void updateCurrentGuess(int position, String color) {
        Circle circle = (Circle) currentGuess.getChildren().get(position);
        circle.setFill(Color.valueOf(color));
        currentGuessColors.set(position, color);

        // Enable submit button if all positions have a color
        submitButton.setDisable(currentGuessColors.contains(null));
    }

    @FXML
    private void submitGuess() {
        String[] guess = currentGuessColors.toArray(new String[0]);
        GuessResult result = game.makeGuess(guess);

        // Add guess to the display
        addGuessToGrid(guess, result);

        // Clear current guess
        for (int i = 0; i < MastermindGame.CODE_LENGTH; i++) {
            ((Circle) currentGuess.getChildren().get(i)).setFill(Color.WHITE);
            currentGuessColors.set(i, null);
        }
        submitButton.setDisable(true);

        // Check game state
        if (game.isGameOver()) {
            handleGameOver();
        }
    }

    private void addGuessToGrid(String[] guess, GuessResult result) {
        HBox guessRow = new HBox(10);
        guessRow.setPadding(new Insets(5));

        // Add guess circles
        for (String color : guess) {
            guessRow.getChildren().add(createColorCircle(color));
        }

        // Add separator
        Separator separator = new Separator();
        separator.setOrientation(javafx.geometry.Orientation.VERTICAL);
        guessRow.getChildren().add(separator);

        // Add result pegs (black for exact matches, white for color matches)
        HBox resultBox = new HBox(5);
        for (int i = 0; i < result.getExactMatches(); i++) {
            Circle blackPeg = new Circle(5);
            blackPeg.setFill(Color.BLACK);
            resultBox.getChildren().add(blackPeg);
        }
        for (int i = 0; i < result.getColorMatches(); i++) {
            Circle whitePeg = new Circle(5);
            whitePeg.setFill(Color.LIGHTGRAY);
            resultBox.getChildren().add(whitePeg);
        }
        guessRow.getChildren().add(resultBox);

        // Add to grid
        guessRows.add(0, guessRow);
        guessGrid.getChildren().clear();
        for (int i = 0; i < guessRows.size(); i++) {
            guessGrid.add(guessRows.get(i), 0, i);
        }
    }

    private void handleGameOver() {
        stopTimer();

        if (game.isGameWon()) {
            messageLabel.setText("You won in " + game.getAttempts() + " attempts!");

            // Check for high score
            if (highScoreManager.isHighScore(game.getAttempts(), game.getGameDurationSeconds())) {
                Platform.runLater(this::promptForHighScoreName);
            }
        } else {
            messageLabel.setText("Game over! The code was: ");
            HBox secretCodeBox = new HBox(10);
            for (String color : game.getSecretCode()) {
                secretCodeBox.getChildren().add(createColorCircle(color));
            }
            guessGrid.add(secretCodeBox, 0, guessRows.size() + 1);
        }

        currentGuess.setDisable(true);
        submitButton.setDisable(true);
    }

    private void promptForHighScoreName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("High Score!");
        dialog.setHeaderText("You made the high score list!");
        dialog.setContentText("Enter your name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            highScoreManager.addHighScore(name, game.getGameDurationSeconds(), game.getAttempts());
            showHighScores();
        });
    }

    private void showHighScores() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("High Scores");
        alert.setHeaderText("Top Scores");

        StringBuilder content = new StringBuilder();
        for (HighScore score : highScoreManager.getHighScores()) {
            content.append(score.toString()).append("\n");
        }

        alert.setContentText(content.toString());
        alert.showAndWait();
    }

    @FXML
    private void startNewGame() {
        game = new MastermindGame();
        guessRows.clear();
        guessGrid.getChildren().clear();
        messageLabel.setText("Make your guess!");

        // Reset current guess
        for (int i = 0; i < MastermindGame.CODE_LENGTH; i++) {
            ((Circle) currentGuess.getChildren().get(i)).setFill(Color.WHITE);
            currentGuessColors.set(i, null);
        }

        currentGuess.setDisable(false);
        submitButton.setDisable(true);
        startTimer();
    }

    private void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(Duration.seconds(1), e -> {
                    updateTimerDisplay();
                })
        );
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
    }

    private void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    private void updateTimerDisplay() {
        long seconds = game.getGameDurationSeconds();
        timerLabel.setText(String.format("Time: %02d:%02d", seconds / 60, seconds % 60));
    }

    private Circle createColorCircle(String colorName) {
        Circle circle = new Circle(15);
        circle.setFill(Color.valueOf(colorName));
        circle.setStroke(Color.BLACK);
        return circle;
    }

    private Circle createEmptyCircle() {
        Circle circle = new Circle(15);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);
        return circle;
    }

    @FXML
    private void showHighScoresAction() {
        showHighScores();
    }

    @FXML
    private void exitGame() {
        Platform.exit();
    }
}
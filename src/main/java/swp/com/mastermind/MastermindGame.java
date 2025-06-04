package swp.com.mastermind;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.logging.Level;

public class MastermindGame {
    private static final Logger LOGGER = Logger.getLogger(MastermindGame.class.getName());

    public static final int CODE_LENGTH = 4;
    public static final int MAX_ATTEMPTS = 10;
    public static final String[] COLORS = {"RED", "BLUE", "GREEN", "YELLOW", "PURPLE", "ORANGE"};

    private String[] secretCode;
    private List<GuessResult> guesses;
    private boolean gameWon;
    private boolean gameOver;
    private long startTime;
    private long endTime;

    public MastermindGame() {
        secretCode = new String[CODE_LENGTH];
        guesses = new ArrayList<>();
        gameWon = false;
        gameOver = false;
        generateSecretCode();
        startTime = System.currentTimeMillis();
        LOGGER.info("New game started with secret code: " + String.join(",", secretCode));
    }

    private void generateSecretCode() {
        Random random = new Random();
        for (int i = 0; i < CODE_LENGTH; i++) {
            secretCode[i] = COLORS[random.nextInt(COLORS.length)];
        }
    }

    public GuessResult makeGuess(String[] guess) {
        if (gameOver) {
            LOGGER.warning("Attempt to guess after game over");
            return null;
        }

        int exactMatches = 0;
        int colorMatches = 0;

        // Count exact matches and track used positions
        boolean[] secretUsed = new boolean[CODE_LENGTH];
        boolean[] guessUsed = new boolean[CODE_LENGTH];

        // Check for exact matches
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (guess[i].equals(secretCode[i])) {
                exactMatches++;
                secretUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        // Check for color matches (right color, wrong position)
        for (int i = 0; i < CODE_LENGTH; i++) {
            if (!guessUsed[i]) {
                for (int j = 0; j < CODE_LENGTH; j++) {
                    if (!secretUsed[j] && guess[i].equals(secretCode[j])) {
                        colorMatches++;
                        secretUsed[j] = true;
                        break;
                    }
                }
            }
        }

        GuessResult result = new GuessResult(guess, exactMatches, colorMatches);
        guesses.add(result);
        LOGGER.info("Guess made: " + String.join(",", guess) +
                " | Result: Exact=" + exactMatches + ", Color=" + colorMatches);

        // Check for win or loss
        if (exactMatches == CODE_LENGTH) {
            gameWon = true;
            gameOver = true;
            endTime = System.currentTimeMillis();
            LOGGER.info("Game won in " + guesses.size() + " attempts");
        } else if (guesses.size() >= MAX_ATTEMPTS) {
            gameOver = true;
            endTime = System.currentTimeMillis();
            LOGGER.info("Game lost after max attempts");
        }

        return result;
    }

    public List<GuessResult> getGuesses() {
        return guesses;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String[] getSecretCode() {
        return secretCode;
    }

    public long getGameDurationSeconds() {
        if (endTime == 0) {
            return (System.currentTimeMillis() - startTime) / 1000;
        }
        return (endTime - startTime) / 1000;
    }

    public int getAttempts() {
        return guesses.size();
    }
}
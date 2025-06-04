package swp.com.mastermind;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class MastermindGameTest {

    private MastermindGame game;

    @BeforeEach
    void setUp() {
        game = new MastermindGame();
    }

    @Test
    void testNewGameCreatesSecretCode() {
        // Secret code should have been created at initialization
        String[] secretCode = game.getSecretCode();
        Assertions.assertNotNull(secretCode);
        Assertions.assertEquals(MastermindGame.CODE_LENGTH, secretCode.length);

        for (String color : secretCode) {
            Assertions.assertTrue(containsColor(MastermindGame.COLORS, color));
        }
    }

    @Test
    void testMakeCorrectGuess() {
        // Set a known secret code using reflection
        String[] knownCode = {"RED", "GREEN", "BLUE", "YELLOW"};
        setSecretCode(knownCode);

        // Make the correct guess
        GuessResult result = game.makeGuess(knownCode);

        // Verify result
        Assertions.assertEquals(MastermindGame.CODE_LENGTH, result.getExactMatches());
        Assertions.assertEquals(0, result.getColorMatches());
        Assertions.assertTrue(game.isGameOver());
        Assertions.assertTrue(game.isGameWon());
        Assertions.assertEquals(1, game.getAttempts());
    }

    @Test
    void testMakeIncorrectGuess() {
        // Set a known secret code using reflection
        String[] knownCode = {"RED", "GREEN", "BLUE", "YELLOW"};
        setSecretCode(knownCode);

        // Make an incorrect guess
        String[] guess = {"YELLOW", "BLUE", "GREEN", "RED"};
        GuessResult result = game.makeGuess(guess);

        // Verify result
        Assertions.assertEquals(0, result.getExactMatches());
        Assertions.assertEquals(4, result.getColorMatches());
        Assertions.assertFalse(game.isGameOver());
        Assertions.assertFalse(game.isGameWon());
        Assertions.assertEquals(1, game.getAttempts());
    }

    @Test
    void testPartialMatchGuess() {
        // Set a known secret code using reflection
        String[] knownCode = {"RED", "GREEN", "BLUE", "YELLOW"};
        setSecretCode(knownCode);

        // Make a guess with some matches
        String[] guess = {"RED", "BLUE", "YELLOW", "PURPLE"};
        GuessResult result = game.makeGuess(guess);

        // Verify result - 1 exact match (RED) and 2 color matches (BLUE, YELLOW)
        Assertions.assertEquals(1, result.getExactMatches());
        Assertions.assertEquals(2, result.getColorMatches());
        Assertions.assertFalse(game.isGameOver());
        Assertions.assertFalse(game.isGameWon());
    }

    @Test
    void testMaxAttemptsReached() {
        // Set a known secret code
        String[] knownCode = {"RED", "GREEN", "BLUE", "YELLOW"};
        setSecretCode(knownCode);

        // Incorrect guess that we'll repeat
        String[] guess = {"PURPLE", "PURPLE", "PURPLE", "PURPLE"};

        // Make guesses until max attempts reached
        for (int i = 0; i < MastermindGame.MAX_ATTEMPTS - 1; i++) {
            game.makeGuess(guess);
            Assertions.assertFalse(game.isGameOver());
        }

        // Make the final guess
        game.makeGuess(guess);
        Assertions.assertTrue(game.isGameOver());
        Assertions.assertFalse(game.isGameWon());
        Assertions.assertEquals(MastermindGame.MAX_ATTEMPTS, game.getAttempts());
    }

    @Test
    void testGameTimer() throws InterruptedException {
        // Make a short delay to ensure timer increments
        Thread.sleep(2000);

        // Check time is greater than 0
        Assertions.assertTrue(game.getGameDurationSeconds() >= 1);
    }

    // Helper methods
    private boolean containsColor(String[] colors, String colorToCheck) {
        for (String color : colors) {
            if (color.equals(colorToCheck)) {
                return true;
            }
        }
        return false;
    }

    private void setSecretCode(String[] code) {
        try {
            Field secretCodeField = MastermindGame.class.getDeclaredField("secretCode");
            secretCodeField.setAccessible(true);
            secretCodeField.set(game, code);
        } catch (Exception e) {
            Assertions.fail("Failed to set secret code using reflection: " + e.getMessage());
        }
    }
}
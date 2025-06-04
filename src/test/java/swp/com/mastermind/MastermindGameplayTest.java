package swp.com.mastermind;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

public class MastermindGameplayTest {

    private MastermindGame game;

    @BeforeEach
    void setUp() {
        game = new MastermindGame();
    }

    @Test
    void testGameInitialization() {
        // Verify game initializes correctly
        assertNotNull(game);
        assertFalse(game.isGameOver());
        assertFalse(game.isGameWon());
        assertEquals(0, game.getAttempts());
        assertNotNull(game.getSecretCode());
        assertEquals(4, game.getSecretCode().length);
    }

    @Test
    void testFullGameWithWin() throws Exception {
        // Set a known secret code
        String[] secretCode = {"RED", "BLUE", "GREEN", "YELLOW"};
        setSecretCode(secretCode);

        // First attempt - completely wrong
        GuessResult result1 = game.makeGuess(new String[]{"ORANGE", "PURPLE", "PURPLE", "ORANGE"});
        assertEquals(0, result1.getExactMatches());
        assertEquals(0, result1.getColorMatches());
        assertEquals(1, game.getAttempts());
        assertFalse(game.isGameOver());

        // Second attempt - partially correct
        GuessResult result2 = game.makeGuess(new String[]{"RED", "YELLOW", "ORANGE", "GREEN"});
        assertEquals(1, result2.getExactMatches()); // RED is in correct position
        assertEquals(2, result2.getColorMatches()); // YELLOW and GREEN are in wrong positions
        assertEquals(2, game.getAttempts());
        assertFalse(game.isGameOver());

        // Third attempt - more correct
        GuessResult result3 = game.makeGuess(new String[]{"RED", "BLUE", "ORANGE", "YELLOW"});
        assertEquals(3, result3.getExactMatches()); // RED, BLUE, YELLOW in correct positions
        assertEquals(0, result3.getColorMatches()); // No color-only matches
        assertEquals(3, game.getAttempts());
        assertFalse(game.isGameOver());

        // Final attempt - all correct
        GuessResult result4 = game.makeGuess(new String[]{"RED", "BLUE", "GREEN", "YELLOW"});
        assertEquals(4, result4.getExactMatches());
        assertEquals(0, result4.getColorMatches());
        assertEquals(4, game.getAttempts());
        assertTrue(game.isGameOver());
        assertTrue(game.isGameWon());
    }

    @Test
    void testGameLoss() throws Exception {
        // Set a known secret code
        String[] secretCode = {"RED", "BLUE", "GREEN", "YELLOW"};
        setSecretCode(secretCode);

        // Make MAX_ATTEMPTS incorrect guesses
        String[] incorrectGuess = {"PURPLE", "ORANGE", "PURPLE", "ORANGE"};
        for (int i = 0; i < MastermindGame.MAX_ATTEMPTS; i++) {
            game.makeGuess(incorrectGuess);
        }

        assertTrue(game.isGameOver());
        assertFalse(game.isGameWon());
        assertEquals(MastermindGame.MAX_ATTEMPTS, game.getAttempts());
    }

    @Test
    void testGuessResultCalculation() throws Exception {
        // Set a known secret code
        String[] secretCode = {"RED", "BLUE", "GREEN", "YELLOW"};
        setSecretCode(secretCode);

        // Test case 1: No matches
        GuessResult result1 = game.makeGuess(new String[]{"PURPLE", "ORANGE", "PURPLE", "ORANGE"});
        assertEquals(0, result1.getExactMatches());
        assertEquals(0, result1.getColorMatches());

        // Test case 2: Some exact matches, some color matches
        GuessResult result2 = game.makeGuess(new String[]{"RED", "YELLOW", "ORANGE", "GREEN"});
        assertEquals(1, result2.getExactMatches()); // RED in correct position
        assertEquals(2, result2.getColorMatches()); // YELLOW and GREEN in wrong positions
    }

    // Helper method to set the secret code using reflection
    private void setSecretCode(String[] code) throws Exception {
        Field secretCodeField = MastermindGame.class.getDeclaredField("secretCode");
        secretCodeField.setAccessible(true);
        secretCodeField.set(game, code);
    }
}
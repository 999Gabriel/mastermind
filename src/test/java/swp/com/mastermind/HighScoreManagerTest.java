package swp.com.mastermind;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HighScoreManagerTest {

    private HighScoreManager highScoreManager;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        // Create a new high score manager
        highScoreManager = new HighScoreManager();

        // Set initial empty high scores list using reflection
        Field highScoresField = HighScoreManager.class.getDeclaredField("highScores");
        highScoresField.setAccessible(true);
        highScoresField.set(highScoreManager, new ArrayList<>());
    }

    @Test
    void testAddHighScore() {
        // Add a high score
        highScoreManager.addHighScore("TestPlayer", 120, 8);

        // Verify high score was added
        List<HighScore> scores = highScoreManager.getHighScores();
        assertEquals(1, scores.size());
        HighScore score = scores.get(0);
        assertEquals("TestPlayer", score.getPlayerName());
        assertEquals(120, score.getTimeInSeconds());
        assertEquals(8, score.getAttempts());
    }

    @Test
    void testHighScoreSorting() {
        // Add multiple high scores
        highScoreManager.addHighScore("Player1", 180, 10);  // Worst (more attempts, more time)
        highScoreManager.addHighScore("Player2", 120, 8);   // Middle
        highScoreManager.addHighScore("Player3", 100, 5);   // Best (fewer attempts, less time)

        // Verify scores are sorted correctly
        List<HighScore> scores = highScoreManager.getHighScores();
        assertEquals(3, scores.size());
        assertEquals("Player3", scores.get(0).getPlayerName());  // Best score should be first
        assertEquals("Player2", scores.get(1).getPlayerName());
        assertEquals("Player1", scores.get(2).getPlayerName());  // Worst score should be last
    }

    @Test
    void testIsHighScore() {
        // Set up some existing high scores
        for (int i = 0; i < 10; i++) {
            highScoreManager.addHighScore("Player" + i, 100 + i, 10);
        }

        // Test with a better score
        assertTrue(highScoreManager.isHighScore(8, 90));

        // Test with a worse score
        assertFalse(highScoreManager.isHighScore(11, 200));
    }

    @Test
    void testMaxHighScores() {
        // Clear any existing high scores first
        try {
            Field highScoresField = HighScoreManager.class.getDeclaredField("highScores");
            highScoresField.setAccessible(true);
            highScoresField.set(highScoreManager, new ArrayList<>());
        } catch (Exception e) {
            fail("Failed to clear high scores: " + e.getMessage());
        }

        // Add exactly MAX_HIGH_SCORES scores first (assuming MAX_HIGH_SCORES is 10)
        for (int i = 0; i < 10; i++) {
            highScoreManager.addHighScore("Player" + i, 100 + i, 10);
        }

        // Then add one more better score
        highScoreManager.addHighScore("BetterPlayer", 90, 5);

        // Verify we still have 10 scores
        List<HighScore> scores = highScoreManager.getHighScores();
        assertEquals(10, scores.size());

        // Verify the better score is included
        boolean foundBetterPlayer = false;
        for (HighScore score : scores) {
            if (score.getPlayerName().equals("BetterPlayer")) {
                foundBetterPlayer = true;
                break;
            }
        }
        assertTrue(foundBetterPlayer, "The better score should be in the high scores list");
    }
}
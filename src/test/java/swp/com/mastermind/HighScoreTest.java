package swp.com.mastermind;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HighScoreTest {

    @Test
    void testHighScoreConstruction() {
        HighScore score = new HighScore("TestPlayer", 120, 8);
        assertEquals("TestPlayer", score.getPlayerName());
        assertEquals(120, score.getTimeInSeconds());
        assertEquals(8, score.getAttempts());
    }

    @Test
    void testHighScoreComparison() {
        HighScore score1 = new HighScore("Player1", 120, 8);
        HighScore score2 = new HighScore("Player2", 120, 10);  // More attempts (worse)
        HighScore score3 = new HighScore("Player3", 120, 8);   // Same attempts, same time
        HighScore score4 = new HighScore("Player4", 150, 8);   // Same attempts, more time (worse)
        HighScore score5 = new HighScore("Player5", 100, 7);   // Fewer attempts, less time (better)

        // Test comparisons
        assertTrue(score1.compareTo(score2) < 0);  // score1 is better than score2
        assertEquals(0, score1.compareTo(score3)); // score1 is equal to score3
        assertTrue(score1.compareTo(score4) < 0);  // score1 is better than score4
        assertTrue(score1.compareTo(score5) > 0);  // score1 is worse than score5
    }

    @Test
    void testToString() {
        HighScore score = new HighScore("TestPlayer", 65, 8);
        String expected = String.format("%-15s %3d attempts %3d seconds", "TestPlayer", 8, 65);
        assertEquals(expected, score.toString());
    }

    @Test
    void testTimeFormatting() {
        // Test various time values
        HighScore score1 = new HighScore("Player1", 30, 8);    // 30 seconds
        HighScore score2 = new HighScore("Player2", 90, 8);    // 90 seconds
        HighScore score3 = new HighScore("Player3", 3665, 8);  // 3665 seconds

        // Check that the toString output contains the correct time values
        assertTrue(score1.toString().contains("30 seconds"));
        assertTrue(score2.toString().contains("90 seconds"));
        assertTrue(score3.toString().contains("3665 seconds"));
    }
}
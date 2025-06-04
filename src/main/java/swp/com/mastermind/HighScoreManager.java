package swp.com.mastermind;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HighScoreManager {
    private static final Logger LOGGER = Logger.getLogger(HighScoreManager.class.getName());
    private static final String HIGH_SCORE_FILE = "mastermind_highscores.dat";
    private static final int MAX_HIGH_SCORES = 10;

    private List<HighScore> highScores;

    public HighScoreManager() {
        highScores = new ArrayList<>();
        loadHighScores();
    }

    @SuppressWarnings("unchecked")
    private void loadHighScores() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(HIGH_SCORE_FILE))) {
            highScores = (List<HighScore>) in.readObject();
            LOGGER.info("High scores loaded successfully");
        } catch (FileNotFoundException e) {
            LOGGER.info("No high scores file found. Creating a new one when scores are added.");
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, "Failed to load high scores", e);
        }
    }

    // Inside HighScoreManager.java - modify the saveHighScores method
    private void saveHighScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGH_SCORE_FILE))) {
            // Create a new ArrayList with all elements instead of using subList
            List<HighScore> scoresToSave = new ArrayList<>(highScores);
            oos.writeObject(scoresToSave);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to save high scores", e);
        }
    }

    public boolean isHighScore(int attempts, long timeInSeconds) {
        if (highScores.size() < MAX_HIGH_SCORES) {
            return true;
        }

        HighScore worstScore = highScores.get(highScores.size() - 1);
        HighScore newScore = new HighScore("", timeInSeconds, attempts);
        return newScore.compareTo(worstScore) < 0;
    }

    public void addHighScore(String playerName, long timeInSeconds, int attempts) {
        HighScore newScore = new HighScore(playerName, timeInSeconds, attempts);
        highScores.add(newScore);

        // Sort the high scores
        Collections.sort(highScores);

        // Keep only the top scores
        if (highScores.size() > MAX_HIGH_SCORES) {
            highScores = highScores.subList(0, MAX_HIGH_SCORES);
        }

        saveHighScores();
        LOGGER.info("Added high score: " + playerName + " - " + attempts + " attempts, " + timeInSeconds + " seconds");
    }

    public List<HighScore> getHighScores() {
        return new ArrayList<>(highScores);
    }
}
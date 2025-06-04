package swp.com.mastermind;

import java.io.Serializable;

public class HighScore implements Comparable<HighScore>, Serializable {
    private String playerName;
    private long timeInSeconds;
    private int attempts;

    public HighScore(String playerName, long timeInSeconds, int attempts) {
        this.playerName = playerName;
        this.timeInSeconds = timeInSeconds;
        this.attempts = attempts;
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getTimeInSeconds() {
        return timeInSeconds;
    }

    public int getAttempts() {
        return attempts;
    }

    @Override
    public int compareTo(HighScore other) {
        // First compare by attempts
        int attemptComparison = Integer.compare(this.attempts, other.attempts);
        if (attemptComparison != 0) {
            return attemptComparison;
        }
        // If same attempts, compare by time
        return Long.compare(this.timeInSeconds, other.timeInSeconds);
    }

    @Override
    public String toString() {
        return String.format("%-15s %3d attempts %3d seconds",
                playerName, attempts, timeInSeconds);
    }
}
package swp.com.mastermind;

public class GuessResult {
    private String[] guess;
    private int exactMatches;
    private int colorMatches;

    public GuessResult(String[] guess, int exactMatches, int colorMatches) {
        this.guess = guess;
        this.exactMatches = exactMatches;
        this.colorMatches = colorMatches;
    }

    public String[] getGuess() {
        return guess;
    }

    public int getExactMatches() {
        return exactMatches;
    }

    public int getColorMatches() {
        return colorMatches;
    }
}
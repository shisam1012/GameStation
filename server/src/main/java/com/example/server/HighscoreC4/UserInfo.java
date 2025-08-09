package com.example.server.HighscoreC4;

public class UserInfo {
    private String username;
    private int gamesCount;
    private int winsCount;
    private int totalScore;

    public UserInfo(String username, int gamesCount, int winsCount, int totalScore) {
        this.username = username;
        this.gamesCount = gamesCount;
        this.winsCount = winsCount;
        this.totalScore = totalScore;
    }

    // Getters
    public String getUsername() { return username; }
    public int getGamesCount() { return gamesCount; }
    public int getWinsCount() { return winsCount; }
    public int getTotalScore() { return totalScore; }
}

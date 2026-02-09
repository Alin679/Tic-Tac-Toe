package com.tictactoe.service.leaderboard;

import com.tictactoe.domain.model.PlayerScore;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LeaderboardService {
    private static final String LEADERBOARD_FILE = "leaderboard.txt";
    private final Map<String, PlayerScore> scores;
    private final Path filePath;
    
    public LeaderboardService() {
        this.scores = new HashMap<>();
        this.filePath = Paths.get(System.getProperty("user.home"), ".tictactoe", LEADERBOARD_FILE);
        loadScores();
    }
    
    public void recordGame(String playerName, GameResult result) {
        PlayerScore score = scores.computeIfAbsent(playerName, PlayerScore::new);
        
        switch (result) {
            case WIN -> score.addWin();
            case LOSS -> score.addLoss();
            case DRAW -> score.addDraw();
        }
        
        saveScores();
    }
    
    public List<PlayerScore> getLeaderboard() {
        List<PlayerScore> leaderboard = new ArrayList<>(scores.values());
        Collections.sort(leaderboard);
        return leaderboard;
    }
    
    public PlayerScore getPlayerScore(String playerName) {
        return scores.get(playerName);
    }
    
    public void clearLeaderboard() {
        scores.clear();
        saveScores();
    }
    
    private void loadScores() {
        try {
            if (!Files.exists(filePath)) {
                return;
            }
            
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    int wins = Integer.parseInt(parts[1]);
                    int losses = Integer.parseInt(parts[2]);
                    int draws = Integer.parseInt(parts[3]);
                    scores.put(name, new PlayerScore(name, wins, losses, draws));
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load leaderboard: " + e.getMessage());
        }
    }
    
    private void saveScores() {
        try {
            Files.createDirectories(filePath.getParent());
            
            List<String> lines = new ArrayList<>();
            for (PlayerScore score : scores.values()) {
                lines.add(String.format("%s,%d,%d,%d",
                    score.getPlayerName(),
                    score.getWins(),
                    score.getLosses(),
                    score.getDraws()));
            }
            
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.err.println("Failed to save leaderboard: " + e.getMessage());
        }
    }
    
    public enum GameResult {
        WIN, LOSS, DRAW
    }
}

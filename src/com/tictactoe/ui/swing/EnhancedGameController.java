package com.tictactoe.ui.swing;

import com.tictactoe.controller.GameController;
import com.tictactoe.domain.enums.GameState;
import com.tictactoe.domain.model.Move;
import com.tictactoe.domain.model.Position;
import com.tictactoe.domain.service.GameEngine;
import com.tictactoe.service.audio.AudioManager;
import com.tictactoe.service.i18n.LanguageManager;
import com.tictactoe.service.leaderboard.LeaderboardService;
import com.tictactoe.ui.GameUI;
import javax.swing.*;

public class EnhancedGameController extends GameController {
    private final LeaderboardService leaderboardService;
    private final JFrame mainMenu;
    private final AudioManager audioManager;
    private final LanguageManager langManager;
    
    public EnhancedGameController(GameEngine gameEngine, 
                                  LeaderboardService leaderboardService,
                                  JFrame mainMenu) {
        super(gameEngine, new SwingGameUI(gameEngine.getBoard().getSize()));
        this.leaderboardService = leaderboardService;
        this.mainMenu = mainMenu;
        this.audioManager = AudioManager.getInstance();
        this.langManager = LanguageManager.getInstance();
    }
    
    @Override
    public void startGame() {
        super.startGame();
        
        if (gameEngine.getCurrentPlayer().isAI()) {
            SwingUtilities.invokeLater(() -> makeAIMove());
        }
    }
    
    @Override
    public void onCellClicked(int row, int col) {
        if (gameEngine.getGameState().isGameOver()) {
            return;
        }
        
        if (gameEngine.getCurrentPlayer().isAI()) {
            return;
        }
        
        Position position = new Position(row, col);
        Move move = new Move(position, gameEngine.getCurrentTurn());
        
        boolean success = gameEngine.executeMove(move);
        
        if (success) {
            updateUI();
            checkGameOver();
            
            if (!gameEngine.getGameState().isGameOver() && 
                gameEngine.getCurrentPlayer().isAI()) {
                
                Timer timer = new Timer(500, e -> makeAIMove());
                timer.setRepeats(false);
                timer.start();
            }
        }
    }
    
    private void makeAIMove() {
        if (gameEngine.getGameState().isGameOver()) {
            return;
        }
        
        try {
            Move aiMove = gameEngine.getCurrentPlayer()
                .getAIStrategy()
                .calculateMove(gameEngine.getBoard(), gameEngine.getCurrentTurn());
            
            boolean success = gameEngine.executeMove(aiMove);
            
            if (success) {
                try {
                } catch (Exception e) {
                }
                
                updateUI();
                checkGameOver();
                
                if (!gameEngine.getGameState().isGameOver() && 
                    gameEngine.getCurrentPlayer().isAI()) {
                    
                    Timer timer = new Timer(500, e -> makeAIMove());
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        } catch (Exception e) {
            System.err.println("AI move failed: " + e.getMessage());
        }
    }
    
    private void checkGameOver() {
        GameState state = gameEngine.getGameState();
        
        if (state.isGameOver()) {
            try {
            } catch (Exception e) {
            }
            
            recordGameResult(state);
            showGameOverDialog(state);
        }
    }
    
    private void recordGameResult(GameState state) {
        String playerXName = gameEngine.getPlayerX().getName();
        String playerOName = gameEngine.getPlayerO().getName();
        
        if (gameEngine.getPlayerX().isAI() && gameEngine.getPlayerO().isAI()) {
            return;
        }
        
        switch (state) {
            case X_WON:
                if (!gameEngine.getPlayerX().isAI()) {
                    leaderboardService.recordGame(playerXName, LeaderboardService.GameResult.WIN);
                }
                if (!gameEngine.getPlayerO().isAI()) {
                    leaderboardService.recordGame(playerOName, LeaderboardService.GameResult.LOSS);
                }
                break;
            case O_WON:
                if (!gameEngine.getPlayerO().isAI()) {
                    leaderboardService.recordGame(playerOName, LeaderboardService.GameResult.WIN);
                }
                if (!gameEngine.getPlayerX().isAI()) {
                    leaderboardService.recordGame(playerXName, LeaderboardService.GameResult.LOSS);
                }
                break;
            case DRAW:
                if (!gameEngine.getPlayerX().isAI()) {
                    leaderboardService.recordGame(playerXName, LeaderboardService.GameResult.DRAW);
                }
                if (!gameEngine.getPlayerO().isAI()) {
                    leaderboardService.recordGame(playerOName, LeaderboardService.GameResult.DRAW);
                }
                break;
        }
    }
    
    private void showGameOverDialog(GameState state) {
        String message = buildGameOverMessage(state);
        
        int choice = JOptionPane.showOptionDialog(
            null,
            message,
            langManager.get("app.title"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new Object[]{
                langManager.get("game.newGame"), 
                langManager.get("game.backToMenu")
            },
            langManager.get("game.newGame")
        );
        
        if (choice == 0) {
            if (gameUI instanceof SwingGameUI) {
                ((SwingGameUI) gameUI).close();
            }
            mainMenu.setVisible(false);
            GameModeSelectionUI modeSelection = new GameModeSelectionUI(mainMenu, leaderboardService);
            modeSelection.setVisible(true);
        } else {
            if (gameUI instanceof SwingGameUI) {
                ((SwingGameUI) gameUI).close();
            }
            mainMenu.setVisible(true);
        }
    }
    
    private void updateUI() {
        gameUI.updateBoard(gameEngine.getBoard().getBoardStateCopy());
        gameUI.updateStatus(buildStatusMessage());
    }
    
    private String buildStatusMessage() {
        GameState state = gameEngine.getGameState();
        
        if (state == GameState.IN_PROGRESS) {
            return langManager.get("game.playerTurn")
                .replace("{0}", gameEngine.getCurrentPlayer().toString());
        } else {
            return buildGameOverMessage(state);
        }
    }
    
    private String buildGameOverMessage(GameState state) {
        return switch (state) {
            case X_WON, O_WON -> langManager.get("game.winner")
                .replace("{0}", gameEngine.getWinner().toString());
            case DRAW -> langManager.get("game.draw");
            case IN_PROGRESS -> langManager.get("game.playerTurn")
                .replace("{0}", gameEngine.getCurrentPlayer().toString());
        };
    }
}

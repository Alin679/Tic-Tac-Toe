package com.tictactoe.ui.swing;

import com.tictactoe.service.audio.AudioManager;
import com.tictactoe.service.i18n.LanguageManager;
import com.tictactoe.service.leaderboard.LeaderboardService;
import javax.swing.*;
import java.awt.*;

public class MainMenuUI extends JFrame {
    private final LanguageManager langManager;
    private final AudioManager audioManager;
    private final LeaderboardService leaderboardService;
    
    private JLabel titleLabel;
    private JButton newGameButton;
    private JButton settingsButton;
    private JButton leaderboardButton;
    private JButton exitButton;
    
    public MainMenuUI() {
        this.langManager = LanguageManager.getInstance();
        this.audioManager = AudioManager.getInstance();
        this.leaderboardService = new LeaderboardService();
        
        initializeUI();
        langManager.addListener(lang -> updateTexts());
    }
    
    private void initializeUI() {
        setTitle(langManager.get("app.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(true);
        
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                    0, 0, ColorScheme.PRIMARY,
                    0, getHeight(), ColorScheme.PRIMARY_DARK
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 50, 60, 50));
        
        titleLabel = new JLabel("⭕ " + langManager.get("app.title") + " ❌");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(50));
        
        newGameButton = createMenuButton(langManager.get("menu.newGame"));
        settingsButton = createMenuButton(langManager.get("menu.settings"));
        leaderboardButton = createMenuButton(langManager.get("menu.leaderboard"));
        exitButton = createMenuButton(langManager.get("menu.exit"));
        
        newGameButton.addActionListener(e -> onNewGame());
        settingsButton.addActionListener(e -> onSettings());
        leaderboardButton.addActionListener(e -> onLeaderboard());
        exitButton.addActionListener(e -> onExit());
        
        mainPanel.add(newGameButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(settingsButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(leaderboardButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(exitButton);
        
        add(mainPanel);
    }
    
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(ColorScheme.getShadowColor());
                g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 20, 20);
                
                Color bgColor = getModel().isRollover() ? 
                    new Color(255, 255, 255, 240) : 
                    new Color(255, 255, 255, 220);
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 20, 20);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setMaximumSize(new Dimension(300, 55));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setForeground(ColorScheme.PRIMARY);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void updateTexts() {
        setTitle(langManager.get("app.title"));
        titleLabel.setText("⭕ " + langManager.get("app.title") + " ❌");
        newGameButton.setText(langManager.get("menu.newGame"));
        settingsButton.setText(langManager.get("menu.settings"));
        leaderboardButton.setText(langManager.get("menu.leaderboard"));
        exitButton.setText(langManager.get("menu.exit"));
    }
    
    private void onNewGame() {
        GameModeSelectionUI modeSelection = new GameModeSelectionUI(this, leaderboardService);
        modeSelection.setVisible(true);
        setVisible(false);
    }
    
    private void onSettings() {
        SettingsUI settingsUI = new SettingsUI(this);
        settingsUI.setVisible(true);
        setVisible(false);
    }
    
    private void onLeaderboard() {
        LeaderboardUI leaderboardUI = new LeaderboardUI(this, leaderboardService);
        leaderboardUI.setVisible(true);
        setVisible(false);
    }
    
    private void onExit() {
        audioManager.dispose();
        System.exit(0);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuUI menu = new MainMenuUI();
            menu.setVisible(true);
        });
    }
}

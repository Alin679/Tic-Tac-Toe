package com.tictactoe.ui.swing;

import com.tictactoe.domain.enums.CellState;
import com.tictactoe.domain.enums.PlayerType;
import com.tictactoe.domain.model.Board;
import com.tictactoe.domain.model.Player;
import com.tictactoe.domain.service.GameEngine;
import com.tictactoe.domain.service.WinConditionChecker;
import com.tictactoe.service.i18n.LanguageManager;
import com.tictactoe.service.leaderboard.LeaderboardService;
import javax.swing.*;
import java.awt.*;

public class MultiplayerSetupUI extends JFrame {
    private final LanguageManager langManager;
    private final JFrame parentFrame;
    private final LeaderboardService leaderboardService;
    
    private JLabel titleLabel;
    private JLabel playerXLabel;
    private JLabel playerOLabel;
    private JTextField playerXNameField;
    private JTextField playerONameField;
    private JComboBox<PlayerType> playerXTypeCombo;
    private JComboBox<PlayerType> playerOTypeCombo;
    private JButton startButton;
    private JButton backButton;
    
    public MultiplayerSetupUI(JFrame parent, LeaderboardService leaderboardService) {
        this.parentFrame = parent;
        this.leaderboardService = leaderboardService;
        this.langManager = LanguageManager.getInstance();
        
        initializeUI();
        langManager.addListener(lang -> updateTexts());
    }
    
    private void initializeUI() {
        setTitle("Multiplayer Setup");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
                    0, 0, ColorScheme.PRIMARY_LIGHT,
                    0, getHeight(), ColorScheme.PRIMARY
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        titleLabel = new JLabel("👥 " + langManager.get("setup.title"));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        JPanel setupCard = new JPanel();
        setupCard.setLayout(new BoxLayout(setupCard, BoxLayout.Y_AXIS));
        setupCard.setBackground(ColorScheme.SURFACE);
        setupCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        setupCard.setMaximumSize(new Dimension(400, 350));
        setupCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        playerXLabel = new JLabel("❌ " + langManager.get("setup.playerX"));
        playerXLabel.setFont(new Font("Arial", Font.BOLD, 16));
        playerXLabel.setForeground(ColorScheme.X_COLOR);
        playerXLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        setupCard.add(playerXLabel);
        setupCard.add(Box.createVerticalStrut(10));
        
        playerXNameField = new JTextField("Player X");
        playerXNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        playerXNameField.setMaximumSize(new Dimension(350, 35));
        playerXNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.BORDER, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        setupCard.add(playerXNameField);
        setupCard.add(Box.createVerticalStrut(25));
        
        playerOLabel = new JLabel("⭕ " + langManager.get("setup.playerO"));
        playerOLabel.setFont(new Font("Arial", Font.BOLD, 16));
        playerOLabel.setForeground(ColorScheme.O_COLOR);
        playerOLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        setupCard.add(playerOLabel);
        setupCard.add(Box.createVerticalStrut(10));
        
        playerONameField = new JTextField("Player O");
        playerONameField.setFont(new Font("Arial", Font.PLAIN, 14));
        playerONameField.setMaximumSize(new Dimension(350, 35));
        playerONameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.BORDER, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        setupCard.add(playerONameField);
        
        mainPanel.add(setupCard);
        mainPanel.add(Box.createVerticalStrut(30));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        final boolean[] backHovered = {false};
        backButton = new JButton("← " + langManager.get("settings.back")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(backHovered[0] ? new Color(255, 255, 255, 50) : new Color(255, 255, 255, 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(140, 45));
        backButton.addActionListener(e -> goBack());
        
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backHovered[0] = true;
                backButton.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backHovered[0] = false;
                backButton.repaint();
            }
        });
        
        final boolean[] startHovered = {false};
        startButton = new JButton(langManager.get("setup.start") + " →") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(startHovered[0] ? ColorScheme.SUCCESS.brighter() : ColorScheme.SUCCESS);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setForeground(Color.WHITE);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.setPreferredSize(new Dimension(140, 45));
        startButton.addActionListener(e -> startGame());
        
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startHovered[0] = true;
                startButton.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startHovered[0] = false;
                startButton.repaint();
            }
        });
        
        buttonPanel.add(backButton);
        buttonPanel.add(startButton);
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
    }
    
    private void updateTexts() {
        setTitle(langManager.get("setup.title"));
        titleLabel.setText("👥 " + langManager.get("setup.title"));
        playerXLabel.setText("❌ " + langManager.get("setup.playerX"));
        playerOLabel.setText("⭕ " + langManager.get("setup.playerO"));
        startButton.setText(langManager.get("setup.start") + " →");
        backButton.setText("← " + langManager.get("settings.back"));
    }
    
    private void startGame() {
        String nameX = playerXNameField.getText().trim();
        String nameO = playerONameField.getText().trim();
        
        if (nameX.isEmpty() || nameO.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter names for both players",
                "Invalid Input", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Player playerX = new Player(nameX, CellState.X, PlayerType.HUMAN);
        Player playerO = new Player(nameO, CellState.O, PlayerType.HUMAN);
        
        Board board = new Board(3);
        WinConditionChecker winChecker = new WinConditionChecker();
        GameEngine gameEngine = new GameEngine(board, playerX, playerO, winChecker);
        
        EnhancedGameController controller = new EnhancedGameController(
            gameEngine, 
            leaderboardService,
            parentFrame
        );
        
        controller.startGame();
        dispose();
    }
    
    private void goBack() {
        GameModeSelectionUI modeSelection = new GameModeSelectionUI(parentFrame, leaderboardService);
        modeSelection.setVisible(true);
        dispose();
    }
}

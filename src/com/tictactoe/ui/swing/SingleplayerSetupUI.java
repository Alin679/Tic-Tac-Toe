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

public class SingleplayerSetupUI extends JFrame {
    private final LanguageManager langManager;
    private final JFrame mainMenu;
    private final LeaderboardService leaderboardService;
    
    private JTextField playerNameField;
    private ButtonGroup difficultyGroup;
    private JRadioButton easyRadio;
    private JRadioButton mediumRadio;
    private JRadioButton hardRadio;
    
    public SingleplayerSetupUI(JFrame mainMenu, LeaderboardService leaderboardService) {
        this.mainMenu = mainMenu;
        this.leaderboardService = leaderboardService;
        this.langManager = LanguageManager.getInstance();
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Singleplayer Setup");
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
        
        JLabel titleLabel = new JLabel("🎮 Singleplayer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(40));
        
        JPanel nameCard = createCard();
        JLabel nameLabel = new JLabel("Numele tău:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        
        playerNameField = new JTextField();
        playerNameField.setFont(new Font("Arial", Font.PLAIN, 16));
        playerNameField.setPreferredSize(new Dimension(350, 40));
        playerNameField.setMaximumSize(new Dimension(350, 40));
        playerNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.BORDER, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        nameCard.add(nameLabel);
        nameCard.add(Box.createVerticalStrut(10));
        nameCard.add(playerNameField);
        mainPanel.add(nameCard);
        mainPanel.add(Box.createVerticalStrut(25));
        
        JPanel difficultyCard = createCard();
        JLabel diffLabel = new JLabel("Alege dificultatea:");
        diffLabel.setFont(new Font("Arial", Font.BOLD, 16));
        diffLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        difficultyCard.add(diffLabel);
        difficultyCard.add(Box.createVerticalStrut(15));
        
        difficultyGroup = new ButtonGroup();
        
        JPanel easyPanel = createDifficultyOption("😊 Ușor", "Perfect pentru începători", ColorScheme.SUCCESS);
        easyRadio = (JRadioButton) easyPanel.getComponent(0);
        
        JPanel mediumPanel = createDifficultyOption("🤔 Mediu", "O provocare echilibrată", ColorScheme.WARNING);
        mediumRadio = (JRadioButton) mediumPanel.getComponent(0);
        
        JPanel hardPanel = createDifficultyOption("😈 Greu", "Imposibil de învins!", ColorScheme.DANGER);
        hardRadio = (JRadioButton) hardPanel.getComponent(0);
        
        difficultyGroup.add(easyRadio);
        difficultyGroup.add(mediumRadio);
        difficultyGroup.add(hardRadio);
        
        easyRadio.setSelected(true);
        
        difficultyCard.add(easyPanel);
        difficultyCard.add(Box.createVerticalStrut(10));
        difficultyCard.add(mediumPanel);
        difficultyCard.add(Box.createVerticalStrut(10));
        difficultyCard.add(hardPanel);
        
        mainPanel.add(difficultyCard);
        mainPanel.add(Box.createVerticalStrut(30));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        JButton backBtn = createButton("← Înapoi", ColorScheme.TEXT_SECONDARY, false);
        backBtn.addActionListener(e -> goBack());
        
        JButton startBtn = createButton("Start Joc →", ColorScheme.ACCENT, true);
        startBtn.addActionListener(e -> startGame());
        
        buttonPanel.add(backBtn);
        buttonPanel.add(startBtn);
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
    }
    
    private JPanel createCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(ColorScheme.CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ColorScheme.BORDER, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(400, 500));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        return card;
    }
    
    private JPanel createDifficultyOption(String title, String description, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(ColorScheme.CARD_BG);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(350, 50));
        
        JRadioButton radio = new JRadioButton();
        radio.setBackground(ColorScheme.CARD_BG);
        radio.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(ColorScheme.CARD_BG);
        textPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(color);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        descLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        
        textPanel.add(titleLabel);
        textPanel.add(descLabel);
        
        panel.add(radio);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(textPanel);
        
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radio.setSelected(true);
            }
        });
        
        return panel;
    }
    
    private JButton createButton(String text, Color bgColor, boolean isPrimary) {
        final boolean[] isHovered = {false};
        
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color currentBg;
                if (isHovered[0]) {
                    currentBg = isPrimary ? bgColor.brighter() : ColorScheme.BACKGROUND;
                } else {
                    currentBg = isPrimary ? bgColor : ColorScheme.SURFACE;
                }
                
                g2d.setColor(currentBg);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                if (!isPrimary) {
                    g2d.setColor(ColorScheme.BORDER);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
                }
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(isPrimary ? Color.WHITE : bgColor);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 45));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                isHovered[0] = true;
                button.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                isHovered[0] = false;
                button.repaint();
            }
        });
        
        return button;
    }
    
    private void startGame() {
        String playerName = playerNameField.getText().trim();
        
        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Te rog introdu numele tău!",
                "Nume lipsă",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        PlayerType aiDifficulty;
        if (easyRadio.isSelected()) {
            aiDifficulty = PlayerType.AI_EASY;
        } else if (mediumRadio.isSelected()) {
            aiDifficulty = PlayerType.AI_MEDIUM;
        } else {
            aiDifficulty = PlayerType.AI_HARD;
        }
        
        Player playerX = new Player(playerName, CellState.X, PlayerType.HUMAN);
        Player playerO = new Player("AI", CellState.O, aiDifficulty);
        
        Board board = new Board(3);
        WinConditionChecker winChecker = new WinConditionChecker();
        GameEngine gameEngine = new GameEngine(board, playerX, playerO, winChecker);
        
        EnhancedGameController controller = new EnhancedGameController(
            gameEngine,
            leaderboardService,
            mainMenu
        );
        
        controller.startGame();
        dispose();
    }
    
    private void goBack() {
        GameModeSelectionUI modeSelection = new GameModeSelectionUI(mainMenu, leaderboardService);
        modeSelection.setVisible(true);
        dispose();
    }
}

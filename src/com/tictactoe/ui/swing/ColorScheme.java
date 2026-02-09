package com.tictactoe.ui.swing;

import java.awt.*;

public class ColorScheme {
    public static final Color PRIMARY = new Color(99, 102, 241);
    public static final Color PRIMARY_DARK = new Color(79, 70, 229);
    public static final Color PRIMARY_LIGHT = new Color(129, 140, 248);
    
    public static final Color ACCENT = new Color(236, 72, 153);
    public static final Color ACCENT_LIGHT = new Color(244, 114, 182);
    
    public static final Color SUCCESS = new Color(34, 197, 94);
    public static final Color INFO = new Color(59, 130, 246);
    public static final Color WARNING = new Color(251, 146, 60);
    public static final Color DANGER = new Color(239, 68, 68);
    
    public static final Color BACKGROUND = new Color(248, 250, 252);
    public static final Color SURFACE = new Color(255, 255, 255);
    public static final Color CARD_BG = new Color(255, 255, 255);
    
    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139);
    public static final Color TEXT_ON_PRIMARY = Color.WHITE;
    
    public static final Color BORDER = new Color(226, 232, 240);
    public static final Color DIVIDER = new Color(241, 245, 249);
    
    public static final Color X_COLOR = new Color(239, 68, 68);
    public static final Color O_COLOR = new Color(59, 130, 246);
    

    public static GradientPaint createGradientBackground(int height) {
        return new GradientPaint(
            0, 0, new Color(99, 102, 241),
            0, height, new Color(139, 92, 246)
        );
    }
    
    public static Color getShadowColor() {
        return new Color(0, 0, 0, 20);
    }
}

package com.towerdefense.ui.hud;

import java.awt.*;

/**
 * Reusable health bar component for displaying entity health status.
 * Can be used for tanks, houses, enemies, or any entity with health.
 */
public class HealthBar {
    
    private static final Color BACKGROUND_COLOR = Color.RED;
    private static final Color HEALTH_COLOR = Color.GREEN;
    private static final Color BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_HEIGHT = 4;
    
    private final int width;
    private final int height;
    
    // Constructor for health bar with custom dimensions
    // @param width - width of the health bar in pixels
    // @param height - height of the health bar in pixels
    public HealthBar(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    // Constructor for health bar with default height
    // @param width - width of the health bar in pixels
    public HealthBar(int width) {
        this(width, DEFAULT_HEIGHT);
    }
    
    // Renders the health bar at the specified position
    // @param graphics - Graphics context for drawing
    // @param x - x position to draw the health bar
    // @param y - y position to draw the health bar
    // @param currentHealth - current health value
    // @param maxHealth - maximum health value
    public void render(Graphics graphics, int x, int y, int currentHealth, int maxHealth) {
        if (currentHealth >= maxHealth) {
            return; // Don't draw health bar if at full health
        }
        
        // Calculate health percentage
        double healthPercentage = (double) currentHealth / maxHealth;
        int healthWidth = (int) (width * healthPercentage);
        
        // Draw background (red)
        graphics.setColor(BACKGROUND_COLOR);
        graphics.fillRect(x, y, width, height);
        
        // Draw health (green)
        graphics.setColor(HEALTH_COLOR);
        graphics.fillRect(x, y, healthWidth, height);
        
        // Draw border
        graphics.setColor(BORDER_COLOR);
        graphics.drawRect(x, y, width, height);
    }
    
    // Renders the health bar with custom colors
    // @param graphics - Graphics context for drawing
    // @param x - x position to draw the health bar
    // @param y - y position to draw the health bar
    // @param currentHealth - current health value
    // @param maxHealth - maximum health value
    // @param backgroundColor - color for the background (low health)
    // @param healthColor - color for the health portion
    public void render(Graphics graphics, int x, int y, int currentHealth, int maxHealth, 
                      Color backgroundColor, Color healthColor) {
        if (currentHealth >= maxHealth) {
            return;
        }
        
        double healthPercentage = (double) currentHealth / maxHealth;
        int healthWidth = (int) (width * healthPercentage);
        
        // Draw background
        graphics.setColor(backgroundColor);
        graphics.fillRect(x, y, width, height);
        
        // Draw health
        graphics.setColor(healthColor);
        graphics.fillRect(x, y, healthWidth, height);
        
        // Draw border
        graphics.setColor(BORDER_COLOR);
        graphics.drawRect(x, y, width, height);
    }
}
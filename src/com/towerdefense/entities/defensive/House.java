package com.towerdefense.entities.defensive;

import com.towerdefense.entities.base.GameObject;
import com.towerdefense.utils.Constants;
import java.awt.*;

/**
 * Represents the house that needs to be defended in the tower defense game.
 * The house can take damage from enemies and has a health system.
 */
public class House extends GameObject {
    
    private static final int MINIMUM_HEALTH = 0;
    
    private int currentHealth = Constants.Entities.HOUSE_INITIAL_HEALTH;
    
    // Constructor that creates a house at the specified grid position
    // Houses are larger objects occupying 3x4 tiles
    // @param gridColumn - column position in the tile grid
    // @param gridRow - row position in the tile grid
    // @param tileSize - size of each tile in pixels
    // @param houseImage - visual representation of the house
    public House(int gridColumn, int gridRow, int tileSize, Image houseImage) {
        super(gridColumn, gridRow, tileSize, Constants.Entities.HOUSE_WIDTH_TILES, 
              Constants.Entities.HOUSE_HEIGHT_TILES, houseImage);
    }
    
    // Applies damage to the house and reduces its health
    // Ensures health never goes below minimum and logs current health
    // @param damageAmount - amount of damage to apply
    public void damage(int damageAmount) {
        currentHealth -= damageAmount;
        if (currentHealth < MINIMUM_HEALTH) {
            currentHealth = MINIMUM_HEALTH;
        }
        System.out.println("House HP: " + currentHealth);
    }
    
    // Gets the collision bounds for this house
    // Used for collision detection with enemies
    // @return Rectangle representing the house's bounds
    public Rectangle getBounds() {
        return new Rectangle(positionX, positionY, objectWidth, objectHeight);
    }
    
    // Gets the current health of the house
    // Used for UI display and game over conditions
    // @return current health value
    public int getHealth() {
        return currentHealth;
    }

    // Renders the house with health bar
    // Overrides parent draw to add health bar display
    // @param graphics - Graphics context for drawing
    @Override
    public void draw(Graphics graphics) {
        // Draw the house image
        super.draw(graphics);
        
        // Draw health bar
        drawHealthBar(graphics);
    }

    // Draws a health bar above the house to show current health status
    // @param graphics - Graphics context for drawing
    private void drawHealthBar(Graphics graphics) {
        if (currentHealth < Constants.Entities.HOUSE_INITIAL_HEALTH) {
            int barWidth = objectWidth;
            int barHeight = 8;
            int barX = positionX;
            int barY = positionY - 15;

            // Background (red)
            graphics.setColor(Color.RED);
            graphics.fillRect(barX, barY, barWidth, barHeight);

            // Health (green)
            graphics.setColor(Color.GREEN);
            double healthPercentage = (double) currentHealth / Constants.Entities.HOUSE_INITIAL_HEALTH;
            int healthWidth = (int) (barWidth * healthPercentage);
            graphics.fillRect(barX, barY, healthWidth, barHeight);

            // Border
            graphics.setColor(Color.BLACK);
            graphics.drawRect(barX, barY, barWidth, barHeight);

            // Health text
            graphics.setColor(Color.WHITE);
            String healthText = currentHealth + "/" + Constants.Entities.HOUSE_INITIAL_HEALTH;
            graphics.drawString(healthText, barX + 5, barY + barHeight - 1);
        }
    }
}
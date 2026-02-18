package com.towerdefense.entities.defensive;

import com.towerdefense.entities.base.GameObject;
import com.towerdefense.entities.base.Defensive;
import com.towerdefense.utils.Constants;
import java.awt.*;

/**
 * Represents the house that needs to be defended in the tower defense game.
 * The house can take damage from enemies and has a health system.
 */
public class House extends GameObject implements Defensive {
    
    private int currentHealth = Constants.Entities.HOUSE_INITIAL_HEALTH;
    
    // Constructor that creates a house at the specified grid position
    // Houses are larger objects occupying 7x7 tiles
    // @param gridColumn - column position in the tile grid
    // @param gridRow - row position in the tile grid
    // @param tileSize - size of each tile in pixels
    // @param houseImage - visual representation of the house
    public House(int gridColumn, int gridRow, int tileSize, Image houseImage) {
        super(gridColumn, gridRow, tileSize, Constants.Entities.HOUSE_WIDTH_TILES, 
              Constants.Entities.HOUSE_HEIGHT_TILES, houseImage);
    }
    
    // Implementation of Defensive interface
    @Override
    public void takeDamage(int damageAmount) {
        currentHealth -= damageAmount;
        if (currentHealth < Constants.Entities.MINIMUM_HEALTH) {
            currentHealth = Constants.Entities.MINIMUM_HEALTH;
        }
    }
    
    @Override
    public boolean isDestroyed() {
        return currentHealth <= Constants.Entities.MINIMUM_HEALTH;
    }
    
    @Override
    public int getCurrentHealth() {
        return currentHealth;
    }
    
    @Override
    public int getMaxHealth() {
        return Constants.Entities.HOUSE_INITIAL_HEALTH;
    }
    
    // Gets the collision bounds for this house
    // Used for collision detection with enemies
    // @return Rectangle representing the house's bounds
    public Rectangle getBounds() {
        return new Rectangle(positionX, positionY, objectWidth, objectHeight);
    }
    
    // Legacy method for compatibility
    // @return current health value
    public int getHealth() {
        return getCurrentHealth();
    }

    // Renders the house
    // @param graphics - Graphics context for drawing
    @Override
    public void draw(Graphics graphics) {
        // Draw the house image only (no health bar for house)
        super.draw(graphics);
    }
}
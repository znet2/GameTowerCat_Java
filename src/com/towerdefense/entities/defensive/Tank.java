package com.towerdefense.entities.defensive;

import com.towerdefense.entities.base.GameObject;
import com.towerdefense.entities.base.Defensive;
import com.towerdefense.entities.base.Collidable;
import com.towerdefense.utils.Constants;
import java.awt.*;

/**
 * Represents a pure defensive tank unit that can be placed on the map.
 * Tanks serve as obstacles that block enemy movement and absorb damage.
 * They do not attack or deal damage to enemies - they are purely defensive.
 */
public class Tank extends GameObject implements Defensive, Collidable {

    // Visual positioning
    private static final int Y_OFFSET = -30;
    private static final int MINIMUM_HEALTH = 0;

    // Current state
    private int currentHealth = Constants.Entities.TANK_INITIAL_HEALTH;
    private boolean hasBeenAttacked = false;

    // Images
    private Image defendImage;

    // Constructor that creates a defensive tank at the specified grid position
    // Tanks occupy a 2x2 tile area and serve as defensive obstacles
    // @param gridColumn - column position in the tile grid
    // @param gridRow - row position in the tile grid
    // @param tileSize - size of each tile in pixels
    // @param tankImage - visual representation of the tank
    public Tank(int gridColumn, int gridRow, int tileSize, Image tankImage) {
        super(gridColumn, gridRow, tileSize, 2, 2, tankImage);

        // Load defend image
        try {
            this.defendImage = javax.imageio.ImageIO.read(new java.io.File(Constants.Paths.TANK_DEFEND_IMAGE));
        } catch (java.io.IOException e) {
            // If defend image not found, use normal image
            this.defendImage = tankImage;
        }
    }

    // Updates the tank each frame
    // Defensive tanks have no active behavior - they simply exist as obstacles
    // This method is kept for interface compatibility but performs no actions
    public void update() {
        // Defensive tanks do not perform any active updates
        // They simply exist as static defensive obstacles
    }

    // Implementation of Defensive interface
    @Override
    public void takeDamage(int damageAmount) {
        // Change to defend image when attacked for the first time
        if (!hasBeenAttacked) {
            hasBeenAttacked = true;
            objectImage = defendImage;
        }

        // Apply defense rating to reduce incoming damage
        int actualDamage = Math.max(1, damageAmount - Constants.Entities.TANK_DEFENSE_RATING);

        currentHealth -= actualDamage;
        if (currentHealth < MINIMUM_HEALTH) {
            currentHealth = MINIMUM_HEALTH;
        }

        System.out.println("Tank HP: " + currentHealth + " (absorbed " +
                (damageAmount - actualDamage) + " damage)");
    }

    @Override
    public boolean isDestroyed() {
        return currentHealth <= MINIMUM_HEALTH;
    }

    @Override
    public int getCurrentHealth() {
        return currentHealth;
    }

    @Override
    public int getMaxHealth() {
        return Constants.Entities.TANK_INITIAL_HEALTH;
    }

    @Override
    public int getDefenseRating() {
        return Constants.Entities.TANK_DEFENSE_RATING;
    }

    // Implementation of Collidable interface
    @Override
    public Rectangle getCollisionBounds() {
        return new Rectangle(positionX, positionY + Y_OFFSET, objectWidth, objectHeight);
    }

    // Legacy method for compatibility with existing enemy damage system
    // Delegates to the Defensive interface method
    // @param damageAmount - amount of damage to apply
    public void damage(int damageAmount) {
        takeDamage(damageAmount);
    }

    // Legacy method for compatibility with existing game systems
    // Delegates to the Defensive interface method
    // @return true if tank is dead, false otherwise
    public boolean isDead() {
        return isDestroyed();
    }

    // Legacy method for compatibility with existing collision system
    // Delegates to the Collidable interface method
    // @return Rectangle representing the tank's bounds
    public Rectangle getBounds() {
        return getCollisionBounds();
    }

    // Converts the tank's pixel X position to grid column
    // Used for grid-based positioning and placement validation
    // @param tileSize - size of each tile in pixels
    // @return grid column position
    public int getGridColumn(int tileSize) {
        return positionX / tileSize;
    }

    // Converts the tank's pixel Y position to grid row
    // Used for grid-based positioning and placement validation
    // @param tileSize - size of each tile in pixels
    // @return grid row position
    public int getGridRow(int tileSize) {
        return positionY / tileSize;
    }

    // Gets the health percentage for UI display
    // @return health as a percentage (0.0 to 1.0)
    public double getHealthPercentage() {
        return (double) currentHealth / Constants.Entities.TANK_INITIAL_HEALTH;
    }

    // Renders the tank to the screen
    // Draws the tank image at its position with visual offset
    // @param graphics - Graphics context for drawing
    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(
                objectImage,
                positionX,
                positionY + Y_OFFSET,
                objectWidth,
                objectHeight,
                null);

        // Optional: Draw health bar above tank
        drawHealthBar(graphics);
    }

    // Draws a health bar above the tank to show current health status
    // Provides visual feedback about the tank's defensive state
    // @param graphics - Graphics context for drawing
    private void drawHealthBar(Graphics graphics) {
        if (currentHealth < Constants.Entities.TANK_INITIAL_HEALTH) {
            int barWidth = objectWidth;
            int barHeight = 4;
            int barX = positionX;
            int barY = positionY + Y_OFFSET - 8;

            // Background (red)
            graphics.setColor(Color.RED);
            graphics.fillRect(barX, barY, barWidth, barHeight);

            // Health (green)
            graphics.setColor(Color.GREEN);
            int healthWidth = (int) (barWidth * getHealthPercentage());
            graphics.fillRect(barX, barY, healthWidth, barHeight);

            // Border
            graphics.setColor(Color.BLACK);
            graphics.drawRect(barX, barY, barWidth, barHeight);
        }
    }
}
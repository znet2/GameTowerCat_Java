import java.awt.*;

/**
 * Represents a defensive tank unit that can be placed on the map.
 * Tanks can take damage from enemies and be destroyed.
 */
public class Tank extends GameObject {

    private static final int INITIAL_HEALTH = 200;
    private static final int MINIMUM_HEALTH = 0;

    private int currentHealth = INITIAL_HEALTH;

    private static final int Y_OFFSET = -30;

    // Constructor that creates a tank at the specified grid position
    // Tanks occupy a single tile (1x1) and use the provided image
    // @param gridColumn - column position in the tile grid
    // @param gridRow - row position in the tile grid
    // @param tileSize - size of each tile in pixels
    // @param tankImage - visual representation of the tank
    public Tank(int gridColumn, int gridRow, int tileSize, Image tankImage) {
        super(gridColumn, gridRow, tileSize, 2, 2, tankImage);
    }

    // Applies damage to the tank and reduces its health
    // Ensures health never goes below minimum and logs current health
    // @param damageAmount - amount of damage to apply
    public void damage(int damageAmount) {
        currentHealth -= damageAmount;
        if (currentHealth < MINIMUM_HEALTH) {
            currentHealth = MINIMUM_HEALTH;
        }
        System.out.println("Tank HP: " + currentHealth);
    }

    // Checks if the tank has been destroyed
    // Returns true when health reaches minimum value
    // @return true if tank is dead, false otherwise
    public boolean isDead() {
        return currentHealth <= MINIMUM_HEALTH;
    }

    // Gets the collision bounds for this tank
    // Used for collision detection with enemies and other objects
    // @return Rectangle representing the tank's bounds
    public Rectangle getBounds() {
        return new Rectangle(positionX, positionY + Y_OFFSET, objectWidth, objectHeight);
    }

    // Converts the tank's pixel X position to grid column
    // Used for grid-based positioning and collision checks
    // @param tileSize - size of each tile in pixels
    // @return grid column position
    public int getGridColumn(int tileSize) {
        return positionX / tileSize;
    }

    // Converts the tank's pixel Y position to grid row
    // Used for grid-based positioning and collision checks
    // @param tileSize - size of each tile in pixels
    // @return grid row position
    public int getGridRow(int tileSize) {
        return positionY / tileSize;
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(
                objectImage,
                positionX,
                positionY + Y_OFFSET,
                objectWidth,
                objectHeight,
                null);
    }
}

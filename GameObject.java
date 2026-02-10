import java.awt.*;

/**
 * Abstract base class for all game objects that can be drawn on the map.
 * Provides common functionality for position, size, and rendering.
 */
public abstract class GameObject {
    
    protected int positionX, positionY;
    protected int objectWidth, objectHeight;
    protected Image objectImage;
    
    // Constructor that initializes a game object with grid-based positioning
    // Converts grid coordinates to pixel coordinates and sets up dimensions
    // @param gridColumn - column position in the tile grid
    // @param gridRow - row position in the tile grid  
    // @param tileSize - size of each tile in pixels
    // @param widthInTiles - width of object in tiles
    // @param heightInTiles - height of object in tiles
    // @param image - visual representation of the object
    public GameObject(int gridColumn, int gridRow, int tileSize,
                      int widthInTiles, int heightInTiles,
                      Image image) {
        
        this.positionX = gridColumn * tileSize;
        this.positionY = gridRow * tileSize;
        this.objectWidth = widthInTiles * tileSize;
        this.objectHeight = heightInTiles * tileSize;
        this.objectImage = image;
    }
    
    // Renders the game object to the screen
    // Draws the object's image at its current position with its dimensions
    // @param graphics - Graphics context for drawing
    public void draw(Graphics graphics) {
        graphics.drawImage(objectImage, positionX, positionY, objectWidth, objectHeight, null);
    }
}

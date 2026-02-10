import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * Represents the game map with tiles, objects, and tanks.
 * Handles map rendering, tank placement, and collision detection.
 */
public class Map extends JPanel {

    // Map configuration
    private static final int TILE_SIZE = 32;
    private static final int HOUSE_COLUMN = 37;
    private static final int HOUSE_ROW = 6;

    // Tile type constants
    private static final int TILE_ROAD = 0;
    private static final int TILE_GRASS = 1;
    private static final int TILE_WATER = 2;
    private static final int TILE_WATER_UP = 3;
    private static final int TILE_WATER_DOWN = 4;
    private static final int TILE_WATER_LEFT = 5;
    private static final int TILE_WATER_RIGHT = 6;
    private static final int TILE_TREE = 7;

    // Game objects
    private final ArrayList<GameObject> mapObjects = new ArrayList<>();
    private final ArrayList<Tank> defensiveTanks = new ArrayList<>();

    // Images
    private Image houseImage;
    private Image tankImage;
    private Image grassTile, roadTile, waterTile, waterUpTile, waterDownTile,
            waterLeftTile, waterRightTile, treeTile;

    // Map data - defines the layout of the game world
    // 0=road, 1=grass, 2=water, 3=water_up, 4=water_down, etc.
    private final int[][] mapGrid = {
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 4, 4 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 4, 1, 1 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 4, 1, 1, 1 },
            { 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                    4, 1, 1, 1, 0 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 0, 0 },
            { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                    3, 1, 1, 1, 0 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 3, 1, 1, 1 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 3, 1, 1 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 3, 3 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 }
    };

    // Constructor that initializes the map and sets up all components
    // Loads images, creates map objects, and configures the panel
    public Map() {
        loadImages();
        initializeMapObjects();
        configurePanel();
    }

    // Loads all tile and object images from the resource directory
    // Handles image loading errors and sets up visual assets
    private void loadImages() {
        try {
            grassTile = ImageIO.read(getClass().getResourceAsStream("image\\grass.png"));
            roadTile = ImageIO.read(getClass().getResourceAsStream("image\\dirt.png"));
            waterTile = ImageIO.read(getClass().getResourceAsStream("image\\water.png"));
            waterUpTile = ImageIO.read(getClass().getResourceAsStream("image\\water_up.png"));
            waterDownTile = ImageIO.read(getClass().getResourceAsStream("image\\water_down.png"));
            waterLeftTile = ImageIO.read(getClass().getResourceAsStream("image\\water_left.png"));
            waterRightTile = ImageIO.read(getClass().getResourceAsStream("image\\water_right.png"));
            treeTile = ImageIO.read(getClass().getResourceAsStream("image\\water.png"));
            houseImage = ImageIO.read(getClass().getResource("image\\house.png"));
            tankImage = ImageIO.read(getClass().getResource("image\\tank.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Creates and places the house object on the map
    // Initializes the house at the predefined location
    private void initializeMapObjects() {
        mapObjects.add(new House(HOUSE_COLUMN, HOUSE_ROW, TILE_SIZE, houseImage));
    }

    // Configures the panel size based on the map dimensions
    // Sets the preferred size for proper window sizing
    private void configurePanel() {
        setPreferredSize(new Dimension(
                mapGrid[0].length * TILE_SIZE,
                mapGrid.length * TILE_SIZE));
    }

    // Main rendering method that draws all map elements
    // Renders tiles, objects, and tanks in the correct order
    // @param graphics - Graphics context for drawing
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        renderMapTiles(graphics);
        renderMapObjects(graphics);
        renderTanks(graphics);
    }

    // Renders all map tiles based on the grid data
    // Iterates through the grid and draws appropriate tile images
    // @param graphics - Graphics context for drawing
    private void renderMapTiles(Graphics graphics) {
        for (int row = 0; row < mapGrid.length; row++) {
            for (int col = 0; col < mapGrid[row].length; col++) {
                Image tileImage = getTileImage(mapGrid[row][col]);
                graphics.drawImage(tileImage,
                        col * TILE_SIZE,
                        row * TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE,
                        this);
            }
        }
    }

    // Maps tile type numbers to their corresponding images
    // Returns the appropriate image based on the tile type value
    // @param tileType - numeric tile type from the map grid
    // @return Image corresponding to the tile type
    private Image getTileImage(int tileType) {
        switch (tileType) {
            case TILE_ROAD:
                return roadTile;
            case TILE_GRASS:
                return grassTile;
            case TILE_WATER:
                return waterTile;
            case TILE_WATER_UP:
                return waterUpTile;
            case TILE_WATER_DOWN:
                return waterDownTile;
            case TILE_WATER_LEFT:
                return waterLeftTile;
            case TILE_WATER_RIGHT:
                return waterRightTile;
            case TILE_TREE:
                return treeTile;
            default:
                return grassTile;
        }
    }

    // Renders all static map objects like the house
    // Draws objects that don't move during gameplay
    // @param graphics - Graphics context for drawing
    private void renderMapObjects(Graphics graphics) {
        for (GameObject object : mapObjects) {
            object.draw(graphics);
        }
    }

    // Renders all player-placed tanks
    // Draws tanks on top of other elements for visibility
    // @param graphics - Graphics context for drawing
    private void renderTanks(Graphics graphics) {
        for (Tank tank : defensiveTanks) {
            tank.draw(graphics);
        }
    }

    // Gets the raw map grid data
    // Used by other classes for pathfinding and collision detection
    // @return 2D array representing the map layout
    public int[][] getRawMap() {
        return mapGrid;
    }

    // Gets the size of each tile in pixels
    // Used for coordinate conversions between grid and pixel space
    // @return tile size in pixels
    public int getTileSize() {
        return TILE_SIZE;
    }

    // Calculates the total width of the map in pixels
    // @return map width in pixels
    public int getMapWidth() {
        return mapGrid[0].length * TILE_SIZE;
    }

    // Calculates the total height of the map in pixels
    // @return map height in pixels
    public int getMapHeight() {
        return mapGrid.length * TILE_SIZE;
    }

    // Gets the house object that needs to be defended
    // @return House object from the map objects list
    public House getHouse() {
        return (House) mapObjects.get(0);
    }

    // Alternative drawing method for external rendering
    // Delegates to the standard paintComponent method
    // @param graphics - Graphics context for drawing
    public void draw(Graphics graphics) {
        paintComponent(graphics);
    }

    // Gets the tile type at specific pixel coordinates
    // Converts pixel coordinates to grid coordinates and returns tile type
    // @param pixelX - X coordinate in pixels
    // @param pixelY - Y coordinate in pixels
    // @return tile type number, or -1 if out of bounds
    public int getTileAtPixel(double pixelX, double pixelY) {
        int col = (int) (pixelX / TILE_SIZE);
        int row = (int) (pixelY / TILE_SIZE);

        if (row < 0 || col < 0 ||
                row >= mapGrid.length ||
                col >= mapGrid[0].length) {
            return -1;
        }
        return mapGrid[row][col];
    }

    // Places a new tank at the specified grid position
    // Creates a tank object and adds it to the defensive tanks list
    // @param gridColumn - column position in the grid
    // @param gridRow - row position in the grid
    public void placeTank(int gridColumn, int gridRow) {
        defensiveTanks.add(new Tank(gridColumn, gridRow, TILE_SIZE, tankImage));
        repaint();
    }

    // Gets the list of all defensive tanks
    // Used by enemies for collision detection and combat
    // @return ArrayList of Tank objects
    public ArrayList<Tank> getTanks() {
        return defensiveTanks;
    }

    // Removes all dead tanks from the game
    // Cleans up destroyed tanks to prevent memory leaks
    public void removeDeadTanks() {
        defensiveTanks.removeIf(Tank::isDead);
    }

    // Checks if a tank already exists at the specified grid position
    // Prevents placing multiple tanks in the same location
    // @param gridColumn - column position to check
    // @param gridRow - row position to check
    // @return true if a tank exists at the position, false otherwise
    public boolean hasTankAt(int gridColumn, int gridRow) {
        for (Tank tank : defensiveTanks) {
            if (tank.getGridColumn(TILE_SIZE) == gridColumn &&
                    tank.getGridRow(TILE_SIZE) == gridRow) {
                return true;
            }
        }
        return false;
    }
}
package com.towerdefense.world;

import com.towerdefense.entities.base.GameObject;
import com.towerdefense.entities.defensive.House;
import com.towerdefense.entities.defensive.Tank;
import com.towerdefense.entities.defensive.Magic;
import com.towerdefense.entities.defensive.Archer;
import com.towerdefense.entities.defensive.Assassin;
import com.towerdefense.entities.enemies.Enemy;
import com.towerdefense.utils.Constants;
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

    // Game objects
    private final ArrayList<GameObject> mapObjects = new ArrayList<>();
    private final ArrayList<Tank> defensiveTanks = new ArrayList<>();
    private final ArrayList<Magic> magicTowers = new ArrayList<>();
    private final ArrayList<Archer> archerTowers = new ArrayList<>();
    private final ArrayList<Assassin> assassins = new ArrayList<>();

    // Images
    private Image houseImage;
    private Image tankImage;
    private Image magicImage;
    private Image archerImage;
    private Image assassinImage;
    private Image grassTile, roadTile, waterTile, waterUpTile, waterDownTile,
            waterLeftTile, waterRightTile, treeTile;

    // Map data - defines the layout of the game world
    // 0=road, 1=grass, 2=water, 3=water_up, 4=water_down, etc.
    private final int[][] mapGrid = {
            { 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2,
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
                    1, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1 },
            { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                    3, 3, 3, 3, 3 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
            { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                    2, 2, 2, 2, 2 },
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
            grassTile = ImageIO.read(new java.io.File(Constants.Paths.GRASS_TILE));
            roadTile = ImageIO.read(new java.io.File(Constants.Paths.ROAD_TILE));
            waterTile = ImageIO.read(new java.io.File(Constants.Paths.WATER_TILE));
            waterUpTile = ImageIO.read(new java.io.File(Constants.Paths.WATER_UP_TILE));
            waterDownTile = ImageIO.read(new java.io.File(Constants.Paths.WATER_DOWN_TILE));
            waterLeftTile = ImageIO.read(new java.io.File(Constants.Paths.WATER_LEFT_TILE));
            waterRightTile = ImageIO.read(new java.io.File(Constants.Paths.WATER_RIGHT_TILE));
            treeTile = ImageIO.read(new java.io.File(Constants.Paths.TREE_TILE));
            houseImage = ImageIO.read(new java.io.File(Constants.Paths.HOUSE_IMAGE));
            tankImage = ImageIO.read(new java.io.File(Constants.Paths.TANK_IMAGE));
            magicImage = ImageIO.read(new java.io.File(Constants.Paths.MAGIC_IMAGE));
            archerImage = ImageIO.read(new java.io.File(Constants.Paths.ARCHER_IMAGE));
            assassinImage = ImageIO.read(new java.io.File(Constants.Paths.ASSASSIN_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Creates and places the house object on the map
    // Initializes the house at the predefined location
    private void initializeMapObjects() {
        mapObjects.add(new House(Constants.Map.HOUSE_COLUMN, Constants.Map.HOUSE_ROW,
                Constants.Map.TILE_SIZE, houseImage));
    }

    // Configures the panel size based on the map dimensions
    // Sets the preferred size for proper window sizing
    private void configurePanel() {
        setPreferredSize(new Dimension(
                mapGrid[0].length * Constants.Map.TILE_SIZE,
                mapGrid.length * Constants.Map.TILE_SIZE));
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
        renderMagicTowers(graphics);
        renderArcherTowers(graphics);
        renderAssassins(graphics);
    }

    // Renders all map tiles based on the grid data
    // Iterates through the grid and draws appropriate tile images
    // @param graphics - Graphics context for drawing
    private void renderMapTiles(Graphics graphics) {
        for (int row = 0; row < mapGrid.length; row++) {
            for (int col = 0; col < mapGrid[row].length; col++) {
                Image tileImage = getTileImage(mapGrid[row][col]);
                graphics.drawImage(tileImage,
                        col * Constants.Map.TILE_SIZE,
                        row * Constants.Map.TILE_SIZE,
                        Constants.Map.TILE_SIZE,
                        Constants.Map.TILE_SIZE,
                        this);
            }
        }
    }

    // Maps tile type numbers to their corresponding images
    // Returns the appropriate image based on the tile type value
    // @param tileType - numeric tile type from the map grid
    // @return Image corresponding to the tile type
    private Image getTileImage(int tileType) {
        if (tileType == Constants.Map.TILE_ROAD) {
            return roadTile;
        } else if (tileType == Constants.Map.TILE_GRASS) {
            return grassTile;
        } else if (tileType == Constants.Map.TILE_WATER) {
            return waterTile;
        } else if (tileType == Constants.Map.TILE_WATER_UP) {
            return waterUpTile;
        } else if (tileType == Constants.Map.TILE_WATER_DOWN) {
            return waterDownTile;
        } else if (tileType == Constants.Map.TILE_WATER_LEFT) {
            return waterLeftTile;
        } else if (tileType == Constants.Map.TILE_WATER_RIGHT) {
            return waterRightTile;
        } else if (tileType == Constants.Map.TILE_TREE) {
            return treeTile;
        } else {
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

    // Renders all player-placed magic towers
    // Draws magic towers on top of other elements for visibility
    // @param graphics - Graphics context for drawing
    private void renderMagicTowers(Graphics graphics) {
        for (Magic magic : magicTowers) {
            magic.draw(graphics);
        }
    }

    // Renders all player-placed archer towers
    // Draws archer towers on top of other elements for visibility
    // @param graphics - Graphics context for drawing
    private void renderArcherTowers(Graphics graphics) {
        for (Archer archer : archerTowers) {
            archer.draw(graphics);
        }
    }

    // Renders all player-placed assassins
    // Draws assassins on top of other elements for visibility
    // @param graphics - Graphics context for drawing
    private void renderAssassins(Graphics graphics) {
        for (Assassin assassin : assassins) {
            assassin.draw(graphics);
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
        return Constants.Map.TILE_SIZE;
    }

    // Calculates the total width of the map in pixels
    // @return map width in pixels
    public int getMapWidth() {
        return mapGrid[0].length * Constants.Map.TILE_SIZE;
    }

    // Calculates the total height of the map in pixels
    // @return map height in pixels
    public int getMapHeight() {
        return mapGrid.length * Constants.Map.TILE_SIZE;
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
        int col = (int) (pixelX / Constants.Map.TILE_SIZE);
        int row = (int) (pixelY / Constants.Map.TILE_SIZE);

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
        defensiveTanks.add(new Tank(gridColumn, gridRow, Constants.Map.TILE_SIZE, tankImage));
        repaint();
    }

    // Places a new magic tower at the specified grid position
    // Creates a magic object and adds it to the magic towers list
    // @param gridColumn - column position in the grid
    // @param gridRow - row position in the grid
    // @param enemies - reference to enemy list for targeting
    public void placeMagic(int gridColumn, int gridRow, ArrayList<Enemy> enemies) {
        magicTowers.add(new Magic(gridColumn, gridRow, Constants.Map.TILE_SIZE, magicImage, enemies));
        repaint();
    }

    // Places a new archer tower at the specified grid position
    // Creates an archer object and adds it to the archer towers list
    // @param gridColumn - column position in the grid
    // @param gridRow - row position in the grid
    // @param enemies - reference to enemy list for targeting
    public void placeArcher(int gridColumn, int gridRow, ArrayList<Enemy> enemies) {
        archerTowers.add(new Archer(gridColumn, gridRow, Constants.Map.TILE_SIZE, archerImage, enemies));
        repaint();
    }

    // Places a new assassin at the specified grid position
    // Creates an assassin object and adds it to the assassins list
    // @param gridColumn - column position in the grid
    // @param gridRow - row position in the grid
    // @param enemies - reference to enemy list for targeting
    public void placeAssassin(int gridColumn, int gridRow, ArrayList<Enemy> enemies) {
        assassins.add(new Assassin(gridColumn, gridRow, Constants.Map.TILE_SIZE, assassinImage, enemies));
        repaint();
    }

    // Updates all magic towers each frame
    // Handles targeting and attack logic for all magic towers
    public void updateMagicTowers() {
        for (Magic magic : magicTowers) {
            magic.update();
        }
    }

    // Updates all archer towers each frame
    // Handles targeting and attack logic for all archer towers
    public void updateArcherTowers() {
        for (Archer archer : archerTowers) {
            archer.update();
        }
    }

    // Updates all assassins each frame
    // Handles attack logic for all assassins
    public void updateAssassins() {
        for (Assassin assassin : assassins) {
            assassin.update();
        }
    }

    // Gets the list of all defensive tanks
    // Used by enemies for collision detection and combat
    // @return ArrayList of Tank objects
    public ArrayList<Tank> getTanks() {
        return defensiveTanks;
    }

    // Gets the list of all magic towers
    // Used by enemies for collision detection and combat
    // @return ArrayList of Magic objects
    public ArrayList<Magic> getMagicTowers() {
        return magicTowers;
    }

    // Gets the list of all archer towers
    // Used by enemies for collision detection and combat
    // @return ArrayList of Archer objects
    public ArrayList<Archer> getArcherTowers() {
        return archerTowers;
    }

    // Gets the list of all assassins
    // @return ArrayList of Assassin objects
    public ArrayList<Assassin> getAssassins() {
        return assassins;
    }

    // Removes all dead tanks from the game
    // Cleans up destroyed tanks to prevent memory leaks
    public void removeDeadTanks() {
        defensiveTanks.removeIf(Tank::isDead);
    }

    // Removes all dead magic towers from the game
    // Cleans up destroyed magic towers to prevent memory leaks
    public void removeDeadMagicTowers() {
        magicTowers.removeIf(Magic::isDead);
    }

    // Removes all dead archer towers from the game
    // Cleans up destroyed archer towers to prevent memory leaks
    public void removeDeadArcherTowers() {
        archerTowers.removeIf(Archer::isDead);
    }

    // Checks if a tank already exists at the specified grid position
    // Prevents placing multiple tanks in the same location
    // @param gridColumn - column position to check
    // @param gridRow - row position to check
    // @return true if a tank exists at the position, false otherwise
    public boolean hasTankAt(int gridColumn, int gridRow) {
        for (Tank tank : defensiveTanks) {
            if (tank.getGridColumn(Constants.Map.TILE_SIZE) == gridColumn &&
                    tank.getGridRow(Constants.Map.TILE_SIZE) == gridRow) {
                return true;
            }
        }
        return false;
    }

    // Checks if a magic tower already exists at the specified grid position
    // Prevents placing multiple magic towers in the same location
    // @param gridColumn - column position to check
    // @param gridRow - row position to check
    // @return true if a magic tower exists at the position, false otherwise
    public boolean hasMagicAt(int gridColumn, int gridRow) {
        for (Magic magic : magicTowers) {
            if (magic.getGridColumn(Constants.Map.TILE_SIZE) == gridColumn &&
                    magic.getGridRow(Constants.Map.TILE_SIZE) == gridRow) {
                return true;
            }
        }
        return false;
    }

    // Checks if an archer tower already exists at the specified grid position
    // Prevents placing multiple archer towers in the same location
    // @param gridColumn - column position to check
    // @param gridRow - row position to check
    // @return true if an archer tower exists at the position, false otherwise
    public boolean hasArcherAt(int gridColumn, int gridRow) {
        for (Archer archer : archerTowers) {
            if (archer.getGridColumn(Constants.Map.TILE_SIZE) == gridColumn &&
                    archer.getGridRow(Constants.Map.TILE_SIZE) == gridRow) {
                return true;
            }
        }
        return false;
    }

    // Checks if an assassin already exists at the specified grid position
    // Prevents placing multiple assassins in the same location
    // @param gridColumn - column position to check
    // @param gridRow - row position to check
    // @return true if an assassin exists at the position, false otherwise
    public boolean hasAssassinAt(int gridColumn, int gridRow) {
        for (Assassin assassin : assassins) {
            if (assassin.getGridColumn(Constants.Map.TILE_SIZE) == gridColumn &&
                    assassin.getGridRow(Constants.Map.TILE_SIZE) == gridRow) {
                return true;
            }
        }
        return false;
    }
}
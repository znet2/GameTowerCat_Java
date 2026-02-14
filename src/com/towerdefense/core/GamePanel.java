package com.towerdefense.core;

import com.towerdefense.world.Map;
import com.towerdefense.entities.enemies.Enemy;
import com.towerdefense.managers.WaveManager;
import com.towerdefense.managers.CoinManager;
import com.towerdefense.utils.Constants;
import com.towerdefense.utils.MathUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Main game panel that handles the game loop, rendering, and user input.
 * Coordinates between the map, enemies, wave manager, and user interface.
 */
public class GamePanel extends JPanel implements Runnable, MouseListener, MouseMotionListener {

    // Game components
    private Thread gameLoopThread;
    private Map gameMap;
    private ArrayList<Enemy> activeEnemies = new ArrayList<>();
    private WaveManager waveManager;
    private CoinManager coinManager;
    private JFrame parentFrame;

    // Game state
    private boolean isGameOver = false;
    private boolean isWin = false;

    // User interface
    private Rectangle heroBarArea;
    private Rectangle tankIconArea;
    private Rectangle magicIconArea;
    private Rectangle archerIconArea;
    private Rectangle assassinIconArea;

    // Input state
    private boolean isDraggingTank = false;
    private boolean isDraggingMagic = false;
    private boolean isDraggingArcher = false;
    private boolean isDraggingAssassin = false;
    private int dragPositionX, dragPositionY;

    // Constructor that initializes the game panel and starts the game
    // Sets up all game components, UI elements, and begins the game loop
    public GamePanel(JFrame frame) {
        this.parentFrame = frame;
        initializeGameComponents();
        setupUserInterface();
        configurePanel();
        startGameLoop();
    }

    // Initializes core game components
    // Creates the map, wave manager, coin manager, and starts the first wave
    private void initializeGameComponents() {
        gameMap = new Map();
        coinManager = new CoinManager();
        waveManager = new WaveManager(gameMap, activeEnemies, coinManager);
        waveManager.startNextWave();
    }

    // Sets up user interface elements
    // Creates rectangles for the hero bar and tank icon positioning
    private void setupUserInterface() {
        heroBarArea = new Rectangle(
                0,
                gameMap.getMapHeight(),
                gameMap.getMapWidth(),
                Constants.UI.HERO_BAR_HEIGHT);

        tankIconArea = new Rectangle(
                Constants.UI.TANK_ICON_MARGIN,
                gameMap.getMapHeight() + Constants.UI.TANK_ICON_TOP_MARGIN,
                Constants.UI.TANK_ICON_SIZE,
                Constants.UI.TANK_ICON_SIZE);

        magicIconArea = new Rectangle(
                Constants.UI.TANK_ICON_MARGIN + Constants.UI.TANK_ICON_SIZE + Constants.UI.TANK_ICON_MARGIN,
                gameMap.getMapHeight() + Constants.UI.TANK_ICON_TOP_MARGIN,
                Constants.UI.TANK_ICON_SIZE,
                Constants.UI.TANK_ICON_SIZE);

        archerIconArea = new Rectangle(
                Constants.UI.TANK_ICON_MARGIN + (Constants.UI.TANK_ICON_SIZE + Constants.UI.TANK_ICON_MARGIN) * 2,
                gameMap.getMapHeight() + Constants.UI.TANK_ICON_TOP_MARGIN,
                Constants.UI.TANK_ICON_SIZE,
                Constants.UI.TANK_ICON_SIZE);

        assassinIconArea = new Rectangle(
                Constants.UI.TANK_ICON_MARGIN + (Constants.UI.TANK_ICON_SIZE + Constants.UI.TANK_ICON_MARGIN) * 3,
                gameMap.getMapHeight() + Constants.UI.TANK_ICON_TOP_MARGIN,
                Constants.UI.TANK_ICON_SIZE,
                Constants.UI.TANK_ICON_SIZE);
    }

    // Configures the panel properties and input handling
    // Sets size, background, focus, and mouse listeners
    private void configurePanel() {
        setPreferredSize(new Dimension(
                gameMap.getMapWidth(),
                gameMap.getMapHeight() + Constants.UI.HERO_BAR_HEIGHT));

        setBackground(Color.BLACK);
        setFocusable(true);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // Starts the game loop thread
    // Creates and starts the thread that runs the main game loop
    private void startGameLoop() {
        gameLoopThread = new Thread(this);
        gameLoopThread.start();
    }

    // Thread run method that executes the game loop
    // Required by Runnable interface, delegates to the main game loop
    @Override
    public void run() {
        runGameLoop();
    }

    // Main game loop that runs at 60 FPS
    // Uses delta time to maintain consistent frame rate regardless of system
    // performance
    private void runGameLoop() {
        double deltaTime = 0;
        long previousTime = System.nanoTime();

        while (true) {
            long currentTime = System.nanoTime();
            deltaTime += (currentTime - previousTime) / Constants.Game.NANOSECONDS_PER_FRAME;
            previousTime = currentTime;

            if (deltaTime >= 1) {
                updateGame();
                repaint();
                deltaTime--;
            }
        }
    }

    // Updates all game components each frame
    // Coordinates updates between wave manager, enemies, and wave progression
    private void updateGame() {
        if (isGameOver) {
            return; // Stop updating if game is over
        }

        updateWaveManager();
        updateCoinManager();
        updateEnemies();
        updateMagicTowers();
        updateArcherTowers();
        updateAssassins();
        cleanupDeadTanks();
        cleanupDeadMagicTowers();
        cleanupDeadArcherTowers();
        checkGameOver();
        checkForNextWave();
    }

    // Updates the wave manager for enemy spawning
    // Handles timing and creation of new enemies
    private void updateWaveManager() {
        waveManager.update();
    }

    // Updates the coin manager for passive income
    // Handles automatic coin generation over time
    private void updateCoinManager() {
        coinManager.update();
    }

    // Updates all active enemies
    // Processes enemy movement, combat, and AI behavior, removes dead enemies
    private void updateEnemies() {
        for (int i = activeEnemies.size() - 1; i >= 0; i--) {
            Enemy enemy = activeEnemies.get(i);
            enemy.update();

            // Remove dead enemies from the list
            if (enemy.isDead()) {
                activeEnemies.remove(i);
            }
        }
    }

    // Updates all magic towers
    // Handles targeting and attack logic for magic towers
    private void updateMagicTowers() {
        gameMap.updateMagicTowers();
    }

    // Updates all archer towers
    // Handles targeting and attack logic for archer towers
    private void updateArcherTowers() {
        gameMap.updateArcherTowers();
    }

    // Updates all assassins
    // Handles attack logic for assassins
    private void updateAssassins() {
        gameMap.updateAssassins();
    }

    // Removes destroyed tanks from the game
    // Cleans up dead tanks to prevent memory leaks and visual clutter
    private void cleanupDeadTanks() {
        gameMap.removeDeadTanks();
    }

    // Removes destroyed magic towers from the game
    // Cleans up dead magic towers to prevent memory leaks and visual clutter
    private void cleanupDeadMagicTowers() {
        gameMap.removeDeadMagicTowers();
    }

    // Removes destroyed archer towers from the game
    // Cleans up dead archer towers to prevent memory leaks and visual clutter
    private void cleanupDeadArcherTowers() {
        gameMap.removeDeadArcherTowers();
    }

    // Checks if the current wave is finished and starts the next one
    // Handles wave progression when all enemies are defeated
    private void checkForNextWave() {
        if (waveManager.isWaveFinished()) {
            // Check if all waves completed
            if (waveManager.getCurrentWave() >= Constants.Waves.MAX_WAVES) {
                isGameOver = true;
                isWin = true;
                showGameOverScreen();
            } else {
                waveManager.startNextWave();
            }
        }
    }

    // Checks if game is over (house destroyed)
    private void checkGameOver() {
        if (gameMap.getHouse().getHealth() <= 0) {
            isGameOver = true;
            isWin = false;
            showGameOverScreen();
        }
    }

    // Shows game over screen
    private void showGameOverScreen() {
        SwingUtilities.invokeLater(() -> {
            parentFrame.getContentPane().removeAll();
            GameOverPanel gameOverPanel = new GameOverPanel(parentFrame, isWin);
            parentFrame.add(gameOverPanel);
            parentFrame.pack();
            parentFrame.setLocationRelativeTo(null);
            parentFrame.revalidate();
            parentFrame.repaint();
        });
    }

    // Main rendering method that draws all game elements
    // Renders map, enemies, and UI in the correct order
    // @param graphics - Graphics context for drawing
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        renderMap(graphics);
        renderEnemies(graphics);
        renderUserInterface(graphics);
        renderCoinDisplay(graphics);
    }

    // Renders the game map including tiles, objects, and tanks
    // @param graphics - Graphics context for drawing
    private void renderMap(Graphics graphics) {
        gameMap.draw(graphics);
    }

    // Renders all active enemies on the screen
    // @param graphics - Graphics context for drawing
    private void renderEnemies(Graphics graphics) {
        for (Enemy enemy : activeEnemies) {
            enemy.draw(graphics);
        }
    }

    // Renders all user interface elements
    // Draws hero bar, tank icon, magic icon, and drag preview
    // @param graphics - Graphics context for drawing
    private void renderUserInterface(Graphics graphics) {
        drawHeroBar(graphics);
        drawTankIcon(graphics);
        drawMagicIcon(graphics);
        drawArcherIcon(graphics);
        drawAssassinIcon(graphics);
        drawDragPreview(graphics);
        drawHouseHealthBar(graphics);
    }

    // Renders the coin display in the bottom-right corner
    // Shows current coin count with proper formatting
    // @param graphics - Graphics context for drawing
    private void renderCoinDisplay(Graphics graphics) {
        coinManager.renderCoinDisplay(graphics, getWidth(), getHeight());
    }

    // Draws the hero bar at the bottom of the screen
    // Provides background for UI elements
    // @param graphics - Graphics context for drawing
    private void drawHeroBar(Graphics graphics) {
        graphics.setColor(Constants.UI.HERO_BAR_COLOR);
        graphics.fillRect(heroBarArea.x, heroBarArea.y, heroBarArea.width, heroBarArea.height);
    }

    // Draws the tank icon that players can drag to place tanks
    // Shows the draggable tank placement tool with cost indication
    // @param graphics - Graphics context for drawing
    private void drawTankIcon(Graphics graphics) {
        boolean canAffordTank = coinManager.canAfford(coinManager.getTankCost());

        // Set color based on affordability
        if (canAffordTank) {
            graphics.setColor(Constants.UI.TANK_ICON_COLOR);
        } else {
            graphics.setColor(Constants.UI.TANK_ICON_DISABLED_COLOR);
        }

        graphics.fillRect(tankIconArea.x, tankIconArea.y, tankIconArea.width, tankIconArea.height);

        graphics.setColor(Color.BLACK);
        graphics.drawRect(tankIconArea.x, tankIconArea.y, tankIconArea.width, tankIconArea.height);

        // Draw tank label and cost
        graphics.drawString("Tank", tankIconArea.x + 8, tankIconArea.y + 20);
        graphics.drawString("$" + coinManager.getTankCost(), tankIconArea.x + 8, tankIconArea.y + 40);
    }

    // Draws the magic icon that players can drag to place magic towers
    // Shows the draggable magic placement tool with cost indication
    // @param graphics - Graphics context for drawing
    private void drawMagicIcon(Graphics graphics) {
        boolean canAffordMagic = coinManager.canAfford(Constants.Entities.MAGIC_COST);

        // Set color based on affordability
        if (canAffordMagic) {
            graphics.setColor(Constants.UI.MAGIC_ICON_COLOR);
        } else {
            graphics.setColor(Constants.UI.MAGIC_ICON_DISABLED_COLOR);
        }

        graphics.fillRect(magicIconArea.x, magicIconArea.y, magicIconArea.width, magicIconArea.height);

        graphics.setColor(Color.BLACK);
        graphics.drawRect(magicIconArea.x, magicIconArea.y, magicIconArea.width, magicIconArea.height);

        // Draw magic label and cost
        graphics.drawString("Magic", magicIconArea.x + 6, magicIconArea.y + 20);
        graphics.drawString("$" + Constants.Entities.MAGIC_COST, magicIconArea.x + 8, magicIconArea.y + 40);
    }

    // Draws the archer icon that players can drag to place archer towers
    // Shows the draggable archer placement tool with cost indication
    // @param graphics - Graphics context for drawing
    private void drawArcherIcon(Graphics graphics) {
        boolean canAffordArcher = coinManager.canAfford(Constants.Entities.ARCHER_COST);

        // Set color based on affordability
        if (canAffordArcher) {
            graphics.setColor(new Color(139, 69, 19)); // Brown color for archer
        } else {
            graphics.setColor(Constants.UI.MAGIC_ICON_DISABLED_COLOR);
        }

        graphics.fillRect(archerIconArea.x, archerIconArea.y, archerIconArea.width, archerIconArea.height);

        graphics.setColor(Color.BLACK);
        graphics.drawRect(archerIconArea.x, archerIconArea.y, archerIconArea.width, archerIconArea.height);

        // Draw archer label and cost
        graphics.drawString("Archer", archerIconArea.x + 4, archerIconArea.y + 20);
        graphics.drawString("$" + Constants.Entities.ARCHER_COST, archerIconArea.x + 8, archerIconArea.y + 40);
    }

    // Draws the assassin icon that players can drag to place assassins
    // Shows the draggable assassin placement tool with cost indication
    // @param graphics - Graphics context for drawing
    private void drawAssassinIcon(Graphics graphics) {
        boolean canAffordAssassin = coinManager.canAfford(Constants.Entities.ASSASSIN_COST);

        // Set color based on affordability
        if (canAffordAssassin) {
            graphics.setColor(new Color(128, 0, 128)); // Purple color for assassin
        } else {
            graphics.setColor(Constants.UI.MAGIC_ICON_DISABLED_COLOR);
        }

        graphics.fillRect(assassinIconArea.x, assassinIconArea.y, assassinIconArea.width, assassinIconArea.height);

        graphics.setColor(Color.BLACK);
        graphics.drawRect(assassinIconArea.x, assassinIconArea.y, assassinIconArea.width, assassinIconArea.height);

        // Draw assassin label and cost
        graphics.drawString("Assassin", assassinIconArea.x + 2, assassinIconArea.y + 20);
        graphics.drawString("$" + Constants.Entities.ASSASSIN_COST, assassinIconArea.x + 8, assassinIconArea.y + 40);
    }

    // Draws the preview of tank placement while dragging
    // Shows semi-transparent tank at cursor position during drag
    // @param graphics - Graphics context for drawing
    private void drawDragPreview(Graphics graphics) {
        if (isDraggingTank) {
            graphics.setColor(Constants.UI.DRAG_PREVIEW_COLOR);
            graphics.fillRect(
                    dragPositionX - Constants.UI.TANK_ICON_SIZE / 2,
                    dragPositionY - Constants.UI.TANK_ICON_SIZE / 2,
                    Constants.UI.TANK_ICON_SIZE,
                    Constants.UI.TANK_ICON_SIZE);
        }
        if (isDraggingMagic) {
            graphics.setColor(new Color(0, 255, 255, 150)); // Cyan semi-transparent
            graphics.fillRect(
                    dragPositionX - Constants.UI.TANK_ICON_SIZE / 2,
                    dragPositionY - Constants.UI.TANK_ICON_SIZE / 2,
                    Constants.UI.TANK_ICON_SIZE,
                    Constants.UI.TANK_ICON_SIZE);
        }
        if (isDraggingArcher) {
            graphics.setColor(new Color(139, 69, 19, 150)); // Brown semi-transparent
            graphics.fillRect(
                    dragPositionX - Constants.UI.TANK_ICON_SIZE / 2,
                    dragPositionY - Constants.UI.TANK_ICON_SIZE / 2,
                    Constants.UI.TANK_ICON_SIZE,
                    Constants.UI.TANK_ICON_SIZE);
        }
        if (isDraggingAssassin) {
            graphics.setColor(new Color(128, 0, 128, 150)); // Purple semi-transparent
            graphics.fillRect(
                    dragPositionX - Constants.UI.TANK_ICON_SIZE / 2,
                    dragPositionY - Constants.UI.TANK_ICON_SIZE / 2,
                    Constants.UI.TANK_ICON_SIZE,
                    Constants.UI.TANK_ICON_SIZE);
        }
    }

    // Draws the house health bar at the top-right corner
    // Shows current house health status
    // @param graphics - Graphics context for drawing
    private void drawHouseHealthBar(Graphics graphics) {
        int barWidth = 200;
        int barHeight = 20;
        int barX = getWidth() - barWidth - 20;
        int barY = 20;

        // Get house health
        int currentHealth = gameMap.getHouse().getHealth();
        int maxHealth = Constants.Entities.HOUSE_INITIAL_HEALTH;
        double healthPercentage = (double) currentHealth / maxHealth;

        // Background (red)
        graphics.setColor(Color.RED);
        graphics.fillRect(barX, barY, barWidth, barHeight);

        // Health (green)
        graphics.setColor(Color.GREEN);
        int healthWidth = (int) (barWidth * healthPercentage);
        graphics.fillRect(barX, barY, healthWidth, barHeight);

        // Border
        graphics.setColor(Color.BLACK);
        graphics.drawRect(barX, barY, barWidth, barHeight);

        // Text
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 14));
        String healthText = "House: " + currentHealth + "/" + maxHealth;
        graphics.drawString(healthText, barX + 10, barY + 15);
    }

    // Handles mouse press events for starting tank drag
    // Detects clicks on the tank icon to begin placement (if affordable)
    // @param event - MouseEvent containing click information
    @Override
    public void mousePressed(MouseEvent event) {
        if (tankIconArea.contains(event.getPoint()) && coinManager.canAfford(coinManager.getTankCost())) {
            startTankDrag(event);
        } else if (magicIconArea.contains(event.getPoint()) && coinManager.canAfford(Constants.Entities.MAGIC_COST)) {
            startMagicDrag(event);
        } else if (archerIconArea.contains(event.getPoint()) && coinManager.canAfford(Constants.Entities.ARCHER_COST)) {
            startArcherDrag(event);
        } else if (assassinIconArea.contains(event.getPoint())
                && coinManager.canAfford(Constants.Entities.ASSASSIN_COST)) {
            startAssassinDrag(event);
        }
    }

    // Initiates tank dragging mode
    // Sets drag state and initial position
    // @param event - MouseEvent containing initial position
    private void startTankDrag(MouseEvent event) {
        isDraggingTank = true;
        dragPositionX = event.getX();
        dragPositionY = event.getY();
    }

    // Initiates magic dragging mode
    // Sets drag state and initial position
    // @param event - MouseEvent containing initial position
    private void startMagicDrag(MouseEvent event) {
        isDraggingMagic = true;
        dragPositionX = event.getX();
        dragPositionY = event.getY();
    }

    // Initiates archer dragging mode
    // Sets drag state and initial position
    // @param event - MouseEvent containing initial position
    private void startArcherDrag(MouseEvent event) {
        isDraggingArcher = true;
        dragPositionX = event.getX();
        dragPositionY = event.getY();
    }

    // Initiates assassin dragging mode
    // Sets drag state and initial position
    // @param event - MouseEvent containing initial position
    private void startAssassinDrag(MouseEvent event) {
        isDraggingAssassin = true;
        dragPositionX = event.getX();
        dragPositionY = event.getY();
    }

    // Handles mouse drag events for tank placement preview
    // Updates drag position while tank is being dragged
    // @param event - MouseEvent containing current position
    @Override
    public void mouseDragged(MouseEvent event) {
        if (isDraggingTank || isDraggingMagic || isDraggingArcher || isDraggingAssassin) {
            updateDragPosition(event);
        }
    }

    // Updates the drag position for visual feedback
    // Moves the drag preview to follow the cursor
    // @param event - MouseEvent containing new position
    private void updateDragPosition(MouseEvent event) {
        dragPositionX = event.getX();
        dragPositionY = event.getY();
    }

    // Handles mouse release events for tank placement
    // Attempts to place tank at release position and ends drag mode
    // @param event - MouseEvent containing release position
    @Override
    public void mouseReleased(MouseEvent event) {
        if (isDraggingTank) {
            attemptTankPlacement(event);
            stopTankDrag();
        } else if (isDraggingMagic) {
            attemptMagicPlacement(event);
            stopMagicDrag();
        } else if (isDraggingArcher) {
            attemptArcherPlacement(event);
            stopArcherDrag();
        } else if (isDraggingAssassin) {
            attemptAssassinPlacement(event);
            stopAssassinDrag();
        }
    }

    // Attempts to place a tank at the mouse release position
    // Validates placement location and processes purchase if valid
    // @param event - MouseEvent containing placement position
    private void attemptTankPlacement(MouseEvent event) {
        int gridColumn = MathUtils.pixelToGrid(event.getX(), gameMap.getTileSize());
        int gridRow = MathUtils.pixelToGrid(event.getY(), gameMap.getTileSize());

        if (isValidPlacementPosition(gridColumn, gridRow) && coinManager.purchaseTank()) {
            gameMap.placeTank(gridColumn, gridRow);
        }
    }

    // Validates if a tank can be placed at the specified position
    // Checks map bounds, tile type, and existing tank presence
    // @param gridColumn - column position to validate
    // @param gridRow - row position to validate
    // @return true if placement is valid, false otherwise
    private boolean isValidPlacementPosition(int gridColumn, int gridRow) {
        return isWithinMapBounds(gridColumn, gridRow) &&
                isRoadTile(gridColumn, gridRow) &&
                !gameMap.hasTankAt(gridColumn, gridRow) &&
                !gameMap.hasMagicAt(gridColumn, gridRow) &&
                !gameMap.hasArcherAt(gridColumn, gridRow) &&
                !gameMap.hasAssassinAt(gridColumn, gridRow);
    }

    // Checks if the position is within the map boundaries
    // Ensures placement doesn't go outside the game area
    // @param gridColumn - column position to check
    // @param gridRow - row position to check
    // @return true if within bounds, false otherwise
    private boolean isWithinMapBounds(int gridColumn, int gridRow) {
        int[][] mapGrid = gameMap.getRawMap();
        return MathUtils.isWithinBounds(gridColumn, gridRow, mapGrid[0].length, mapGrid.length);
    }

    // Checks if the tile at the position is a road tile
    // Tanks can only be placed on road tiles (type 0)
    // @param gridColumn - column position to check
    // @param gridRow - row position to check
    // @return true if tile is road, false otherwise
    private boolean isRoadTile(int gridColumn, int gridRow) {
        int tileType = gameMap.getRawMap()[gridRow][gridColumn];
        return tileType == Constants.Map.TILE_ROAD;
    }

    // Stops tank dragging mode
    // Resets drag state after placement attempt
    private void stopTankDrag() {
        isDraggingTank = false;
    }

    // Attempts to place a magic tower at the mouse release position
    // Validates placement location and processes purchase if valid
    // @param event - MouseEvent containing placement position
    private void attemptMagicPlacement(MouseEvent event) {
        int gridColumn = MathUtils.pixelToGrid(event.getX(), gameMap.getTileSize());
        int gridRow = MathUtils.pixelToGrid(event.getY(), gameMap.getTileSize());

        if (isValidPlacementPosition(gridColumn, gridRow) && coinManager.spendCoins(Constants.Entities.MAGIC_COST)) {
            gameMap.placeMagic(gridColumn, gridRow, activeEnemies);
        }
    }

    // Stops magic dragging mode
    // Resets drag state after placement attempt
    private void stopMagicDrag() {
        isDraggingMagic = false;
    }

    // Attempts to place an archer tower at the mouse release position
    // Validates placement location and processes purchase if valid
    // @param event - MouseEvent containing placement position
    private void attemptArcherPlacement(MouseEvent event) {
        int gridColumn = MathUtils.pixelToGrid(event.getX(), gameMap.getTileSize());
        int gridRow = MathUtils.pixelToGrid(event.getY(), gameMap.getTileSize());

        if (isValidPlacementPosition(gridColumn, gridRow) && coinManager.spendCoins(Constants.Entities.ARCHER_COST)) {
            gameMap.placeArcher(gridColumn, gridRow, activeEnemies);
        }
    }

    // Stops archer dragging mode
    // Resets drag state after placement attempt
    private void stopArcherDrag() {
        isDraggingArcher = false;
    }

    // Attempts to place an assassin at the mouse release position
    // Validates placement location and processes purchase if valid
    // @param event - MouseEvent containing placement position
    private void attemptAssassinPlacement(MouseEvent event) {
        int gridColumn = MathUtils.pixelToGrid(event.getX(), gameMap.getTileSize());
        int gridRow = MathUtils.pixelToGrid(event.getY(), gameMap.getTileSize());

        if (isValidPlacementPosition(gridColumn, gridRow) && coinManager.spendCoins(Constants.Entities.ASSASSIN_COST)) {
            gameMap.placeAssassin(gridColumn, gridRow, activeEnemies);
        }
    }

    // Stops assassin dragging mode
    // Resets drag state after placement attempt
    private void stopAssassinDrag() {
        isDraggingAssassin = false;
    }

    // Gets the coin manager for external access
    // Used by other components that need to interact with the coin system
    // @return the coin manager instance
    public CoinManager getCoinManager() {
        return coinManager;
    }

    // Unused mouse event methods required by MouseListener interface
    // These methods are required but not used in this implementation
    @Override
    public void mouseClicked(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mouseMoved(MouseEvent event) {
    }
}
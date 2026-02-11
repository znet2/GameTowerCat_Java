package com.towerdefense.entities.enemies;

import com.towerdefense.world.Map;
import com.towerdefense.entities.defensive.House;
import com.towerdefense.entities.defensive.Tank;
import com.towerdefense.entities.defensive.Magic;
import com.towerdefense.managers.CoinManager;
import com.towerdefense.utils.Constants;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Represents an enemy unit that moves along a path and attacks tanks and the
 * house.
 * Enemies follow predefined routes, engage in combat, and have AI behavior.
 */
public class Enemy {

    // Visual properties
    private final Image enemyImage;

    // Position and movement
    private double positionX, positionY;

    // Path following
    private final ArrayList<Point> movementPath = new ArrayList<>();
    private int currentPathIndex = 0;

    // Game references
    private final Map gameMap;
    private final House targetHouse;

    // Combat state
    private boolean isAttacking = false;
    private int attackTimer = 0;
    private Object currentAttackTarget = null;

    // Enemy state
    private boolean isDead = false;
    private int currentHealth = Constants.Entities.ENEMY_INITIAL_HEALTH;

    // Position management to prevent enemy overlap
    private static int totalEnemyCount = 0;
    private final int attackPositionOffset;
    private boolean isPositionLocked = false;

    // Constants
    private static final int POSITION_OFFSET_MULTIPLIER = 12;

    // Constructor that creates an enemy and sets up its path
    // Initializes movement path, position, and assigns unique attack offset
    // @param gameMap - reference to the game map for pathfinding and collision
    // @param coinManager - reference to coin system (unused in defensive mode)
    public Enemy(Map gameMap, CoinManager coinManager) {
        this.gameMap = gameMap;
        this.targetHouse = gameMap.getHouse();

        this.enemyImage = new ImageIcon(Constants.Paths.ENEMY_IMAGE).getImage();

        // Calculate unique attack position offset for this enemy
        // Counter is reset at the start of each wave for consistent positioning
        this.attackPositionOffset = totalEnemyCount * POSITION_OFFSET_MULTIPLIER;
        totalEnemyCount++;

        buildMovementPath();
        initializeStartingPosition();
    }

    // Builds the movement path from the leftmost road tile to the house
    // Scans the map for road tiles and creates a sequential path
    private void buildMovementPath() {
        int[][] mapGrid = gameMap.getRawMap();
        int tileSize = gameMap.getTileSize();

        Point roadStart = findRoadStartPosition(mapGrid);
        createPathFromRoadStart(mapGrid, tileSize, roadStart);
    }

    // Finds the leftmost road tile in the map as the starting position
    // Searches through all tiles to locate the spawn point for enemies
    // @param mapGrid - 2D array representing the map tiles
    // @return Point containing the starting column and row
    private Point findRoadStartPosition(int[][] mapGrid) {
        int roadRow = -1;
        int leftmostRoadColumn = Integer.MAX_VALUE;

        // Find the leftmost road tile (value 0) in the map
        for (int row = 0; row < mapGrid.length; row++) {
            for (int col = 0; col < mapGrid[row].length; col++) {
                if (mapGrid[row][col] == 0 && col < leftmostRoadColumn) {
                    leftmostRoadColumn = col;
                    roadRow = row;
                }
            }
        }

        return new Point(leftmostRoadColumn, roadRow);
    }

    // Creates a path along the road from the starting position
    // Builds a sequence of points following the road tiles from left to right
    // @param mapGrid - 2D array representing the map tiles
    // @param tileSize - size of each tile in pixels
    // @param roadStart - starting position containing column and row
    private void createPathFromRoadStart(int[][] mapGrid, int tileSize, Point roadStart) {
        int roadRow = roadStart.y;
        int startColumn = roadStart.x;

        // Build path along the road from left to right
        for (int col = startColumn; col < mapGrid[roadRow].length; col++) {
            if (mapGrid[roadRow][col] == 0) {
                movementPath.add(new Point(col * tileSize, roadRow * tileSize + Constants.Entities.ENEMY_Y_OFFSET));
            }
        }
    }

    // Sets the enemy's initial position to the first point in the path
    // Places the enemy at the spawn location to begin movement
    private void initializeStartingPosition() {
        Point startingPoint = movementPath.get(0);
        positionX = startingPoint.x;
        positionY = startingPoint.y;
    }

    // Main update method that handles enemy behavior each frame
    // Processes collision detection, combat, movement, and state transitions
    public void update() {
        if (isDead) {
            handleDeath();
            return;
        }

        if (checkForTankCollision()) {
            return;
        }

        if (checkForMagicCollision()) {
            return;
        }

        if (isAttacking) {
            processAttack();
            return;
        }

        moveAlongPath();
        checkForHouseCollision();
        checkIfReachedEnd();
    }

    // Handles enemy death
    // Enemies die when they reach the end of their path (no coin reward)
    // or when they are killed by external means (with coin reward)
    private void handleDeath() {
        // Death handling is now passive - enemies are simply removed when dead
        // No automatic coin rewards since tanks don't kill enemies
    }

    // Checks if enemy has reached the end of the path
    // Marks enemy as dead if it completes the path without being killed
    private void checkIfReachedEnd() {
        if (currentPathIndex >= movementPath.size()) {
            isDead = true;
        }
    }

    // Checks for collisions with tanks and initiates combat
    // Scans all tanks for intersection and starts attacking if collision detected
    // @return true if collision occurred and combat started, false otherwise
    private boolean checkForTankCollision() {
        for (Tank tank : gameMap.getTanks()) {
            if (!tank.isDead() && getBounds().intersects(tank.getBounds())) {
                positionForAttack(tank);
                startAttacking(tank);
                return true;
            }
        }
        return false;
    }

    // Checks for collisions with magic towers and initiates combat
    // Scans all magic towers for intersection and starts attacking if collision
    // detected
    // @return true if collision occurred and combat started, false otherwise
    private boolean checkForMagicCollision() {
        for (Magic magic : gameMap.getMagicTowers()) {
            if (!magic.isDead() && getBounds().intersects(magic.getBounds())) {
                positionForAttackMagic(magic);
                startAttacking(magic);
                return true;
            }
        }
        return false;
    }

    // Positions the enemy for attacking a tank
    // Places enemy to the left of the tank with unique offset to prevent overlap
    // @param tank - the tank being attacked
    private void positionForAttack(Tank tank) {
        Rectangle tankBounds = tank.getBounds();
        positionX = tankBounds.x - Constants.Entities.ENEMY_SIZE - attackPositionOffset;
    }

    // Positions the enemy for attacking a magic tower
    // Places enemy to the left of the magic tower with unique offset to prevent
    // overlap
    // @param magic - the magic tower being attacked
    private void positionForAttackMagic(Magic magic) {
        Rectangle magicBounds = magic.getBounds();
        positionX = magicBounds.x - Constants.Entities.ENEMY_SIZE - attackPositionOffset;
    }

    // Initiates attack mode on a target
    // Sets combat state and locks position to prevent movement during attack
    // @param target - the object being attacked (Tank or House)
    private void startAttacking(Object target) {
        currentAttackTarget = target;
        isAttacking = true;
        isPositionLocked = true;
    }

    // Processes attack timing and execution
    // Manages attack cooldown and triggers damage when timer expires
    private void processAttack() {
        attackTimer++;

        if (attackTimer >= Constants.Entities.ENEMY_ATTACK_COOLDOWN_FRAMES) {
            executeAttack();
            attackTimer = 0;
        }
    }

    // Executes an attack on the current target
    // Applies damage to tanks, magic towers, or house, handles target death
    private void executeAttack() {
        if (currentAttackTarget instanceof Tank) {
            Tank tank = (Tank) currentAttackTarget;

            if (tank.isDead()) {
                stopAttacking();
                return;
            }

            tank.damage(Constants.Entities.ENEMY_ATTACK_DAMAGE);
        } else if (currentAttackTarget instanceof Magic) {
            Magic magic = (Magic) currentAttackTarget;

            if (magic.isDead()) {
                stopAttacking();
                return;
            }

            magic.damage(Constants.Entities.ENEMY_ATTACK_DAMAGE);
        } else if (currentAttackTarget instanceof House) {
            targetHouse.damage(Constants.Entities.ENEMY_ATTACK_DAMAGE);
        }
    }

    // Moves the enemy along its predefined path
    // Advances to next waypoint when current target is reached
    private void moveAlongPath() {
        if (currentPathIndex < movementPath.size()) {
            Point targetPoint = movementPath.get(currentPathIndex);

            if (moveTowardsPoint(targetPoint)) {
                currentPathIndex++;
            }
        }
    }

    // Moves the enemy towards a specific point at constant speed
    // Uses vector math to maintain consistent movement speed
    // @param targetPoint - destination point to move towards
    // @return true if target point was reached, false if still moving
    private boolean moveTowardsPoint(Point targetPoint) {
        double deltaX = targetPoint.x - positionX;
        double deltaY = targetPoint.y - positionY;
        double distanceToTarget = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distanceToTarget > Constants.Entities.ENEMY_SPEED) {
            // Move towards target at constant speed
            positionX += deltaX / distanceToTarget * Constants.Entities.ENEMY_SPEED;
            positionY += deltaY / distanceToTarget * Constants.Entities.ENEMY_SPEED;
            return false;
        } else {
            // Reached target point
            positionX = targetPoint.x;
            positionY = targetPoint.y;
            return true;
        }
    }

    // Checks for collision with the house and initiates attack
    // Only triggers if enemy is not already position-locked from tank combat
    private void checkForHouseCollision() {
        if (!isPositionLocked && getBounds().intersects(targetHouse.getBounds())) {
            Rectangle houseBounds = targetHouse.getBounds();
            positionX = houseBounds.x - Constants.Entities.ENEMY_SIZE - attackPositionOffset;

            isPositionLocked = true;
            startAttacking(targetHouse);
        }
    }

    // Stops attacking and resets combat state
    // Allows enemy to resume movement after target is destroyed
    private void stopAttacking() {
        isAttacking = false;
        isPositionLocked = false;
        currentAttackTarget = null;
    }

    // Applies damage to the enemy and reduces its health
    // Marks enemy as dead if health reaches zero
    // @param damage - amount of damage to apply
    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            isDead = true;
        }
    }

    // Gets the current health of the enemy
    // @return current health value
    public int getCurrentHealth() {
        return currentHealth;
    }

    // Gets the maximum health of the enemy
    // @return maximum health value
    public int getMaxHealth() {
        return Constants.Entities.ENEMY_INITIAL_HEALTH;
    }

    // Marks the enemy as dead
    // Used when enemy is killed by external forces (not reaching end)
    public void kill() {
        isDead = true;
    }

    // Checks if the enemy is dead
    // Used by game systems to determine if enemy should be removed
    // @return true if enemy is dead, false otherwise
    public boolean isDead() {
        return isDead;
    }

    // Renders the enemy to the screen
    // Draws the enemy image at its current position
    // @param graphics - Graphics context for drawing
    public void draw(Graphics graphics) {
        if (!isDead) {
            graphics.drawImage(enemyImage, (int) positionX, (int) positionY,
                    Constants.Entities.ENEMY_SIZE, Constants.Entities.ENEMY_SIZE, null);
            drawHealthBar(graphics);
        }
    }

    // Draws a health bar above the enemy to show current health status
    // @param graphics - Graphics context for drawing
    private void drawHealthBar(Graphics graphics) {
        if (currentHealth < Constants.Entities.ENEMY_INITIAL_HEALTH) {
            int barWidth = Constants.Entities.ENEMY_SIZE;
            int barHeight = 4;
            int barX = (int) positionX;
            int barY = (int) positionY - 8;

            // Background (red)
            graphics.setColor(Color.RED);
            graphics.fillRect(barX, barY, barWidth, barHeight);

            // Health (green)
            graphics.setColor(Color.GREEN);
            double healthPercentage = (double) currentHealth / Constants.Entities.ENEMY_INITIAL_HEALTH;
            int healthWidth = (int) (barWidth * healthPercentage);
            graphics.fillRect(barX, barY, healthWidth, barHeight);

            // Border
            graphics.setColor(Color.BLACK);
            graphics.drawRect(barX, barY, barWidth, barHeight);
        }
    }

    // Gets the collision bounds for this enemy
    // Used for collision detection with tanks and house
    // @return Rectangle representing the enemy's bounds
    public Rectangle getBounds() {
        return new Rectangle((int) positionX, (int) positionY,
                Constants.Entities.ENEMY_SIZE, Constants.Entities.ENEMY_SIZE);
    }

    // Resets the enemy count for new wave
    // Call this at the start of each wave to reset attack positions
    public static void resetEnemyCount() {
        totalEnemyCount = 0;
    }
}
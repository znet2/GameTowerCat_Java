package com.towerdefense.entities.enemies;

import com.towerdefense.world.Map;
import com.towerdefense.entities.defensive.House;
import com.towerdefense.entities.defensive.Tank;
import com.towerdefense.entities.defensive.Magic;
import com.towerdefense.entities.defensive.Archer;
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
    private final CoinManager coinManager;

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

    // Constructor that creates an enemy and sets up its path
    // Initializes movement path, position, and assigns unique attack offset
    // @param gameMap - reference to the game map for pathfinding and collision
    // @param coinManager - reference to coin system for rewarding kills
    public Enemy(Map gameMap, CoinManager coinManager) {
        this.gameMap = gameMap;
        this.targetHouse = gameMap.getHouse();
        this.coinManager = coinManager;

        this.enemyImage = new ImageIcon(Constants.Paths.ENEMY_IMAGE).getImage();

        // Calculate unique attack position offset for this enemy
        // Counter is reset at the start of each wave for consistent positioning
        this.attackPositionOffset = totalEnemyCount * Constants.Entities.ENEMY_POSITION_OFFSET_MULTIPLIER;
        totalEnemyCount++;

        buildMovementPath();
        initializeStartingPosition();
    }

    // Builds the movement path from the leftmost road tile to the house
    // Uses BFS pathfinding to follow road tiles in any direction
    private void buildMovementPath() {
        int[][] mapGrid = gameMap.getRawMap();
        int tileSize = gameMap.getTileSize();

        Point roadStart = findRoadStartPosition(mapGrid);
        Point housePosition = findHousePosition(mapGrid);
        createPathUsingBFS(mapGrid, tileSize, roadStart, housePosition);
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

    // Finds the house position on the map (rightmost road tile in spawn row)
    // @param mapGrid - 2D array representing the map tiles
    // @return Point containing the rightmost road tile in spawn row
    private Point findHousePosition(int[][] mapGrid) {
        // Find spawn row first
        Point spawn = findRoadStartPosition(mapGrid);
        int spawnRow = spawn.y;
        
        // Find rightmost road tile in the same row
        int rightmostCol = -1;
        for (int col = mapGrid[spawnRow].length - 1; col >= 0; col--) {
            if (mapGrid[spawnRow][col] == 0) {
                rightmostCol = col;
                break;
            }
        }
        
        if (rightmostCol == -1) {
            // Fallback: find any road tile closest to house
            int houseCol = Constants.Map.HOUSE_COLUMN;
            int houseRow = Constants.Map.HOUSE_ROW;
            Point closestRoad = null;
            double minDistance = Double.MAX_VALUE;
            
            for (int row = 0; row < mapGrid.length; row++) {
                for (int col = 0; col < mapGrid[row].length; col++) {
                    if (mapGrid[row][col] == 0) {
                        double distance = Math.sqrt(Math.pow(col - houseCol, 2) + Math.pow(row - houseRow, 2));
                        if (distance < minDistance) {
                            minDistance = distance;
                            closestRoad = new Point(col, row);
                        }
                    }
                }
            }
            return closestRoad != null ? closestRoad : new Point(houseCol, houseRow);
        }
        
        return new Point(rightmostCol, spawnRow);
    }

    // Creates a path using BFS algorithm to follow road tiles
    // Finds the shortest path along road tiles from start to house
    // @param mapGrid - 2D array representing the map tiles
    // @param tileSize - size of each tile in pixels
    // @param start - starting position
    // @param goal - goal position (closest road to house)
    private void createPathUsingBFS(int[][] mapGrid, int tileSize, Point start, Point goal) {
        ArrayList<Point> queue = new ArrayList<>();
        boolean[][] visited = new boolean[mapGrid.length][mapGrid[0].length];
        Point[][] parent = new Point[mapGrid.length][mapGrid[0].length];

        queue.add(start);
        visited[start.y][start.x] = true;

        // BFS to find path
        while (!queue.isEmpty()) {
            Point current = queue.remove(0);

            // Check if reached goal
            if (current.x == goal.x && current.y == goal.y) {
                reconstructPath(parent, start, current, tileSize);
                return;
            }

            // Check all 4 directions (up, down, left, right)
            int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
            for (int[] dir : directions) {
                int newCol = current.x + dir[0];
                int newRow = current.y + dir[1];

                // Check bounds and if it's a road tile
                if (newRow >= 0 && newRow < mapGrid.length && 
                    newCol >= 0 && newCol < mapGrid[0].length &&
                    !visited[newRow][newCol] && mapGrid[newRow][newCol] == 0) {
                    
                    queue.add(new Point(newCol, newRow));
                    visited[newRow][newCol] = true;
                    parent[newRow][newCol] = current;
                }
            }
        }

        // Fallback: if no path found, create simple left-to-right path
        createFallbackPath(mapGrid, tileSize, start);
    }

    // Reconstructs the path from BFS parent array
    // @param parent - array storing parent nodes
    // @param start - starting position
    // @param goal - goal position
    // @param tileSize - size of each tile in pixels
    private void reconstructPath(Point[][] parent, Point start, Point goal, int tileSize) {
        ArrayList<Point> path = new ArrayList<>();
        Point current = goal;

        // Trace back from goal to start
        while (current != null && !current.equals(start)) {
            // Add center of tile for smoother movement
            int centerX = current.x * tileSize + tileSize / 2;
            int centerY = current.y * tileSize + tileSize / 2;
            path.add(0, new Point(centerX, centerY));
            current = parent[current.y][current.x];
        }

        // Add start position (center of tile)
        int startCenterX = start.x * tileSize + tileSize / 2;
        int startCenterY = start.y * tileSize + tileSize / 2;
        path.add(0, new Point(startCenterX, startCenterY));

        movementPath.addAll(path);
    }

    // Creates a fallback path if BFS fails
    // @param mapGrid - 2D array representing the map tiles
    // @param tileSize - size of each tile in pixels
    // @param start - starting position
    private void createFallbackPath(int[][] mapGrid, int tileSize, Point start) {
        int roadRow = start.y;
        int startColumn = start.x;

        // Build path along the road from left to right
        for (int col = startColumn; col < mapGrid[roadRow].length; col++) {
            if (mapGrid[roadRow][col] == 0) {
                movementPath.add(new Point(col * tileSize + tileSize / 2, roadRow * tileSize + tileSize / 2));
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
            return;
        }

        if (checkForDefensiveCollision()) {
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

    // Checks if enemy has reached the end of the path
    // Marks enemy as dead if it completes the path without being killed
    private void checkIfReachedEnd() {
        if (currentPathIndex >= movementPath.size()) {
            isDead = true;
        }
    }

    // Checks for collisions with all defensive units (Tank, Magic, Archer)
    // Scans all defensive units for intersection and starts attacking if collision detected
    // @return true if collision occurred and combat started, false otherwise
    private boolean checkForDefensiveCollision() {
        // Check tanks
        for (Tank tank : gameMap.getTanks()) {
            if (!tank.isDead() && getBounds().intersects(tank.getBounds())) {
                positionForAttack(tank);
                startAttacking(tank);
                return true;
            }
        }

        // Check magic towers
        for (Magic magic : gameMap.getMagicTowers()) {
            if (!magic.isDead() && getBounds().intersects(magic.getBounds())) {
                positionForAttack(magic);
                startAttacking(magic);
                return true;
            }
        }

        // Check archer towers
        for (Archer archer : gameMap.getArcherTowers()) {
            if (!archer.isDead() && getBounds().intersects(archer.getBounds())) {
                positionForAttack(archer);
                startAttacking(archer);
                return true;
            }
        }

        return false;
    }

    // Positions the enemy for attacking a defensive unit
    // Places enemy to the left of the target with unique offset to prevent overlap
    // @param target - the defensive unit being attacked (Tank, Magic, or Archer)
    private void positionForAttack(Object target) {
        Rectangle targetBounds = null;
        
        if (target instanceof Tank) {
            targetBounds = ((Tank) target).getBounds();
        } else if (target instanceof Magic) {
            targetBounds = ((Magic) target).getBounds();
        } else if (target instanceof Archer) {
            targetBounds = ((Archer) target).getBounds();
        }
        
        if (targetBounds != null) {
            positionX = targetBounds.x - Constants.Entities.ENEMY_SIZE - attackPositionOffset;
        }
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
    // Applies damage to defensive units or house, handles target death
    private void executeAttack() {
        // Check if target is a defensive unit (Tank, Magic, or Archer)
        if (currentAttackTarget instanceof Tank || 
            currentAttackTarget instanceof Magic || 
            currentAttackTarget instanceof Archer) {
            
            // Cast to Defensive interface to use common methods
            Object target = currentAttackTarget;
            boolean targetDead = false;
            
            if (target instanceof Tank) {
                Tank tank = (Tank) target;
                targetDead = tank.isDead();
                if (!targetDead) {
                    tank.damage(Constants.Entities.ENEMY_ATTACK_DAMAGE);
                }
            } else if (target instanceof Magic) {
                Magic magic = (Magic) target;
                targetDead = magic.isDead();
                if (!targetDead) {
                    magic.damage(Constants.Entities.ENEMY_ATTACK_DAMAGE);
                }
            } else if (target instanceof Archer) {
                Archer archer = (Archer) target;
                targetDead = archer.isDead();
                if (!targetDead) {
                    archer.damage(Constants.Entities.ENEMY_ATTACK_DAMAGE);
                }
            }
            
            if (targetDead) {
                stopAttacking();
            }
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
    // Marks enemy as dead if health reaches zero and awards coins
    // @param damage - amount of damage to apply
    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            isDead = true;
            // Award coins for killing the enemy
            coinManager.awardCoinsForEnemyKill();
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
    // Draws the enemy image at its current position with offset
    // @param graphics - Graphics context for drawing
    public void draw(Graphics graphics) {
        if (!isDead) {
            graphics.drawImage(enemyImage, 
                    (int) positionX + Constants.Entities.ENEMY_X_OFFSET, 
                    (int) positionY + Constants.Entities.ENEMY_Y_OFFSET,
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
            int barX = (int) positionX + Constants.Entities.ENEMY_X_OFFSET;
            int barY = (int) positionY + Constants.Entities.ENEMY_Y_OFFSET - 8;

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
    // @return Rectangle representing the enemy's bounds (with offset applied)
    public Rectangle getBounds() {
        return new Rectangle(
                (int) positionX + Constants.Entities.ENEMY_X_OFFSET, 
                (int) positionY + Constants.Entities.ENEMY_Y_OFFSET,
                Constants.Entities.ENEMY_SIZE, 
                Constants.Entities.ENEMY_SIZE);
    }

    // Resets the enemy count for new wave
    // Call this at the start of each wave to reset attack positions
    public static void resetEnemyCount() {
        totalEnemyCount = 0;
    }
}
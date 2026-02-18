package com.towerdefense.entities.enemies;

import com.towerdefense.world.Map;
import com.towerdefense.entities.base.Defensive;
import com.towerdefense.entities.defensive.House;
import com.towerdefense.entities.defensive.Tank;
import com.towerdefense.entities.defensive.Magic;
import com.towerdefense.entities.defensive.Archer;
import com.towerdefense.managers.CoinManager;
import com.towerdefense.utils.Constants;
import com.towerdefense.utils.MathUtils;
import java.awt.*;
import java.util.ArrayList;

/**
 * Base class for all enemy types in the game.
 * Provides common functionality for pathfinding, movement, combat, and rendering.
 */
public abstract class BaseEnemy {

    // Position and movement
    protected double positionX, positionY;
    protected final ArrayList<Point> movementPath = new ArrayList<>();
    protected int currentPathIndex = 0;

    // Game references
    protected final Map gameMap;
    protected final House targetHouse;
    protected final CoinManager coinManager;
    protected ArrayList<BaseEnemy> allEnemies; // Reference to all enemies for collision avoidance

    // Combat state
    protected boolean isAttacking = false;
    protected int attackTimer = 0;
    protected Object currentAttackTarget = null;

    // Enemy state
    protected boolean isDead = false;
    protected int currentHealth;

    // Constructor for base enemy
    protected BaseEnemy(Map gameMap, CoinManager coinManager, int initialHealth) {
        this.gameMap = gameMap;
        this.targetHouse = gameMap.getHouse();
        this.coinManager = coinManager;
        this.currentHealth = initialHealth;

        buildMovementPath();
        initializeStartingPosition();
    }

    // Abstract methods that subclasses must implement
    protected abstract int getMaxHealth();
    protected abstract int getAttackDamage();
    protected abstract int getAttackCooldown();
    protected abstract double getSpeed();
    protected abstract int getSize();
    protected abstract int getXOffset();
    protected abstract int getYOffset();
    protected abstract Image getImage();
    protected abstract int getCoinReward();

    // Builds the movement path from the leftmost road tile to the house
    protected void buildMovementPath() {
        int[][] mapGrid = gameMap.getRawMap();
        int tileSize = gameMap.getTileSize();

        Point roadStart = findRoadStartPosition(mapGrid);
        Point housePosition = findHousePosition(mapGrid);
        createPathUsingBFS(mapGrid, tileSize, roadStart, housePosition);
    }

    // Finds the leftmost road tile in the map as the starting position
    protected Point findRoadStartPosition(int[][] mapGrid) {
        int roadRow = -1;
        int leftmostRoadColumn = Integer.MAX_VALUE;

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

    // Finds the house position on the map
    protected Point findHousePosition(int[][] mapGrid) {
        Point spawn = findRoadStartPosition(mapGrid);
        int spawnRow = spawn.y;
        
        int rightmostCol = -1;
        for (int col = mapGrid[spawnRow].length - 1; col >= 0; col--) {
            if (mapGrid[spawnRow][col] == 0) {
                rightmostCol = col;
                break;
            }
        }
        
        if (rightmostCol == -1) {
            int houseCol = Constants.Map.HOUSE_COLUMN;
            int houseRow = Constants.Map.HOUSE_ROW;
            Point closestRoad = null;
            double minDistance = Double.MAX_VALUE;
            
            for (int row = 0; row < mapGrid.length; row++) {
                for (int col = 0; col < mapGrid[row].length; col++) {
                    if (mapGrid[row][col] == 0) {
                        double distance = MathUtils.calculateDistance(col, row, houseCol, houseRow);
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

    // Creates a path using BFS algorithm
    protected void createPathUsingBFS(int[][] mapGrid, int tileSize, Point start, Point goal) {
        ArrayList<Point> queue = new ArrayList<>();
        boolean[][] visited = new boolean[mapGrid.length][mapGrid[0].length];
        Point[][] parent = new Point[mapGrid.length][mapGrid[0].length];

        queue.add(start);
        visited[start.y][start.x] = true;

        while (!queue.isEmpty()) {
            Point current = queue.remove(0);

            if (current.x == goal.x && current.y == goal.y) {
                reconstructPath(parent, start, current, tileSize);
                return;
            }

            int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
            for (int[] dir : directions) {
                int newCol = current.x + dir[0];
                int newRow = current.y + dir[1];

                if (newRow >= 0 && newRow < mapGrid.length && 
                    newCol >= 0 && newCol < mapGrid[0].length &&
                    !visited[newRow][newCol] && mapGrid[newRow][newCol] == 0) {
                    
                    queue.add(new Point(newCol, newRow));
                    visited[newRow][newCol] = true;
                    parent[newRow][newCol] = current;
                }
            }
        }

        createFallbackPath(mapGrid, tileSize, start);
    }

    // Reconstructs the path from BFS parent array
    protected void reconstructPath(Point[][] parent, Point start, Point goal, int tileSize) {
        ArrayList<Point> path = new ArrayList<>();
        Point current = goal;

        while (current != null && !current.equals(start)) {
            int centerX = current.x * tileSize + tileSize / 2;
            int centerY = current.y * tileSize + tileSize / 2;
            path.add(0, new Point(centerX, centerY));
            current = parent[current.y][current.x];
        }

        int startCenterX = start.x * tileSize + tileSize / 2;
        int startCenterY = start.y * tileSize + tileSize / 2;
        path.add(0, new Point(startCenterX, startCenterY));

        movementPath.addAll(path);
    }

    // Creates a fallback path if BFS fails
    protected void createFallbackPath(int[][] mapGrid, int tileSize, Point start) {
        int roadRow = start.y;
        int startColumn = start.x;

        for (int col = startColumn; col < mapGrid[roadRow].length; col++) {
            if (mapGrid[roadRow][col] == 0) {
                movementPath.add(new Point(col * tileSize + tileSize / 2, roadRow * tileSize + tileSize / 2));
            }
        }
    }

    // Sets the enemy's initial position
    protected void initializeStartingPosition() {
        Point startingPoint = movementPath.get(0);
        positionX = startingPoint.x;
        positionY = startingPoint.y;
    }

    // Main update method
    public void update() {
        if (isDead) {
            return;
        }

        // If already attacking, process the attack
        if (isAttacking) {
            processAttack();
            return;
        }

        // Check for new collisions only if not attacking
        if (checkForDefensiveCollision()) {
            return;
        }

        // Move along path if not attacking
        moveAlongPath();
        checkForHouseCollision();
        checkIfReachedEnd();
    }

    // Checks if enemy has reached the end of the path
    protected void checkIfReachedEnd() {
        if (currentPathIndex >= movementPath.size()) {
            isDead = true;
        }
    }

    // Checks for collisions with all defensive units
    protected boolean checkForDefensiveCollision() {
        // Check Tank units
        for (Tank tank : gameMap.getTanks()) {
            if (checkAndAttackUnit(tank)) return true;
        }
        
        // Check Magic towers
        for (Magic magic : gameMap.getMagicTowers()) {
            if (checkAndAttackUnit(magic)) return true;
        }
        
        // Check Archer towers
        for (Archer archer : gameMap.getArcherTowers()) {
            if (checkAndAttackUnit(archer)) return true;
        }
        
        return false;
    }

    // Helper method to check collision with a single defensive unit
    private boolean checkAndAttackUnit(Defensive unit) {
        if (unit.isDead()) {
            return false;
        }
        
        Rectangle unitBounds = getBoundsForDefensive(unit);
        if (unitBounds != null && getBounds().intersects(unitBounds)) {
            startAttacking(unit);
            return true;
        }
        return false;
    }

    // Gets bounds for any defensive unit
    private Rectangle getBoundsForDefensive(Defensive unit) {
        if (unit instanceof Tank) {
            return ((Tank) unit).getBounds();
        } else if (unit instanceof Magic) {
            return ((Magic) unit).getBounds();
        } else if (unit instanceof Archer) {
            return ((Archer) unit).getBounds();
        }
        return null;
    }

    // Initiates attack mode on a target
    protected void startAttacking(Object target) {
        currentAttackTarget = target;
        isAttacking = true;
    }

    // Processes attack timing and execution
    protected void processAttack() {
        attackTimer++;

        if (attackTimer >= getAttackCooldown()) {
            executeAttack();
            attackTimer = 0;
        }
    }

    // Executes an attack on the current target
    protected void executeAttack() {
        if (currentAttackTarget == null) {
            return;
        }

        // Attack any defensive target (Tank, Magic, Archer, House)
        if (currentAttackTarget instanceof Defensive) {
            Defensive defensiveUnit = (Defensive) currentAttackTarget;
            
            if (defensiveUnit.isDead()) {
                stopAttacking();
            } else {
                defensiveUnit.takeDamage(getAttackDamage());
            }
        }
    }

    // Moves the enemy along its predefined path
    protected void moveAlongPath() {
        if (currentPathIndex < movementPath.size()) {
            Point targetPoint = movementPath.get(currentPathIndex);

            // Check if there's another enemy blocking the path
            if (!isPathBlocked()) {
                if (moveTowardsPoint(targetPoint)) {
                    currentPathIndex++;
                }
            }
            // If blocked, enemy will wait (not move this frame)
        }
    }

    // Checks if another enemy is blocking the path ahead
    protected boolean isPathBlocked() {
        if (allEnemies == null || currentPathIndex >= movementPath.size()) {
            return false;
        }

        Rectangle myBounds = getBounds();
        Point nextTarget = movementPath.get(currentPathIndex);
        double minSafeDistance = getSize() * Constants.Entities.ENEMY_SPACING_MULTIPLIER;

        for (BaseEnemy other : allEnemies) {
            if (shouldSkipEnemy(other)) {
                continue;
            }

            double distance = calculateDistance(myBounds, other.getBounds());

            if (distance < minSafeDistance && isEnemyAhead(other, nextTarget)) {
                return true;
            }
        }

        return false;
    }

    // Checks if enemy should be skipped in collision detection
    private boolean shouldSkipEnemy(BaseEnemy other) {
        return other == this || other.isDead() || other.isAttacking;
    }

    // Calculates distance between two rectangles
    private double calculateDistance(Rectangle bounds1, Rectangle bounds2) {
        return Math.sqrt(
            Math.pow(bounds2.getCenterX() - bounds1.getCenterX(), 2) +
            Math.pow(bounds2.getCenterY() - bounds1.getCenterY(), 2)
        );
    }

    // Checks if other enemy is ahead on the path
    private boolean isEnemyAhead(BaseEnemy other, Point nextTarget) {
        if (other.currentPathIndex > this.currentPathIndex) {
            return true;
        } else if (other.currentPathIndex == this.currentPathIndex) {
            double myDistToTarget = calculateDistanceToPoint(positionX, positionY, nextTarget);
            double otherDistToTarget = calculateDistanceToPoint(other.positionX, other.positionY, nextTarget);
            return otherDistToTarget < myDistToTarget;
        }
        return false;
    }

    // Calculates distance from position to point
    private double calculateDistanceToPoint(double x, double y, Point target) {
        return Math.sqrt(
            Math.pow(target.x - x, 2) +
            Math.pow(target.y - y, 2)
        );
    }

    // Moves the enemy towards a specific point at constant speed
    protected boolean moveTowardsPoint(Point targetPoint) {
        double deltaX = targetPoint.x - positionX;
        double deltaY = targetPoint.y - positionY;
        double distanceToTarget = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distanceToTarget > getSpeed()) {
            positionX += deltaX / distanceToTarget * getSpeed();
            positionY += deltaY / distanceToTarget * getSpeed();
            return false;
        } else {
            positionX = targetPoint.x;
            positionY = targetPoint.y;
            return true;
        }
    }

    // Checks for collision with the house and initiates attack
    protected void checkForHouseCollision() {
        if (!isAttacking && getBounds().intersects(targetHouse.getBounds())) {
            startAttacking(targetHouse);
        }
    }

    // Stops attacking and resets combat state
    protected void stopAttacking() {
        isAttacking = false;
        currentAttackTarget = null;
    }

    // Applies damage to the enemy
    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            isDead = true;
            coinManager.addCoins(getCoinReward());
        }
    }

    // Gets the current health of the enemy
    public int getCurrentHealth() {
        return currentHealth;
    }

    // Marks the enemy as dead
    public void kill() {
        isDead = true;
    }

    // Checks if the enemy is dead
    public boolean isDead() {
        return isDead;
    }

    // Sets the reference to all enemies for collision avoidance
    public void setAllEnemies(ArrayList<BaseEnemy> enemies) {
        this.allEnemies = enemies;
    }

    // Renders the enemy to the screen
    public void draw(Graphics graphics) {
        if (!isDead) {
            graphics.drawImage(getImage(), 
                    (int) positionX + getXOffset(), 
                    (int) positionY + getYOffset(),
                    getSize(), getSize(), null);
            drawHealthBar(graphics);
        }
    }

    // Draws a health bar above the enemy
    protected void drawHealthBar(Graphics graphics) {
        if (currentHealth < getMaxHealth()) {
            int barWidth = getSize();
            int barHeight = 4;
            int barX = (int) positionX + getXOffset();
            int barY = (int) positionY + getYOffset() - 8;

            graphics.setColor(Color.RED);
            graphics.fillRect(barX, barY, barWidth, barHeight);

            graphics.setColor(Color.GREEN);
            double healthPercentage = (double) currentHealth / getMaxHealth();
            int healthWidth = (int) (barWidth * healthPercentage);
            graphics.fillRect(barX, barY, healthWidth, barHeight);

            graphics.setColor(Color.BLACK);
            graphics.drawRect(barX, barY, barWidth, barHeight);
        }
    }

    // Gets the collision bounds for this enemy
    public Rectangle getBounds() {
        return new Rectangle(
                (int) positionX + getXOffset(), 
                (int) positionY + getYOffset(),
                getSize(), 
                getSize());
    }

}

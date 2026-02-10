import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Represents an enemy unit that moves along a path and attacks tanks and the house.
 * Enemies follow predefined routes, engage in combat, and have AI behavior.
 */
public class Enemy {

    // Visual properties
    private final Image enemyImage;
    private final int enemySize = 64;

    // Position and movement
    private double positionX, positionY;
    private final double movementSpeed = 1.2;

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

    // Position management to prevent enemy overlap
    private static int totalEnemyCount = 0;
    private final int attackPositionOffset;
    private boolean isPositionLocked = false;

    // Constants
    private static final int ATTACK_DAMAGE = 5;
    private static final int ATTACK_COOLDOWN_FRAMES = 60;
    private static final int POSITION_OFFSET_MULTIPLIER = 12;

    private static final int Y_OFFSET = -25; 

    // Constructor that creates an enemy and sets up its path
    // Initializes movement path, position, and assigns unique attack offset
    // @param gameMap - reference to the game map for pathfinding and collision
    public Enemy(Map gameMap) {
        this.gameMap = gameMap;
        this.targetHouse = gameMap.getHouse();

        this.enemyImage = new ImageIcon("image\\catEnemy.png").getImage();

        // Calculate unique attack position offset for this enemy
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
                movementPath.add(new Point(col * tileSize, roadRow * tileSize + Y_OFFSET));
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
        if (checkForTankCollision()) {
            return;
        }

        if (isAttacking) {
            processAttack();
            return;
        }

        moveAlongPath();
        checkForHouseCollision();
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

    // Positions the enemy for attacking a tank
    // Places enemy to the left of the tank with unique offset to prevent overlap
    // @param tank - the tank being attacked
    private void positionForAttack(Tank tank) {
        Rectangle tankBounds = tank.getBounds();
        positionX = tankBounds.x - enemySize - attackPositionOffset;
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

        if (attackTimer >= ATTACK_COOLDOWN_FRAMES) {
            executeAttack();
            attackTimer = 0;
        }
    }

    // Executes an attack on the current target
    // Applies damage to tanks or house, handles target death
    private void executeAttack() {
        if (currentAttackTarget instanceof Tank) {
            Tank tank = (Tank) currentAttackTarget;

            if (tank.isDead()) {
                stopAttacking();
                return;
            }

            tank.damage(ATTACK_DAMAGE);
        } else if (currentAttackTarget instanceof House) {
            targetHouse.damage(ATTACK_DAMAGE);
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

        if (distanceToTarget > movementSpeed) {
            // Move towards target at constant speed
            positionX += deltaX / distanceToTarget * movementSpeed;
            positionY += deltaY / distanceToTarget * movementSpeed;
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
            positionX = houseBounds.x - enemySize - attackPositionOffset;

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

    // Renders the enemy to the screen
    // Draws the enemy image at its current position
    // @param graphics - Graphics context for drawing
    public void draw(Graphics graphics) {
        graphics.drawImage(enemyImage, (int) positionX, (int) positionY, enemySize, enemySize, null);
    }

    // Gets the collision bounds for this enemy
    // Used for collision detection with tanks and house
    // @return Rectangle representing the enemy's bounds
    private Rectangle getBounds() {
        return new Rectangle((int) positionX, (int) positionY, enemySize, enemySize);
    }
}

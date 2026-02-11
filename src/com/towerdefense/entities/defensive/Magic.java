package com.towerdefense.entities.defensive;

import com.towerdefense.entities.base.GameObject;
import com.towerdefense.entities.base.Defensive;
import com.towerdefense.entities.base.Collidable;
import com.towerdefense.entities.enemies.Enemy;
import com.towerdefense.utils.Constants;
import com.towerdefense.utils.MathUtils;
import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a magic tower that can attack enemies from range.
 * Magic towers lock onto a single enemy target and perform 4 normal attacks
 * followed by a powerful spell on the 5th attack, then reset the counter.
 * Magic towers are defensive units that can be attacked by enemies.
 */
public class Magic extends GameObject implements Defensive, Collidable {

    // Visual positioning
    private static final int Y_OFFSET = -30;
    private static final int MINIMUM_HEALTH = 0;

    // Current state
    private int currentHealth = Constants.Entities.MAGIC_INITIAL_HEALTH;

    // Combat state
    private Enemy lockedTarget = null;
    private int attackTimer = 0;
    private int attackCounter = 0; // Tracks number of attacks (0-4)

    // Reference to enemy list for target acquisition
    private ArrayList<Enemy> enemyList;

    // Constructor that creates a magic tower at the specified grid position
    // Magic towers occupy a 2x2 tile area and attack enemies from range
    // @param gridColumn - column position in the tile grid
    // @param gridRow - row position in the tile grid
    // @param tileSize - size of each tile in pixels
    // @param magicImage - visual representation of the magic tower
    // @param enemies - reference to the list of active enemies for targeting
    public Magic(int gridColumn, int gridRow, int tileSize, Image magicImage, ArrayList<Enemy> enemies) {
        super(gridColumn, gridRow, tileSize, 2, 2, magicImage);
        this.enemyList = enemies;
    }

    // Updates the magic tower each frame
    // Handles target acquisition, attack timing, and combat logic
    public void update() {
        // Update target if current target is invalid
        if (!isTargetValid()) {
            acquireNewTarget();
        }

        // If we have a valid target, attack it
        if (lockedTarget != null) {
            performAttack();
        }
    }

    // Checks if the current locked target is still valid
    // Target is invalid if it's dead or out of attack range
    // @return true if target is valid, false otherwise
    private boolean isTargetValid() {
        if (lockedTarget == null || lockedTarget.isDead()) {
            return false;
        }

        // Check if target is still in range
        double distance = calculateDistanceToTarget(lockedTarget);
        return distance <= Constants.Entities.MAGIC_ATTACK_RANGE;
    }

    // Acquires a new enemy target from the enemy list
    // Selects the closest enemy within attack range
    private void acquireNewTarget() {
        lockedTarget = null;
        double closestDistance = Double.MAX_VALUE;

        for (Enemy enemy : enemyList) {
            if (enemy.isDead()) {
                continue;
            }

            double distance = calculateDistanceToTarget(enemy);

            if (distance <= Constants.Entities.MAGIC_ATTACK_RANGE && distance < closestDistance) {
                closestDistance = distance;
                lockedTarget = enemy;
            }
        }
    }

    // Calculates the distance between this magic tower and an enemy
    // Uses the center points of both entities for accurate distance calculation
    // @param enemy - the enemy to calculate distance to
    // @return distance in pixels
    private double calculateDistanceToTarget(Enemy enemy) {
        // Get center point of magic tower
        int magicCenterX = positionX + objectWidth / 2;
        int magicCenterY = positionY + objectHeight / 2;

        // Get center point of enemy (assuming enemy has a getBounds method)
        Rectangle enemyBounds = getEnemyBounds(enemy);
        int enemyCenterX = enemyBounds.x + enemyBounds.width / 2;
        int enemyCenterY = enemyBounds.y + enemyBounds.height / 2;

        return MathUtils.calculateDistance(magicCenterX, magicCenterY, enemyCenterX, enemyCenterY);
    }

    // Gets the bounds of an enemy
    // @param enemy - the enemy to get bounds for
    // @return Rectangle representing enemy bounds
    private Rectangle getEnemyBounds(Enemy enemy) {
        return enemy.getBounds();
    }

    // Performs attack logic with cooldown and damage application
    // Handles both normal attacks and special spell attacks
    private void performAttack() {
        attackTimer++;

        if (attackTimer >= Constants.Entities.MAGIC_ATTACK_COOLDOWN_FRAMES) {
            executeAttack();
            attackTimer = 0;
        }
    }

    // Executes an attack on the locked target
    // Determines if it's a normal attack or special spell based on attack counter
    private void executeAttack() {
        if (lockedTarget == null || lockedTarget.isDead()) {
            return;
        }

        // Check if this is the 5th attack (counter = 4, since we count 0-4)
        if (attackCounter >= Constants.Entities.MAGIC_ATTACKS_BEFORE_SPELL) {
            castSpecialSpell();
            attackCounter = 0; // Reset counter after spell
        } else {
            performNormalAttack();
            attackCounter++; // Increment counter after normal attack
        }
    }

    // Performs a normal magic attack on the locked target
    // Deals standard magic damage
    private void performNormalAttack() {
        damageEnemy(lockedTarget, Constants.Entities.MAGIC_ATTACK_DAMAGE);
        System.out.println("Magic normal attack! Damage: " + Constants.Entities.MAGIC_ATTACK_DAMAGE +
                " (Attack " + (attackCounter + 1) + "/5)");
    }

    // Casts a powerful magic spell on the locked target
    // Deals increased damage compared to normal attacks
    private void castSpecialSpell() {
        damageEnemy(lockedTarget, Constants.Entities.MAGIC_SPELL_DAMAGE);
        System.out.println("Magic SPELL CAST! Damage: " + Constants.Entities.MAGIC_SPELL_DAMAGE + " ⚡");
    }

    // Applies damage to an enemy
    // Enemy will die and disappear when health reaches zero
    // @param enemy - the enemy to damage
    // @param damage - amount of damage to apply
    private void damageEnemy(Enemy enemy, int damage) {
        enemy.takeDamage(damage);
        System.out.println("  -> Enemy hit! HP: " + enemy.getCurrentHealth() + "/" + enemy.getMaxHealth());
    }

    // Implementation of Defensive interface
    @Override
    public void takeDamage(int damageAmount) {
        // Apply defense rating to reduce incoming damage
        int actualDamage = Math.max(1, damageAmount - Constants.Entities.MAGIC_DEFENSE_RATING);

        currentHealth -= actualDamage;
        if (currentHealth < MINIMUM_HEALTH) {
            currentHealth = MINIMUM_HEALTH;
        }

        System.out.println("Magic HP: " + currentHealth + " (absorbed " +
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
        return Constants.Entities.MAGIC_INITIAL_HEALTH;
    }

    @Override
    public int getDefenseRating() {
        return Constants.Entities.MAGIC_DEFENSE_RATING;
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
    // @return true if magic tower is dead, false otherwise
    public boolean isDead() {
        return isDestroyed();
    }

    // Legacy method for compatibility with existing collision system
    // Delegates to the Collidable interface method
    // @return Rectangle representing the magic tower's bounds
    public Rectangle getBounds() {
        return getCollisionBounds();
    }

    // Converts the magic tower's pixel X position to grid column
    // Used for grid-based positioning and placement validation
    // @param tileSize - size of each tile in pixels
    // @return grid column position
    public int getGridColumn(int tileSize) {
        return positionX / tileSize;
    }

    // Converts the magic tower's pixel Y position to grid row
    // Used for grid-based positioning and placement validation
    // @param tileSize - size of each tile in pixels
    // @return grid row position
    public int getGridRow(int tileSize) {
        return positionY / tileSize;
    }

    // Gets the health percentage for UI display
    // @return health as a percentage (0.0 to 1.0)
    public double getHealthPercentage() {
        return (double) currentHealth / Constants.Entities.MAGIC_INITIAL_HEALTH;
    }

    // Renders the magic tower to the screen
    // Draws the magic tower image at its position with visual offset
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

        // Draw health bar above magic tower
        drawHealthBar(graphics);

        // Draw attack range indicator (optional, for debugging)
        // drawAttackRange(graphics);

        // Draw targeting line to locked enemy (optional, for debugging)
        if (lockedTarget != null && !lockedTarget.isDead()) {
            drawTargetingLine(graphics);
        }
    }

    // Draws a health bar above the magic tower to show current health status
    // Provides visual feedback about the magic tower's defensive state
    // @param graphics - Graphics context for drawing
    private void drawHealthBar(Graphics graphics) {
        if (currentHealth < Constants.Entities.MAGIC_INITIAL_HEALTH) {
            int barWidth = objectWidth;
            int barHeight = 4;
            int barX = positionX;
            int barY = positionY + Y_OFFSET - 8;

            // Background (red)
            graphics.setColor(Color.RED);
            graphics.fillRect(barX, barY, barWidth, barHeight);

            // Health (cyan for magic)
            graphics.setColor(Color.CYAN);
            int healthWidth = (int) (barWidth * getHealthPercentage());
            graphics.fillRect(barX, barY, healthWidth, barHeight);

            // Border
            graphics.setColor(Color.BLACK);
            graphics.drawRect(barX, barY, barWidth, barHeight);
        }
    }

    // Draws a line from magic tower to its locked target
    // Visual indicator showing which enemy is being targeted
    // @param graphics - Graphics context for drawing
    private void drawTargetingLine(Graphics graphics) {
        Rectangle enemyBounds = getEnemyBounds(lockedTarget);

        int magicCenterX = positionX + objectWidth / 2;
        int magicCenterY = positionY + Y_OFFSET + objectHeight / 2;
        int enemyCenterX = enemyBounds.x + enemyBounds.width / 2;
        int enemyCenterY = enemyBounds.y + enemyBounds.height / 2;

        // Draw a cyan line to show targeting
        graphics.setColor(new Color(0, 255, 255, 100)); // Semi-transparent cyan
        graphics.drawLine(magicCenterX, magicCenterY, enemyCenterX, enemyCenterY);
    }

    // Draws the attack range circle (for debugging/visualization)
    // Shows the area where magic tower can acquire targets
    // @param graphics - Graphics context for drawing
    @SuppressWarnings("unused")
    private void drawAttackRange(Graphics graphics) {
        int centerX = positionX + objectWidth / 2;
        int centerY = positionY + Y_OFFSET + objectHeight / 2;
        int range = Constants.Entities.MAGIC_ATTACK_RANGE;

        graphics.setColor(new Color(0, 255, 255, 30)); // Very transparent cyan
        graphics.drawOval(centerX - range, centerY - range, range * 2, range * 2);
    }
}

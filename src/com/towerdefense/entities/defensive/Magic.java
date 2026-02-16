package com.towerdefense.entities.defensive;

import com.towerdefense.entities.base.GameObject;
import com.towerdefense.entities.base.Defensive;
import com.towerdefense.entities.base.Collidable;
import com.towerdefense.entities.enemies.Enemy;
import com.towerdefense.entities.projectiles.MagicBall;
import com.towerdefense.utils.Constants;
import com.towerdefense.utils.MathUtils;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a magic tower that can attack enemies from range.
 * Magic towers lock onto a single enemy target and perform 4 normal attacks
 * followed by a powerful spell on the 5th attack, then reset the counter.
 * Magic towers are defensive units that can be attacked by enemies.
 */
public class Magic extends GameObject implements Defensive, Collidable {

    // Current state
    private int currentHealth = Constants.Entities.MAGIC_INITIAL_HEALTH;

    // Combat state
    private Enemy lockedTarget = null;
    private int attackTimer = 0;
    private int attackCounter = 0; // Tracks number of attacks (0-4)
    private boolean isUsingSpecialSpell = false;
    private int spellAnimationTimer = 0;

    // Reference to enemy list for target acquisition
    private ArrayList<Enemy> enemyList;
    private ArrayList<MagicBall> magicBalls = new ArrayList<>();

    // Images
    private Image normalImage;
    private Image bombImage;
    private Image normalBallImage;
    private Image superBallImage;

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
        this.normalImage = magicImage;

        // Load bomb image for special spell
        try {
            this.bombImage = javax.imageio.ImageIO.read(new java.io.File(Constants.Paths.MAGIC_BOMB_IMAGE));
        } catch (java.io.IOException e) {
            this.bombImage = magicImage;
        }

        // Load magic ball images
        try {
            this.normalBallImage = javax.imageio.ImageIO
                    .read(new java.io.File(Constants.Paths.NORMAL_MAGIC_BALL_IMAGE));
        } catch (java.io.IOException e) {
            this.normalBallImage = null;
        }

        try {
            this.superBallImage = javax.imageio.ImageIO.read(new java.io.File(Constants.Paths.SUPER_MAGIC_BALL_IMAGE));
        } catch (java.io.IOException e) {
            this.superBallImage = null;
        }
    }

    // Updates the magic tower each frame
    // Handles target acquisition, attack timing, and combat logic
    public void update() {
        // Update all magic balls
        Iterator<MagicBall> ballIterator = magicBalls.iterator();
        while (ballIterator.hasNext()) {
            MagicBall ball = ballIterator.next();
            ball.update();
            if (!ball.isActive()) {
                ballIterator.remove();
            }
        }

        // Update spell animation
        if (isUsingSpecialSpell) {
            spellAnimationTimer++;
            if (spellAnimationTimer >= Constants.Entities.SPELL_ANIMATION_DURATION) {
                isUsingSpecialSpell = false;
                spellAnimationTimer = 0;
                objectImage = normalImage; // Change back to normal image
            }
        }

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
        shootMagicBall(false);
        System.out.println("Magic normal attack! Damage: " + Constants.Entities.MAGIC_ATTACK_DAMAGE +
                " (Attack " + (attackCounter + 1) + "/5)");
    }

    // Casts a powerful magic spell on the locked target
    // Deals increased damage compared to normal attacks
    private void castSpecialSpell() {
        // Change to bomb image for special spell
        isUsingSpecialSpell = true;
        spellAnimationTimer = 0;
        objectImage = bombImage;

        shootMagicBall(true);
        System.out.println("Magic SPELL CAST! Damage: " + Constants.Entities.MAGIC_SPELL_DAMAGE + " 💥");
    }

    // Shoots a magic ball projectile
    // @param isSuper - true for super ball, false for normal ball
    private void shootMagicBall(boolean isSuper) {
        if (lockedTarget == null || lockedTarget.isDead()) {
            return;
        }

        int startX = positionX + Constants.Entities.MAGIC_X_OFFSET + objectWidth / 2;
        int startY = positionY + Constants.Entities.MAGIC_Y_OFFSET + objectHeight / 2;
        int damage = isSuper ? Constants.Entities.MAGIC_SPELL_DAMAGE : Constants.Entities.MAGIC_ATTACK_DAMAGE;
        Image ballImage = isSuper ? superBallImage : normalBallImage;

        MagicBall ball = new MagicBall(startX, startY, lockedTarget, damage, ballImage);
        magicBalls.add(ball);
    }

    // Implementation of Defensive interface
    @Override
    public void takeDamage(int damageAmount) {
        // Apply defense rating to reduce incoming damage
        int actualDamage = Math.max(1, damageAmount - Constants.Entities.MAGIC_DEFENSE_RATING);

        currentHealth -= actualDamage;
        if (currentHealth < Constants.Entities.MINIMUM_HEALTH) {
            currentHealth = Constants.Entities.MINIMUM_HEALTH;
        }

        System.out.println("Magic HP: " + currentHealth + " (absorbed " +
                (damageAmount - actualDamage) + " damage)");
    }

    @Override
    public boolean isDestroyed() {
        return currentHealth <= Constants.Entities.MINIMUM_HEALTH;
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
        return new Rectangle(positionX + Constants.Entities.MAGIC_X_OFFSET, 
                           positionY + Constants.Entities.MAGIC_Y_OFFSET, 
                           objectWidth, objectHeight);
    }

    // Legacy method for compatibility with existing collision system
    // Delegates to the Collidable interface method
    // @return Rectangle representing the magic tower's bounds
    public Rectangle getBounds() {
        return getCollisionBounds();
    }

    // Renders the magic tower to the screen
    // Draws the magic tower image at its position with visual offset
    // @param graphics - Graphics context for drawing
    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(
                objectImage,
                positionX + Constants.Entities.MAGIC_X_OFFSET,
                positionY + Constants.Entities.MAGIC_Y_OFFSET,
                objectWidth,
                objectHeight,
                null);

        // Draw all magic balls
        for (MagicBall ball : magicBalls) {
            ball.draw(graphics);
        }

        // Draw health bar above magic tower
        drawHealthBar(graphics);
    }

    // Draws a health bar above the magic tower to show current health status
    // Provides visual feedback about the magic tower's defensive state
    // @param graphics - Graphics context for drawing
    private void drawHealthBar(Graphics graphics) {
        if (currentHealth < Constants.Entities.MAGIC_INITIAL_HEALTH) {
            int barWidth = objectWidth;
            int barHeight = 4;
            int barX = positionX + Constants.Entities.MAGIC_X_OFFSET;
            int barY = positionY + Constants.Entities.MAGIC_Y_OFFSET - 8;

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
}

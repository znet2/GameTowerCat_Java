package com.towerdefense.entities.defensive;

import com.towerdefense.entities.base.GameObject;
import com.towerdefense.entities.base.Defensive;
import com.towerdefense.entities.base.Collidable;
import com.towerdefense.entities.enemies.BaseEnemy;
import com.towerdefense.entities.projectiles.Arrow;
import com.towerdefense.utils.Constants;
import com.towerdefense.utils.MathUtils;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents an archer tower that shoots arrows at enemies.
 * Archers attack enemies within range by firing arrow projectiles every 1.5
 * seconds.
 */
public class Archer extends GameObject implements Defensive, Collidable {

    private int currentHealth = Constants.Entities.ARCHER_INITIAL_HEALTH;
    private BaseEnemy lockedTarget = null;
    private int attackTimer = 0;
    private boolean isAttacking = false;
    private int attackAnimationTimer = 0;
    private ArrayList<BaseEnemy> enemyList;
    private ArrayList<Arrow> arrows = new ArrayList<>();
    private Image arrowImage;
    private Image normalImage;
    private Image attackImage;

    public Archer(int gridColumn, int gridRow, int tileSize, Image archerImage, ArrayList<BaseEnemy> enemies) {
        super(gridColumn, gridRow, tileSize, 2, 2, archerImage);
        this.enemyList = enemies;
        this.normalImage = archerImage;

        // Load arrow image
        try {
            this.arrowImage = javax.imageio.ImageIO.read(new java.io.File(Constants.Paths.ARROW_IMAGE));
        } catch (java.io.IOException e) {
            this.arrowImage = null;
        }

        // Load attack image
        try {
            this.attackImage = javax.imageio.ImageIO.read(new java.io.File(Constants.Paths.ARCHER_ATTACK_IMAGE));
        } catch (java.io.IOException e) {
            this.attackImage = archerImage;
        }
    }

    public void update() {
        // Update attack animation
        if (isAttacking) {
            attackAnimationTimer++;
            if (attackAnimationTimer >= Constants.Entities.ATTACK_ANIMATION_DURATION) {
                isAttacking = false;
                attackAnimationTimer = 0;
                objectImage = normalImage; // Change back to normal image
            }
        }

        // Update all arrows
        Iterator<Arrow> arrowIterator = arrows.iterator();
        while (arrowIterator.hasNext()) {
            Arrow arrow = arrowIterator.next();
            arrow.update();
            if (!arrow.isActive()) {
                arrowIterator.remove();
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

    private boolean isTargetValid() {
        if (lockedTarget == null || lockedTarget.isDead()) {
            return false;
        }

        double distance = calculateDistanceToTarget(lockedTarget);
        return distance <= Constants.Entities.ARCHER_ATTACK_RANGE;
    }

    private void acquireNewTarget() {
        lockedTarget = null;
        double closestDistance = Double.MAX_VALUE;

        for (BaseEnemy enemy : enemyList) {
            if (enemy.isDead()) {
                continue;
            }

            double distance = calculateDistanceToTarget(enemy);

            if (distance <= Constants.Entities.ARCHER_ATTACK_RANGE && distance < closestDistance) {
                closestDistance = distance;
                lockedTarget = enemy;
            }
        }
    }

    private double calculateDistanceToTarget(BaseEnemy enemy) {
        int archerCenterX = positionX + objectWidth / 2;
        int archerCenterY = positionY + objectHeight / 2;

        Rectangle enemyBounds = enemy.getBounds();
        int enemyCenterX = enemyBounds.x + enemyBounds.width / 2;
        int enemyCenterY = enemyBounds.y + enemyBounds.height / 2;

        return MathUtils.calculateDistance(archerCenterX, archerCenterY, enemyCenterX, enemyCenterY);
    }

    private void performAttack() {
        attackTimer++;

        if (attackTimer >= Constants.Entities.ARCHER_ATTACK_COOLDOWN_FRAMES) {
            shootArrow();
            attackTimer = 0;
        }
    }

    private void shootArrow() {
        if (lockedTarget == null || lockedTarget.isDead()) {
            return;
        }

        // Change to attack image
        isAttacking = true;
        attackAnimationTimer = 0;
        objectImage = attackImage;

        // Create arrow from archer center position
        int startX = positionX + Constants.Entities.ARCHER_X_OFFSET + objectWidth / 2;
        int startY = positionY + Constants.Entities.ARCHER_Y_OFFSET + objectHeight / 2;

        Arrow arrow = new Arrow(startX, startY, lockedTarget, Constants.Entities.ARCHER_ATTACK_DAMAGE, arrowImage);
        arrows.add(arrow);
    }

    @Override
    public void takeDamage(int damageAmount) {
        int actualDamage = Math.max(1, damageAmount - Constants.Entities.ARCHER_DEFENSE_RATING);

        currentHealth -= actualDamage;
        if (currentHealth < Constants.Entities.MINIMUM_HEALTH) {
            currentHealth = Constants.Entities.MINIMUM_HEALTH;
        }
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
        return Constants.Entities.ARCHER_INITIAL_HEALTH;
    }

    @Override
    public int getDefenseRating() {
        return Constants.Entities.ARCHER_DEFENSE_RATING;
    }

    @Override
    public Rectangle getCollisionBounds() {
        return new Rectangle(positionX + Constants.Entities.ARCHER_X_OFFSET, 
                           positionY + Constants.Entities.ARCHER_Y_OFFSET, 
                           objectWidth, objectHeight);
    }

    public Rectangle getBounds() {
        return getCollisionBounds();
    }

    @Override
    public void draw(Graphics graphics) {
        // Draw archer
        graphics.drawImage(
                objectImage,
                positionX + Constants.Entities.ARCHER_X_OFFSET,
                positionY + Constants.Entities.ARCHER_Y_OFFSET,
                objectWidth,
                objectHeight,
                null);

        // Draw all arrows
        for (Arrow arrow : arrows) {
            arrow.draw(graphics);
        }

        // Draw health bar
        drawHealthBar(graphics);
    }

    private void drawHealthBar(Graphics graphics) {
        if (currentHealth < Constants.Entities.ARCHER_INITIAL_HEALTH) {
            int barWidth = objectWidth;
            int barHeight = 4;
            int barX = positionX + Constants.Entities.ARCHER_X_OFFSET;
            int barY = positionY + Constants.Entities.ARCHER_Y_OFFSET - 8;

            // Background (red)
            graphics.setColor(Color.RED);
            graphics.fillRect(barX, barY, barWidth, barHeight);

            // Health (green)
            graphics.setColor(new Color(0, 255, 0));
            int healthWidth = (int) (barWidth * getHealthPercentage());
            graphics.fillRect(barX, barY, healthWidth, barHeight);

            // Border
            graphics.setColor(Color.BLACK);
            graphics.drawRect(barX, barY, barWidth, barHeight);
        }
    }
}

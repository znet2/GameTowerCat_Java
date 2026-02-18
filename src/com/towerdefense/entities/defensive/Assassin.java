package com.towerdefense.entities.defensive;

import com.towerdefense.entities.base.GameObject;
import com.towerdefense.entities.enemies.BaseEnemy;
import com.towerdefense.utils.Constants;
import com.towerdefense.utils.MathUtils;
import java.awt.*;
import java.util.ArrayList;

/**
 * Represents an assassin trap that attacks enemies passing by.
 * Assassins are hidden traps that deal high damage when enemies walk over them.
 * They cannot be attacked by enemies and only attack when enemies are in range.
 */
public class Assassin extends GameObject {

    private ArrayList<BaseEnemy> enemyList;
    private ArrayList<BaseEnemy> attackedEnemies = new ArrayList<>();
    private int attackCooldown = 0;
    private boolean isAttacking = false;
    private int attackAnimationTimer = 0;
    private Image normalImage;
    private Image attackImage;

    public Assassin(int gridColumn, int gridRow, int tileSize, Image assassinImage, ArrayList<BaseEnemy> enemies) {
        super(gridColumn, gridRow, tileSize, 2, 2, assassinImage);
        this.enemyList = enemies;
        this.normalImage = assassinImage;

        // Load attack image
        try {
            this.attackImage = javax.imageio.ImageIO.read(new java.io.File(Constants.Paths.ASSASSIN_ATTACK_IMAGE));
        } catch (java.io.IOException e) {
            this.attackImage = assassinImage;
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

        if (attackCooldown > 0) {
            attackCooldown--;
        }

        // Check for enemies in range
        for (BaseEnemy enemy : enemyList) {
            if (enemy.isDead()) {
                continue;
            }

            // Check if enemy is in attack range
            double distance = calculateDistanceToEnemy(enemy);
            if (distance <= Constants.Entities.ASSASSIN_ATTACK_RANGE) {
                // Attack enemy if not attacked recently
                if (!attackedEnemies.contains(enemy) && attackCooldown == 0) {
                    attackEnemy(enemy);
                    attackedEnemies.add(enemy);
                    attackCooldown = Constants.Entities.ASSASSIN_ATTACK_COOLDOWN;
                }
            } else {
                // Remove from attacked list when enemy moves away
                attackedEnemies.remove(enemy);
            }
        }

        // Clean up dead enemies from attacked list
        attackedEnemies.removeIf(BaseEnemy::isDead);
    }

    private double calculateDistanceToEnemy(BaseEnemy enemy) {
        int assassinCenterX = positionX + objectWidth / 2;
        int assassinCenterY = positionY + objectHeight / 2;

        Rectangle enemyBounds = enemy.getBounds();
        int enemyCenterX = enemyBounds.x + enemyBounds.width / 2;
        int enemyCenterY = enemyBounds.y + enemyBounds.height / 2;

        return MathUtils.calculateDistance(assassinCenterX, assassinCenterY, enemyCenterX, enemyCenterY);
    }

    private void attackEnemy(BaseEnemy enemy) {
        // Change to attack image
        isAttacking = true;
        attackAnimationTimer = 0;
        objectImage = attackImage;

        enemy.takeDamage(Constants.Entities.ASSASSIN_ATTACK_DAMAGE);
    }

    @Override
    public void draw(Graphics graphics) {
        graphics.drawImage(
                objectImage,
                positionX + Constants.Entities.ASSASSIN_X_OFFSET,
                positionY + Constants.Entities.ASSASSIN_Y_OFFSET,
                objectWidth,
                objectHeight,
                null);
    }
}

package com.towerdefense.entities.projectiles;

import com.towerdefense.entities.enemies.Enemy;
import java.awt.*;

/**
 * Represents an arrow projectile fired by an Archer tower.
 * Arrows travel in a straight line towards their target enemy.
 */
public class Arrow {

    private static final double ARROW_SPEED = 5.0; // Reduced from 8.0 for better visibility
    private static final int ARROW_SIZE = 24; // Increased from 16 for better visibility

    private double positionX;
    private double positionY;
    private Enemy target;
    private int damage;
    private boolean isActive = true;
    private Image arrowImage;

    public Arrow(double startX, double startY, Enemy target, int damage, Image arrowImage) {
        this.positionX = startX;
        this.positionY = startY;
        this.target = target;
        this.damage = damage;
        this.arrowImage = arrowImage;
    }

    public void update() {
        if (!isActive || target == null || target.isDead()) {
            isActive = false;
            return;
        }

        // Get target center position
        Rectangle targetBounds = target.getBounds();
        double targetX = targetBounds.x + targetBounds.width / 2.0;
        double targetY = targetBounds.y + targetBounds.height / 2.0;

        // Calculate direction to target
        double dx = targetX - positionX;
        double dy = targetY - positionY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Check if arrow reached target
        if (distance < ARROW_SPEED) {
            hitTarget();
            return;
        }

        // Move arrow towards target
        double directionX = dx / distance;
        double directionY = dy / distance;
        positionX += directionX * ARROW_SPEED;
        positionY += directionY * ARROW_SPEED;
    }

    private void hitTarget() {
        if (target != null && !target.isDead()) {
            target.takeDamage(damage);
        }
        isActive = false;
    }

    public void draw(Graphics graphics) {
        if (!isActive) {
            return;
        }

        if (arrowImage != null) {
            graphics.drawImage(
                    arrowImage,
                    (int) positionX - ARROW_SIZE / 2,
                    (int) positionY - ARROW_SIZE / 2,
                    ARROW_SIZE,
                    ARROW_SIZE,
                    null);
        } else {
            // Fallback: draw simple arrow shape
            graphics.setColor(new Color(139, 69, 19)); // Brown color
            graphics.fillOval(
                    (int) positionX - 3,
                    (int) positionY - 3,
                    6,
                    6);
        }
    }

    public boolean isActive() {
        return isActive;
    }
}

package com.towerdefense.entities.projectiles;

import com.towerdefense.entities.enemies.Enemy;
import java.awt.*;

/**
 * Represents a magic ball projectile fired by a Magic tower.
 * Magic balls travel towards their target enemy.
 */
public class MagicBall {

    private static final double MAGIC_BALL_SPEED = 6.0;
    private static final int MAGIC_BALL_SIZE = 20;

    private double positionX;
    private double positionY;
    private Enemy target;
    private int damage;
    private boolean isActive = true;
    private Image ballImage;

    public MagicBall(double startX, double startY, Enemy target, int damage, Image ballImage) {
        this.positionX = startX;
        this.positionY = startY;
        this.target = target;
        this.damage = damage;
        this.ballImage = ballImage;
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

        // Check if ball reached target
        if (distance < MAGIC_BALL_SPEED) {
            hitTarget();
            return;
        }

        // Move ball towards target
        double directionX = dx / distance;
        double directionY = dy / distance;
        positionX += directionX * MAGIC_BALL_SPEED;
        positionY += directionY * MAGIC_BALL_SPEED;
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

        if (ballImage != null) {
            graphics.drawImage(
                    ballImage,
                    (int) positionX - MAGIC_BALL_SIZE / 2,
                    (int) positionY - MAGIC_BALL_SIZE / 2,
                    MAGIC_BALL_SIZE,
                    MAGIC_BALL_SIZE,
                    null);
        } else {
            // Fallback: draw simple circle
            graphics.setColor(new Color(255, 0, 255)); // Magenta color
            graphics.fillOval(
                    (int) positionX - 5,
                    (int) positionY - 5,
                    10,
                    10);
        }
    }

    public boolean isActive() {
        return isActive;
    }
}

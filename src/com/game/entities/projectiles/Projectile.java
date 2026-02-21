package com.game.entities.projectiles;

import com.game.entities.enemies.BaseEnemy;
import java.awt.*;

/**
 * Abstract base class for all projectiles in the game.
 * Handles common projectile behavior like movement, targeting, and collision.
 */
public abstract class Projectile {

    protected double positionX;
    protected double positionY;
    protected BaseEnemy target;
    protected int damage;
    protected boolean isActive = true;
    protected Image projectileImage;

    // Constructor for creating a projectile
    // @param startX - starting X position
    // @param startY - starting Y position
    // @param target - enemy to track and hit
    // @param damage - damage to deal on hit
    // @param image - projectile image
    public Projectile(double startX, double startY, BaseEnemy target, int damage, Image image) {
        this.positionX = startX;
        this.positionY = startY;
        this.target = target;
        this.damage = damage;
        this.projectileImage = image;
    }

    // Updates the projectile position and checks for collision
    public void update() {
        if (!isActive || target == null || target.isDead()) {
            isActive = false;
            return;
        }

        Rectangle targetBounds = target.getBounds();
        double targetX = targetBounds.getCenterX();
        double targetY = targetBounds.getCenterY();

        double dx = targetX - positionX;
        double dy = targetY - positionY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Check if projectile reached target
        if (distance < getSpeed()) {
            hitTarget();
            return;
        }

        // Move projectile towards target
        positionX += (dx / distance) * getSpeed();
        positionY += (dy / distance) * getSpeed();
    }

    // Handles collision with target
    protected void hitTarget() {
        if (target != null && !target.isDead()) {
            target.takeDamage(damage);
        }
        isActive = false;
    }

    // Draws the projectile on screen
    public void draw(Graphics graphics) {
        if (!isActive) {
            return;
        }

        if (projectileImage != null) {
            graphics.drawImage(
                    projectileImage,
                    (int) positionX - getSize() / 2,
                    (int) positionY - getSize() / 2,
                    getSize(),
                    getSize(),
                    null);
        } else {
            // Fallback: draw simple shape
            graphics.setColor(getFallbackColor());
            graphics.fillOval(
                    (int) positionX - getFallbackSize() / 2,
                    (int) positionY - getFallbackSize() / 2,
                    getFallbackSize(),
                    getFallbackSize());
        }
    }

    // Checks if projectile is still active
    public boolean isActive() {
        return isActive;
    }

    // Abstract methods to be implemented by subclasses
    protected abstract double getSpeed();
    protected abstract int getSize();
    protected abstract Color getFallbackColor();
    protected abstract int getFallbackSize();
}

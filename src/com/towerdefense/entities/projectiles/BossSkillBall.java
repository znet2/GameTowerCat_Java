package com.towerdefense.entities.projectiles;

import com.towerdefense.entities.base.Defensive;
import com.towerdefense.utils.Constants;
import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a boss skill ball projectile that travels in a specific direction.
 * Unlike other projectiles, this doesn't track a target but moves in a straight line.
 */
public class BossSkillBall {

    private double positionX, positionY;
    private double velocityX, velocityY;
    private int damage;
    private boolean isActive = true;
    private Image ballImage;
    private ArrayList<Defensive> defensiveUnits;

    // Constructor that creates a skill ball with a specific direction
    // @param startX - starting X position
    // @param startY - starting Y position
    // @param angle - direction angle in radians
    // @param damage - damage to deal
    // @param image - visual representation
    // @param defensiveUnits - list of defensive units to check collision
    public BossSkillBall(double startX, double startY, double angle, int damage, Image image, ArrayList<Defensive> defensiveUnits) {
        this.positionX = startX;
        this.positionY = startY;
        this.damage = damage;
        this.ballImage = image;
        this.defensiveUnits = defensiveUnits;
        
        // Calculate velocity based on angle
        double speed = Constants.Projectiles.BOSS_SKILL_BALL_SPEED;
        this.velocityX = Math.cos(angle) * speed;
        this.velocityY = Math.sin(angle) * speed;
    }

    // Updates the ball position and checks for collisions
    public void update() {
        if (!isActive) {
            return;
        }

        // Move the ball
        positionX += velocityX;
        positionY += velocityY;

        // Check if ball is out of bounds
        if (positionX < -100 || positionX > Constants.Game.WINDOW_WIDTH + 100 ||
            positionY < -100 || positionY > Constants.Game.WINDOW_HEIGHT + 100) {
            isActive = false;
            return;
        }

        // Check collision with defensive units
        checkCollisions();
    }

    // Checks for collisions with defensive units
    private void checkCollisions() {
        Rectangle ballBounds = getBounds();
        
        for (Defensive unit : defensiveUnits) {
            if (unit.isDead()) {
                continue;
            }

            Rectangle unitBounds = getUnitBounds(unit);
            if (unitBounds != null && ballBounds.intersects(unitBounds)) {
                hitTarget(unit);
                return;
            }
        }
    }

    // Gets bounds for a defensive unit
    private Rectangle getUnitBounds(Defensive unit) {
        if (unit instanceof com.towerdefense.entities.defensive.Tank) {
            return ((com.towerdefense.entities.defensive.Tank) unit).getBounds();
        } else if (unit instanceof com.towerdefense.entities.defensive.Magic) {
            return ((com.towerdefense.entities.defensive.Magic) unit).getBounds();
        } else if (unit instanceof com.towerdefense.entities.defensive.Archer) {
            return ((com.towerdefense.entities.defensive.Archer) unit).getBounds();
        } else if (unit instanceof com.towerdefense.entities.defensive.House) {
            return ((com.towerdefense.entities.defensive.House) unit).getBounds();
        }
        return null;
    }

    // Handles hitting a target
    private void hitTarget(Defensive target) {
        target.takeDamage(damage);
        isActive = false;
    }

    // Draws the ball on screen
    public void draw(Graphics graphics) {
        if (!isActive) {
            return;
        }

        int size = Constants.Projectiles.BOSS_SKILL_BALL_SIZE;
        int drawX = (int) positionX - size / 2;
        int drawY = (int) positionY - size / 2;

        if (ballImage != null) {
            graphics.drawImage(ballImage, drawX, drawY, size, size, null);
        } else {
            // Fallback: draw a red circle
            graphics.setColor(Constants.Projectiles.BOSS_SKILL_BALL_FALLBACK_COLOR);
            graphics.fillOval(drawX, drawY, 
                Constants.Projectiles.BOSS_SKILL_BALL_FALLBACK_SIZE, 
                Constants.Projectiles.BOSS_SKILL_BALL_FALLBACK_SIZE);
        }
    }

    // Gets the collision bounds
    private Rectangle getBounds() {
        int size = Constants.Projectiles.BOSS_SKILL_BALL_SIZE;
        return new Rectangle((int) positionX - size / 2, (int) positionY - size / 2, size, size);
    }

    // Checks if the ball is still active
    public boolean isActive() {
        return isActive;
    }
}

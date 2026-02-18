package com.towerdefense.entities.projectiles;

import com.towerdefense.entities.enemies.BaseEnemy;
import com.towerdefense.utils.Constants;
import java.awt.*;

/**
 * Represents an arrow projectile fired by an Archer tower.
 * Arrows travel in a straight line towards their target enemy.
 */
public class Arrow extends Projectile {

    public Arrow(double startX, double startY, BaseEnemy target, int damage, Image arrowImage) {
        super(startX, startY, target, damage, arrowImage);
    }

    @Override
    protected double getSpeed() {
        return Constants.Projectiles.ARROW_SPEED;
    }

    @Override
    protected int getSize() {
        return Constants.Projectiles.ARROW_SIZE;
    }

    @Override
    protected Color getFallbackColor() {
        return Constants.Projectiles.ARROW_FALLBACK_COLOR;
    }

    @Override
    protected int getFallbackSize() {
        return Constants.Projectiles.ARROW_FALLBACK_SIZE;
    }
}

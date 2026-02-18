package com.towerdefense.entities.projectiles;

import com.towerdefense.entities.enemies.BaseEnemy;
import com.towerdefense.utils.Constants;
import java.awt.*;

/**
 * Represents a magic ball projectile fired by a Magic tower.
 * Magic balls travel towards their target enemy.
 */
public class MagicBall extends Projectile {

    public MagicBall(double startX, double startY, BaseEnemy target, int damage, Image ballImage) {
        super(startX, startY, target, damage, ballImage);
    }

    @Override
    protected double getSpeed() {
        return Constants.Projectiles.MAGIC_BALL_SPEED;
    }

    @Override
    protected int getSize() {
        return Constants.Projectiles.MAGIC_BALL_SIZE;
    }

    @Override
    protected Color getFallbackColor() {
        return Constants.Projectiles.MAGIC_BALL_FALLBACK_COLOR;
    }

    @Override
    protected int getFallbackSize() {
        return Constants.Projectiles.MAGIC_BALL_FALLBACK_SIZE;
    }
}

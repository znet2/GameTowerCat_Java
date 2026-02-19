package com.towerdefense.entities.enemies;

import com.towerdefense.world.Map;
import com.towerdefense.managers.CoinManager;
import com.towerdefense.utils.Constants;
import java.awt.*;
import javax.swing.ImageIcon;

/**
 * Represents a normal enemy unit that moves along a path and attacks defensive units.
 * Extends BaseEnemy to inherit common enemy functionality.
 */
public class Enemy extends BaseEnemy {

    private final Image idleImage;
    private final Image walkImage;
    private final Image attackImage;

    // Constructor that creates an enemy and sets up its path
    public Enemy(Map gameMap, CoinManager coinManager) {
        super(gameMap, coinManager, Constants.Entities.ENEMY_INITIAL_HEALTH);
        this.idleImage = new ImageIcon(Constants.Paths.ENEMY_IMAGE).getImage();
        this.walkImage = new ImageIcon(Constants.Paths.ENEMY_WALK_IMAGE).getImage();
        this.attackImage = new ImageIcon(Constants.Paths.ENEMY_ATTACK_IMAGE).getImage();
    }

    @Override
    protected int getMaxHealth() {
        return Constants.Entities.ENEMY_INITIAL_HEALTH;
    }

    @Override
    protected int getAttackDamage() {
        return Constants.Entities.ENEMY_ATTACK_DAMAGE;
    }

    @Override
    protected int getAttackCooldown() {
        return Constants.Entities.ENEMY_ATTACK_COOLDOWN_FRAMES;
    }

    @Override
    protected double getSpeed() {
        return Constants.Entities.ENEMY_SPEED;
    }

    @Override
    protected int getSize() {
        return Constants.Entities.ENEMY_SIZE;
    }

    @Override
    protected int getXOffset() {
        return Constants.Entities.ENEMY_X_OFFSET;
    }

    @Override
    protected int getYOffset() {
        return Constants.Entities.ENEMY_Y_OFFSET;
    }

    @Override
    protected Image getIdleImage() {
        return idleImage;
    }

    @Override
    protected Image getWalkImage() {
        return walkImage;
    }

    @Override
    protected Image getAttackImage() {
        return attackImage;
    }

    @Override
    protected int getCoinReward() {
        return Constants.Economy.COINS_PER_ENEMY_KILL;
    }
}

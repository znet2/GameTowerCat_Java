package com.towerdefense.entities.enemies;

import com.towerdefense.world.Map;
import com.towerdefense.managers.CoinManager;
import com.towerdefense.utils.Constants;
import java.awt.*;
import javax.swing.ImageIcon;

/**
 * Represents a boss enemy unit with higher health, damage, and coin reward.
 * Extends BaseEnemy to inherit common enemy functionality.
 * Bosses are larger, slower, and more powerful than normal enemies.
 */
public class EnemyBoss extends BaseEnemy {

    private final Image bossImage;

    // Constructor that creates a boss enemy
    public EnemyBoss(Map gameMap, CoinManager coinManager) {
        super(gameMap, coinManager, Constants.Entities.BOSS_INITIAL_HEALTH);
        this.bossImage = new ImageIcon(Constants.Paths.BOSS_IMAGE).getImage();
    }

    @Override
    protected int getMaxHealth() {
        return Constants.Entities.BOSS_INITIAL_HEALTH;
    }

    @Override
    protected int getAttackDamage() {
        return Constants.Entities.BOSS_ATTACK_DAMAGE;
    }

    @Override
    protected int getAttackCooldown() {
        return Constants.Entities.BOSS_ATTACK_COOLDOWN_FRAMES;
    }

    @Override
    protected double getSpeed() {
        return Constants.Entities.BOSS_SPEED;
    }

    @Override
    protected int getSize() {
        return Constants.Entities.BOSS_SIZE;
    }

    @Override
    protected int getXOffset() {
        return Constants.Entities.BOSS_X_OFFSET;
    }

    @Override
    protected int getYOffset() {
        return Constants.Entities.BOSS_Y_OFFSET;
    }


    @Override
    protected Image getImage() {
        return bossImage;
    }

    @Override
    protected int getCoinReward() {
        return Constants.Economy.COINS_PER_BOSS_KILL;
    }
}

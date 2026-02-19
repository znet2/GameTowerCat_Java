package com.towerdefense.entities.enemies;

import com.towerdefense.world.Map;
import com.towerdefense.managers.CoinManager;
import com.towerdefense.entities.projectiles.BossSkillBall;
import com.towerdefense.entities.base.Defensive;
import com.towerdefense.utils.Constants;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;

/**
 * Represents a boss enemy unit with higher health, damage, and coin reward.
 * Extends BaseEnemy to inherit common enemy functionality.
 * Bosses are larger, slower, and more powerful than normal enemies.
 * Boss has a special skill that fires projectiles in all directions every 10 seconds.
 */
public class EnemyBoss extends BaseEnemy {

    private final Image idleImage;
    private final Image flyImage;
    private final Image attackImage;
    private final Image skillImage;
    private final Image skillBallImage;
    
    // Skill state
    private int skillCooldownTimer = Constants.Entities.BOSS_SKILL_COOLDOWN_FRAMES;
    private boolean isUsingSkill = false;
    private int skillAnimationTimer = 0;
    private ArrayList<BossSkillBall> skillBalls = new ArrayList<>();

    // Constructor that creates a boss enemy
    public EnemyBoss(Map gameMap, CoinManager coinManager) {
        super(gameMap, coinManager, Constants.Entities.BOSS_INITIAL_HEALTH);
        this.idleImage = new ImageIcon(Constants.Paths.BOSS_IMAGE).getImage();
        this.flyImage = new ImageIcon(Constants.Paths.BOSS_FLY_IMAGE).getImage();
        this.attackImage = new ImageIcon(Constants.Paths.BOSS_ATTACK_IMAGE).getImage();
        this.skillImage = new ImageIcon(Constants.Paths.BOSS_SKILL_IMAGE).getImage();
        this.skillBallImage = new ImageIcon(Constants.Paths.BOSS_SKILL_BALL_IMAGE).getImage();
    }

    @Override
    public void update() {
        // Update skill balls first
        updateSkillBalls();
        
        // Update skill animation
        if (isUsingSkill) {
            skillAnimationTimer++;
            if (skillAnimationTimer >= Constants.Entities.BOSS_SKILL_ANIMATION_DURATION) {
                isUsingSkill = false;
                skillAnimationTimer = 0;
            }
        }
        
        // Update skill cooldown
        if (!isUsingSkill && !isDead) {
            skillCooldownTimer--;
            if (skillCooldownTimer <= 0) {
                useSkill();
                skillCooldownTimer = Constants.Entities.BOSS_SKILL_COOLDOWN_FRAMES;
            }
        }
        
        // Call parent update
        super.update();
    }

    // Updates all skill balls
    private void updateSkillBalls() {
        Iterator<BossSkillBall> iterator = skillBalls.iterator();
        while (iterator.hasNext()) {
            BossSkillBall ball = iterator.next();
            ball.update();
            if (!ball.isActive()) {
                iterator.remove();
            }
        }
    }

    // Uses the boss special skill
    private void useSkill() {
        isUsingSkill = true;
        skillAnimationTimer = 0;
        
        // Get all defensive units
        ArrayList<Defensive> defensiveUnits = new ArrayList<>();
        defensiveUnits.addAll(gameMap.getTanks());
        defensiveUnits.addAll(gameMap.getMagicTowers());
        defensiveUnits.addAll(gameMap.getArcherTowers());
        defensiveUnits.add(gameMap.getHouse());
        
        // Spawn balls in all directions
        int ballCount = Constants.Entities.BOSS_SKILL_BALL_COUNT;
        double angleStep = (2 * Math.PI) / ballCount;
        
        double centerX = positionX;
        double centerY = positionY;
        
        for (int i = 0; i < ballCount; i++) {
            double angle = i * angleStep;
            BossSkillBall ball = new BossSkillBall(
                centerX, centerY, angle, 
                Constants.Entities.BOSS_SKILL_BALL_DAMAGE, 
                skillBallImage, defensiveUnits
            );
            skillBalls.add(ball);
        }
    }

    @Override
    public void draw(Graphics graphics) {
        // Draw skill balls first (behind boss)
        for (BossSkillBall ball : skillBalls) {
            ball.draw(graphics);
        }
        
        // Draw boss
        if (!isDead) {
            Image currentImage = getCurrentBossImage();
            graphics.drawImage(currentImage, 
                    (int) positionX + getXOffset(), 
                    (int) positionY + getYOffset(),
                    getSize(), getSize(), null);
            drawHealthBar(graphics);
        }
    }

    // Gets the current image based on state
    private Image getCurrentBossImage() {
        if (isUsingSkill) {
            return skillImage;
        }
        return getCurrentImage();
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
    protected Image getIdleImage() {
        return idleImage;
    }

    @Override
    protected Image getWalkImage() {
        return flyImage;
    }

    @Override
    protected Image getAttackImage() {
        return attackImage;
    }

    @Override
    protected int getCoinReward() {
        return Constants.Economy.COINS_PER_BOSS_KILL;
    }
}


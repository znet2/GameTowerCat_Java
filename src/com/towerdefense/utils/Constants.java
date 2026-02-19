package com.towerdefense.utils;

import java.awt.Color;
import java.awt.Font;

/**
 * Central location for all game constants and configuration values.
 * Provides easy access to commonly used values and makes them easy to modify.
 */
public final class Constants {

    // Prevent instantiation
    private Constants() {
    }

    // Game Configuration
    public static final class Game {
        public static final int TARGET_FPS = 60;
        public static final String TITLE = "Tower Defense";
        public static final double NANOSECONDS_PER_FRAME = 1_000_000_000.0 / TARGET_FPS;
        public static final int WINDOW_WIDTH = 1920;
        public static final int WINDOW_HEIGHT = 1080;
    }

    // Map and Tile Configuration
    public static final class Map {
        public static final int TILE_SIZE = 32;
        public static final int HOUSE_COLUMN = 53;
        public static final int HOUSE_ROW = 9;

        // Tile Types
        public static final int TILE_ROAD = 0;
        public static final int TILE_GRASS = 1;
        public static final int TILE_WATER = 2;
        public static final int TILE_WATER_UP = 3;
        public static final int TILE_WATER_DOWN = 4;
        public static final int TILE_WATER_LEFT = 5;
        public static final int TILE_WATER_RIGHT = 6;
        public static final int TILE_TREE = 7;
    }

    // Entity Configuration
    public static final class Entities {
        // Common Configuration
        public static final int MINIMUM_HEALTH = 0;
        public static final int ATTACK_ANIMATION_DURATION = 15; // frames
        public static final int SPELL_ANIMATION_DURATION = 30; // frames
        
        // Enemy Animation Configuration
        public static final int ENEMY_WALK_ANIMATION_FRAMES = 30; // Switch every 0.5 seconds
        public static final int ENEMY_ATTACK_ANIMATION_FRAMES = 20; // Switch during attack

        // Tank Configuration
        public static final int TANK_INITIAL_HEALTH = 16000;
        public static final int TANK_COST = 120;
        public static final int TANK_X_OFFSET = -15; // Visual offset X
        public static final int TANK_Y_OFFSET = -30; // Visual offset Y

        // Magic Configuration
        public static final int MAGIC_INITIAL_HEALTH = 3500;
        public static final int MAGIC_DEFENSE_RATING = 0;
        public static final int MAGIC_COST = 90;
        public static final int MAGIC_ATTACK_DAMAGE = 280;
        public static final int MAGIC_SPELL_DAMAGE = 400;
        public static final int MAGIC_ATTACK_COOLDOWN_FRAMES = 60;
        public static final int MAGIC_ATTACK_RANGE = 250;
        public static final int MAGIC_ATTACKS_BEFORE_SPELL = 4;
        public static final int MAGIC_X_OFFSET = -15; // Visual offset X
        public static final int MAGIC_Y_OFFSET = -30; // Visual offset Y

        // Archer Configuration
        public static final int ARCHER_INITIAL_HEALTH = 3500;
        public static final int ARCHER_DEFENSE_RATING = 0;
        public static final int ARCHER_COST = 180;
        public static final int ARCHER_ATTACK_DAMAGE = 135;
        public static final int ARCHER_ATTACK_COOLDOWN_FRAMES = 30; // 1.5 seconds at 60 FPS
        public static final int ARCHER_ATTACK_RANGE = 400;
        public static final int ARCHER_X_OFFSET = -15; // Visual offset X
        public static final int ARCHER_Y_OFFSET = -30; // Visual offset Y

        // Assassin Configuration
        public static final int ASSASSIN_COST = 150;
        public static final int ASSASSIN_ATTACK_DAMAGE = 600;
        public static final int ASSASSIN_ATTACK_RANGE = 80; // Small range for melee attack
        public static final int ASSASSIN_ATTACK_COOLDOWN = 45; // 0.5 seconds
        public static final int ASSASSIN_X_OFFSET = -15; // Visual offset X
        public static final int ASSASSIN_Y_OFFSET = -30; // Visual offset Y

        // House Configuration
        public static final int HOUSE_INITIAL_HEALTH = 12000;
        public static final int HOUSE_WIDTH_TILES = 7;
        public static final int HOUSE_HEIGHT_TILES = 7;

        // Enemy Configuration
        public static final int ENEMY_SIZE = 48;
        public static final double ENEMY_SPEED = 0.5;
        public static final int ENEMY_INITIAL_HEALTH = 3000;
        public static final int ENEMY_ATTACK_DAMAGE = 500;
        public static final int ENEMY_ATTACK_COOLDOWN_FRAMES = 60;
        public static final int ENEMY_X_OFFSET = -32; // Center the image (half of size)
        public static final int ENEMY_Y_OFFSET = -36; // Center the image (half of size)
        public static final double ENEMY_SPACING_MULTIPLIER = 1; // Spacing between enemies (1.1 = 10% gap)

        // Boss Configuration
        public static final int BOSS_SIZE = 128; // Larger than normal enemy
        public static final double BOSS_SPEED = 0.3; // Slower than normal enemy
        public static final int BOSS_INITIAL_HEALTH = 17000; // Much higher health
        public static final int BOSS_ATTACK_DAMAGE = 2000; // Double damage
        public static final int BOSS_ATTACK_COOLDOWN_FRAMES = 45; // Faster attack (0.75 seconds)
        public static final int BOSS_X_OFFSET = -48; // Center the image (half of size)
        public static final int BOSS_Y_OFFSET = -100; // Center the image (half of size)
        public static final int BOSS_SKILL_COOLDOWN_FRAMES = 600; // 10 seconds at 60 FPS
        public static final int BOSS_SKILL_ANIMATION_DURATION = 60; // 1 second animation
        public static final int BOSS_SKILL_BALL_COUNT = 16; // Number of balls to spawn
        public static final int BOSS_SKILL_BALL_DAMAGE = 750; // Damage per ball
    }

    // Projectile Configuration
    public static final class Projectiles {
        // Arrow Configuration
        public static final double ARROW_SPEED = 5.0;
        public static final int ARROW_SIZE = 24;
        public static final Color ARROW_FALLBACK_COLOR = new Color(139, 69, 19); // Brown
        public static final int ARROW_FALLBACK_SIZE = 6;

        // Magic Ball Configuration
        public static final double MAGIC_BALL_SPEED = 6.0;
        public static final int MAGIC_BALL_SIZE = 20;
        public static final Color MAGIC_BALL_FALLBACK_COLOR = new Color(255, 0, 255); // Magenta
        public static final int MAGIC_BALL_FALLBACK_SIZE = 10;
        
        // Boss Skill Ball Configuration
        public static final double BOSS_SKILL_BALL_SPEED = 3.0;
        public static final int BOSS_SKILL_BALL_SIZE = 32;
        public static final Color BOSS_SKILL_BALL_FALLBACK_COLOR = new Color(255, 0, 0); // Red
        public static final int BOSS_SKILL_BALL_FALLBACK_SIZE = 16;
    }

    // UI Configuration
    public static final class UI {
        public static final int HERO_BAR_HEIGHT = 80;
        public static final int TANK_ICON_SIZE = 48;
        public static final int TANK_ICON_MARGIN = 20;
        public static final int TANK_ICON_TOP_MARGIN = 16;

        // Menu
        public static final int START_BUTTON_WIDTH = 510;
        public static final int START_BUTTON_HEIGHT = 240;
        public static final int START_BUTTON_Y_OFFSET = 150; // Offset from center (positive = down, negative = up)
        public static final int LOGO_WIDTH = 750;
        public static final int LOGO_HEIGHT = 400;
        public static final int LOGO_TOP_MARGIN = 150;

        // Game Over Screen - Win
        public static final int WIN_IMAGE_WIDTH = 870;
        public static final int WIN_IMAGE_HEIGHT = 460;
        public static final int WIN_IMAGE_Y_OFFSET = -150; // Offset from center (positive = down, negative = up)

        // Game Over Screen - Lose
        public static final int LOSE_IMAGE_WIDTH = 850;
        public static final int LOSE_IMAGE_HEIGHT = 560;
        public static final int LOSE_IMAGE_Y_OFFSET = -150; // Offset from center (positive = down, negative = up)

        // Game Over Screen - Restart Button
        public static final int RESTART_BUTTON_WIDTH = 400;
        public static final int RESTART_BUTTON_HEIGHT = 200;
        public static final int RESTART_BUTTON_Y_OFFSET = 45; // Offset from result image bottom

        // Colors
        public static final Color HERO_BAR_COLOR = Color.DARK_GRAY;
        public static final Color TANK_ICON_COLOR = Color.ORANGE;
        public static final Color TANK_ICON_DISABLED_COLOR = Color.GRAY;
        public static final Color MAGIC_ICON_COLOR = Color.CYAN;
        public static final Color MAGIC_ICON_DISABLED_COLOR = Color.GRAY;
        public static final Color DRAG_PREVIEW_COLOR = new Color(255, 165, 0, 150);

        // Coin Display
        public static final Color COIN_TEXT_COLOR = Color.YELLOW;
        public static final Color COIN_BACKGROUND_COLOR = new Color(0, 0, 0, 128);
        public static final Font COIN_FONT = new Font("Arial", Font.BOLD, 16);
        public static final int COIN_DISPLAY_PADDING = 10;
    }

    // Economy Configuration
    public static final class Economy {
        public static final int STARTING_COINS = 550;
        public static final int COINS_PER_ENEMY_KILL = 15;
        public static final int COINS_PER_BOSS_KILL = 30; // Boss gives more coins
        public static final int COINS_PER_WAVE_COMPLETE = 200; // Bonus coins when completing a wave
    }

    // Wave Configuration
    public static final class Waves {
        public static final int BASE_ENEMIES_PER_WAVE = 3;
        public static final int ENEMIES_INCREASE_PER_WAVE = 3;
        public static final int SPAWN_DELAY_FRAMES = 30;
        public static final int MAX_WAVES = 5; // Total waves to win
    }

    // File Paths
    public static final class Paths {
        public static final String IMAGES = "image/";
        public static final String ENEMY_IMAGE = IMAGES + "catEnemy.png";
        public static final String ENEMY_WALK_IMAGE = IMAGES + "catEnemyWalk.png";
        public static final String ENEMY_ATTACK_IMAGE = IMAGES + "catEnemyAttack.png";
        public static final String BOSS_IMAGE = IMAGES + "enemyBoss.png";
        public static final String BOSS_FLY_IMAGE = IMAGES + "enemyBossFly.png";
        public static final String BOSS_ATTACK_IMAGE = IMAGES + "enemyBossAttack.png";
        public static final String BOSS_SKILL_IMAGE = IMAGES + "enemyBossSkill.png";
        public static final String BOSS_SKILL_BALL_IMAGE = IMAGES + "BossSkillBall.png";
        public static final String TANK_IMAGE = IMAGES + "tank.png";
        public static final String TANK_DEFEND_IMAGE = IMAGES + "tankDef.png";
        public static final String HOUSE_IMAGE = IMAGES + "castle.png";
        public static final String MAGIC_IMAGE = IMAGES + "magic.png";
        public static final String MAGIC_BOMB_IMAGE = IMAGES + "magicBomb.png";
        public static final String NORMAL_MAGIC_BALL_IMAGE = IMAGES + "normalMagicBall.png";
        public static final String SUPER_MAGIC_BALL_IMAGE = IMAGES + "superMagicBall.png";
        public static final String ARCHER_IMAGE = IMAGES + "archer.png";
        public static final String ARCHER_ATTACK_IMAGE = IMAGES + "archerAttack.png";
        public static final String ARROW_IMAGE = IMAGES + "arrow.png";
        public static final String ASSASSIN_IMAGE = IMAGES + "assasin.png"; // Note: file is spelled "assasin"
        public static final String ASSASSIN_ATTACK_IMAGE = IMAGES + "assasinAttack.png";
        public static final String WALLPAPER_IMAGE = IMAGES + "wallpaper.png";
        public static final String START_BUTTON_IMAGE = IMAGES + "startbotton.png";
        public static final String LOGO_GAME_IMAGE = IMAGES + "logoGame.png";
        public static final String WIN_IMAGE = IMAGES + "win.png";
        public static final String LOSE_IMAGE = IMAGES + "lose.png";
        public static final String WIN_BACKGROUND_IMAGE = IMAGES + "winBackground.png";
        public static final String LOSE_BACKGROUND_IMAGE = IMAGES + "loseBackground.png";
        public static final String RESTART_BUTTON_IMAGE = IMAGES + "restart.png";

        // Tile Images
        public static final String GRASS_TILE = IMAGES + "grass.png";
        public static final String ROAD_TILE = IMAGES + "dirt.png";
        public static final String WATER_TILE = IMAGES + "water.png";
        public static final String WATER_UP_TILE = IMAGES + "water_up.png";
        public static final String WATER_DOWN_TILE = IMAGES + "water_down.png";
        public static final String WATER_LEFT_TILE = IMAGES + "water_left.png";
        public static final String WATER_RIGHT_TILE = IMAGES + "water_right.png";
        public static final String TREE_TILE = IMAGES + "water.png";
    }
}
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
    }

    // Map and Tile Configuration
    public static final class Map {
        public static final int TILE_SIZE = 32;
        public static final int HOUSE_COLUMN = 32;
        public static final int HOUSE_ROW = 4;

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
        // Tank Configuration
        public static final int TANK_INITIAL_HEALTH = 10000;
        public static final int TANK_DEFENSE_RATING = 0;
        public static final int TANK_COST = 25;

        // Magic Configuration
        public static final int MAGIC_INITIAL_HEALTH = 50;
        public static final int MAGIC_DEFENSE_RATING = 0;
        public static final int MAGIC_COST = 10;
        public static final int MAGIC_ATTACK_DAMAGE = 5;
        public static final int MAGIC_SPELL_DAMAGE = 20;
        public static final int MAGIC_ATTACK_COOLDOWN_FRAMES = 45;
        public static final int MAGIC_ATTACK_RANGE = 300;
        public static final int MAGIC_ATTACKS_BEFORE_SPELL = 4;

        // Archer Configuration
        public static final int ARCHER_INITIAL_HEALTH = 75;
        public static final int ARCHER_DEFENSE_RATING = 0;
        public static final int ARCHER_COST = 15;
        public static final int ARCHER_ATTACK_DAMAGE = 10;
        public static final int ARCHER_ATTACK_COOLDOWN_FRAMES = 90; // 1.5 seconds at 60 FPS
        public static final int ARCHER_ATTACK_RANGE = 1000;

        // House Configuration
        public static final int HOUSE_INITIAL_HEALTH = 100;
        public static final int HOUSE_WIDTH_TILES = 7;
        public static final int HOUSE_HEIGHT_TILES = 7;

        // Enemy Configuration
        public static final int ENEMY_SIZE = 64;
        public static final double ENEMY_SPEED = 1.2;
        public static final int ENEMY_INITIAL_HEALTH = 50;
        public static final int ENEMY_ATTACK_DAMAGE = 5;
        public static final int ENEMY_ATTACK_COOLDOWN_FRAMES = 60;
        public static final int ENEMY_Y_OFFSET = -25;
    }

    // UI Configuration
    public static final class UI {
        public static final int HERO_BAR_HEIGHT = 80;
        public static final int TANK_ICON_SIZE = 48;
        public static final int TANK_ICON_MARGIN = 20;
        public static final int TANK_ICON_TOP_MARGIN = 16;

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
        public static final int STARTING_COINS = 50;
        public static final int COINS_PER_ENEMY_KILL = 10;
        public static final int PASSIVE_INCOME_AMOUNT = 5;
        public static final int PASSIVE_INCOME_INTERVAL_FRAMES = 300; // 5 seconds at 60 FPS
    }

    // Wave Configuration
    public static final class Waves {
        public static final int BASE_ENEMIES_PER_WAVE = 3;
        public static final int ENEMIES_INCREASE_PER_WAVE = 2;
        public static final int SPAWN_DELAY_FRAMES = 30;
    }

    // File Paths
    public static final class Paths {
        public static final String IMAGES = "image/";
        public static final String ENEMY_IMAGE = IMAGES + "catEnemy.png";
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
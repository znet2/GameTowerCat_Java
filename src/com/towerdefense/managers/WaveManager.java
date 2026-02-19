package com.towerdefense.managers;

import com.towerdefense.world.Map;
import com.towerdefense.entities.enemies.Enemy;
import com.towerdefense.entities.enemies.EnemyBoss;
import com.towerdefense.entities.enemies.BaseEnemy;
import com.towerdefense.utils.Constants;
import java.util.ArrayList;

/**
 * Manages enemy wave spawning and progression in the tower defense game.
 * Controls when enemies spawn, how many spawn per wave, and wave advancement.
 * In the final wave, spawns a Boss as the last enemy.
 */
public class WaveManager {

    // Wave state
    private int currentWaveNumber = 1;
    private int enemiesToSpawnInWave;
    private int enemiesSpawnedInWave = 0;
    private int spawnTimer = 0;
    private boolean isCurrentlySpawning = false;

    // Wave announcement display
    private String waveAnnouncementText = "";
    private int waveAnnouncementTimer = 0;
    private static final int WAVE_ANNOUNCEMENT_DURATION = 180; // 3 seconds at 60 FPS

    // Game references
    private final Map gameMap;
    private final ArrayList<BaseEnemy> activeEnemies;
    private final CoinManager coinManager;

    // Constructor that initializes the wave manager with game references
    // Sets up the connection to the map, enemy list, and coin system for spawning
    // @param gameMap - reference to the game map for enemy creation
    // @param enemyList - list of active enemies to add new spawns to
    // @param coinManager - reference to coin system for enemy kill rewards
    public WaveManager(Map gameMap, ArrayList<BaseEnemy> enemyList, CoinManager coinManager) {
        this.gameMap = gameMap;
        this.activeEnemies = enemyList;
        this.coinManager = coinManager;
    }

    // Starts the next wave of enemies
    // Calculates enemy count, resets state, and announces the wave
    public void startNextWave() {
        calculateEnemiesForWave();
        resetWaveState();
        announceWaveStart();
    }

    // Calculates how many enemies should spawn in the current wave
    // Uses base count plus scaling based on wave number for difficulty progression
    // Adds extra slots for Boss enemies based on wave number
    private void calculateEnemiesForWave() {
        int baseEnemies = Constants.Waves.BASE_ENEMIES_PER_WAVE + 
                          currentWaveNumber * Constants.Waves.ENEMIES_INCREASE_PER_WAVE;
        
        // Add slots for Boss enemies
        int bossCount = calculateBossCount();
        enemiesToSpawnInWave = baseEnemies + bossCount;
    }

    // Calculates how many bosses should spawn in the current wave
    // Wave 3: 1 boss
    // Wave 5: 2 bosses
    // Wave 7: 3 bosses, etc.
    // @return number of bosses to spawn
    private int calculateBossCount() {
        if (currentWaveNumber < 3) {
            return 0; // No boss before wave 3
        }
        
        // Wave 3 = 1 boss, Wave 5 = 2 bosses, Wave 7 = 3 bosses
        // Formula: (waveNumber - 3) / 2 + 1
        return (currentWaveNumber - 3) / 2 + 1;
    }

    // Checks if current wave should have bosses
    // @return true if bosses should spawn in this wave
    private boolean shouldHaveBoss() {
        return currentWaveNumber >= 3;
    }

    // Resets all wave state variables for a new wave
    // Clears spawn counters and timers, enables spawning
    private void resetWaveState() {
        enemiesSpawnedInWave = 0;
        spawnTimer = 0;
        isCurrentlySpawning = true;
    }

    // Announces the start of a new wave to the console and screen
    // Provides feedback about wave progression
    private void announceWaveStart() {
        waveAnnouncementText = "Wave " + currentWaveNumber;
        waveAnnouncementTimer = WAVE_ANNOUNCEMENT_DURATION;
    }

    // Updates the wave manager each frame
    // Handles spawn timing and enemy creation during active waves
    public void update() {
        // Update wave announcement timer
        if (waveAnnouncementTimer > 0) {
            waveAnnouncementTimer--;
        }

        if (!isCurrentlySpawning) {
            return;
        }

        updateSpawnTimer();

        if (shouldSpawnEnemy()) {
            spawnEnemy();
        }

        if (hasFinishedSpawning()) {
            completeWave();
        }
    }

    // Increments the spawn timer for timing enemy spawns
    // Tracks frames since last enemy spawn
    private void updateSpawnTimer() {
        spawnTimer++;
    }

    // Determines if it's time to spawn another enemy
    // Checks timer and remaining enemy count
    // @return true if an enemy should be spawned, false otherwise
    private boolean shouldSpawnEnemy() {
        return spawnTimer >= Constants.Waves.SPAWN_DELAY_FRAMES && 
               enemiesSpawnedInWave < enemiesToSpawnInWave;
    }

    // Creates and spawns a new enemy
    // Spawns bosses at the end of waves starting from wave 3
    // Number of bosses increases every 2 waves after wave 3
    // Adds enemy to the active list and resets spawn timer
    private void spawnEnemy() {
        BaseEnemy newEnemy;
        
        if (shouldSpawnBoss()) {
            // Spawn Boss
            newEnemy = new EnemyBoss(gameMap, coinManager);
        } else {
            // Spawn normal enemy
            newEnemy = new Enemy(gameMap, coinManager);
        }
        
        // Set reference to all enemies for collision avoidance
        newEnemy.setAllEnemies(activeEnemies);
        activeEnemies.add(newEnemy);
        
        // Update all existing enemies with the new list
        for (BaseEnemy enemy : activeEnemies) {
            enemy.setAllEnemies(activeEnemies);
        }
        
        enemiesSpawnedInWave++;
        spawnTimer = 0;
    }

    // Determines if a Boss should be spawned
    // Bosses spawn at the end of waves starting from wave 3
    // Number of bosses = (waveNumber - 3) / 2 + 1
    // Wave 3: last 1 enemy is boss
    // Wave 5: last 2 enemies are bosses
    // Wave 7: last 3 enemies are bosses, etc.
    // @return true if Boss should spawn, false otherwise
    private boolean shouldSpawnBoss() {
        if (!shouldHaveBoss()) {
            return false;
        }
        
        int bossCount = calculateBossCount();
        int normalEnemyCount = enemiesToSpawnInWave - bossCount;
        
        // Spawn boss if we've spawned all normal enemies
        return enemiesSpawnedInWave >= normalEnemyCount;
    }

    // Checks if all enemies for the current wave have been spawned
    // @return true if spawning is complete, false otherwise
    private boolean hasFinishedSpawning() {
        return enemiesSpawnedInWave >= enemiesToSpawnInWave;
    }

    // Completes the current wave and prepares for the next
    // Stops spawning and increments wave number
    private void completeWave() {
        isCurrentlySpawning = false;
        currentWaveNumber++;
    }

    // Checks if the current wave is completely finished
    // Wave is finished when spawning is done and all enemies are defeated
    // @return true if wave is finished and next wave should start
    public boolean isWaveFinished() {
        return !isCurrentlySpawning && activeEnemies.isEmpty();
    }

    // Gets the current wave number
    // @return the current wave number
    public int getCurrentWave() {
        return currentWaveNumber;
    }

    // Gets the wave announcement text to display
    // @return the wave announcement text, or empty string if no announcement
    public String getWaveAnnouncementText() {
        return waveAnnouncementTimer > 0 ? waveAnnouncementText : "";
    }

    // Checks if wave announcement should be displayed
    // @return true if announcement should be shown
    public boolean shouldShowWaveAnnouncement() {
        return waveAnnouncementTimer > 0;
    }
}
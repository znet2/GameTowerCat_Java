import java.util.ArrayList;

/**
 * Manages enemy wave spawning and progression in the tower defense game.
 * Controls when enemies spawn, how many spawn per wave, and wave advancement.
 */
public class WaveManager {

    // Wave configuration
    private static final int BASE_ENEMIES_PER_WAVE = 3;
    private static final int ENEMIES_INCREASE_PER_WAVE = 2;
    private static final int SPAWN_DELAY_FRAMES = 30;

    // Wave state
    private int currentWaveNumber = 1;
    private int enemiesToSpawnInWave;
    private int enemiesSpawnedInWave = 0;
    private int spawnTimer = 0;
    private boolean isCurrentlySpawning = false;

    // Game references
    private final Map gameMap;
    private final ArrayList<Enemy> activeEnemies;

    // Constructor that initializes the wave manager with game references
    // Sets up the connection to the map and enemy list for spawning
    // @param gameMap - reference to the game map for enemy creation
    // @param enemyList - list of active enemies to add new spawns to
    public WaveManager(Map gameMap, ArrayList<Enemy> enemyList) {
        this.gameMap = gameMap;
        this.activeEnemies = enemyList;
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
    private void calculateEnemiesForWave() {
        enemiesToSpawnInWave = BASE_ENEMIES_PER_WAVE + currentWaveNumber * ENEMIES_INCREASE_PER_WAVE;
    }

    // Resets all wave state variables for a new wave
    // Clears spawn counters and timers, enables spawning
    private void resetWaveState() {
        enemiesSpawnedInWave = 0;
        spawnTimer = 0;
        isCurrentlySpawning = true;
    }

    // Announces the start of a new wave to the console
    // Provides feedback about wave progression
    private void announceWaveStart() {
        System.out.println("Wave " + currentWaveNumber + " started");
    }

    // Updates the wave manager each frame
    // Handles spawn timing and enemy creation during active waves
    public void update() {
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
        return spawnTimer >= SPAWN_DELAY_FRAMES && enemiesSpawnedInWave < enemiesToSpawnInWave;
    }

    // Creates and spawns a new enemy
    // Adds enemy to the active list and resets spawn timer
    private void spawnEnemy() {
        activeEnemies.add(new Enemy(gameMap));
        enemiesSpawnedInWave++;
        spawnTimer = 0;
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
}

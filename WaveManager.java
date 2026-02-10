import java.util.ArrayList;

public class WaveManager {

    private int wave = 1;

    private int enemiesToSpawn;
    private int spawned = 0;

    private int spawnCooldown = 0;
    private int spawnDelay = 30;

    private boolean spawning = false;

    private final Map map;
    private final ArrayList<Enemy> enemies;

    public WaveManager(Map map, ArrayList<Enemy> enemies) {
        this.map = map;
        this.enemies = enemies;
    }

    public void startNextWave() {
        enemiesToSpawn = 3 + wave * 2;
        spawned = 0;
        spawnCooldown = 0;
        spawning = true;

        System.out.println("Wave " + wave + " started");
    }

    public void update() {
        if (!spawning) return;

        spawnCooldown++;

        if (spawnCooldown >= spawnDelay && spawned < enemiesToSpawn) {
            enemies.add(new Enemy(map));
            spawned++;
            spawnCooldown = 0;
        }

        if (spawned >= enemiesToSpawn) {
            spawning = false;
            wave++;
        }
    }

    public boolean isWaveFinished() {
        return !spawning && enemies.isEmpty();
    }
}

# 🚀 Extension Examples - Adding New Features

This document shows how the organized package structure makes it easy to add new features to the tower defense game.

## 📝 Example 1: Adding a New Enemy Type

### **Fast Enemy Implementation**

```java
// com/towerdefense/entities/enemies/FastEnemy.java
package com.towerdefense.entities.enemies;

import com.towerdefense.world.Map;
import com.towerdefense.managers.CoinManager;

/**
 * Fast-moving enemy with lower health but higher speed.
 * Harder to block but easier to kill.
 */
public class FastEnemy extends Enemy {
    
    private static final double FAST_SPEED = 2.4; // Double normal speed
    private static final int FAST_ENEMY_SIZE = 48; // Smaller than normal
    
    public FastEnemy(Map gameMap, CoinManager coinManager) {
        super(gameMap, coinManager);
        // Override speed and size in constructor
        this.movementSpeed = FAST_SPEED;
        this.enemySize = FAST_ENEMY_SIZE;
    }
    
    @Override
    protected String getImagePath() {
        return "image\\fastEnemy.png";
    }
}
```

### **Integration with Wave Manager**

```java
// In com/towerdefense/managers/WaveManager.java
private void spawnEnemy() {
    Enemy newEnemy;
    
    // 30% chance to spawn fast enemy after wave 3
    if (currentWaveNumber > 3 && Math.random() < 0.3) {
        newEnemy = new FastEnemy(gameMap, coinManager);
    } else {
        newEnemy = new Enemy(gameMap, coinManager);
    }
    
    activeEnemies.add(newEnemy);
    enemiesSpawnedInWave++;
    spawnTimer = 0;
}
```

**Benefits**: 
- ✅ No changes to core game loop
- ✅ No changes to existing Enemy class
- ✅ Easy to add more enemy types
- ✅ Clean inheritance hierarchy

---

## 📝 Example 2: Adding a New Defensive Unit

### **Wall Implementation**

```java
// com/towerdefense/entities/defensive/Wall.java
package com.towerdefense.entities.defensive;

import com.towerdefense.entities.base.GameObject;
import com.towerdefense.entities.base.Defensive;
import com.towerdefense.entities.base.Collidable;
import java.awt.*;

/**
 * Simple wall that blocks enemies but has no special abilities.
 * Cheaper than tanks but with less health and no defense rating.
 */
public class Wall extends GameObject implements Defensive, Collidable {
    
    private static final int WALL_HEALTH = 100;
    private static final int WALL_COST = 10;
    
    private int currentHealth = WALL_HEALTH;
    
    public Wall(int gridColumn, int gridRow, int tileSize, Image wallImage) {
        super(gridColumn, gridRow, tileSize, 1, 1, wallImage);
    }
    
    @Override
    public void takeDamage(int damageAmount) {
        currentHealth -= damageAmount; // No defense rating
        if (currentHealth < 0) currentHealth = 0;
        System.out.println("Wall HP: " + currentHealth);
    }
    
    @Override
    public boolean isDestroyed() {
        return currentHealth <= 0;
    }
    
    @Override
    public int getCurrentHealth() {
        return currentHealth;
    }
    
    @Override
    public int getMaxHealth() {
        return WALL_HEALTH;
    }
    
    @Override
    public Rectangle getCollisionBounds() {
        return new Rectangle(positionX, positionY, objectWidth, objectHeight);
    }
    
    public static int getCost() {
        return WALL_COST;
    }
}
```

### **Integration with Coin Manager**

```java
// In com/towerdefense/managers/CoinManager.java
private static final int WALL_COST = 10;

public int getWallCost() {
    return WALL_COST;
}

public boolean purchaseWall() {
    return spendCoins(WALL_COST);
}
```

**Benefits**:
- ✅ Implements same interfaces as Tank
- ✅ Easy to add to UI and placement system
- ✅ Consistent with existing defensive units
- ✅ No changes to enemy collision logic

---

## 📝 Example 3: Adding UI Components

### **Wave Display Component**

```java
// com/towerdefense/ui/hud/WaveDisplay.java
package com.towerdefense.ui.hud;

import java.awt.*;

/**
 * Displays current wave number and progress.
 */
public class WaveDisplay {
    
    private static final Font WAVE_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Color WAVE_COLOR = Color.WHITE;
    private static final Color BACKGROUND_COLOR = new Color(0, 0, 0, 128);
    
    public void render(Graphics graphics, int x, int y, int currentWave, 
                      int enemiesRemaining, int totalEnemies) {
        
        String waveText = "Wave " + currentWave;
        String progressText = enemiesRemaining + "/" + totalEnemies + " enemies";
        
        graphics.setFont(WAVE_FONT);
        FontMetrics fm = graphics.getFontMetrics();
        
        int textWidth = Math.max(fm.stringWidth(waveText), 
                                fm.stringWidth(progressText));
        int textHeight = fm.getHeight() * 2;
        
        // Draw background
        graphics.setColor(BACKGROUND_COLOR);
        graphics.fillRect(x, y, textWidth + 20, textHeight + 10);
        
        // Draw text
        graphics.setColor(WAVE_COLOR);
        graphics.drawString(waveText, x + 10, y + fm.getHeight());
        graphics.drawString(progressText, x + 10, y + fm.getHeight() * 2);
    }
}
```

### **Integration with Game Panel**

```java
// In com/towerdefense/core/GamePanel.java
private WaveDisplay waveDisplay = new WaveDisplay();

private void renderUserInterface(Graphics graphics) {
    drawHeroBar(graphics);
    drawTankIcon(graphics);
    drawDragPreview(graphics);
    
    // Add wave display
    waveDisplay.render(graphics, 10, 10, 
                      waveManager.getCurrentWave(),
                      activeEnemies.size(),
                      waveManager.getTotalEnemiesInWave());
}
```

**Benefits**:
- ✅ Reusable UI component
- ✅ Easy to position and style
- ✅ No coupling to game logic
- ✅ Can be used in different screens

---

## 📝 Example 4: Adding a New Manager

### **Upgrade Manager**

```java
// com/towerdefense/managers/UpgradeManager.java
package com.towerdefense.managers;

import com.towerdefense.entities.defensive.Tank;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages unit upgrades and improvements.
 */
public class UpgradeManager {
    
    private Map<Tank, Integer> tankUpgradeLevels = new HashMap<>();
    private static final int UPGRADE_COST = 50;
    private static final int MAX_UPGRADE_LEVEL = 3;
    
    public boolean canUpgrade(Tank tank) {
        int currentLevel = tankUpgradeLevels.getOrDefault(tank, 0);
        return currentLevel < MAX_UPGRADE_LEVEL;
    }
    
    public boolean upgradeTank(Tank tank, CoinManager coinManager) {
        if (!canUpgrade(tank) || !coinManager.spendCoins(UPGRADE_COST)) {
            return false;
        }
        
        int newLevel = tankUpgradeLevels.getOrDefault(tank, 0) + 1;
        tankUpgradeLevels.put(tank, newLevel);
        
        // Apply upgrade benefits (example: increase max health)
        // This would require extending the Tank class or using composition
        
        return true;
    }
    
    public int getUpgradeLevel(Tank tank) {
        return tankUpgradeLevels.getOrDefault(tank, 0);
    }
}
```

**Benefits**:
- ✅ Separate concern from other managers
- ✅ Easy to extend with new upgrade types
- ✅ Clean integration with existing systems
- ✅ Testable in isolation

---

## 📝 Example 5: Adding Constants and Configuration

### **Enhanced Constants**

```java
// com/towerdefense/utils/Constants.java
public final class Constants {
    
    // Enemy Types
    public static final class Enemies {
        public static final class Basic {
            public static final int SIZE = 64;
            public static final double SPEED = 1.2;
            public static final int ATTACK_DAMAGE = 5;
            public static final int REWARD_COINS = 10;
        }
        
        public static final class Fast {
            public static final int SIZE = 48;
            public static final double SPEED = 2.4;
            public static final int ATTACK_DAMAGE = 3;
            public static final int REWARD_COINS = 15;
        }
        
        public static final class Armored {
            public static final int SIZE = 72;
            public static final double SPEED = 0.8;
            public static final int ATTACK_DAMAGE = 8;
            public static final int REWARD_COINS = 25;
        }
    }
    
    // Defensive Units
    public static final class Defensive {
        public static final class Tank {
            public static final int HEALTH = 200;
            public static final int DEFENSE_RATING = 5;
            public static final int COST = 25;
        }
        
        public static final class Wall {
            public static final int HEALTH = 100;
            public static final int DEFENSE_RATING = 0;
            public static final int COST = 10;
        }
    }
    
    // Upgrades
    public static final class Upgrades {
        public static final int COST_PER_LEVEL = 50;
        public static final int MAX_LEVEL = 3;
        public static final double HEALTH_INCREASE_PER_LEVEL = 1.5;
        public static final double DEFENSE_INCREASE_PER_LEVEL = 1.2;
    }
}
```

**Benefits**:
- ✅ Centralized configuration
- ✅ Easy to balance game mechanics
- ✅ Type-safe constants
- ✅ Organized by functional area

---

## 🎯 Key Takeaways

### **Why This Structure Works**

1. **Single Responsibility**: Each package has one clear purpose
2. **Open/Closed Principle**: Easy to extend without modifying existing code
3. **Dependency Inversion**: Depend on interfaces, not concrete classes
4. **Interface Segregation**: Small, focused interfaces
5. **Don't Repeat Yourself**: Reusable components and utilities

### **Adding New Features is Easy**

- **New Enemy Types**: Extend Enemy class or implement interfaces
- **New Defensive Units**: Implement Defensive and Collidable interfaces
- **New UI Components**: Create in ui package, integrate in GamePanel
- **New Managers**: Add to managers package, initialize in GamePanel
- **New Utilities**: Add to utils package, use throughout codebase

### **Maintenance Benefits**

- **Easy Testing**: Each component can be tested independently
- **Clear Dependencies**: Know exactly what each component needs
- **Consistent Patterns**: Same approach for similar features
- **Documentation**: Package structure self-documents the architecture

This organized structure transforms the codebase from a collection of files into a well-architected system that's easy to understand, extend, and maintain.
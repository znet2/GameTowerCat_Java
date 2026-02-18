# EnemyBoss Class Documentation

## ภาพรวม
คลาส `EnemyBoss` เป็นศัตรู Boss ที่มีพลังชีวิต ความเสียหาย และรางวัลเหรียญสูงกว่าศัตรูปกติ Boss มีขนาดใหญ่กว่า เคลื่อนที่ช้ากว่า แต่แข็งแกร่งกว่าศัตรูปกติมาก

## Package
`com.towerdefense.entities.enemies`

## Imports
```java
import com.towerdefense.world.Map;
import com.towerdefense.managers.CoinManager;
import com.towerdefense.utils.Constants;
import java.awt.*;
import javax.swing.ImageIcon;
```

## การสืบทอด
```java
public class EnemyBoss extends BaseEnemy
```
- Extends `BaseEnemy` เพื่อใช้ logic ที่ใช้ร่วมกันกับศัตรูปกติ

## คุณสมบัติหลัก (Fields)

### Visual Properties
- `bossImage` (Image, private, final): รูปภาพของ Boss

## Constructor

### `public EnemyBoss(Map gameMap, CoinManager coinManager)`
สร้าง Boss enemy และตั้งค่าเส้นทาง

**Parameters:**
- `gameMap` (Map): อ้างอิงถึงแผนที่เกม
- `coinManager` (CoinManager): อ้างอิงถึงระบบเหรียญ

**การทำงาน:**
1. เรียก super constructor ด้วย BOSS_INITIAL_HEALTH
2. โหลดรูปภาพ Boss จาก Constants.Paths.BOSS_IMAGE
3. สร้างเส้นทางและตั้งค่าตำแหน่งเริ่มต้น (ผ่าน BaseEnemy)

## Implemented Abstract Methods

### `protected int getMaxHealth()`
**Returns:** `Constants.Entities.BOSS_INITIAL_HEALTH` (10000)

### `protected int getAttackDamage()`
**Returns:** `Constants.Entities.BOSS_ATTACK_DAMAGE` (1000)

### `protected int getAttackCooldown()`
**Returns:** `Constants.Entities.BOSS_ATTACK_COOLDOWN_FRAMES` (45 frames = 0.75 วินาที)

### `protected double getSpeed()`
**Returns:** `Constants.Entities.BOSS_SPEED` (0.3 - ช้ากว่าศัตรูปกติ)

### `protected int getSize()`
**Returns:** `Constants.Entities.BOSS_SIZE` (96 pixels - ใหญ่กว่าศัตรูปกติ 50%)

### `protected int getXOffset()`
**Returns:** `Constants.Entities.BOSS_X_OFFSET` (-48)

### `protected int getYOffset()`
**Returns:** `Constants.Entities.BOSS_Y_OFFSET` (-48)

### `protected Image getImage()`
**Returns:** `bossImage` - รูปภาพ Boss

### `protected int getCoinReward()`
**Returns:** `Constants.Economy.COINS_PER_BOSS_KILL` (100 เหรียญ - มากกว่าศัตรูปกติ 6.67 เท่า)

## คุณสมบัติของ Boss

### ข้อมูลเปรียบเทียบกับศัตรูปกติ

| คุณสมบัติ | ศัตรูปกติ | Boss | อัตราส่วน |
|----------|----------|------|-----------|
| พลังชีวิต | 3,000 | 10,000 | 3.33x |
| ความเสียหาย | 500 | 1,000 | 2x |
| ความเร็ว | 0.5 | 0.3 | 0.6x (ช้ากว่า) |
| ขนาด | 64px | 96px | 1.5x |
| Cooldown | 60 frames | 45 frames | 0.75x (เร็วกว่า) |
| รางวัลเหรียญ | 15 | 100 | 6.67x |

### จุดเด่นของ Boss
1. **พลังชีวิตสูง**: มีเลือดมากกว่าศัตรูปกติ 3.33 เท่า
2. **ความเสียหายสูง**: สร้างความเสียหายมากกว่า 2 เท่า
3. **โจมตีเร็วขึ้น**: Cooldown สั้นกว่า (0.75 วินาที vs 1 วินาที)
4. **ขนาดใหญ่**: ใหญ่กว่า 50% ทำให้เห็นชัดเจน
5. **เคลื่อนที่ช้า**: ช้ากว่า 40% ให้เวลาผู้เล่นเตรียมตัว
6. **รางวัลสูง**: ให้เหรียญมากกว่า 6.67 เท่า

## การทำงาน

### Pathfinding
- ใช้ BFS algorithm เหมือนศัตรูปกติ (จาก BaseEnemy)
- หาเส้นทางจากซ้ายสุดไปยังบ้าน

### Movement
- เคลื่อนที่ช้ากว่าศัตรูปกติ (0.3 vs 0.5)
- ทำให้ผู้เล่นมีเวลาเตรียมตัวมากขึ้น

### Combat
- โจมตีหน่วยป้องกันและบ้านเหมือนศัตรูปกติ
- สร้างความเสียหายมากกว่า 2 เท่า
- Cooldown สั้นกว่า (โจมตีเร็วขึ้น)

### Rewards
- ให้เหรียญ 100 เหรียญเมื่อถูกฆ่า
- มากกว่าศัตรูปกติ 6.67 เท่า

## Constants ที่ใช้

จาก `Constants.Entities`:
- `BOSS_INITIAL_HEALTH = 10000` - พลังชีวิตเริ่มต้น
- `BOSS_ATTACK_DAMAGE = 1000` - ความเสียหายต่อการโจมตี
- `BOSS_ATTACK_COOLDOWN_FRAMES = 45` - cooldown การโจมตี
- `BOSS_SPEED = 0.3` - ความเร็วการเคลื่อนที่
- `BOSS_SIZE = 96` - ขนาด Boss
- `BOSS_X_OFFSET = -48` - offset แกน X (ครึ่งหนึ่งของขนาด)
- `BOSS_Y_OFFSET = -48` - offset แกน Y (ครึ่งหนึ่งของขนาด)
- `BOSS_INITIAL_HEALTH = 10000` - เลือดเริ่มต้น
- `BOSS_ATTACK_DAMAGE = 1000` - ความเสียหายต่อการโจมตี
- `BOSS_ATTACK_COOLDOWN_FRAMES = 45` - cooldown การโจมตี

จาก `Constants.Paths`:
- `BOSS_IMAGE` - เส้นทางรูปภาพ Boss

จาก `Constants.Economy`:
- `COINS_PER_BOSS_KILL = 100` - รางวัลเหรียญ

## ความสัมพันธ์กับคลาสอื่น

### BaseEnemy.java
- Extends BaseEnemy เพื่อใช้ logic ที่ใช้ร่วมกัน
- Override abstract methods เพื่อกำหนดค่าเฉพาะของ Boss

### Map.java
- ใช้สำหรับ pathfinding และ collision detection

### CoinManager.java
- ให้รางวัลเหรียญเมื่อ Boss ถูกฆ่า

### WaveManager.java
- สามารถสร้าง Boss แทนศัตรูปกติได้

### GamePanel.java
- อัปเดตและวาด Boss เหมือนศัตรูปกติ

## การใช้งาน

```java
// สร้าง Boss
EnemyBoss boss = new EnemyBoss(gameMap, coinManager);

// อัปเดต Boss ในแต่ละเฟรม
boss.update();

// วาด Boss
boss.draw(graphics);

// ให้ความเสียหาย Boss
boss.takeDamage(250);

// ตรวจสอบว่า Boss ตายหรือไม่
if (boss.isDead()) {
    // ลบ Boss ออกจากเกม
    // ผู้เล่นได้รับ 100 เหรียญ
}
```

## ตัวอย่างการใช้ใน WaveManager

```java
// สร้าง Boss ใน wave สุดท้าย
if (currentWaveNumber == Constants.Waves.MAX_WAVES) {
    // Spawn boss instead of normal enemy
    activeEnemies.add(new EnemyBoss(gameMap, coinManager));
} else {
    // Spawn normal enemy
    activeEnemies.add(new Enemy(gameMap, coinManager));
}
```

## กลยุทธ์การเล่น

### สำหรับผู้เล่น
1. **เตรียมตัวล่วงหน้า**: Boss เคลื่อนที่ช้า ใช้เวลานี้วางหน่วยป้องกัน
2. **ใช้หน่วยหลายตัว**: Boss มีเลือดมาก ต้องใช้หน่วยหลายตัวโจมตีพร้อมกัน
3. **ป้องกันบ้าน**: Boss สร้างความเสียหายสูง อย่าให้ถึงบ้าน
4. **รางวัลคุ้มค่า**: ฆ่า Boss ได้ 100 เหรียญ คุ้มค่ากับความยาก

### สำหรับ Game Designer
1. **Balance**: Boss ควรยากพอที่ท้าทาย แต่ไม่ยากเกินไป
2. **Timing**: ควร spawn Boss ใน wave สุดท้ายหรือ wave พิเศษ
3. **Rewards**: รางวัลควรคุ้มค่ากับความยาก
4. **Visual**: ขนาดใหญ่ทำให้ผู้เล่นรู้ว่านี่คือ Boss

## หมายเหตุ

1. Boss ใช้รูปภาพเดียวกับศัตรูปกติในตอนนี้ (สามารถเปลี่ยนได้ภายหลัง)
2. Boss เคลื่อนที่ช้ากว่าเพื่อให้ผู้เล่นมีเวลาเตรียมตัว
3. Boss ให้รางวัลเหรียญมากกว่าเพื่อชดเชยความยาก
4. สามารถปรับค่าต่างๆ ผ่าน Constants.java ได้ง่าย
5. Boss ใช้ logic เดียวกับศัตรูปกติ (จาก BaseEnemy) ทำให้ maintain ง่าย

## การขยายระบบในอนาคต

1. **Boss หลายประเภท**: สร้าง Boss ที่มีความสามารถพิเศษต่างกัน
2. **Boss Skills**: เพิ่มทักษะพิเศษให้ Boss (เช่น AOE attack, heal)
3. **Boss Phases**: เพิ่มระยะต่างๆ ของ Boss (เช่น enrage เมื่อเลือดต่ำ)
4. **Boss Minions**: Boss สามารถเรียกศัตรูเล็กมาช่วยได้
5. **Boss Loot**: เพิ่มรางวัลพิเศษนอกจากเหรียญ

# Enemy Class Documentation

## ภาพรวม
คลาส `Enemy` เป็นศัตรูปกติในเกม Tower Defense ที่เคลื่อนที่ตามเส้นทางและโจมตีหน่วยป้องกัน Extends `BaseEnemy` เพื่อใช้ logic ที่ใช้ร่วมกันกับศัตรูประเภทอื่น

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
public class Enemy extends BaseEnemy
```
- Extends `BaseEnemy` เพื่อใช้ logic ที่ใช้ร่วมกันกับ EnemyBoss

## คุณสมบัติหลัก (Fields)

### Visual Properties
- `enemyImage` (Image, private, final): รูปภาพของศัตรู

## Constructor

### `public Enemy(Map gameMap, CoinManager coinManager)`
สร้างศัตรูใหม่และตั้งค่าเส้นทาง

**Parameters:**
- `gameMap` (Map): อ้างอิงถึงแผนที่เกม
- `coinManager` (CoinManager): อ้างอิงถึงระบบเหรียญ

**การทำงาน:**
1. เรียก super constructor ด้วย ENEMY_INITIAL_HEALTH
2. โหลดรูปภาพศัตรูจาก Constants.Paths.ENEMY_IMAGE
3. สร้างเส้นทางและตั้งค่าตำแหน่งเริ่มต้น (ผ่าน BaseEnemy)

## Implemented Abstract Methods

### `protected int getMaxHealth()`
**Returns:** `Constants.Entities.ENEMY_INITIAL_HEALTH` (3000)

### `protected int getAttackDamage()`
**Returns:** `Constants.Entities.ENEMY_ATTACK_DAMAGE` (500)

### `protected int getAttackCooldown()`
**Returns:** `Constants.Entities.ENEMY_ATTACK_COOLDOWN_FRAMES` (60 frames = 1 วินาที)

### `protected double getSpeed()`
**Returns:** `Constants.Entities.ENEMY_SPEED` (0.5)

### `protected int getSize()`
**Returns:** `Constants.Entities.ENEMY_SIZE` (64 pixels)

### `protected int getXOffset()`
**Returns:** `Constants.Entities.ENEMY_X_OFFSET` (-32)

### `protected int getYOffset()`
**Returns:** `Constants.Entities.ENEMY_Y_OFFSET` (-32)

### `protected Image getImage()`
**Returns:** `enemyImage` - รูปภาพศัตรู

### `protected int getCoinReward()`
**Returns:** `Constants.Economy.COINS_PER_ENEMY_KILL` (15 เหรียญ)

## การทำงาน

ศัตรูปกติใช้ logic ทั้งหมดจาก `BaseEnemy`:

### Pathfinding
- ใช้ BFS algorithm หาเส้นทางจากซ้ายสุดไปยังบ้าน
- หลีกเลี่ยงช่องที่ไม่ใช่ถนน

### Movement
- เคลื่อนที่ตามเส้นทางด้วยความเร็วคงที่
- หยุดเมื่อพบหน่วยป้องกันหรือบ้าน

### Combat
- โจมตีหน่วยป้องกัน (Tank, Magic, Archer) และบ้าน
- สร้างความเสียหาย 500 ต่อการโจมตี
- Cooldown 60 frames (1 วินาที)

### Rewards
- ให้เหรียญ 15 เหรียญเมื่อถูกฆ่า
- ไม่ให้รางวัลถ้าเดินถึงจุดสิ้นสุด

## Constants ที่ใช้

จาก `Constants.Entities`:
- `ENEMY_INITIAL_HEALTH = 3000` - พลังชีวิตเริ่มต้น
- `ENEMY_ATTACK_DAMAGE = 500` - ความเสียหายต่อการโจมตี
- `ENEMY_ATTACK_COOLDOWN_FRAMES = 60` - cooldown การโจมตี
- `ENEMY_SPEED = 0.5` - ความเร็วการเคลื่อนที่
- `ENEMY_SIZE = 64` - ขนาดศัตรู
- `ENEMY_X_OFFSET = -32` - offset แกน X (ครึ่งหนึ่งของขนาด)
- `ENEMY_Y_OFFSET = -32` - offset แกน Y (ครึ่งหนึ่งของขนาด)
- `ENEMY_INITIAL_HEALTH = 3000` - เลือดเริ่มต้น
- `ENEMY_ATTACK_DAMAGE = 500` - ความเสียหายต่อการโจมตี
- `ENEMY_ATTACK_COOLDOWN_FRAMES = 60` - cooldown การโจมตี

จาก `Constants.Paths`:
- `ENEMY_IMAGE` - เส้นทางรูปภาพศัตรู

จาก `Constants.Economy`:
- `COINS_PER_ENEMY_KILL = 15` - รางวัลเหรียญ

## ความสัมพันธ์กับคลาสอื่น

### BaseEnemy.java
- Extends BaseEnemy เพื่อใช้ logic ที่ใช้ร่วมกัน
- Override abstract methods เพื่อกำหนดค่าเฉพาะของศัตรูปกติ

### EnemyBoss.java
- ศัตรูปกติและ Boss ใช้ base class เดียวกัน
- แตกต่างกันเฉพาะค่าต่างๆ (health, damage, speed, etc.)

### Map.java
- ใช้สำหรับ pathfinding และ collision detection

### CoinManager.java
- ให้รางวัลเหรียญเมื่อศัตรูถูกฆ่า

### WaveManager.java
- สร้างศัตรูในแต่ละ wave

### GamePanel.java
- อัปเดตและวาดศัตรู

## การใช้งาน

```java
// สร้างศัตรูใหม่
Enemy enemy = new Enemy(gameMap, coinManager);

// อัปเดตศัตรูในแต่ละเฟรม
enemy.update();

// วาดศัตรู
enemy.draw(graphics);

// ให้ความเสียหายศัตรู (จะให้รางวัลเหรียญถ้าตาย)
enemy.takeDamage(100);

// ตรวจสอบว่าศัตรูตายหรือไม่
if (enemy.isDead()) {
    // ลบศัตรูออกจากเกม
}
```

## หมายเหตุ

1. ศัตรูใช้ BFS pathfinding เพื่อหาเส้นทางที่สั้นที่สุดตามช่องถนน
2. ศัตรูจะหยุดและโจมตีเมื่อพบหน่วยป้องกัน (Tank, Magic, Archer) หรือบ้าน
3. ศัตรูสามารถซ้อนทับกันได้เมื่อโจมตีเป้าหมายเดียวกัน
3. ศัตรูแต่ละตัวมี attackPositionOffset ที่ไม่ซ้ำกันเพื่อป้องกันการซ้อนกัน
4. ศัตรูจะให้รางวัลเหรียญเมื่อถูกฆ่า แต่ไม่ให้รางวัลถ้าเดินถึงจุดสิ้นสุด
5. ระบบ collision detection ตรวจสอบทั้ง Tank, Magic, Archer และ House
6. ศัตรูจะโจมตีเป้าหมายจนกว่าเป้าหมายจะตาย จากนั้นจะเดินต่อ
7. Logic ทั้งหมดอยู่ใน BaseEnemy ทำให้ maintain ง่าย

## การเปรียบเทียบกับ Boss

| คุณสมบัติ | Enemy | Boss |
|----------|-------|------|
| พลังชีวิต | 3,000 | 10,000 |
| ความเสียหาย | 500 | 1,000 |
| ความเร็ว | 0.5 | 0.3 |
| ขนาด | 64px | 96px |
| Cooldown | 60 frames | 45 frames |
| รางวัลเหรียญ | 15 | 100 |

## จุดเด่นของการออกแบบ

### Clean Code
- ใช้ BaseEnemy เพื่อลด code duplication
- Override เฉพาะค่าที่แตกต่างกัน
- Logic ทั้งหมดอยู่ที่เดียว (BaseEnemy)

### Maintainability
- แก้ไข logic ได้ที่ BaseEnemy
- เพิ่มศัตรูประเภทใหม่ได้ง่าย
- ลดโอกาสเกิดข้อผิดพลาด

### Flexibility
- ปรับค่าต่างๆ ผ่าน Constants ได้ง่าย
- สามารถสร้างศัตรูประเภทใหม่โดยไม่ต้องแก้ base class

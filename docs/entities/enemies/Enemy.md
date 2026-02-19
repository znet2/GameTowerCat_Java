# Enemy.java - ศัตรูปกติ

## ภาพรวม
Enemy เป็นศัตรูปกติที่เคลื่อนที่ตามเส้นทางและโจมตีหน่วยป้องกัน สืบทอดจาก BaseEnemy เพื่อใช้ฟังก์ชันพื้นฐาน มีระบบแอนิเมชันที่สลับรูปภาพตามสถานะ

## คลาสและการสืบทอด
```java
public class Enemy extends BaseEnemy
```
- สืบทอดจาก `BaseEnemy` สำหรับ logic พื้นฐาน
- Override abstract methods เพื่อกำหนดค่าเฉพาะของศัตรูปกติ

## ตัวแปรสำคัญ

### รูปภาพ
- `Image idleImage` - รูปภาพเมื่อหยุดนิ่ง (catEnemy.png)
- `Image walkImage` - รูปภาพเมื่อเดิน (catEnemyWalk.png)
- `Image attackImage` - รูปภาพเมื่อโจมตี (catEnemyAttack.png)

## Constructor
```java
public Enemy(Map gameMap, CoinManager coinManager)
```
**การทำงาน**:
1. เรียก constructor ของ BaseEnemy ด้วย ENEMY_INITIAL_HEALTH
2. โหลดรูปภาพทั้ง 3 แบบ (idle, walk, attack)

## Implementation ของ Abstract Methods

### getMaxHealth()
return `Constants.Entities.ENEMY_INITIAL_HEALTH` (3000)

### getAttackDamage()
return `Constants.Entities.ENEMY_ATTACK_DAMAGE` (500)

### getAttackCooldown()
return `Constants.Entities.ENEMY_ATTACK_COOLDOWN_FRAMES` (60 frames = 1 วินาที)

### getSpeed()
return `Constants.Entities.ENEMY_SPEED` (0.5 พิกเซล/frame)

### getSize()
return `Constants.Entities.ENEMY_SIZE` (64 พิกเซล)

### getXOffset()
return `Constants.Entities.ENEMY_X_OFFSET` (-32 พิกเซล)

### getYOffset()
return `Constants.Entities.ENEMY_Y_OFFSET` (-36 พิกเซล)

### getIdleImage()
return `idleImage` (catEnemy.png)

### getWalkImage()
return `walkImage` (catEnemyWalk.png)

### getAttackImage()
return `attackImage` (catEnemyAttack.png)

### getCoinReward()
return `Constants.Economy.COINS_PER_ENEMY_KILL` (15 เหรียญ)

## ค่าคงที่ที่ใช้จาก Constants

### สถิติศัตรู
- `ENEMY_SIZE = 64` - ขนาด
- `ENEMY_SPEED = 0.5` - ความเร็ว
- `ENEMY_INITIAL_HEALTH = 3000` - เลือดเริ่มต้น
- `ENEMY_ATTACK_DAMAGE = 500` - ความเสียหาย
- `ENEMY_ATTACK_COOLDOWN_FRAMES = 60` - cooldown การโจมตี
- `ENEMY_X_OFFSET = -32` - offset X
- `ENEMY_Y_OFFSET = -36` - offset Y

### รูปภาพ
- `ENEMY_IMAGE = "image/catEnemy.png"` - รูปหยุดนิ่ง
- `ENEMY_WALK_IMAGE = "image/catEnemyWalk.png"` - รูปเดิน
- `ENEMY_ATTACK_IMAGE = "image/catEnemyAttack.png"` - รูปโจมตี

### รางวัล
- `COINS_PER_ENEMY_KILL = 15` - เหรียญที่ได้เมื่อฆ่า

## ระบบแอนิเมชัน

### เมื่อเดิน
สลับระหว่าง catEnemy.png และ catEnemyWalk.png ทุก 30 frames (0.5 วินาที)

```
Frame 0-29: catEnemy.png
Frame 30-59: catEnemyWalk.png
Frame 60-89: catEnemy.png
...
```

### เมื่อโจมตี
สลับระหว่าง catEnemy.png และ catEnemyAttack.png ทุก 20 frames (0.33 วินาที)

```
Frame 0-19: catEnemy.png
Frame 20-39: catEnemyAttack.png
Frame 40-59: catEnemy.png
...
```

## Methods ที่ได้จาก BaseEnemy
- `update()` - อัปเดตสถานะและแอนิเมชัน
- `updateAnimation()` - อัปเดตเฟรมแอนิเมชัน
- `getCurrentImage()` - ดึงรูปภาพปัจจุบันตามสถานะ
- `draw(Graphics)` - วาดศัตรูและแถบเลือด
- `takeDamage(int)` - รับความเสียหาย
- `isDead()` - ตรวจสอบว่าตายหรือยัง
- `getBounds()` - ดึง collision bounds

## การใช้งาน

### WaveManager.java
```java
// สร้างศัตรูปกติ
private void spawnEnemy() {
    Enemy enemy = new Enemy(gameMap, coinManager);
    enemy.setAllEnemies(enemies);
    enemies.add(enemy);
}
```

### GamePanel.java
```java
// อัปเดตและวาดศัตรูทั้งหมด
for (BaseEnemy enemy : enemies) {
    enemy.update(); // อัปเดตสถานะและแอนิเมชัน
    enemy.draw(graphics); // วาดด้วยรูปภาพที่ถูกต้อง
}
```

## จุดเด่น
- ขนาดปานกลาง (64 พิกเซล)
- ความเร็วปานกลาง (0.5 พิกเซล/frame)
- เลือดปานกลาง (3000)
- ความเสียหายปานกลาง (500)
- รางวัลปานกลาง (15 เหรียญ)
- มีแอนิเมชันครบทั้ง 3 สถานะ (idle, walk, attack)

## เปรียบเทียบกับ EnemyBoss
| คุณสมบัติ | Enemy | EnemyBoss |
|----------|-------|-----------|
| ขนาด | 64 | 96 |
| ความเร็ว | 0.5 | 0.3 |
| เลือด | 3000 | 10000 |
| ความเสียหาย | 500 | 1000 |
| รางวัล | 15 | 100 |
| รูปเดิน | catEnemyWalk.png | enemyBossFly.png |
| รูปโจมตี | catEnemyAttack.png | enemyBossAttack.png |

# EnemyBoss.java - ศัตรูบอส

## ภาพรวม
EnemyBoss เป็นศัตรูบอสที่มีเลือด ความเสียหาย และรางวัลสูงกว่าศัตรูปกติ สืบทอดจาก BaseEnemy เพื่อใช้ฟังก์ชันพื้นฐาน บอสมีขนาดใหญ่กว่า ช้ากว่า และแข็งแกร่งกว่าศัตรูปกติ มีสกิลพิเศษที่ปล่อยลูกไฟรอบตัวทุก 10 วินาที

## คลาสและการสืบทอด
```java
public class EnemyBoss extends BaseEnemy
```
- สืบทอดจาก `BaseEnemy` สำหรับ logic พื้นฐาน
- Override abstract methods เพื่อกำหนดค่าเฉพาะของบอส
- เพิ่มระบบสกิลพิเศษ

## ตัวแปรสำคัญ

### รูปภาพ
- `Image idleImage` - รูปภาพเมื่อหยุดนิ่ง (enemyBoss.png)
- `Image flyImage` - รูปภาพเมื่อบิน (enemyBossFly.png)
- `Image attackImage` - รูปภาพเมื่อโจมตี (enemyBossAttack.png)
- `Image skillImage` - รูปภาพเมื่อใช้สกิล (enemyBossSkill.png)
- `Image skillBallImage` - รูปภาพลูกไฟสกิล (BossSkillBall.png)

### สถานะสกิล
- `int skillCooldownTimer` - ตัวจับเวลา cooldown สกิล (เริ่มต้น 600 frames = 10 วินาที)
- `boolean isUsingSkill` - กำลังใช้สกิลหรือไม่
- `int skillAnimationTimer` - ตัวจับเวลาแอนิเมชันสกิล
- `ArrayList<BossSkillBall> skillBalls` - รายการลูกไฟสกิล

## Constructor
```java
public EnemyBoss(Map gameMap, CoinManager coinManager)
```
**การทำงาน**:
1. เรียก constructor ของ BaseEnemy ด้วย BOSS_INITIAL_HEALTH
2. โหลดรูปภาพทั้ง 5 แบบ (idle, fly, attack, skill, skillBall)
3. ตั้งค่า skillCooldownTimer เริ่มต้น

## Methods สำคัญ

### update()
**การทำงาน** (Override จาก BaseEnemy):
1. **อัปเดตลูกไฟสกิล**: เรียก updateSkillBalls()
2. **อัปเดตแอนิเมชันสกิล**: ตรวจสอบและจัดการ skillAnimationTimer
3. **อัปเดต cooldown สกิล**: ลด skillCooldownTimer และเรียก useSkill() เมื่อถึงเวลา
4. **เรียก parent update**: เรียก super.update() สำหรับการเคลื่อนที่และการต่อสู้ปกติ

### updateSkillBalls()
**การทำงาน**:
1. วนลูปผ่านลูกไฟทั้งหมด
2. เรียก update() สำหรับแต่ละลูก
3. ลบลูกที่ไม่ active ออกจาก list

### useSkill()
**การทำงาน**:
1. ตั้ง isUsingSkill = true และรีเซ็ต skillAnimationTimer
2. รวบรวมหน่วยป้องกันทั้งหมด (Tank, Magic, Archer, House)
3. คำนวณมุมสำหรับแต่ละลูก: `angleStep = (2 * PI) / ballCount`
4. สร้างลูกไฟ 8 ลูกในทิศทางต่างๆ รอบตัวบอส
5. เพิ่มลูกไฟเข้า skillBalls list

**สูตรการคำนวณ**:
```
ballCount = 8
angleStep = 360° / 8 = 45°
ลูกที่ 0: 0°
ลูกที่ 1: 45°
ลูกที่ 2: 90°
...
ลูกที่ 7: 315°
```

### draw(Graphics graphics)
**การทำงาน** (Override):
1. **วาดลูกไฟสกิล**: วาดก่อนเพื่อให้อยู่ด้านหลังบอส
2. **วาดบอส**: ใช้ getCurrentBossImage() เพื่อเลือกรูปภาพที่ถูกต้อง
3. **วาดแถบเลือด**: เรียก drawHealthBar()

### getCurrentBossImage()
**การทำงาน**:
- หากกำลังใช้สกิล: return skillImage
- หากไม่ใช่: return getCurrentImage() (จาก BaseEnemy)

## Implementation ของ Abstract Methods

### getMaxHealth()
return `Constants.Entities.BOSS_INITIAL_HEALTH` (10000)

### getAttackDamage()
return `Constants.Entities.BOSS_ATTACK_DAMAGE` (1000)

### getAttackCooldown()
return `Constants.Entities.BOSS_ATTACK_COOLDOWN_FRAMES` (45 frames = 0.75 วินาที)

### getSpeed()
return `Constants.Entities.BOSS_SPEED` (0.3 พิกเซล/frame)

### getSize()
return `Constants.Entities.BOSS_SIZE` (96 พิกเซล)

### getXOffset()
return `Constants.Entities.BOSS_X_OFFSET` (-48 พิกเซล)

### getYOffset()
return `Constants.Entities.BOSS_Y_OFFSET` (-48 พิกเซล)

### getIdleImage()
return `idleImage` (enemyBoss.png)

### getWalkImage()
return `flyImage` (enemyBossFly.png)

### getAttackImage()
return `attackImage` (enemyBossAttack.png)

### getCoinReward()
return `Constants.Economy.COINS_PER_BOSS_KILL` (100 เหรียญ)

## ค่าคงที่ที่ใช้จาก Constants

### สถิติบอส
- `BOSS_SIZE = 96` - ขนาด (ใหญ่กว่า Enemy)
- `BOSS_SPEED = 0.3` - ความเร็ว (ช้ากว่า Enemy)
- `BOSS_INITIAL_HEALTH = 10000` - เลือดเริ่มต้น (มากกว่า Enemy มาก)
- `BOSS_ATTACK_DAMAGE = 1000` - ความเสียหาย (สูงกว่า Enemy 2 เท่า)
- `BOSS_ATTACK_COOLDOWN_FRAMES = 45` - cooldown การโจมตี (เร็วกว่า Enemy)
- `BOSS_X_OFFSET = -48` - offset X
- `BOSS_Y_OFFSET = -48` - offset Y

### รูปภาพ
- `BOSS_IMAGE = "image/enemyBoss.png"` - รูปหยุดนิ่ง
- `BOSS_FLY_IMAGE = "image/enemyBossFly.png"` - รูปบิน
- `BOSS_ATTACK_IMAGE = "image/enemyBossAttack.png"` - รูปโจมตี
- `BOSS_SKILL_IMAGE = "image/enemyBossSkill.png"` - รูปใช้สกิล
- `BOSS_SKILL_BALL_IMAGE = "image/BossSkillBall.png"` - รูปลูกไฟสกิล

### สกิลพิเศษ
- `BOSS_SKILL_COOLDOWN_FRAMES = 600` - cooldown สกิล (10 วินาที)
- `BOSS_SKILL_ANIMATION_DURATION = 60` - ระยะเวลาแอนิเมชัน (1 วินาที)
- `BOSS_SKILL_BALL_COUNT = 8` - จำนวนลูกไฟที่ปล่อย
- `BOSS_SKILL_BALL_DAMAGE = 300` - ความเสียหายต่อลูก

### รางวัล
- `COINS_PER_BOSS_KILL = 100` - เหรียญที่ได้เมื่อฆ่า (มากกว่า Enemy มาก)

## ระบบแอนิเมชัน

### เมื่อบิน (เคลื่อนที่)
สลับระหว่าง enemyBoss.png และ enemyBossFly.png ทุก 30 frames (0.5 วินาที)

```
Frame 0-29: enemyBoss.png
Frame 30-59: enemyBossFly.png
Frame 60-89: enemyBoss.png
...
```

### เมื่อโจมตี
สลับระหว่าง enemyBoss.png และ enemyBossAttack.png ทุก 20 frames (0.33 วินาที)

```
Frame 0-19: enemyBoss.png
Frame 20-39: enemyBossAttack.png
Frame 40-59: enemyBoss.png
...
```

## Methods ที่ได้จาก BaseEnemy
- `update()` - อัปเดตสถานะและแอนิเมชัน
- `updateAnimation()` - อัปเดตเฟรมแอนิเมชัน
- `getCurrentImage()` - ดึงรูปภาพปัจจุบันตามสถานะ
- `draw(Graphics)` - วาดบอสและแถบเลือด
- `takeDamage(int)` - รับความเสียหาย
- `isDead()` - ตรวจสอบว่าตายหรือยัง
- `getBounds()` - ดึง collision bounds

## การใช้งาน

### WaveManager.java
```java
// สร้างบอส
private void spawnBoss() {
    EnemyBoss boss = new EnemyBoss(gameMap, coinManager);
    boss.setAllEnemies(enemies);
    enemies.add(boss);
}
```

### GamePanel.java
```java
// อัปเดตและวาดศัตรูทั้งหมด (รวมบอส)
for (BaseEnemy enemy : enemies) {
    enemy.update(); // อัปเดตสถานะและแอนิเมชัน
    enemy.draw(graphics); // วาดด้วยรูปภาพที่ถูกต้อง
}
```

## จุดเด่น
- ขนาดใหญ่ (96 พิกเซล)
- ความเร็วช้า (0.3 พิกเซล/frame)
- เลือดสูงมาก (10000)
- ความเสียหายสูง (1000)
- รางวัลสูง (100 เหรียญ)
- โจมตีเร็วกว่า Enemy (0.75 วินาที vs 1 วินาที)
- มีแอนิเมชันครบทั้ง 4 สถานะ (idle, fly, attack, skill)
- **สกิลพิเศษ**: ปล่อยลูกไฟ 8 ลูกรอบตัวทุก 10 วินาที

## สกิลพิเศษ: Boss Skill

### การทำงาน
1. ทุก 10 วินาที บอสจะหยุดและแสดงแอนิเมชันสกิล (enemyBossSkill.png)
2. ปล่อยลูกไฟ 8 ลูกในทิศทางต่างๆ รอบตัว (ทุก 45 องศา)
3. ลูกไฟเคลื่อนที่เป็นเส้นตรง ทำความเสียหาย 300 ต่อลูก
4. ลูกไฟสามารถทำความเสียหายกับหน่วยป้องกันทุกประเภท

### ทิศทางลูกไฟ
```
        ↑ (0°)
    ↖       ↗ (45°)
  ←           → (90°)
    ↙       ↘ (135°)
        ↓ (180°)
```

### ความเสียหายรวม
- ลูกไฟ 1 ลูก: 300 ความเสียหาย
- ลูกไฟทั้งหมด (8 ลูก): 2400 ความเสียหายสูงสุด
- หากโดนหลายลูก: อันตรายมาก!

## เปรียบเทียบกับ Enemy
| คุณสมบัติ | Enemy | EnemyBoss |
|----------|-------|-----------|
| ขนาด | 64 | 96 |
| ความเร็ว | 0.5 | 0.3 |
| เลือด | 3000 | 10000 |
| ความเสียหาย | 500 | 1000 |
| Cooldown | 60 frames | 45 frames |
| รางวัล | 15 | 100 |
| รูปเดิน | catEnemyWalk.png | enemyBossFly.png |
| รูปโจมตี | catEnemyAttack.png | enemyBossAttack.png |

## กลยุทธ์การเอาชนะ
- ใช้หน่วยป้องกันหลายตัวเพราะบอสมีเลือดมาก
- Tank ช่วยดูดความเสียหายได้ดี (ทั้งการโจมตีปกติและลูกไฟสกิล)
- Archer และ Magic ช่วยทำความเสียหายจากระยะไกล
- Assassin ทำความเสียหายสูงเมื่อบอสเข้ามาใกล้
- บอสช้า ดังนั้นมีเวลาเตรียมการป้องกัน
- **กระจายหน่วยป้องกัน**: อย่าให้อยู่ใกล้กันเกินไป เพื่อป้องกันลูกไฟสกิลโดนหลายหน่วย
- **ระวังสกิล**: เมื่อบอสแสดงแอนิเมชันสกิล เตรียมรับมือลูกไฟ 8 ลูก
- **ใช้ Tank กั้น**: วาง Tank ไว้รอบๆ เพื่อดูดลูกไฟสกิล

# BossSkillBall.java - ลูกไฟสกิลบอส

## ภาพรวม
BossSkillBall เป็นกระสุนพิเศษที่บอสปล่อยออกมาเป็นวงกลมรอบตัว เคลื่อนที่เป็นเส้นตรงในทิศทางที่กำหนด ไม่ติดตามเป้าหมายแต่จะทำความเสียหายกับหน่วยป้องกันที่ชน

## คลาสและการสืบทอด
```java
public class BossSkillBall
```
- คลาสอิสระ ไม่สืบทอดจาก Projectile เพราะมีพฤติกรรมแตกต่าง
- เคลื่อนที่เป็นเส้นตรงตามมุมที่กำหนด ไม่ติดตามเป้าหมาย

## ตัวแปรสำคัญ

### ตำแหน่งและการเคลื่อนที่
- `double positionX, positionY` - ตำแหน่งปัจจุบัน
- `double velocityX, velocityY` - ความเร็วในแต่ละแกน
- `int damage` - ความเสียหายที่จะสร้าง
- `boolean isActive` - สถานะว่ากระสุนยังใช้งานอยู่หรือไม่

### การอ้างอิง
- `Image ballImage` - รูปภาพของลูกไฟ
- `ArrayList<Defensive> defensiveUnits` - รายการหน่วยป้องกันเพื่อตรวจสอบการชน

## Constructor
```java
public BossSkillBall(double startX, double startY, double angle, int damage, Image image, ArrayList<Defensive> defensiveUnits)
```
**พารามิเตอร์**:
- `startX, startY` - ตำแหน่งเริ่มต้น (ตำแหน่งบอส)
- `angle` - มุมทิศทางเป็น radians
- `damage` - ความเสียหายที่จะสร้าง
- `image` - รูปภาพลูกไฟ
- `defensiveUnits` - รายการหน่วยป้องกันทั้งหมด

**การทำงาน**:
1. บันทึกตำแหน่งเริ่มต้นและความเสียหาย
2. คำนวณ velocityX และ velocityY จากมุมและความเร็ว:
   - `velocityX = cos(angle) * speed`
   - `velocityY = sin(angle) * speed`

## Methods สำคัญ

### update()
**การทำงาน**:
1. ตรวจสอบว่ายัง active หรือไม่
2. เคลื่อนที่ตาม velocity: `positionX += velocityX`, `positionY += velocityY`
3. ตรวจสอบว่าออกนอกหน้าจอหรือยัง - หากออกนอก ตั้ง isActive = false
4. ตรวจสอบการชนกับหน่วยป้องกัน

### checkCollisions()
**การทำงาน**:
1. ดึง bounds ของลูกไฟ
2. วนลูปตรวจสอบหน่วยป้องกันทั้งหมด
3. หากชน: เรียก hitTarget() และหยุดการทำงาน

### getUnitBounds(Defensive unit)
**การทำงาน**: ดึง bounds ของหน่วยป้องกันแต่ละประเภท (Tank, Magic, Archer, House)

### hitTarget(Defensive target)
**การทำงาน**:
1. เรียก `target.takeDamage(damage)`
2. ตั้ง `isActive = false`

### draw(Graphics graphics)
**การทำงาน**:
1. ตรวจสอบว่ายัง active
2. คำนวณตำแหน่งวาด (ตรงกลางของลูกไฟ)
3. วาดรูปภาพหากมี หรือวาดวงกลมสีแดงหากไม่มี

### isActive()
return true หากลูกไฟยังใช้งานอยู่

## ค่าคงที่ที่ใช้จาก Constants
- `BOSS_SKILL_BALL_SPEED = 3.0` - ความเร็ว (พิกเซล/frame)
- `BOSS_SKILL_BALL_SIZE = 32` - ขนาดรูปภาพ
- `BOSS_SKILL_BALL_FALLBACK_COLOR = Color.RED` - สีสำรอง
- `BOSS_SKILL_BALL_FALLBACK_SIZE = 16` - ขนาดวงกลมสำรอง
- `BOSS_SKILL_BALL_DAMAGE = 300` - ความเสียหาย
- `WINDOW_WIDTH, WINDOW_HEIGHT` - ขนาดหน้าจอสำหรับตรวจสอบขอบเขต

## การใช้งาน

### EnemyBoss.java
```java
// สร้างลูกไฟรอบตัวบอส
private void useSkill() {
    ArrayList<Defensive> defensiveUnits = new ArrayList<>();
    defensiveUnits.addAll(gameMap.getTanks());
    defensiveUnits.addAll(gameMap.getMagicTowers());
    defensiveUnits.addAll(gameMap.getArcherTowers());
    defensiveUnits.add(gameMap.getHouse());
    
    int ballCount = Constants.Entities.BOSS_SKILL_BALL_COUNT; // 8 ลูก
    double angleStep = (2 * Math.PI) / ballCount;
    
    for (int i = 0; i < ballCount; i++) {
        double angle = i * angleStep;
        BossSkillBall ball = new BossSkillBall(
            positionX, positionY, angle, 
            Constants.Entities.BOSS_SKILL_BALL_DAMAGE, 
            skillBallImage, defensiveUnits
        );
        skillBalls.add(ball);
    }
}

// อัปเดตลูกไฟทั้งหมด
private void updateSkillBalls() {
    Iterator<BossSkillBall> iterator = skillBalls.iterator();
    while (iterator.hasNext()) {
        BossSkillBall ball = iterator.next();
        ball.update();
        if (!ball.isActive()) {
            iterator.remove();
        }
    }
}

// วาดลูกไฟทั้งหมด
public void draw(Graphics graphics) {
    for (BossSkillBall ball : skillBalls) {
        ball.draw(graphics);
    }
    // ... วาดบอส
}
```

## คุณสมบัติ
- ความเร็วช้า (3.0 พิกเซล/frame)
- ขนาดใหญ่ (32 พิกเซล)
- ความเสียหายสูง (300)
- เคลื่อนที่เป็นเส้นตรง ไม่เปลี่ยนทิศทาง
- ทำความเสียหายได้ทุกหน่วยป้องกัน (Tank, Magic, Archer, House)
- หายไปเมื่อออกนอกหน้าจอหรือชนเป้าหมาย

## เปรียบเทียบกับกระสุนอื่น
| คุณสมบัติ | BossSkillBall | Arrow | MagicBall |
|----------|---------------|-------|-----------|
| ความเร็ว | 3.0 | 5.0 | 6.0 |
| ขนาด | 32 | 24 | 20 |
| ความเสียหาย | 300 | 10 | 5-20 |
| การติดตาม | ไม่ติดตาม | ติดตาม | ติดตาม |
| เป้าหมาย | ทุกหน่วย | ศัตรู | ศัตรู |
| ใช้โดย | Boss | Archer | Magic |

## จุดเด่น
- ปล่อยเป็นวงกลมรอบตัวบอส (8 ทิศทาง)
- ทำความเสียหายสูง (300)
- โจมตีได้ทุกหน่วยป้องกัน
- เคลื่อนที่เป็นเส้นตรง ทำให้หลบได้ยาก
- ใช้ร่วมกับแอนิเมชันพิเศษของบอส (enemyBossSkill.png)

## กลยุทธ์การรับมือ
- กระจายหน่วยป้องกันไม่ให้อยู่ใกล้กันเกินไป
- Tank ช่วยดูดความเสียหายได้ดี
- ระวังเมื่อบอสหยุดและแสดงแอนิเมชันสกิล
- ลูกไฟเคลื่อนที่ช้า ดังนั้นมีเวลาเตรียมรับมือ

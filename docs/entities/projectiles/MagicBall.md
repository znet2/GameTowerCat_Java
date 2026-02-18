# MagicBall.java - ลูกไฟเวทมนตร์

## ภาพรวม
MagicBall เป็นกระสุนที่ยิงออกมาจาก Magic tower เคลื่อนที่ไปยังศัตรูเป้าหมาย มี 2 แบบ: ปกติและพิเศษ

## คลาสและการสืบทอด
```java
public class MagicBall extends Projectile
```
- สืบทอดจาก `Projectile` สำหรับพฤติกรรมพื้นฐาน
- Override abstract methods เพื่อกำหนดค่าเฉพาะของลูกไฟ

## Constructor
```java
public MagicBall(double startX, double startY, BaseEnemy target, int damage, Image ballImage)
```
เรียก constructor ของ Projectile ด้วยพารามิเตอร์ที่ได้รับ

## Implementation ของ Abstract Methods

### getSpeed()
```java
@Override
protected double getSpeed() {
    return Constants.Projectiles.MAGIC_BALL_SPEED; // 6.0
}
```
return ความเร็ว 6.0 พิกเซลต่อ frame (เร็วกว่า Arrow)

### getSize()
```java
@Override
protected int getSize() {
    return Constants.Projectiles.MAGIC_BALL_SIZE; // 20
}
```
return ขนาดรูปภาพ 20 พิกเซล (เล็กกว่า Arrow)

### getFallbackColor()
```java
@Override
protected Color getFallbackColor() {
    return Constants.Projectiles.MAGIC_BALL_FALLBACK_COLOR; // Magenta
}
```
return สีม่วงแดงสำหรับวาดหากไม่มีรูปภาพ

### getFallbackSize()
```java
@Override
protected int getFallbackSize() {
    return Constants.Projectiles.MAGIC_BALL_FALLBACK_SIZE; // 10
}
```
return ขนาดวงกลมสำรอง 10 พิกเซล

## ค่าคงที่ที่ใช้จาก Constants
- `MAGIC_BALL_SPEED = 6.0` - ความเร็ว (พิกเซล/frame)
- `MAGIC_BALL_SIZE = 20` - ขนาดรูปภาพ
- `MAGIC_BALL_FALLBACK_COLOR = Color(255, 0, 255)` - สีม่วงแดง
- `MAGIC_BALL_FALLBACK_SIZE = 10` - ขนาดวงกลมสำรอง

## Methods ที่ได้จาก Projectile
- `update()` - อัปเดตตำแหน่งและตรวจสอบการชน
- `draw(Graphics)` - วาดลูกไฟบนหน้าจอ
- `isActive()` - ตรวจสอบว่ายังใช้งานอยู่หรือไม่
- `hitTarget()` - จัดการการชนกับเป้าหมาย

## การใช้งาน

### Magic.java
```java
// สร้างลูกไฟใหม่
private void shootMagicBall(boolean isSuper) {
    int startX = positionX + Constants.Entities.MAGIC_X_OFFSET + objectWidth / 2;
    int startY = positionY + Constants.Entities.MAGIC_Y_OFFSET + objectHeight / 2;
    int damage = isSuper ? Constants.Entities.MAGIC_SPELL_DAMAGE 
                        : Constants.Entities.MAGIC_ATTACK_DAMAGE;
    Image ballImage = isSuper ? superBallImage : normalBallImage;
    
    MagicBall ball = new MagicBall(startX, startY, lockedTarget, damage, ballImage);
    magicBalls.add(ball);
}

// อัปเดตลูกไฟทั้งหมด
public void update() {
    Iterator<MagicBall> ballIterator = magicBalls.iterator();
    while (ballIterator.hasNext()) {
        MagicBall ball = ballIterator.next();
        ball.update();
        if (!ball.isActive()) {
            ballIterator.remove();
        }
    }
}

// วาดลูกไฟทั้งหมด
public void draw(Graphics graphics) {
    for (MagicBall ball : magicBalls) {
        ball.draw(graphics);
    }
}
```

## ประเภทของ MagicBall

### ลูกไฟปกติ (Normal)
- รูปภาพ: normalMagicBall.png
- ความเสียหาย: 5
- ใช้ในการโจมตี 4 ครั้งแรก

### ลูกไฟพิเศษ (Super)
- รูปภาพ: superMagicBall.png
- ความเสียหาย: 20
- ใช้ในการโจมตีครั้งที่ 5 (เวทมนตร์พิเศษ)

## คุณสมบัติ
- ความเร็วสูง (6.0 พิกเซล/frame)
- ขนาดเล็กกว่า Arrow (20 vs 24)
- เร็วกว่า Arrow
- ความเสียหายแตกต่างกันตามประเภท (5 หรือ 20)

## เปรียบเทียบกับ Arrow
| คุณสมบัติ | MagicBall | Arrow |
|----------|-----------|-------|
| ความเร็ว | 6.0 | 5.0 |
| ขนาด | 20 | 24 |
| สีสำรอง | ม่วงแดง | น้ำตาล |
| ใช้โดย | Magic | Archer |
| ความเสียหาย | 5 หรือ 20 | 10 |
| ประเภท | 2 แบบ | 1 แบบ |

## จุดเด่น
- เร็วกว่า Arrow (ถึงเป้าหมายเร็วกว่า)
- มีเวทมนตร์พิเศษที่ทำความเสียหายสูง (20)
- เล็กกว่า Arrow (ดูเบากว่า)
- ใช้โดย Magic tower ที่มีระบบนับการโจมตี

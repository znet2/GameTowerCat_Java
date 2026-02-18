# Projectile.java - คลาสพื้นฐานสำหรับกระสุน

## ภาพรวม
Projectile เป็น abstract base class สำหรับกระสุนทั้งหมดในเกม จัดการพฤติกรรมพื้นฐานเช่น การเคลื่อนที่ การติดตามเป้าหมาย และการชน

## คลาสและการสืบทอด
```java
public abstract class Projectile
```
- `abstract class` - ไม่สามารถสร้าง instance ได้โดยตรง
- เป็นคลาสพื้นฐานสำหรับกระสุนทั้งหมด (Arrow, MagicBall)

## ตัวแปรสำคัญ

### ตัวแปร Protected
- `double positionX, positionY` - ตำแหน่งปัจจุบันของกระสุน
- `BaseEnemy target` - ศัตรูที่กระสุนกำลังติดตาม (Enemy หรือ Boss)
- `int damage` - ความเสียหายที่จะสร้าง
- `boolean isActive` - สถานะว่ากระสุนยังใช้งานอยู่หรือไม่
- `Image projectileImage` - รูปภาพของกระสุน

## Methods สำคัญ

### Constructor
```java
public Projectile(double startX, double startY, BaseEnemy target, int damage, Image image)
```
**พารามิเตอร์**:
- `startX, startY` - ตำแหน่งเริ่มต้น
- `target` - ศัตรูที่จะติดตามและโจมตี (Enemy หรือ EnemyBoss)
- `damage` - ความเสียหายที่จะสร้าง
- `image` - รูปภาพกระสุน

### update()
```java
public void update()
```
**การทำงาน**:
1. ตรวจสอบว่ากระสุนและเป้าหมายยังใช้งานได้
2. ใช้ Rectangle.getCenterX() และ getCenterY() เพื่อหาจุดกึ่งกลางของเป้าหมาย
3. คำนวณระยะทางและทิศทางไปยังเป้าหมาย
4. ตรวจสอบว่าถึงเป้าหมายหรือยัง (ระยะทาง < ความเร็ว)
5. เคลื่อนที่ไปยังเป้าหมายด้วยความเร็วคงที่
**หมายเหตุ**: ใช้ inline calculation เพื่อความกระชับ

### hitTarget()
```java
protected void hitTarget()
```
**การทำงาน**:
1. เรียก `target.takeDamage(damage)`
2. ตั้ง `isActive = false`

### draw(Graphics graphics)
```java
public void draw(Graphics graphics)
```
**การทำงาน**:
1. ตรวจสอบว่ากระสุนยัง active
2. วาดรูปภาพหากมี
3. วาดรูปทรงสำรอง (วงกลม) หากไม่มีรูปภาพ

### isActive()
```java
public boolean isActive()
```
return true หากกระสุนยังใช้งานอยู่

## Abstract Methods (ต้อง implement ในคลาสลูก)

### getSpeed()
```java
protected abstract double getSpeed()
```
return ความเร็วของกระสุนเป็นพิกเซลต่อ frame

### getSize()
```java
protected abstract int getSize()
```
return ขนาดของรูปภาพกระสุนเป็นพิกเซล

### getFallbackColor()
```java
protected abstract Color getFallbackColor()
```
return สีสำหรับวาดกระสุนหากไม่มีรูปภาพ

### getFallbackSize()
```java
protected abstract int getFallbackSize()
```
return ขนาดของรูปทรงสำรองเป็นพิกเซล

## คลาสที่สืบทอด

### Arrow.java
- ความเร็ว: 5.0 พิกเซล/frame
- ขนาด: 24 พิกเซล
- สีสำรอง: น้ำตาล
- ใช้โดย: Archer tower

### MagicBall.java
- ความเร็ว: 6.0 พิกเซล/frame (เร็วกว่า Arrow)
- ขนาด: 20 พิกเซล (เล็กกว่า Arrow)
- สีสำรอง: ม่วงแดง
- ใช้โดย: Magic tower

## ข้อดีของการออกแบบ

### Code Reuse
- ลดการเขียนโค้ดซ้ำระหว่าง Arrow และ MagicBall
- Logic การเคลื่อนที่และการชนเหมือนกัน

### Consistency
- กระสุนทั้งหมดทำงานแบบเดียวกัน
- ง่ายต่อการเพิ่มกระสุนประเภทใหม่

### Flexibility
- คลาสลูกกำหนดค่าเฉพาะของตัวเอง (ความเร็ว, ขนาด, สี)
- สามารถ override methods ได้หากต้องการพฤติกรรมพิเศษ

### Maintainability
- แก้ไข logic พื้นฐานที่เดียว
- ง่ายต่อการดูแลและแก้ไข

## การใช้งานในเกม

### Archer.java
```java
// ยิงได้ทั้ง Enemy และ EnemyBoss
private void shootArrow() {
    int startX = positionX + Constants.Entities.ARCHER_X_OFFSET + objectWidth / 2;
    int startY = positionY + Constants.Entities.ARCHER_Y_OFFSET + objectHeight / 2;
    
    Arrow arrow = new Arrow(startX, startY, lockedTarget, 
                           Constants.Entities.ARCHER_ATTACK_DAMAGE, arrowImage);
    arrows.add(arrow);
}

public void update() {
    // Update all arrows
    Iterator<Arrow> arrowIterator = arrows.iterator();
    while (arrowIterator.hasNext()) {
        Arrow arrow = arrowIterator.next();
        arrow.update();
        if (!arrow.isActive()) {
            arrowIterator.remove();
        }
    }
}
```

### Magic.java
```java
// ยิงได้ทั้ง Enemy และ EnemyBoss
private void shootMagicBall(boolean isSuper) {
    int startX = positionX + Constants.Entities.MAGIC_X_OFFSET + objectWidth / 2;
    int startY = positionY + Constants.Entities.MAGIC_Y_OFFSET + objectHeight / 2;
    int damage = isSuper ? Constants.Entities.MAGIC_SPELL_DAMAGE 
                        : Constants.Entities.MAGIC_ATTACK_DAMAGE;
    Image ballImage = isSuper ? superBallImage : normalBallImage;
    
    MagicBall ball = new MagicBall(startX, startY, lockedTarget, damage, ballImage);
    magicBalls.add(ball);
}
```

## การขยายในอนาคต

หากต้องการเพิ่มกระสุนประเภทใหม่:

```java
public class Fireball extends Projectile {
    public Fireball(double startX, double startY, BaseEnemy target, int damage, Image image) {
        super(startX, startY, target, damage, image);
    }
    
    @Override
    protected double getSpeed() {
        return Constants.Projectiles.FIREBALL_SPEED;
    }
    
    @Override
    protected int getSize() {
        return Constants.Projectiles.FIREBALL_SIZE;
    }
    
    @Override
    protected Color getFallbackColor() {
        return Color.ORANGE;
    }
    
    @Override
    protected int getFallbackSize() {
        return 15;
    }
}
```

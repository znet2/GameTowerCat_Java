# Arrow.java - ลูกธนู

## ภาพรวม
Arrow เป็นกระสุนที่ยิงออกมาจาก Archer tower เคลื่อนที่เป็นเส้นตรงไปยังศัตรูเป้าหมาย

## คลาสและการสืบทอด
```java
public class Arrow extends Projectile
```
- สืบทอดจาก `Projectile` สำหรับพฤติกรรมพื้นฐาน
- Override abstract methods เพื่อกำหนดค่าเฉพาะของลูกธนู

## Constructor
```java
public Arrow(double startX, double startY, BaseEnemy target, int damage, Image arrowImage)
```
เรียก constructor ของ Projectile ด้วยพารามิเตอร์ที่ได้รับ

## Implementation ของ Abstract Methods

### getSpeed()
```java
@Override
protected double getSpeed() {
    return Constants.Projectiles.ARROW_SPEED; // 5.0
}
```
return ความเร็ว 5.0 พิกเซลต่อ frame

### getSize()
```java
@Override
protected int getSize() {
    return Constants.Projectiles.ARROW_SIZE; // 24
}
```
return ขนาดรูปภาพ 24 พิกเซล

### getFallbackColor()
```java
@Override
protected Color getFallbackColor() {
    return Constants.Projectiles.ARROW_FALLBACK_COLOR; // Brown
}
```
return สีน้ำตาลสำหรับวาดหากไม่มีรูปภาพ

### getFallbackSize()
```java
@Override
protected int getFallbackSize() {
    return Constants.Projectiles.ARROW_FALLBACK_SIZE; // 6
}
```
return ขนาดวงกลมสำรอง 6 พิกเซล

## ค่าคงที่ที่ใช้จาก Constants
- `ARROW_SPEED = 5.0` - ความเร็ว (พิกเซล/frame)
- `ARROW_SIZE = 24` - ขนาดรูปภาพ
- `ARROW_FALLBACK_COLOR = Color(139, 69, 19)` - สีน้ำตาล
- `ARROW_FALLBACK_SIZE = 6` - ขนาดวงกลมสำรอง

## Methods ที่ได้จาก Projectile
- `update()` - อัปเดตตำแหน่งและตรวจสอบการชน
- `draw(Graphics)` - วาดลูกธนูบนหน้าจอ
- `isActive()` - ตรวจสอบว่ายังใช้งานอยู่หรือไม่
- `hitTarget()` - จัดการการชนกับเป้าหมาย

## การใช้งาน

### Archer.java
```java
// สร้างลูกธนูใหม่
private void shootArrow() {
    int startX = positionX + Constants.Entities.ARCHER_X_OFFSET + objectWidth / 2;
    int startY = positionY + Constants.Entities.ARCHER_Y_OFFSET + objectHeight / 2;
    
    Arrow arrow = new Arrow(startX, startY, lockedTarget, 
                           Constants.Entities.ARCHER_ATTACK_DAMAGE, arrowImage);
    arrows.add(arrow);
}

// อัปเดตลูกธนูทั้งหมด
public void update() {
    Iterator<Arrow> arrowIterator = arrows.iterator();
    while (arrowIterator.hasNext()) {
        Arrow arrow = arrowIterator.next();
        arrow.update();
        if (!arrow.isActive()) {
            arrowIterator.remove();
        }
    }
}

// วาดลูกธนูทั้งหมด
public void draw(Graphics graphics) {
    for (Arrow arrow : arrows) {
        arrow.draw(graphics);
    }
}
```

## คุณสมบัติ
- ความเร็วปานกลาง (5.0 พิกเซล/frame)
- ขนาดใหญ่กว่า MagicBall (24 vs 20)
- ช้ากว่า MagicBall
- ความเสียหายขึ้นอยู่กับ Archer (10 ต่อลูก)

## เปรียบเทียบกับ MagicBall
| คุณสมบัติ | Arrow | MagicBall |
|----------|-------|-----------|
| ความเร็ว | 5.0 | 6.0 |
| ขนาด | 24 | 20 |
| สีสำรอง | น้ำตาล | ม่วงแดง |
| ใช้โดย | Archer | Magic |
| ความเสียหาย | 10 | 5 หรือ 20 |

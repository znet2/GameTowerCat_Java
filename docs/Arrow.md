# Arrow.java - ลูกธนุจาก Archer Tower

## ภาพรวม
Arrow เป็น projectile ที่ยิงออกมาจาก Archer tower ลูกธนุเคลื่อนที่เป็นเส้นตรงไปยังเป้าหมายและทำความเสียหายเมื่อชน

## คลาสและการสืบทอด
```java
public class Arrow
```
- คลาสอิสระที่ไม่สืบทอดจากคลาสอื่น
- เป็น projectile สำหรับ Archer tower

## ตัวแปรสำคัญ

### ค่าคงที่
- `static final double ARROW_SPEED = 5.0` - ความเร็วลูกธนุ (ลดจาก 8.0 เพื่อให้เห็นชัดขึ้น)
- `static final int ARROW_SIZE = 24` - ขนาดลูกธนุ (เพิ่มจาก 16 เพื่อให้เห็นชัดขึ้น)

### ตัวแปรตำแหน่งและการเคลื่อนที่
- `double positionX, positionY` - ตำแหน่งปัจจุบันของลูกธนุ
- `Enemy target` - เป้าหมายที่ลูกธนุจะไปชน
- `int damage` - ความเสียหายที่จะทำ
- `boolean isActive` - สถานะว่าลูกธนุยังใช้งานได้หรือไม่
- `Image arrowImage` - รูปภาพลูกธนุ

## Methods อย่างละเอียด

### Constructor
```java
public Arrow(double startX, double startY, Enemy target, int damage, Image arrowImage)
```
**วัตถุประสงค์**: สร้างลูกธนุใหม่
**พารามิเตอร์**:
- `startX, startY` - ตำแหน่งเริ่มต้น (จุดกึ่งกลาง Archer tower)
- `target` - ศัตรูเป้าหมาย
- `damage` - ความเสียหายที่จะทำ (100,000)
- `arrowImage` - รูปภาพลูกธนุ (arrow.png)

**การทำงาน**:
1. ตั้งตำแหน่งเริ่มต้น
2. เก็บอ้างอิงเป้าหมายและความเสียหาย
3. ตั้งสถานะ isActive = true
4. เก็บรูปภาพลูกธนุ

### update()
```java
public void update()
```
**วัตถุประสงค์**: อัปเดตลูกธนุทุก frame
**การทำงาน**:
1. **ตรวจสอบสถานะ**: หากไม่ active หรือเป้าหมายตาย ตั้ง isActive = false
2. **คำนวณทิศทาง**: หาตำแหน่งกึ่งกลางของเป้าหมาย
3. **คำนวณระยะทาง**: ใช้ Pythagorean theorem
4. **ตรวจสอบการชน**: หากระยะทางน้อยกว่าความเร็ว เรียก hitTarget()
5. **เคลื่อนที่**: เคลื่อนที่ไปยังเป้าหมายด้วยความเร็วคงที่

**รายละเอียดการเคลื่อนที่**:
```java
// คำนวณ vector ทิศทาง
double dx = targetX - positionX;
double dy = targetY - positionY;
double distance = Math.sqrt(dx * dx + dy * dy);

// เคลื่อนที่ด้วยความเร็วคงที่
double directionX = dx / distance;
double directionY = dy / distance;
positionX += directionX * ARROW_SPEED;
positionY += directionY * ARROW_SPEED;
```

### hitTarget()
```java
private void hitTarget()
```
**วัตถุประสงค์**: จัดการเมื่อลูกธนุชนเป้าหมาย
**การทำงาน**:
1. ตรวจสอบว่าเป้าหมายยังมีชีวิตหรือไม่
2. หากยังมีชีวิต เรียก target.takeDamage(damage)
3. ตั้ง isActive = false เพื่อลบลูกธนุ

### draw(Graphics graphics)
```java
public void draw(Graphics graphics)
```
**วัตถุประสงค์**: วาดลูกธนุบนหน้าจอ
**การทำงาน**:
1. **ตรวจสอบสถานะ**: หากไม่ active ไม่วาดอะไร
2. **วาดรูปภาพ**: หากมี arrowImage วาดรูปภาพ
3. **Fallback**: หากไม่มีรูปภาพ วาดวงกลมสีน้ำตาล

**การวาดรูปภาพ**:
```java
graphics.drawImage(
    arrowImage,
    (int) positionX - ARROW_SIZE / 2,  // จัดกึ่งกลาง
    (int) positionY - ARROW_SIZE / 2,  // จัดกึ่งกลาง
    ARROW_SIZE,
    ARROW_SIZE,
    null
);
```

**Fallback การวาด**:
```java
graphics.setColor(new Color(139, 69, 19)); // สีน้ำตาล
graphics.fillOval(
    (int) positionX - 3,
    (int) positionY - 3,
    6,
    6
);
```

### isActive()
```java
public boolean isActive()
```
**วัตถุประสงค์**: ตรวจสอบว่าลูกธนุยังใช้งานได้หรือไม่
**การทำงาน**: return isActive
**การใช้งาน**: ใช้โดย Archer เพื่อลบลูกธนุที่ไม่ active แล้ว

## ระบบการทำงาน

### การสร้างลูกธนุ
```java
// ใน Archer.java
private void shootArrow() {
    if (lockedTarget == null || lockedTarget.isDead()) {
        return;
    }

    int startX = positionX + objectWidth / 2;
    int startY = positionY + Y_OFFSET + objectHeight / 2;

    Arrow arrow = new Arrow(startX, startY, lockedTarget, 
                           Constants.Entities.ARCHER_ATTACK_DAMAGE, arrowImage);
    arrows.add(arrow);
}
```

### การอัปเดตลูกธนุ
```java
// ใน Archer.java
public void update() {
    Iterator<Arrow> arrowIterator = arrows.iterator();
    while (arrowIterator.hasNext()) {
        Arrow arrow = arrowIterator.next();
        arrow.update();
        if (!arrow.isActive()) {
            arrowIterator.remove();
        }
    }
    // ... other update logic
}
```

### การวาดลูกธนุ
```java
// ใน Archer.java
public void draw(Graphics graphics) {
    // วาด Archer tower
    graphics.drawImage(objectImage, positionX, positionY + Y_OFFSET, 
                      objectWidth, objectHeight, null);

    // วาดลูกธนุทั้งหมด
    for (Arrow arrow : arrows) {
        arrow.draw(graphics);
    }
}
```

## คุณสมบัติของลูกธนุ

### การเคลื่อนที่
- **ความเร็ว**: 5.0 พิกเซลต่อ frame
- **ทิศทาง**: เคลื่อนที่เป็นเส้นตรงไปยังเป้าหมาย
- **การติดตาม**: ติดตามเป้าหมายที่เคลื่อนที่

### ความเสียหาย
- **ความเสียหายสูง**: 100,000 จุด
- **One-shot kill**: สามารถฆ่าศัตรูได้ในการโจมตีเดียว
- **ไม่มีการลดทอน**: ไม่มีค่าป้องกันหรือการลดความเสียหาย

### การแสดงผล
- **ขนาด**: 24x24 พิกเซล
- **รูปภาพ**: arrow.png
- **Fallback**: วงกลมสีน้ำตาลขนาดเล็ก

### การจัดการหน่วยความจำ
- **Auto-cleanup**: ลูกธนุถูกลบอัตโนมัติเมื่อชนเป้าหมาย
- **Target validation**: ตรวจสอบเป้าหมายทุก frame
- **Iterator safety**: ใช้ Iterator เพื่อลบอย่างปลอดภัย

## การเปรียบเทียบกับ MagicBall

### ความเหมือน
- ทั้งคู่เป็น projectiles
- เคลื่อนที่ไปยังเป้าหมาย
- ทำความเสียหายเมื่อชน
- มีระบบ cleanup เหมือนกัน

### ความแตกต่าง

| คุณสมบัติ | Arrow | MagicBall |
|----------|-------|-----------|
| ความเร็ว | 5.0 | 6.0 |
| ขนาด | 24x24 | 20x20 |
| ความเสียหาย | 100,000 | 5-20 |
| สี Fallback | น้ำตาล | ม่วงแดง |
| ความถี่ | 1.5 วินาที | 0.75 วินาที |

## ความสัมพันธ์กับคลาสอื่น

### Archer.java
- **การสร้าง**: Archer สร้าง Arrow เมื่อโจมตี
- **การจัดการ**: Archer เก็บ ArrayList<Arrow>
- **การอัปเดต**: Archer อัปเดตและลบ Arrow
- **การแสดงผล**: Archer วาด Arrow ทั้งหมด

### Enemy.java
- **เป้าหมาย**: Arrow ใช้ Enemy เป็นเป้าหมาย
- **การทำความเสียหาย**: เรียก enemy.takeDamage()
- **การติดตาม**: ใช้ enemy.getBounds() เพื่อติดตาม

### Constants.java
- **ความเสียหาย**: ใช้ ARCHER_ATTACK_DAMAGE
- **รูปภาพ**: ใช้ ARROW_IMAGE path

## จุดเด่นของการออกแบบ

### ความเรียบง่าย
- โครงสร้างง่าย มีหน้าที่เดียว
- ไม่มีความซับซ้อนเกินความจำเป็น

### ประสิทธิภาพ
- ใช้ double สำหรับตำแหน่งเพื่อความแม่นยำ
- การคำนวณที่เรียบง่ายและรวดเร็ว

### ความยืดหยุ่น
- รองรับการไม่มีรูปภาพ (fallback)
- สามารถปรับความเร็วและขนาดได้ง่าย

### การจัดการหน่วยความจำ
- ลบตัวเองเมื่อไม่ใช้งานแล้ว
- ไม่มี memory leak

## การปรับปรุงที่เป็นไปได้

### เพิ่มเอฟเฟกต์การบิน
```java
private double rotation = 0;

public void update() {
    // คำนวณมุมการหมุน
    double dx = targetX - positionX;
    double dy = targetY - positionY;
    rotation = Math.atan2(dy, dx);
    
    // ... existing movement code
}

public void draw(Graphics graphics) {
    Graphics2D g2d = (Graphics2D) graphics;
    g2d.rotate(rotation, positionX, positionY);
    // วาดลูกธนุที่หมุนตามทิศทาง
}
```

### เพิ่มเส้นทางโค้ง
```java
private double gravity = 0.1;
private double velocityY = -2.0; // ความเร็วเริ่มต้นในแนวตั้ง

public void update() {
    velocityY += gravity;
    positionY += velocityY;
    // ... existing horizontal movement
}
```

### เพิ่มเอฟเฟกต์พิเศษ
```java
private void hitTarget() {
    if (target != null && !target.isDead()) {
        target.takeDamage(damage);
        createImpactEffect(); // เอฟเฟกต์เมื่อชน
        playHitSound();       // เสียงเมื่อชน
    }
    isActive = false;
}
```
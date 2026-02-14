# MagicBall.java - ลูกไฟเวทมนตร์จาก Magic Tower

## ภาพรวม
MagicBall เป็น projectile ที่ยิงออกมาจาก Magic tower ลูกไฟเคลื่อนที่ไปยังเป้าหมายและทำความเสียหาย มีทั้งแบบปกติและแบบพิเศษ

## คลาสและการสืบทอด
```java
public class MagicBall
```
- คลาสอิสระที่ไม่สืบทอดจากคลาสอื่น
- เป็น projectile สำหรับ Magic tower

## ตัวแปรสำคัญ

### ค่าคงที่
- `static final double MAGIC_BALL_SPEED = 6.0` - ความเร็วลูกไฟ
- `static final int MAGIC_BALL_SIZE = 20` - ขนาดลูกไฟ

### ตัวแปรตำแหน่งและการเคลื่อนที่
- `double positionX, positionY` - ตำแหน่งปัจจุบันของลูกไฟ
- `Enemy target` - เป้าหมายที่ลูกไฟจะไปชน
- `int damage` - ความเสียหายที่จะทำ (5 หรือ 20)
- `boolean isActive` - สถานะว่าลูกไฟยังใช้งานได้หรือไม่
- `Image ballImage` - รูปภาพลูกไฟ (ปกติหรือพิเศษ)

## Methods อย่างละเอียด

### Constructor
```java
public MagicBall(double startX, double startY, Enemy target, int damage, Image ballImage)
```
**วัตถุประสงค์**: สร้างลูกไฟเวทมนตร์ใหม่
**พารามิเตอร์**:
- `startX, startY` - ตำแหน่งเริ่มต้น (จุดกึ่งกลาง Magic tower)
- `target` - ศัตรูเป้าหมาย
- `damage` - ความเสียหายที่จะทำ (5 สำหรับปกติ, 20 สำหรับพิเศษ)
- `ballImage` - รูปภาพลูกไฟ (normalMagicBall.png หรือ superMagicBall.png)

**การทำงาน**:
1. ตั้งตำแหน่งเริ่มต้น
2. เก็บอ้างอิงเป้าหมายและความเสียหาย
3. ตั้งสถานะ isActive = true
4. เก็บรูปภาพลูกไฟ

### update()
```java
public void update()
```
**วัตถุประสงค์**: อัปเดตลูกไฟทุก frame
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
positionX += directionX * MAGIC_BALL_SPEED;
positionY += directionY * MAGIC_BALL_SPEED;
```

### hitTarget()
```java
private void hitTarget()
```
**วัตถุประสงค์**: จัดการเมื่อลูกไฟชนเป้าหมาย
**การทำงาน**:
1. ตรวจสอบว่าเป้าหมายยังมีชีวิตหรือไม่
2. หากยังมีชีวิต เรียก target.takeDamage(damage)
3. ตั้ง isActive = false เพื่อลบลูกไฟ

### draw(Graphics graphics)
```java
public void draw(Graphics graphics)
```
**วัตถุประสงค์**: วาดลูกไฟบนหน้าจอ
**การทำงาน**:
1. **ตรวจสอบสถานะ**: หากไม่ active ไม่วาดอะไร
2. **วาดรูปภาพ**: หากมี ballImage วาดรูปภาพ
3. **Fallback**: หากไม่มีรูปภาพ วาดวงกลมสีม่วงแดง

**การวาดรูปภาพ**:
```java
graphics.drawImage(
    ballImage,
    (int) positionX - MAGIC_BALL_SIZE / 2,  // จัดกึ่งกลาง
    (int) positionY - MAGIC_BALL_SIZE / 2,  // จัดกึ่งกลาง
    MAGIC_BALL_SIZE,
    MAGIC_BALL_SIZE,
    null
);
```

**Fallback การวาด**:
```java
graphics.setColor(new Color(255, 0, 255)); // สีม่วงแดง (Magenta)
graphics.fillOval(
    (int) positionX - 5,
    (int) positionY - 5,
    10,
    10
);
```

### isActive()
```java
public boolean isActive()
```
**วัตถุประสงค์**: ตรวจสอบว่าลูกไฟยังใช้งานได้หรือไม่
**การทำงาน**: return isActive
**การใช้งาน**: ใช้โดย Magic เพื่อลบลูกไฟที่ไม่ active แล้ว

## ประเภทของ MagicBall

### ลูกไฟปกติ (Normal Magic Ball)
- **รูปภาพ**: normalMagicBall.png
- **ความเสียหาย**: 5 จุด
- **การใช้งาน**: การโจมตี 4 ครั้งแรกของ Magic tower
- **สี Fallback**: ม่วงแดง

### ลูกไฟพิเศษ (Super Magic Ball)
- **รูปภาพ**: superMagicBall.png
- **ความเสียหาย**: 20 จุด
- **การใช้งาน**: การโจมตีครั้งที่ 5 (เวทมนตร์พิเศษ)
- **สี Fallback**: ม่วงแดง (เหมือนกัน)

## ระบบการทำงาน

### การสร้างลูกไฟ
```java
// ใน Magic.java
private void shootMagicBall(boolean isSuper) {
    if (lockedTarget == null || lockedTarget.isDead()) {
        return;
    }

    int startX = positionX + objectWidth / 2;
    int startY = positionY + Y_OFFSET + objectHeight / 2;
    int damage = isSuper ? Constants.Entities.MAGIC_SPELL_DAMAGE : 
                          Constants.Entities.MAGIC_ATTACK_DAMAGE;
    Image ballImage = isSuper ? superBallImage : normalBallImage;

    MagicBall ball = new MagicBall(startX, startY, lockedTarget, damage, ballImage);
    magicBalls.add(ball);
}
```

### การอัปเดตลูกไฟ
```java
// ใน Magic.java
public void update() {
    Iterator<MagicBall> ballIterator = magicBalls.iterator();
    while (ballIterator.hasNext()) {
        MagicBall ball = ballIterator.next();
        ball.update();
        if (!ball.isActive()) {
            ballIterator.remove();
        }
    }
    // ... other update logic
}
```

### การวาดลูกไฟ
```java
// ใน Magic.java
public void draw(Graphics graphics) {
    // วาด Magic tower
    graphics.drawImage(objectImage, positionX, positionY + Y_OFFSET, 
                      objectWidth, objectHeight, null);

    // วาดลูกไฟทั้งหมด
    for (MagicBall ball : magicBalls) {
        ball.draw(graphics);
    }
}
```

## คุณสมบัติของลูกไฟ

### การเคลื่อนที่
- **ความเร็ว**: 6.0 พิกเซลต่อ frame (เร็วกว่า Arrow)
- **ทิศทาง**: เคลื่อนที่เป็นเส้นตรงไปยังเป้าหมาย
- **การติดตาม**: ติดตามเป้าหมายที่เคลื่อนที่

### ความเสียหาย
- **ความเสียหายปกติ**: 5 จุด
- **ความเสียหายพิเศษ**: 20 จุด (4 เท่า)
- **ความถี่**: ทุก 0.75 วินาที (เร็วกว่า Archer)

### การแสดงผล
- **ขนาด**: 20x20 พิกเซล (เล็กกว่า Arrow)
- **รูปภาพ**: normalMagicBall.png / superMagicBall.png
- **Fallback**: วงกลมสีม่วงแดง

### การจัดการหน่วยความจำ
- **Auto-cleanup**: ลูกไฟถูกลบอัตโนมัติเมื่อชนเป้าหมาย
- **Target validation**: ตรวจสอบเป้าหมายทุก frame
- **Iterator safety**: ใช้ Iterator เพื่อลบอย่างปลอดภัย

## การเปรียบเทียบกับ Arrow

### ความเหมือน
- ทั้งคู่เป็น projectiles
- เคลื่อนที่ไปยังเป้าหมาย
- ทำความเสียหายเมื่อชน
- มีระบบ cleanup เหมือนกัน

### ความแตกต่าง

| คุณสมบัติ | MagicBall | Arrow |
|----------|-----------|-------|
| ความเร็ว | 6.0 | 5.0 |
| ขนาด | 20x20 | 24x24 |
| ความเสียหาย | 5-20 | 100,000 |
| สี Fallback | ม่วงแดง | น้ำตาล |
| ความถี่ | 0.75 วินาที | 1.5 วินาที |
| ประเภท | 2 ประเภท | 1 ประเภท |

## ระบบ Combo ของ Magic

### การนับการโจมตี
```java
// ใน Magic.java
private int attackCounter = 0; // 0-4

private void executeAttack() {
    if (attackCounter >= Constants.Entities.MAGIC_ATTACKS_BEFORE_SPELL) {
        castSpecialSpell(); // ยิงลูกไฟพิเศษ
        attackCounter = 0;  // รีเซ็ต
    } else {
        performNormalAttack(); // ยิงลูกไฟปกติ
        attackCounter++;
    }
}
```

### การแสดงผลที่แตกต่าง
- **ลูกไฟปกติ**: ขนาดเล็ก ความเสียหายต่ำ
- **ลูกไฟพิเศษ**: ขนาดเท่าเดิม แต่ความเสียหายสูง
- **Visual Feedback**: Magic tower เปลี่ยนเป็นรูป bomb เมื่อใช้เวทมนตร์

## ความสัมพันธ์กับคลาสอื่น

### Magic.java
- **การสร้าง**: Magic สร้าง MagicBall เมื่อโจมตี
- **การจัดการ**: Magic เก็บ ArrayList<MagicBall>
- **การอัปเดต**: Magic อัปเดตและลบ MagicBall
- **การแสดงผล**: Magic วาด MagicBall ทั้งหมด

### Enemy.java
- **เป้าหมาย**: MagicBall ใช้ Enemy เป็นเป้าหมาย
- **การทำความเสียหาย**: เรียก enemy.takeDamage()
- **การติดตาม**: ใช้ enemy.getBounds() เพื่อติดตาม

### Constants.java
- **ความเสียหาย**: ใช้ MAGIC_ATTACK_DAMAGE และ MAGIC_SPELL_DAMAGE
- **รูปภาพ**: ใช้ NORMAL_MAGIC_BALL_IMAGE และ SUPER_MAGIC_BALL_IMAGE

## จุดเด่นของการออกแบบ

### ความหลากหลาย
- มี 2 ประเภทในคลาสเดียว
- รองรับระบบ combo ของ Magic tower

### ความเรียบง่าย
- โครงสร้างเหมือน Arrow
- ใช้ parameter เดียวกันแต่ค่าต่างกัน

### ประสิทธิภาพ
- ใช้ double สำหรับตำแหน่งเพื่อความแม่นยำ
- การคำนวณที่เรียบง่ายและรวดเร็ว

### ความยืดหยุ่น
- รองรับการไม่มีรูปภาพ (fallback)
- สามารถปรับความเร็วและขนาดได้ง่าย

## การปรับปรุงที่เป็นไปได้

### เพิ่มเอฟเฟกต์พิเศษ
```java
private boolean isSuper;

public MagicBall(double startX, double startY, Enemy target, int damage, 
                 Image ballImage, boolean isSuper) {
    // ... existing constructor
    this.isSuper = isSuper;
}

public void draw(Graphics graphics) {
    if (isSuper) {
        // วาดเอฟเฟกต์เรืองแสง
        graphics.setColor(new Color(255, 255, 0, 100)); // เหลืองโปร่งใส
        graphics.fillOval(
            (int) positionX - MAGIC_BALL_SIZE,
            (int) positionY - MAGIC_BALL_SIZE,
            MAGIC_BALL_SIZE * 2,
            MAGIC_BALL_SIZE * 2
        );
    }
    
    // วาดลูกไฟปกติ
    // ...
}
```

### เพิ่มการระเบิด
```java
private void hitTarget() {
    if (target != null && !target.isDead()) {
        target.takeDamage(damage);
        
        if (isSuper) {
            // ความเสียหายพื้นที่ (Area of Effect)
            damageNearbyEnemies();
        }
    }
    isActive = false;
}

private void damageNearbyEnemies() {
    // หาศัตรูในรัศมี 50 พิกเซล
    // ทำความเสียหาย 50% ให้ศัตรูใกล้เคียง
}
```

### เพิ่มเส้นทางโค้ง
```java
private double curveAmount = 0;
private double timeAlive = 0;

public void update() {
    timeAlive++;
    
    // เพิ่มการโค้งเล็กน้อย
    double curve = Math.sin(timeAlive * 0.1) * curveAmount;
    positionX += curve;
    
    // ... existing movement code
}
```
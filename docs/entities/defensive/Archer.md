# Archer.java - หอคอยนักธนู

## ภาพรวม
Archer เป็นหน่วยป้องกันที่ยิงลูกธนูโจมตีศัตรูจากระยะไกลมาก มีความเสียหายปานกลางและระยะโจมตีไกลที่สุด แต่โจมตีช้า

## คลาสและการสืบทอด
```java
public class Archer extends GameObject implements Defensive, Collidable
```
- สืบทอดจาก `GameObject` สำหรับคุณสมบัติพื้นฐาน
- ใช้ `Defensive` interface สำหรับระบบการป้องกัน
- ใช้ `Collidable` interface สำหรับการตรวจสอบการชน

## ตัวแปรสำคัญ

### สถานะปัจจุบัน
- `int currentHealth` - เลือดปัจจุบัน (เริ่มต้น 75)

### สถานะการต่อสู้
- `Enemy lockedTarget` - ศัตรูที่กำลังเล็ง
- `int attackTimer` - ตัวจับเวลาการโจมตี
- `boolean isAttacking` - กำลังโจมตีหรือไม่
- `int attackAnimationTimer` - ตัวจับเวลาแอนิเมชันการโจมตี

### การอ้างอิงและ Projectiles
- `ArrayList<Enemy> enemyList` - รายการศัตรูสำหรับการเล็ง
- `ArrayList<Arrow> arrows` - รายการลูกธนู

### รูปภาพ
- `Image normalImage` - รูปภาพปกติ
- `Image attackImage` - รูปภาพเมื่อโจมตี
- `Image arrowImage` - รูปภาพลูกธนู

## Methods สำคัญ

### Constructor
```java
public Archer(int gridColumn, int gridRow, int tileSize, Image archerImage, ArrayList<Enemy> enemies)
```
**การทำงาน**:
1. เรียก constructor ของ GameObject ด้วยขนาด 2x2 ไทล์
2. เก็บอ้างอิงรายการศัตรูและรูปภาพปกติ
3. โหลดรูปภาพเพิ่มเติม (arrow, attack image)

### update()
**การทำงาน**:
1. อัปเดตแอนิเมชันการโจมตี (เปลี่ยนกลับเป็นรูปปกติหลังจาก ATTACK_ANIMATION_DURATION frames)
2. อัปเดตและลบลูกธนูที่ไม่ active
3. ตรวจสอบและหาเป้าหมายใหม่หากจำเป็น
4. โจมตีหากมีเป้าหมายที่ถูกต้อง

### isTargetValid()
ตรวจสอบว่าเป้าหมายยังมีชีวิตและอยู่ในระยะโจมตี (ARCHER_ATTACK_RANGE = 1000)

### acquireNewTarget()
หาศัตรูที่ใกล้ที่สุดในระยะโจมตีและเล็งเป้าหมาย

### performAttack()
เพิ่ม attackTimer และเรียก shootArrow() เมื่อถึง ARCHER_ATTACK_COOLDOWN_FRAMES

### shootArrow()
**การทำงาน**:
1. เปลี่ยนเป็น attackImage
2. สร้าง Arrow ใหม่จากจุดกึ่งกลางของ Archer (รวม offset) ไปยังเป้าหมาย
3. เพิ่มลูกธนูเข้า arrows list

### takeDamage(int damageAmount)
ลดเลือดโดยคำนวณ actualDamage = max(1, damageAmount - ARCHER_DEFENSE_RATING)

### getCollisionBounds()
return Rectangle ที่ตำแหน่ง `(positionX + ARCHER_X_OFFSET, positionY + ARCHER_Y_OFFSET)`

### draw(Graphics graphics)
**การทำงาน**:
1. วาดรูปภาพที่ตำแหน่ง `(positionX + ARCHER_X_OFFSET, positionY + ARCHER_Y_OFFSET)`
2. วาดลูกธนูทั้งหมด
3. วาดแถบเลือดด้านบน (สีเขียวสำหรับ Archer)

## ค่าคงที่ที่ใช้จาก Constants
- `ARCHER_INITIAL_HEALTH = 75` - เลือดเริ่มต้น
- `ARCHER_DEFENSE_RATING = 0` - ค่าป้องกัน
- `ARCHER_COST = 15` - ราคา
- `ARCHER_ATTACK_DAMAGE = 10` - ความเสียหาย
- `ARCHER_ATTACK_COOLDOWN_FRAMES = 90` - cooldown (1.5 วินาที)
- `ARCHER_ATTACK_RANGE = 1000` - ระยะโจมตีไกลมาก
- `ARCHER_X_OFFSET = 0` - offset แนวนอน
- `ARCHER_Y_OFFSET = -30` - offset แนวตั้ง
- `ATTACK_ANIMATION_DURATION = 15` - ระยะเวลาแอนิเมชัน
- `MINIMUM_HEALTH = 0` - เลือดต่ำสุด

## จุดเด่น
- ระยะโจมตีไกลที่สุด (1000 พิกเซล)
- ความเสียหายปานกลาง (10)
- โจมตีช้า (1.5 วินาทีต่อครั้ง)
- ราคาปานกลาง (15 เหรียญ)
- เปลี่ยนรูปเมื่อโจมตี

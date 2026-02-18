# Magic.java - หอคอยเวทมนตร์

## ภาพรวม
Magic เป็นหน่วยป้องกันที่สามารถโจมตีศัตรูจากระยะไกล โดยจะทำการโจมตีปกติ 4 ครั้ง แล้วใช้เวทมนตร์พิเศษในครั้งที่ 5

## คลาสและการสืบทอด
```java
public class Magic extends GameObject implements Defensive, Collidable
```
- สืบทอดจาก `GameObject` สำหรับคุณสมบัติพื้นฐาน
- ใช้ `Defensive` interface สำหรับระบบการป้องกัน
- ใช้ `Collidable` interface สำหรับการตรวจสอบการชน

## ตัวแปรสำคัญ

### สถานะปัจจุบัน
- `int currentHealth` - เลือดปัจจุบัน (เริ่มต้น 50)

### สถานะการต่อสู้
- `BaseEnemy lockedTarget` - ศัตรูที่กำลังเล็ง (Enemy หรือ Boss)
- `int attackTimer` - ตัวจับเวลาการโจมตี
- `int attackCounter` - ตัวนับการโจมตี (0-4)
- `boolean isUsingSpecialSpell` - กำลังใช้เวทมนตร์พิเศษหรือไม่
- `int spellAnimationTimer` - ตัวจับเวลาแอนิเมชันเวทมนตร์

### การอ้างอิงและ Projectiles
- `ArrayList<BaseEnemy> enemyList` - รายการศัตรูสำหรับการเล็ง (รวม Enemy และ Boss)
- `ArrayList<MagicBall> magicBalls` - รายการลูกไฟเวทมนตร์

### รูปภาพ
- `Image normalImage` - รูปภาพปกติ
- `Image bombImage` - รูปภาพเมื่อใช้เวทมนตร์พิเศษ
- `Image normalBallImage` - รูปลูกไฟปกติ
- `Image superBallImage` - รูปลูกไฟพิเศษ

## Methods สำคัญ

### Constructor
```java
public Magic(int gridColumn, int gridRow, int tileSize, Image magicImage, ArrayList<BaseEnemy> enemies)
```
**การทำงาน**:
1. เรียก constructor ของ GameObject ด้วยขนาด 2x2 ไทล์
2. เก็บอ้างอิงรายการศัตรูและรูปภาพปกติ
3. โหลดรูปภาพเพิ่มเติม (bomb, magic balls)

### update()
**การทำงาน**:
1. อัปเดตและลบ magic ball ที่ไม่ active
2. อัปเดตแอนิเมชันเวทมนตร์ (เปลี่ยนกลับเป็นรูปปกติหลังจาก SPELL_ANIMATION_DURATION frames)
3. ตรวจสอบและหาเป้าหมายใหม่หากจำเป็น
4. โจมตีหากมีเป้าหมายที่ถูกต้อง

### isTargetValid()
ตรวจสอบว่าเป้าหมายยังมีชีวิตและอยู่ในระยะโจมตี (MAGIC_ATTACK_RANGE)

### acquireNewTarget()
หาศัตรูที่ใกล้ที่สุดในระยะโจมตีและเล็งเป้าหมาย

### performAttack()
เพิ่ม attackTimer และเรียก executeAttack() เมื่อถึง MAGIC_ATTACK_COOLDOWN_FRAMES

### executeAttack()
ตรวจสอบ attackCounter:
- หาก >= MAGIC_ATTACKS_BEFORE_SPELL (4): ใช้เวทมนตร์พิเศษและ reset counter
- หากไม่ใช่: โจมตีปกติและเพิ่ม counter

### shootMagicBall(boolean isSuper)
สร้าง MagicBall ใหม่จากจุดกึ่งกลางของ Magic tower (รวม offset) ไปยังเป้าหมาย

### takeDamage(int damageAmount)
ลดเลือดโดยคำนวณ actualDamage = max(1, damageAmount - MAGIC_DEFENSE_RATING)

### getCollisionBounds()
return Rectangle ที่ตำแหน่ง `(positionX + MAGIC_X_OFFSET, positionY + MAGIC_Y_OFFSET)`

### draw(Graphics graphics)
**การทำงาน**:
1. วาดรูปภาพที่ตำแหน่ง `(positionX + MAGIC_X_OFFSET, positionY + MAGIC_Y_OFFSET)`
2. วาดลูกไฟเวทมนตร์ทั้งหมด
3. วาดแถบเลือดด้านบน (สีฟ้าสำหรับ Magic)

## ค่าคงที่ที่ใช้จาก Constants
- `MAGIC_INITIAL_HEALTH = 50` - เลือดเริ่มต้น
- `MAGIC_DEFENSE_RATING = 0` - ค่าป้องกัน
- `MAGIC_COST = 100` - ราคา
- `MAGIC_ATTACK_DAMAGE = 5` - ความเสียหายปกติ
- `MAGIC_SPELL_DAMAGE = 20` - ความเสียหายเวทมนตร์
- `MAGIC_ATTACK_COOLDOWN_FRAMES = 45` - cooldown (0.75 วินาที)
- `MAGIC_ATTACK_RANGE = 300` - ระยะโจมตี
- `MAGIC_ATTACKS_BEFORE_SPELL = 4` - จำนวนการโจมตีก่อนใช้เวทมนตร์
- `MAGIC_X_OFFSET = 0` - offset แนวนอน
- `MAGIC_Y_OFFSET = -30` - offset แนวตั้ง
- `SPELL_ANIMATION_DURATION = 30` - ระยะเวลาแอนิเมชัน
- `MINIMUM_HEALTH = 0` - เลือดต่ำสุด

## จุดเด่น
- โจมตีได้จากระยะไกล (300 พิกเซล)
- มีเวทมนตร์พิเศษทุก 5 ครั้ง (ความเสียหาย 20)
- ราคาปานกลาง (100 เหรียญ)
- เปลี่ยนรูปเป็น bomb เมื่อใช้เวทมนตร์

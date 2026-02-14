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

### ค่าคงที่
- `static final int Y_OFFSET = -30` - offset การแสดงผลในแนวตั้ง
- `static final int MINIMUM_HEALTH = 0` - เลือดต่ำสุด
- `static final int SPELL_ANIMATION_DURATION = 30` - ระยะเวลาแอนิเมชันเวทมนตร์

### สถานะปัจจุบัน
- `int currentHealth` - เลือดปัจจุบัน (เริ่มต้น 50)

### สถานะการต่อสู้
- `Enemy lockedTarget` - ศัตรูที่กำลังเล็ง
- `int attackTimer` - ตัวจับเวลาการโจมตี
- `int attackCounter` - ตัวนับการโจมตี (0-4)
- `boolean isUsingSpecialSpell` - กำลังใช้เวทมนตร์พิเศษหรือไม่
- `int spellAnimationTimer` - ตัวจับเวลาแอนิเมชันเวทมนตร์

### การอ้างอิงและ Projectiles
- `ArrayList<Enemy> enemyList` - รายการศัตรูสำหรับการเล็ง
- `ArrayList<MagicBall> magicBalls` - รายการลูกไฟเวทมนตร์

### รูปภาพ
- `Image normalImage` - รูปภาพปกติ
- `Image bombImage` - รูปภาพเมื่อใช้เวทมนตร์พิเศษ
- `Image normalBallImage` - รูปลูกไฟปกติ
- `Image superBallImage` - รูปลูกไฟพิเศษ

## Methods อย่างละเอียด

### Constructor
```java
public Magic(int gridColumn, int gridRow, int tileSize, Image magicImage, ArrayList<Enemy> enemies)
```
**วัตถุประสงค์**: สร้าง Magic tower ที่ตำแหน่งกริดที่กำหนด
**พารามิเตอร์**:
- `gridColumn, gridRow` - ตำแหน่งในกริด
- `tileSize` - ขนาดไทล์
- `magicImage` - รูปภาพ Magic tower
- `enemies` - รายการศัตรูสำหรับการเล็ง
**การทำงาน**:
1. เรียก constructor ของ GameObject ด้วยขนาด 2x2 ไทล์
2. เก็บอ้างอิงรายการศัตรูและรูปภาพปกติ
3. โหลดรูปภาพเพิ่มเติม (bomb, magic balls)

### update()
```java
public void update()
```
**วัตถุประสงค์**: อัปเดต Magic tower ทุก frame
**การทำงาน**:
1. **อัปเดต Magic Balls**: อัปเดตและลบ magic ball ที่ไม่ active
2. **อัปเดตแอนิเมชันเวทมนตร์**: จัดการการเปลี่ยนกลับเป็นรูปปกติ
3. **อัปเดตเป้าหมาย**: ตรวจสอบและหาเป้าหมายใหม่หากจำเป็น
4. **โจมตี**: หากมีเป้าหมายที่ถูกต้อง ทำการโจมตี

### isTargetValid()
```java
private boolean isTargetValid()
```
**วัตถุประสงค์**: ตรวจสอบว่าเป้าหมายปัจจุบันยังใช้ได้หรือไม่
**การทำงาน**:
1. ตรวจสอบว่าเป้าหมายไม่เป็น null และยังมีชีวิต
2. ตรวจสอบว่าเป้าหมายยังอยู่ในระยะโจมตี
3. return true หากเป้าหมายยังใช้ได้

### acquireNewTarget()
```java
private void acquireNewTarget()
```
**วัตถุประสงค์**: หาเป้าหมายใหม่จากรายการศัตรู
**การทำงาน**:
1. รีเซ็ต lockedTarget เป็น null
2. วนลูปผ่านศัตรูทั้งหมด
3. ข้ามศัตรูที่ตายแล้ว
4. คำนวณระยะทางไปยังศัตรู
5. เลือกศัตรูที่ใกล้ที่สุดในระยะโจมตี

### calculateDistanceToTarget(Enemy enemy)
```java
private double calculateDistanceToTarget(Enemy enemy)
```
**วัตถุประสงค์**: คำนวณระยะทางไปยังศัตรู
**การทำงาน**:
1. หาจุดกึ่งกลางของ Magic tower
2. หาจุดกึ่งกลางของศัตรู
3. ใช้ MathUtils.calculateDistance() คำนวณระยะทาง

### performAttack()
```java
private void performAttack()
```
**วัตถุประสงค์**: จัดการการโจมตีและ cooldown
**การทำงาน**:
1. เพิ่ม attackTimer
2. เมื่อ timer ถึง cooldown เรียก executeAttack()
3. รีเซ็ต timer

### executeAttack()
```java
private void executeAttack()
```
**วัตถุประสงค์**: ตัดสินใจประเภทการโจมตี
**การทำงาน**:
1. ตรวจสอบว่าเป้าหมายยังมีชีวิตหรือไม่
2. ตรวจสอบ attackCounter:
   - หาก >= 4: ใช้เวทมนตร์พิเศษและรีเซ็ต counter
   - หากไม่: ทำการโจมตีปกติและเพิ่ม counter

### performNormalAttack()
```java
private void performNormalAttack()
```
**วัตถุประสงค์**: ทำการโจมตีปกติ
**การทำงาน**:
1. เรียก shootMagicBall(false) สำหรับลูกไฟปกติ
2. แสดงข้อความการโจมตีใน console

### castSpecialSpell()
```java
private void castSpecialSpell()
```
**วัตถุประสงค์**: ใช้เวทมนตร์พิเศษ
**การทำงาน**:
1. เปลี่ยนเป็น bombImage
2. ตั้งสถานะ isUsingSpecialSpell = true
3. รีเซ็ต spellAnimationTimer
4. เรียก shootMagicBall(true) สำหรับลูกไฟพิเศษ
5. แสดงข้อความเวทมนตร์ใน console

### shootMagicBall(boolean isSuper)
```java
private void shootMagicBall(boolean isSuper)
```
**วัตถุประสงค์**: ยิงลูกไฟเวทมนตร์
**พารามิเตอร์**: `isSuper` - true สำหรับลูกไฟพิเศษ
**การทำงาน**:
1. ตรวจสอบว่าเป้าหมายยังมีชีวิต
2. คำนวณตำแหน่งเริ่มต้น (กึ่งกลาง Magic tower)
3. กำหนดความเสียหายและรูปภาพตามประเภท
4. สร้าง MagicBall object ใหม่
5. เพิ่มเข้าไปใน magicBalls list

## Implementation ของ Defensive Interface

### takeDamage(int damageAmount)
```java
@Override
public void takeDamage(int damageAmount)
```
**วัตถุประสงค์**: รับความเสียหายจากศัตรู
**การทำงาน**:
1. คำนวณความเสียหายจริงหลังหักค่าป้องกัน
2. ลดเลือดตามความเสียหายจริง (ขั้นต่ำ 1)
3. ตรวจสอบไม่ให้เลือดต่ำกว่า 0
4. แสดงข้อมูลเลือดและความเสียหายที่ดูดซับ

### isDestroyed()
```java
@Override
public boolean isDestroyed()
```
**วัตถุประสงค์**: ตรวจสอบว่า Magic tower ถูกทำลายหรือยัง
**การทำงาน**: return true หากเลือดเหลือ 0

### getCurrentHealth() / getMaxHealth() / getDefenseRating()
```java
@Override
public int getCurrentHealth()
@Override
public int getMaxHealth()
@Override
public int getDefenseRating()
```
**วัตถุประสงค์**: ดึงข้อมูลสถิติต่างๆ
**การทำงาน**: return ค่าจาก Constants หรือตัวแปรปัจจุบัน

## Implementation ของ Collidable Interface

### getCollisionBounds()
```java
@Override
public Rectangle getCollisionBounds()
```
**วัตถุประสงค์**: ดึง bounds สำหรับการตรวจสอบการชน
**การทำงาน**: return Rectangle ที่ตำแหน่งปัจจุบัน + Y_OFFSET

## Legacy Methods และ Utility Methods

### damage() / isDead() / getBounds()
```java
public void damage(int damageAmount)
public boolean isDead()
public Rectangle getBounds()
```
**วัตถุประสงค์**: Methods เก่าเพื่อความเข้ากันได้
**การทำงาน**: เรียก methods ของ interface ที่เกี่ยวข้อง

### getGridColumn() / getGridRow()
```java
public int getGridColumn(int tileSize)
public int getGridRow(int tileSize)
```
**วัตถุประสงค์**: แปลงตำแหน่งพิกเซลเป็นกริด
**การทำงาน**: หารตำแหน่งด้วยขนาดไทล์

### getHealthPercentage()
```java
public double getHealthPercentage()
```
**วัตถุประสงค์**: คำนวณเปอร์เซ็นต์เลือด
**การทำงาน**: return เลือดปัจจุบัน / เลือดสูงสุด

## การแสดงผล

### draw(Graphics graphics)
```java
@Override
public void draw(Graphics graphics)
```
**วัตถุประสงค์**: วาด Magic tower และ projectiles
**การทำงาน**:
1. วาดรูป Magic tower ที่ตำแหน่งปัจจุบัน + Y_OFFSET
2. วาด magic balls ทั้งหมด
3. เรียก drawHealthBar() เพื่อวาดแถบเลือด

### drawHealthBar(Graphics graphics)
```java
private void drawHealthBar(Graphics graphics)
```
**วัตถุประสงค์**: วาดแถบเลือดเหนือ Magic tower
**การทำงาน**:
1. แสดงเฉพาะเมื่อเลือดไม่เต็ม
2. วาดพื้นหลังสีแดง
3. วาดแถบเลือดสีฟ้า (cyan) ตามเปอร์เซ็นต์
4. วาดขอบสีดำ

## ระบบการโจมตี

### การเล็งเป้าหมาย
1. **Target Acquisition**: หาศัตรูที่ใกล้ที่สุดในระยะ 300 พิกเซล
2. **Target Locking**: ล็อคเป้าหมายจนกว่าจะตายหรือออกจากระยะ
3. **Target Validation**: ตรวจสอบเป้าหมายทุก frame

### รูปแบบการโจมตี
1. **การโจมตีปกติ (4 ครั้งแรก)**:
   - ความเสียหาย: 5 จุด
   - ใช้ลูกไฟปกติ (normalMagicBall.png)
   - Cooldown: 45 frames (0.75 วินาที)

2. **เวทมนตร์พิเศษ (ครั้งที่ 5)**:
   - ความเสียหาย: 20 จุด
   - ใช้ลูกไฟพิเศษ (superMagicBall.png)
   - เปลี่ยนรูปเป็น magicBomb.png ชั่วคราว
   - รีเซ็ต counter กลับเป็น 0

### Projectile System
- ใช้ MagicBall objects สำหรับ projectiles
- Magic balls เคลื่อนที่ไปยังเป้าหมายอัตโนมัติ
- ลบ magic balls ที่ไม่ active แล้วออกจาก list

## คุณสมบัติเฉพาะของ Magic

### ระบบป้องกัน
- เลือดเริ่มต้น: 50 จุด
- ค่าป้องกัน: 0 (รับความเสียหายเต็มจำนวน)
- สามารถถูกโจมตีและทำลายได้

### ระยะการโจมตี
- ระยะโจมตี: 300 พิกเซล
- สามารถโจมตีศัตรูจากระยะไกล
- ไม่จำเป็นต้องสัมผัสศัตรู

### การเปลี่ยนรูปภาพ
- รูปปกติ: magic.png
- รูปเวทมนตร์: magicBomb.png (30 frames)
- เปลี่ยนกลับเป็นรูปปกติหลังใช้เวทมนตร์

## ความสัมพันธ์กับคลาสอื่น
- **GameObject.java**: สืบทอดคุณสมบัติพื้นฐาน
- **Defensive/Collidable.java**: ใช้ interfaces
- **Enemy.java**: เป้าหมายการโจมตี
- **MagicBall.java**: Projectiles ที่ยิงออกไป
- **Map.java**: จัดการการวางและอัปเดต
- **GamePanel.java**: ผู้เล่นสามารถวางผ่าน UI
- **Constants.java**: ใช้ค่าคงที่สำหรับสถิติ
- **MathUtils.java**: คำนวณระยะทาง

## จุดเด่นของการออกแบบ
1. **Combo System**: ระบบการโจมตี 4+1 ที่น่าสนใจ
2. **Projectile System**: ใช้ projectiles แทนการโจมตีทันที
3. **Visual Feedback**: เปลี่ยนรูปภาพเมื่อใช้เวทมนตร์
4. **Range Combat**: โจมตีจากระยะไกลได้
5. **Target Management**: ระบบเล็งเป้าหมายที่ชาญฉลาด
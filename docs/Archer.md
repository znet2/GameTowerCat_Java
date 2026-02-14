# Archer.java - หอคอยนักธนู

## ภาพรวม
Archer เป็นหน่วยป้องกันที่ยิงลูกธนุโจมตีศัตรูจากระยะไกล มีความเสียหายสูงมากและระยะโจมตีไกล แต่โจมตีช้า

## คลาสและการสืบทอด
```java
public class Archer extends GameObject implements Defensive, Collidable
```
- สืบทอดจาก `GameObject` สำหรับคุณสมบัติพื้นฐาน
- ใช้ `Defensive` interface สำหรับระบบการป้องกัน
- ใช้ `Collidable` interface สำหรับการตรวจสอบการชน

## ตัวแปรสำคัญ

### ค่าคงที่
- `static final int Y_OFFSET = -30` - offset การแสดงผลในแนวตั้ง
- `static final int MINIMUM_HEALTH = 0` - เลือดต่ำสุด
- `static final int ATTACK_ANIMATION_DURATION = 15` - ระยะเวลาแอนิเมชันการโจมตี

### สถานะปัจจุบัน
- `int currentHealth` - เลือดปัจจุบัน (เริ่มต้น 75)

### สถานะการต่อสู้
- `Enemy lockedTarget` - ศัตรูที่กำลังเล็ง
- `int attackTimer` - ตัวจับเวลาการโจมตี
- `boolean isAttacking` - กำลังโจมตีหรือไม่
- `int attackAnimationTimer` - ตัวจับเวลาแอนิเมชันการโจมตี

### การอ้างอิงและ Projectiles
- `ArrayList<Enemy> enemyList` - รายการศัตรูสำหรับการเล็ง
- `ArrayList<Arrow> arrows` - รายการลูกธนุ

### รูปภาพ
- `Image normalImage` - รูปภาพปกติ
- `Image attackImage` - รูปภาพเมื่อโจมตี
- `Image arrowImage` - รูปภาพลูกธนุ

## Methods อย่างละเอียด

### Constructor
```java
public Archer(int gridColumn, int gridRow, int tileSize, Image archerImage, ArrayList<Enemy> enemies)
```
**วัตถุประสงค์**: สร้าง Archer tower ที่ตำแหน่งกริดที่กำหนด
**พารามิเตอร์**:
- `gridColumn, gridRow` - ตำแหน่งในกริด
- `tileSize` - ขนาดไทล์
- `archerImage` - รูปภาพ Archer tower
- `enemies` - รายการศัตรูสำหรับการเล็ง
**การทำงาน**:
1. เรียก constructor ของ GameObject ด้วยขนาด 2x2 ไทล์
2. เก็บอ้างอิงรายการศัตรูและรูปภาพปกติ
3. โหลดรูปภาพเพิ่มเติม (arrow, attack image)

### update()
```java
public void update()
```
**วัตถุประสงค์**: อัปเดต Archer tower ทุก frame
**การทำงาน**:
1. **อัปเดตแอนิเมชันการโจมตี**: จัดการการเปลี่ยนกลับเป็นรูปปกติ
2. **อัปเดต Arrows**: อัปเดตและลบลูกธนุที่ไม่ active
3. **อัปเดตเป้าหมาย**: ตรวจสอบและหาเป้าหมายใหม่หากจำเป็น
4. **โจมตี**: หากมีเป้าหมายที่ถูกต้อง ทำการโจมตี

### isTargetValid()
```java
private boolean isTargetValid()
```
**วัตถุประสงค์**: ตรวจสอบว่าเป้าหมายปัจจุบันยังใช้ได้หรือไม่
**การทำงาน**:
1. ตรวจสอบว่าเป้าหมายไม่เป็น null และยังมีชีวิต
2. ตรวจสอบว่าเป้าหมายยังอยู่ในระยะโจมตี (1000 พิกเซล)
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
1. หาจุดกึ่งกลางของ Archer tower
2. หาจุดกึ่งกลางของศัตรู
3. ใช้ MathUtils.calculateDistance() คำนวณระยะทาง

### performAttack()
```java
private void performAttack()
```
**วัตถุประสงค์**: จัดการการโจมตีและ cooldown
**การทำงาน**:
1. เพิ่ม attackTimer
2. เมื่อ timer ถึง cooldown (90 frames = 1.5 วินาที) เรียก shootArrow()
3. รีเซ็ต timer

### shootArrow()
```java
private void shootArrow()
```
**วัตถุประสงค์**: ยิงลูกธนุไปยังเป้าหมาย
**การทำงาน**:
1. ตรวจสอบว่าเป้าหมายยังมีชีวิต
2. เปลี่ยนเป็น attackImage และตั้งสถานะ isAttacking
3. รีเซ็ต attackAnimationTimer
4. คำนวณตำแหน่งเริ่มต้น (กึ่งกลาง Archer tower)
5. สร้าง Arrow object ใหม่
6. เพิ่มเข้าไปใน arrows list
7. แสดงข้อความการโจมตีใน console

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
**วัตถุประสงค์**: ตรวจสอบว่า Archer tower ถูกทำลายหรือยัง
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
**วัตถุประสงค์**: วาด Archer tower และ projectiles
**การทำงาน**:
1. วาดรูป Archer tower ที่ตำแหน่งปัจจุบัน + Y_OFFSET
2. วาดลูกธนุทั้งหมด
3. เรียก drawHealthBar() เพื่อวาดแถบเลือด

### drawHealthBar(Graphics graphics)
```java
private void drawHealthBar(Graphics graphics)
```
**วัตถุประสงค์**: วาดแถบเลือดเหนือ Archer tower
**การทำงาน**:
1. แสดงเฉพาะเมื่อเลือดไม่เต็ม
2. วาดพื้นหลังสีแดง
3. วาดแถบเลือดสีเขียวตามเปอร์เซ็นต์
4. วาดขอบสีดำ

## ระบบการโจมตี

### การเล็งเป้าหมาย
1. **Target Acquisition**: หาศัตรูที่ใกล้ที่สุดในระยะ 1000 พิกเซล
2. **Target Locking**: ล็อคเป้าหมายจนกว่าจะตายหรือออกจากระยะ
3. **Target Validation**: ตรวจสอบเป้าหมายทุก frame

### คุณสมบัติการโจมตี
1. **ความเสียหาย**: 100,000 จุด (สูงมาก)
2. **ระยะโจมตี**: 1000 พิกเซล (ไกลมาก)
3. **Cooldown**: 90 frames (1.5 วินาที)
4. **Projectile**: ใช้ Arrow objects

### แอนิเมชันการโจมตี
1. **รูปปกติ**: archer.png
2. **รูปโจมตี**: archerAttack.png (15 frames)
3. **การเปลี่ยน**: เปลี่ยนเป็นรูปโจมตีเมื่อยิง แล้วเปลี่ยนกลับ

### Projectile System
- ใช้ Arrow objects สำหรับลูกธนุ
- Arrow เคลื่อนที่ไปยังเป้าหมายอัตโนมัติ
- ลบ arrows ที่ไม่ active แล้วออกจาก list

## คุณสมบัติเฉพาะของ Archer

### ระบบป้องกัน
- เลือดเริ่มต้น: 75 จุด
- ค่าป้องกัน: 0 (รับความเสียหายเต็มจำนวน)
- สามารถถูกโจมตีและทำลายได้

### ความเสียหายสูง
- ความเสียหาย: 100,000 จุด
- สามารถฆ่าศัตรูได้ในการโจมตีเดียว
- เหมาะสำหรับจัดการศัตรูที่มีเลือดมาก

### ระยะโจมตีไกล
- ระยะโจมตี: 1000 พิกเซล
- ไกลที่สุดในบรรดาหน่วยป้องกันทั้งหมด
- สามารถโจมตีศัตรูได้ก่อนที่จะเข้าใกล้

### การโจมตีช้า
- Cooldown: 1.5 วินาที
- ช้าที่สุดในบรรดาหน่วยป้องกันทั้งหมด
- ต้องเล็งเป้าหมายให้ดีเพราะโจมตีช้า

## ความสัมพันธ์กับคลาสอื่น
- **GameObject.java**: สืบทอดคุณสมบัติพื้นฐาน
- **Defensive/Collidable.java**: ใช้ interfaces
- **Enemy.java**: เป้าหมายการโจมตี
- **Arrow.java**: Projectiles ที่ยิงออกไป
- **Map.java**: จัดการการวางและอัปเดต
- **GamePanel.java**: ผู้เล่นสามารถวางผ่าน UI
- **Constants.java**: ใช้ค่าคงที่สำหรับสถิติ
- **MathUtils.java**: คำนวณระยะทาง

## จุดเด่นของการออกแบบ
1. **High Damage**: ความเสียหายสูงมากสำหรับ one-shot kill
2. **Long Range**: ระยะโจมตีไกลที่สุด
3. **Slow Attack**: สมดุลด้วยการโจมตีช้า
4. **Visual Feedback**: เปลี่ยนรูปภาพเมื่อโจมตี
5. **Projectile System**: ใช้ลูกธนุที่เคลื่อนที่ได้
6. **Strategic Placement**: ต้องวางตำแหน่งให้ดีเพื่อใช้ประโยชน์จากระยะไกล

## กลยุทธ์การใช้งาน
1. **Early Warning**: วางในตำแหน่งที่เห็นศัตรูได้ไกล
2. **Backup Defense**: ใช้ร่วมกับหน่วยอื่นเพื่อชะลอศัตรู
3. **High Value Targets**: เหมาะสำหรับศัตรูที่มีเลือดมาก
4. **Strategic Positioning**: วางในตำแหน่งที่ปลอดภัยจากการโจมตี
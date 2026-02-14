# Tank.java - หน่วยป้องกันแบบ Tank

## ภาพรวม
Tank เป็นหน่วยป้องกันที่ทำหน้าที่เป็นกำแพงป้องกัน ไม่สามารถโจมตีได้ แต่มีเลือดมากและสามารถกั้นศัตรูได้

## คลาสและการสืบทอด
```java
public class Tank extends GameObject implements Defensive, Collidable
```
- สืบทอดจาก `GameObject` สำหรับคุณสมบัติพื้นฐาน
- ใช้ `Defensive` interface สำหรับระบบการป้องกัน
- ใช้ `Collidable` interface สำหรับการตรวจสอบการชน

## ตัวแปรสำคัญ

### ค่าคงที่
- `static final int Y_OFFSET = -30` - offset การแสดงผลในแนวตั้ง
- `static final int MINIMUM_HEALTH = 0` - เลือดต่ำสุด

### สถานะปัจจุบัน
- `int currentHealth` - เลือดปัจจุบัน (เริ่มต้น 10,000)
- `boolean hasBeenAttacked` - เคยถูกโจมตีหรือยัง

### รูปภาพ
- `Image defendImage` - รูปภาพเมื่อถูกโจมตี (tankDef.png)

## Methods อย่างละเอียด

### Constructor
```java
public Tank(int gridColumn, int gridRow, int tileSize, Image tankImage)
```
**วัตถุประสงค์**: สร้าง Tank ที่ตำแหน่งกริดที่กำหนด
**พารามิเตอร์**:
- `gridColumn` - คอลัมน์ในกริด
- `gridRow` - แถวในกริด  
- `tileSize` - ขนาดไทล์เป็นพิกเซล
- `tankImage` - รูปภาพ Tank ปกติ
**การทำงาน**:
1. เรียก constructor ของ GameObject ด้วยขนาด 2x2 ไทล์
2. โหลดรูป tankDef.png สำหรับโหมดป้องกัน
3. หากโหลดไม่สำเร็จ ใช้รูปปกติแทน

### update()
```java
public void update()
```
**วัตถุประสงค์**: อัปเดต Tank ทุก frame
**การทำงาน**: ไม่ทำอะไร เพราะ Tank เป็นหน่วยป้องกันแบบ passive
**หมายเหตุ**: Method นี้มีไว้เพื่อความเข้ากันได้กับ interface

## Implementation ของ Defensive Interface

### takeDamage(int damageAmount)
```java
@Override
public void takeDamage(int damageAmount)
```
**วัตถุประสงค์**: รับความเสียหายจากศัตรู
**การทำงาน**:
1. **เปลี่ยนรูปภาพ**: หากถูกโจมตีครั้งแรก เปลี่ยนเป็น defendImage
2. **ลดเลือด**: ลดเลือดตามจำนวนความเสียหาย
3. **ตรวจสอบขั้นต่ำ**: ไม่ให้เลือดต่ำกว่า 0
4. **แสดงผล**: พิมพ์เลือดปัจจุบันใน console

### isDestroyed()
```java
@Override
public boolean isDestroyed()
```
**วัตถุประสงค์**: ตรวจสอบว่า Tank ถูกทำลายหรือยัง
**การทำงาน**: return true หากเลือดเหลือ 0

### getCurrentHealth()
```java
@Override
public int getCurrentHealth()
```
**วัตถุประสงค์**: ดึงเลือดปัจจุบัน
**การทำงาน**: return currentHealth

### getMaxHealth()
```java
@Override
public int getMaxHealth()
```
**วัตถุประสงค์**: ดึงเลือดสูงสุด
**การทำงาน**: return Constants.Entities.TANK_INITIAL_HEALTH (10,000)

### getDefenseRating()
```java
@Override
public int getDefenseRating()
```
**วัตถุประสงค์**: ดึงค่าป้องกัน
**การทำงาน**: return 0 (Tank ไม่มีค่าป้องกัน)

## Implementation ของ Collidable Interface

### getCollisionBounds()
```java
@Override
public Rectangle getCollisionBounds()
```
**วัตถุประสงค์**: ดึง bounds สำหรับการตรวจสอบการชน
**การทำงาน**: return Rectangle ที่ตำแหน่งปัจจุบัน + Y_OFFSET

## Legacy Methods (เพื่อความเข้ากันได้)

### damage(int damageAmount)
```java
public void damage(int damageAmount)
```
**วัตถุประสงค์**: Method เก่าสำหรับรับความเสียหาย
**การทำงาน**: เรียก takeDamage() ของ Defensive interface

### isDead()
```java
public boolean isDead()
```
**วัตถุประสงค์**: Method เก่าสำหรับตรวจสอบการตาย
**การทำงาน**: เรียก isDestroyed() ของ Defensive interface

### getBounds()
```java
public Rectangle getBounds()
```
**วัตถุประสงค์**: Method เก่าสำหรับดึง bounds
**การทำงาน**: เรียก getCollisionBounds() ของ Collidable interface

## Utility Methods

### getGridColumn(int tileSize)
```java
public int getGridColumn(int tileSize)
```
**วัตถุประสงค์**: แปลงตำแหน่งพิกเซลเป็นคอลัมน์กริด
**การทำงาน**: return positionX / tileSize
**การใช้งาน**: ใช้สำหรับการตรวจสอบตำแหน่งและการวางหน่วย

### getGridRow(int tileSize)
```java
public int getGridRow(int tileSize)
```
**วัตถุประสงค์**: แปลงตำแหน่งพิกเซลเป็นแถวกริด
**การทำงาน**: return positionY / tileSize

### getHealthPercentage()
```java
public double getHealthPercentage()
```
**วัตถุประสงค์**: คำนวณเปอร์เซ็นต์เลือด
**การทำงาน**: return (double) currentHealth / TANK_INITIAL_HEALTH
**การใช้งาน**: ใช้สำหรับแสดงแถบเลือด

## การแสดงผล

### draw(Graphics graphics)
```java
@Override
public void draw(Graphics graphics)
```
**วัตถุประสงค์**: วาด Tank บนหน้าจอ
**การทำงาน**:
1. วาดรูป Tank ที่ตำแหน่งปัจจุบัน + Y_OFFSET
2. เรียก drawHealthBar() เพื่อวาดแถบเลือด

### drawHealthBar(Graphics graphics)
```java
private void drawHealthBar(Graphics graphics)
```
**วัตถุประสงค์**: วาดแถบเลือดเหนือ Tank
**การทำงาน**:
1. **ตรวจสอบเงื่อนไข**: แสดงเฉพาะเมื่อเลือดไม่เต็ม
2. **คำนวณตำแหน่ง**: เหนือ Tank 8 พิกเซล
3. **วาดพื้นหลัง**: สี่เหลี่ยมสีแดง
4. **วาดเลือด**: สี่เหลี่ยมสีเขียวตามเปอร์เซ็นต์เลือด
5. **วาดขอบ**: เส้นขอบสีดำ

## คุณสมบัติเฉพาะของ Tank

### การป้องกันแบบ Passive
- Tank ไม่โจมตีศัตรู
- ทำหน้าที่เป็นกำแพงกั้นเท่านั้น
- ศัตรูต้องทำลาย Tank ก่อนจึงจะเดินต่อได้

### ระบบเลือดสูง
- เลือดเริ่มต้น 10,000 จุด
- ไม่มีค่าป้องกัน (รับความเสียหายเต็มจำนวน)
- สามารถกั้นศัตรูได้นาน

### การเปลี่ยนรูปภาพ
- เริ่มต้นใช้รูป tank.png
- เมื่อถูกโจมตีครั้งแรก เปลี่ยนเป็น tankDef.png
- แสดงให้เห็นว่า Tank กำลังถูกโจมตี

### ขนาดและการวาง
- ขนาด 2x2 ไทล์ (64x64 พิกเซล)
- วางได้เฉพาะบนไทล์ถนน
- ไม่สามารถวางซ้อนทับกันได้

## ความสัมพันธ์กับคลาสอื่น
- **GameObject.java**: สืบทอดคุณสมบัติพื้นฐาน
- **Defensive.java**: ใช้ interface สำหรับระบบป้องกัน
- **Collidable.java**: ใช้ interface สำหรับการชน
- **Enemy.java**: ศัตรูจะโจมตี Tank
- **Map.java**: Map จัดการการวางและอัปเดต Tank
- **GamePanel.java**: ผู้เล่นสามารถวาง Tank ผ่าน UI
- **Constants.java**: ใช้ค่าคงที่สำหรับสถิติและราคา

## จุดเด่นของการออกแบบ
1. **Pure Defense**: เน้นการป้องกันอย่างเดียว ไม่โจมตี
2. **High Durability**: เลือดสูงมากเพื่อกั้นศัตรูได้นาน
3. **Visual Feedback**: เปลี่ยนรูปภาพเมื่อถูกโจมตี
4. **Interface Compliance**: ใช้ interface เพื่อความยืดหยุ่น
5. **Legacy Support**: รองรับ method เก่าเพื่อความเข้ากันได้
# Tank.java - หน่วยป้องกันแบบ Tank

## ภาพรวม
Tank เป็นหน่วยป้องกันที่ทำหน้าที่เป็นกำแพงป้องกัน ไม่สามารถโจมตีได้ แต่มีเลือดมากและสามารถกั้นศัตรูได้

## คลาสและการสืบทอด
```java
public class Tank extends GameObject implements Defensive, Collidable
```
- สืบทอดจาก `GameObject` สำหรับคุณสมบัติพื้นฐาน (ตำแหน่ง, ขนาด, การวาด, getGridColumn/Row)
- ใช้ `Defensive` interface สำหรับระบบการป้องกัน (takeDamage, isDestroyed, getHealthPercentage, etc.)
- ใช้ `Collidable` interface สำหรับการตรวจสอบการชน

## ตัวแปรสำคัญ

### สถานะปัจจุบัน
- `int currentHealth` - เลือดปัจจุบัน (เริ่มต้น 10,000 จาก Constants)
- `boolean hasBeenAttacked` - เคยถูกโจมตีหรือยัง

### รูปภาพ
- `Image defendImage` - รูปภาพเมื่อถูกโจมตี (tankDef.png)

## Methods สำคัญ

### Constructor
```java
public Tank(int gridColumn, int gridRow, int tileSize, Image tankImage)
```
**การทำงาน**:
1. เรียก constructor ของ GameObject ด้วยขนาด 2x2 ไทล์
2. โหลดรูป tankDef.png สำหรับโหมดป้องกัน
3. หากโหลดไม่สำเร็จ ใช้รูปปกติแทน

### update()
ไม่ทำอะไร เพราะ Tank เป็นหน่วยป้องกันแบบ passive

### takeDamage(int damageAmount)
**การทำงาน**:
1. เปลี่ยนเป็น defendImage หากถูกโจมตีครั้งแรก
2. ลดเลือดตามจำนวนความเสียหาย
3. ตรวจสอบไม่ให้เลือดต่ำกว่า MINIMUM_HEALTH

### getCollisionBounds()
return Rectangle ที่ตำแหน่ง `(positionX + TANK_X_OFFSET, positionY + TANK_Y_OFFSET)` ขนาด `(objectWidth, objectHeight)`

### draw(Graphics graphics)
**การทำงาน**:
1. วาดรูปภาพที่ตำแหน่ง `(positionX + TANK_X_OFFSET, positionY + TANK_Y_OFFSET)`
2. วาดแถบเลือดด้านบน (หากเลือดไม่เต็ม)

## ค่าคงที่ที่ใช้จาก Constants
- `TANK_INITIAL_HEALTH = 10000` - เลือดเริ่มต้น
- `TANK_COST = 120` - ราคา
- `TANK_X_OFFSET = 0` - offset แนวนอน
- `TANK_Y_OFFSET = -30` - offset แนวตั้ง
- `MINIMUM_HEALTH = 0` - เลือดต่ำสุด

## Methods ที่ได้จาก Base Classes/Interfaces
- `getGridColumn(int)`, `getGridRow(int)` - จาก GameObject
- `getHealthPercentage()` - จาก Defensive interface
- `damage(int)`, `isDead()` - legacy methods จาก Defensive interface
- `getCurrentHealth()`, `getMaxHealth()`, `getDefenseRating()` - จาก Defensive interface

## จุดเด่น
- เลือดสูงมาก (10,000)
- ราคาปานกลาง (120 เหรียญ)
- เปลี่ยนรูปภาพเมื่อถูกโจมตี
- ไม่มีค่าป้องกัน (รับความเสียหายเต็มจำนวน)

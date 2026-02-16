# Assassin.java - กับดักนักฆ่า

## ภาพรวม
Assassin เป็นหน่วยป้องกันแบบกับดักที่โจมตีศัตรูที่เดินผ่าน เป็นกับดักซ่อนที่ทำความเสียหายสูงเมื่อศัตรูเข้ามาในระยะ ไม่สามารถถูกโจมตีโดยศัตรูและโจมตีเฉพาะเมื่อศัตรูอยู่ในระยะเท่านั้น

## คลาสและการสืบทอด
```java
public class Assassin extends GameObject
```
- สืบทอดจาก `GameObject` สำหรับคุณสมบัติพื้นฐาน
- ไม่ implement Defensive interface (ไม่สามารถถูกโจมตีได้)

## ตัวแปรสำคัญ

### การอ้างอิงและการจัดการ
- `ArrayList<Enemy> enemyList` - รายการศัตรูสำหรับการตรวจสอบ
- `ArrayList<Enemy> attackedEnemies` - รายการศัตรูที่โจมตีไปแล้ว (ป้องกันการโจมตีซ้ำ)
- `int attackCooldown` - ตัวจับเวลา cooldown
- `boolean isAttacking` - สถานะการโจมตี
- `int attackAnimationTimer` - ตัวจับเวลาแอนิเมชัน

### รูปภาพ
- `Image normalImage` - รูปภาพปกติ
- `Image attackImage` - รูปภาพเมื่อโจมตี

## Methods สำคัญ

### Constructor
```java
public Assassin(int gridColumn, int gridRow, int tileSize, Image assassinImage, ArrayList<Enemy> enemies)
```
**การทำงาน**:
1. เรียก constructor ของ GameObject ด้วยขนาด 2x2 ไทล์
2. เก็บอ้างอิงรายการศัตรูและรูปภาพปกติ
3. โหลดรูปภาพโจมตี (assasinAttack.png)

### update()
**การทำงาน**:
1. **อัปเดตแอนิเมชันการโจมตี**: เปลี่ยนกลับเป็นรูปปกติหลังจาก ATTACK_ANIMATION_DURATION frames
2. **ลด Attack Cooldown**: ลด attackCooldown หากมากกว่า 0
3. **ตรวจสอบศัตรูในระยะ**: วนลูปผ่านศัตรูทั้งหมด
   - คำนวณระยะทางไปยังศัตรู
   - หากอยู่ในระยะ ASSASSIN_ATTACK_RANGE และยังไม่เคยโจมตี: โจมตีและเพิ่มเข้า attackedEnemies
   - หากออกจากระยะ: ลบออกจาก attackedEnemies (สามารถโจมตีอีกครั้งได้)
4. **ทำความสะอาด**: ลบศัตรูที่ตายแล้วออกจาก attackedEnemies

### calculateDistanceToEnemy(Enemy enemy)
คำนวณระยะทางจากจุดกึ่งกลางของ Assassin ไปยังจุดกึ่งกลางของศัตรู

### attackEnemy(Enemy enemy)
**การทำงาน**:
1. เปลี่ยนเป็น attackImage
2. เรียก enemy.takeDamage(ASSASSIN_ATTACK_DAMAGE)
3. ตั้ง attackCooldown = ASSASSIN_ATTACK_COOLDOWN

### draw(Graphics graphics)
วาดรูปภาพที่ตำแหน่ง `(positionX + ASSASSIN_X_OFFSET, positionY + ASSASSIN_Y_OFFSET)`

## ค่าคงที่ที่ใช้จาก Constants
- `ASSASSIN_COST = 20` - ราคา
- `ASSASSIN_ATTACK_DAMAGE = 30` - ความเสียหายสูง
- `ASSASSIN_ATTACK_RANGE = 80` - ระยะโจมตีใกล้ (melee)
- `ASSASSIN_ATTACK_COOLDOWN = 30` - cooldown (0.5 วินาที)
- `ASSASSIN_X_OFFSET = 0` - offset แนวนอน
- `ASSASSIN_Y_OFFSET = -30` - offset แนวตั้ง
- `ATTACK_ANIMATION_DURATION = 15` - ระยะเวลาแอนิเมชัน

## จุดเด่น
- ความเสียหายสูงมาก (30)
- ไม่สามารถถูกโจมตีได้ (ไม่มีเลือด)
- ระยะโจมตีใกล้ (80 พิกเซล)
- โจมตีเร็ว (0.5 วินาทีต่อครั้ง)
- ราคาปานกลาง (20 เหรียญ)
- เปลี่ยนรูปเมื่อโจมตี
- ระบบป้องกันการโจมตีซ้ำ (ต้องออกจากระยะก่อนจึงจะโจมตีอีกครั้ง)

## จุดอ่อน
- ต้องรอศัตรูเข้ามาใกล้
- ไม่สามารถโจมตีศัตรูที่อยู่ห่างได้
- ต้องวางในเส้นทางศัตรูเพื่อประสิทธิภาพสูงสุด

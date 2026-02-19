# BaseEnemy.java - คลาสพื้นฐานสำหรับศัตรู

## ภาพรวม
BaseEnemy เป็น abstract base class สำหรับศัตรูทุกประเภทในเกม จัดการ logic ที่ใช้ร่วมกันเช่น pathfinding, การเคลื่อนที่, การต่อสู้, การแอนิเมชัน และการแสดงผล

## คลาสและการสืบทอด
```java
public abstract class BaseEnemy
```
- `abstract class` - ไม่สามารถสร้าง instance ได้โดยตรง
- เป็นคลาสพื้นฐานสำหรับ Enemy และ EnemyBoss

## ตัวแปรสำคัญ

### ตำแหน่งและการเคลื่อนที่
- `double positionX, positionY` - ตำแหน่งปัจจุบันของศัตรู
- `ArrayList<Point> movementPath` - เส้นทางที่ศัตรูจะเดิน
- `int currentPathIndex` - ตำแหน่งปัจจุบันในเส้นทาง

### การอ้างอิงเกม
- `Map gameMap` - อ้างอิงถึงแผนที่เกม
- `House targetHouse` - บ้านที่เป็นเป้าหมาย
- `CoinManager coinManager` - ตัวจัดการเหรียญ
- `ArrayList<BaseEnemy> allEnemies` - อ้างอิงถึงศัตรูทั้งหมดสำหรับหลีกเลี่ยงการชน

### สถานะการต่อสู้
- `boolean isAttacking` - กำลังโจมตีหรือไม่
- `int attackTimer` - ตัวนับเวลาสำหรับการโจมตี
- `Object currentAttackTarget` - เป้าหมายที่กำลังโจมตี

### สถานะศัตรู
- `boolean isDead` - ศัตรูตายหรือยัง
- `int currentHealth` - เลือดปัจจุบัน

### สถานะแอนิเมชัน
- `int animationTimer` - ตัวนับเวลาสำหรับแอนิเมชัน
- `boolean useAlternateFrame` - ใช้เฟรมสลับหรือไม่

## Constructor
```java
protected BaseEnemy(Map gameMap, CoinManager coinManager, int initialHealth)
```
**วัตถุประสงค์**: สร้างศัตรูพื้นฐาน
**พารามิเตอร์**:
- `gameMap` - แผนที่เกม
- `coinManager` - ตัวจัดการเหรียญ
- `initialHealth` - เลือดเริ่มต้น
**การทำงาน**:
1. บันทึกการอ้างอิงแผนที่และบ้าน
2. ตั้งค่าเลือดเริ่มต้น
3. สร้างเส้นทางการเคลื่อนที่
4. ตั้งตำแหน่งเริ่มต้น

## Abstract Methods (คลาสลูกต้อง implement)

### getMaxHealth()
return เลือดสูงสุดของศัตรู

### getAttackDamage()
return ความเสียหายที่ศัตรูสร้าง

### getAttackCooldown()
return เวลา cooldown ระหว่างการโจมตี (frames)

### getSpeed()
return ความเร็วการเคลื่อนที่ (พิกเซล/frame)

### getSize()
return ขนาดของศัตรู (พิกเซล)

### getXOffset()
return offset แนวนอนสำหรับการวาด

### getYOffset()
return offset แนวตั้งสำหรับการวาด

### getIdleImage()
return รูปภาพเมื่อหยุดนิ่ง/ปกติ

### getWalkImage()
return รูปภาพเมื่อเดิน

### getAttackImage()
return รูปภาพเมื่อโจมตี

### getCoinReward()
return จำนวนเหรียญที่ได้เมื่อฆ่าศัตรู

## Methods สำคัญ

### update()
**การทำงาน**:
1. ตรวจสอบว่าศัตรูตายหรือยัง - หากตายแล้ว return
2. **อัปเดตแอนิเมชัน**: เรียก updateAnimation()
3. หากกำลังโจมตี: เรียก processAttack() และ return
4. ตรวจสอบการชนกับหน่วยป้องกัน: เรียก checkForDefensiveCollision()
5. เคลื่อนที่ตามเส้นทาง: เรียก moveAlongPath()
6. ตรวจสอบการชนกับบ้าน: เรียก checkForHouseCollision()
7. ตรวจสอบว่าถึงจุดสิ้นสุดหรือยัง: เรียก checkIfReachedEnd()

### updateAnimation()
**การทำงาน**:
1. เพิ่ม animationTimer
2. กำหนด frameDuration ตามสถานะ:
   - หากกำลังโจมตี: ใช้ ENEMY_ATTACK_ANIMATION_FRAMES (20 frames)
   - หากกำลังเดิน: ใช้ ENEMY_WALK_ANIMATION_FRAMES (30 frames)
3. เมื่อ animationTimer >= frameDuration:
   - สลับ useAlternateFrame
   - รีเซ็ต animationTimer เป็น 0

### getCurrentImage()
**การทำงาน**:
- หากกำลังโจมตี: สลับระหว่าง getAttackImage() และ getIdleImage()
- หากกำลังเดิน: สลับระหว่าง getWalkImage() และ getIdleImage()
**การใช้งาน**: เรียกใน draw() เพื่อแสดงรูปภาพที่ถูกต้อง

### draw(Graphics graphics)
**การทำงาน**:
1. ตรวจสอบว่าศัตรูยังไม่ตาย
2. วาดรูปภาพปัจจุบัน (จาก getCurrentImage()) ที่ตำแหน่ง (positionX + offset, positionY + offset)
3. วาดแถบเลือด: เรียก drawHealthBar()

**หมายเหตุ**: ใช้ getCurrentImage() แทน getImage() เพื่อรองรับแอนิเมชัน

## ระบบแอนิเมชัน

### การทำงาน
ศัตรูมีระบบแอนิเมชันที่สลับระหว่างรูปภาพต่างๆ ตามสถานะ:

1. **เมื่อเดิน**: สลับระหว่าง IdleImage และ WalkImage ทุก 30 frames (0.5 วินาที)
2. **เมื่อโจมตี**: สลับระหว่าง IdleImage และ AttackImage ทุก 20 frames (0.33 วินาที)

### ค่าคงที่ที่ใช้
- `ENEMY_WALK_ANIMATION_FRAMES = 30` - ระยะเวลาแต่ละเฟรมเมื่อเดิน
- `ENEMY_ATTACK_ANIMATION_FRAMES = 20` - ระยะเวลาแต่ละเฟรมเมื่อโจมตี

### ตัวอย่างการทำงาน
```
เมื่อเดิน:
Frame 0-29: แสดง IdleImage (catEnemy.png)
Frame 30-59: แสดง WalkImage (catEnemyWalk.png)
Frame 60-89: แสดง IdleImage (catEnemy.png)
...

เมื่อโจมตี:
Frame 0-19: แสดง IdleImage (catEnemy.png)
Frame 20-39: แสดง AttackImage (catEnemyAttack.png)
Frame 40-59: แสดง IdleImage (catEnemy.png)
...
```

## ระบบหลีกเลี่ยงการชนขณะเดิน

### การทำงาน
ศัตรูจะตรวจสอบว่ามีศัตรูตัวอื่นขวางทางหรือไม่ก่อนเดิน:

1. ตรวจสอบระยะห่างกับศัตรูตัวอื่น
2. หากระยะห่างน้อยกว่า minSafeDistance และศัตรูตัวนั้นอยู่ด้านหน้า: หยุดเดิน
3. หากไม่มีอุปสรรค: เดินต่อไป

### ค่าคงที่ที่ใช้
- `ENEMY_SPACING_MULTIPLIER = 0.5` - ตัวคูณระยะห่าง (0.5 = ระยะห่าง 50% ของขนาด)

### หมายเหตุ
- ศัตรูที่กำลังโจมตีจะไม่ถูกนับเป็นอุปสรรค
- ศัตรูที่ตายแล้วจะไม่ถูกนับเป็นอุปสรรค

## คลาสที่สืบทอด

### Enemy.java
- ขนาด: 64 พิกเซล
- ความเร็ว: 0.5 พิกเซล/frame
- เลือด: 3000
- ความเสียหาย: 500
- รูปภาพ: catEnemy.png, catEnemyWalk.png, catEnemyAttack.png

### EnemyBoss.java
- ขนาด: 96 พิกเซล (ใหญ่กว่า Enemy)
- ความเร็ว: 0.3 พิกเซล/frame (ช้ากว่า Enemy)
- เลือด: 10000 (มากกว่า Enemy มาก)
- ความเสียหาย: 1000 (สูงกว่า Enemy 2 เท่า)
- รูปภาพ: enemyBoss.png, enemyBossFly.png, enemyBossAttack.png

## จุดเด่นของการออกแบบ

### Code Reuse
- ลดการเขียนโค้ดซ้ำระหว่าง Enemy และ EnemyBoss
- Logic การเคลื่อนที่และการต่อสู้เหมือนกัน

### Flexibility
- คลาสลูกกำหนดค่าเฉพาะของตัวเอง
- สามารถ override methods ได้หากต้องการพฤติกรรมพิเศษ

### Animation System
- ระบบแอนิเมชันที่ยืดหยุ่น รองรับหลายสถานะ
- ง่ายต่อการเพิ่มแอนิเมชันใหม่

### Maintainability
- แก้ไข logic พื้นฐานที่เดียว
- ง่ายต่อการดูแลและแก้ไข

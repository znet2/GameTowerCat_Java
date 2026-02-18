# BaseEnemy.java - คลาสพื้นฐานสำหรับศัตรู

## ภาพรวม
BaseEnemy เป็น abstract base class สำหรับศัตรูทุกประเภทในเกม จัดการ logic ที่ใช้ร่วมกันเช่น pathfinding, การเคลื่อนที่, การต่อสู้, และการแสดงผล

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
return เลือดสูงสุด

### getAttackDamage()
return ความเสียหายต่อการโจมตี

### getAttackCooldown()
return เวลาคูลดาวน์ระหว่างการโจมตี (frames)

### getSpeed()
return ความเร็วการเคลื่อนที่ (พิกเซล/frame)

### getSize()
return ขนาดของศัตรู (พิกเซล)

### getXOffset() / getYOffset()
return offset สำหรับการวาดรูปภาพ

### getImage()
return รูปภาพของศัตรู

### getCoinReward()
return จำนวนเหรียญที่ได้เมื่อฆ่าศัตรู

## Methods - Pathfinding

### buildMovementPath()
```java
protected void buildMovementPath()
```
**วัตถุประสงค์**: สร้างเส้นทางจากจุดเริ่มต้นไปยังบ้าน
**การทำงาน**:
1. หาจุดเริ่มต้น (ช่องถนนซ้ายสุด)
2. หาตำแหน่งบ้าน
3. ใช้ BFS สร้างเส้นทาง
4. หากล้มเหลว ใช้ fallback path

### findRoadStartPosition(int[][] mapGrid)
```java
protected Point findRoadStartPosition(int[][] mapGrid)
```
**วัตถุประสงค์**: หาช่องถนนซ้ายสุดเป็นจุดเริ่มต้น
**return**: Point ของช่องถนนซ้ายสุด

### findHousePosition(int[][] mapGrid)
```java
protected Point findHousePosition(int[][] mapGrid)
```
**วัตถุประสงค์**: หาตำแหน่งบ้านบนแผนที่
**return**: Point ของตำแหน่งบ้าน

### createPathUsingBFS(...)
```java
protected void createPathUsingBFS(int[][] mapGrid, int tileSize, Point start, Point goal)
```
**วัตถุประสงค์**: สร้างเส้นทางด้วย BFS algorithm
**การทำงาน**:
1. ใช้ queue สำหรับ BFS
2. เดินได้เฉพาะช่องถนน (tile type 0)
3. สร้าง parent array สำหรับ reconstruct path

### reconstructPath(...)
```java
protected void reconstructPath(Point[][] parent, Point start, Point goal, int tileSize)
```
**วัตถุประสงค์**: สร้างเส้นทางจาก parent array
**การทำงาน**: ย้อนกลับจากเป้าหมายไปจุดเริ่มต้น

### createFallbackPath(...)
```java
protected void createFallbackPath(int[][] mapGrid, int tileSize, Point start)
```
**วัตถุประสงค์**: สร้างเส้นทาง fallback หาก BFS ล้มเหลว
**การทำงาน**: เดินตรงไปทางขวา

### initializeStartingPosition()
```java
protected void initializeStartingPosition()
```
**วัตถุประสงค์**: ตั้งค่าตำแหน่งเริ่มต้นของศัตรู
**การทำงาน**: ตั้งตำแหน่งเป็นจุดแรกในเส้นทาง

## Methods - Update & Behavior

### update()
```java
public void update()
```
**วัตถุประสงค์**: อัปเดตพฤติกรรมศัตรูในแต่ละ frame
**การทำงาน**:
1. ตรวจสอบว่าตายแล้วหรือไม่ (ถ้าใช่ return)
2. **ถ้ากำลังโจมตีอยู่**: ประมวลผลการโจมตี (return)
3. **ถ้ายังไม่ได้โจมตี**: ตรวจสอบการชนกับหน่วยป้องกัน (ถ้าชน return)
4. เคลื่อนที่ตามเส้นทาง
5. ตรวจสอบการชนกับบ้าน
6. ตรวจสอบว่าถึงจุดสิ้นสุดหรือไม่

**หมายเหตุ**: 
- ต้องตรวจสอบ isAttacking ก่อนเพื่อให้ enemy ที่กำลังโจมตีอยู่สามารถ process attack ได้
- ถ้าตรวจสอบการชนก่อน จะทำให้ไม่มีทางถึง processAttack()

### checkIfReachedEnd()
```java
protected void checkIfReachedEnd()
```
**วัตถุประสงค์**: ตรวจสอบว่าถึงจุดสิ้นสุดเส้นทางหรือไม่
**การทำงาน**: ถ้า currentPathIndex >= movementPath.size() ตั้ง isDead = true

## Methods - Collision Detection

### checkForDefensiveCollision()
```java
protected boolean checkForDefensiveCollision()
```
**วัตถุประสงค์**: ตรวจสอบการชนกับหน่วยป้องกัน
**การทำงาน**:
1. ตรวจสอบการชนกับ Tank ทั้งหมด
2. ตรวจสอบการชนกับ Magic tower ทั้งหมด
3. ตรวจสอบการชนกับ Archer tower ทั้งหมด
4. หากชน เริ่มโจมตีเป้าหมายนั้น
**return**: true หากเจอการชน, false หากไม่เจอ
**หมายเหตุ**: ศัตรูสามารถซ้อนทับกันได้ ไม่มีการปรับตำแหน่ง

### checkAndAttackUnit(Defensive unit)
```java
private boolean checkAndAttackUnit(Defensive unit)
```
**วัตถุประสงค์**: Helper method สำหรับตรวจสอบและโจมตีหน่วยป้องกันเดี่ยว
**การทำงาน**:
1. ตรวจสอบว่าหน่วยตายหรือยัง
2. ดึง bounds ของหน่วย
3. ตรวจสอบการชน
4. ถ้าชน เริ่มโจมตี
**return**: true หากเจอการชนและเริ่มโจมตี

## Methods - Combat

### startAttacking(Object target)
```java
protected void startAttacking(Object target)
```
**วัตถุประสงค์**: เริ่มโหมดโจมตี
**การทำงาน**:
1. บันทึกเป้าหมาย
2. ตั้ง isAttacking = true

### processAttack()
```java
protected void processAttack()
```
**วัตถุประสงค์**: ประมวลผลเวลาและการโจมตี
**การทำงาน**:
1. เพิ่ม attackTimer
2. ถ้า attackTimer >= getAttackCooldown() เรียก executeAttack() และรีเซ็ต timer

### executeAttack()
```java
protected void executeAttack()
```
**วัตถุประสงค์**: ดำเนินการโจมตีเป้าหมาย
**การทำงาน**:
1. ตรวจสอบว่ามีเป้าหมายหรือไม่
2. ถ้าเป้าหมายเป็น Defensive (Tank, Magic, Archer, House):
   - ตรวจสอบว่าตายหรือยัง
   - ถ้าตายแล้ว เรียก stopAttacking()
   - ถ้ายังไม่ตาย เรียก takeDamage()
**หมายเหตุ**: 
- ใช้ Defensive interface เพื่อจัดการทุกประเภทเป้าหมายแบบเดียวกัน
- House ก็เป็น Defensive ดังนั้นไม่ต้องแยก case

### stopAttacking()
```java
protected void stopAttacking()
```
**วัตถุประสงค์**: หยุดโหมดโจมตี
**การทำงาน**:
1. ตั้ง isAttacking = false
2. ล้างเป้าหมาย
3. รีเซ็ต attackTimer

## Methods - Movement

### moveAlongPath()
```java
protected void moveAlongPath()
```
**วัตถุประสงค์**: เคลื่อนที่ตามเส้นทาง
**การทำงาน**:
1. ดึงจุดเป้าหมายจาก movementPath
2. ตรวจสอบว่ามีศัตรูตัวอื่นขวางทางหรือไม่
3. ถ้าไม่มีขวาง เรียก moveTowardsPoint()
4. ถ้าถึงจุดแล้ว เพิ่ม currentPathIndex
5. ถ้ามีขวาง รอ (ไม่เคลื่อนที่ในเฟรมนี้)

### isPathBlocked()
```java
protected boolean isPathBlocked()
```
**วัตถุประสงค์**: ตรวจสอบว่ามีศัตรูตัวอื่นขวางทางด้านหน้าหรือไม่
**การทำงาน**:
1. ตรวจสอบว่ามี allEnemies reference และยังมีเส้นทางที่ต้องเดินหรือไม่
2. ดึงจุดหมายถัดไปจาก movementPath
3. คำนวณระยะปลอดภัยขั้นต่ำจาก Constants.Entities.ENEMY_SPACING_MULTIPLIER
4. วนลูปตรวจสอบศัตรูทั้งหมด:
   - เรียก shouldSkipEnemy() เพื่อข้ามตัวเอง, ศัตรูที่ตาย, และศัตรูที่กำลังโจมตี
   - เรียก calculateDistance() เพื่อคำนวณระยะห่าง
   - ถ้าระยะห่าง < ระยะปลอดภัย และ isEnemyAhead() คืนค่า true → ทางถูกขวาง
**return**: true ถ้ามีขวาง, false ถ้าไม่มี
**Helper methods**:
- `shouldSkipEnemy()`: ตรวจสอบว่าควรข้ามศัตรูนี้หรือไม่
- `calculateDistance()`: คำนวณระยะห่างระหว่าง 2 rectangles
- `isEnemyAhead()`: ตรวจสอบว่าศัตรูตัวอื่นอยู่ด้านหน้าหรือไม่
- `calculateDistanceToPoint()`: คำนวณระยะห่างจากตำแหน่งไปยังจุด
**หมายเหตุ**: Refactored เป็น helper methods เพื่อความกระชับและอ่านง่าย

### moveTowardsPoint(Point targetPoint)
```java
protected boolean moveTowardsPoint(Point targetPoint)
```
**วัตถุประสงค์**: เคลื่อนที่ไปยังจุดเฉพาะด้วยความเร็วคงที่
**การทำงาน**:
1. คำนวณระยะทางและทิศทาง
2. ถ้าระยะทาง > ความเร็ว เคลื่อนที่ไปตามทิศทาง
3. ถ้าระยะทาง <= ความเร็ว ตั้งตำแหน่งเป็นจุดเป้าหมาย
**return**: true ถ้าถึงจุดแล้ว, false ถ้ายังไม่ถึง

### checkForHouseCollision()
```java
protected void checkForHouseCollision()
```
**วัตถุประสงค์**: ตรวจสอบการชนกับบ้าน
**การทำงาน**: 
- ตรวจสอบว่ายังไม่ได้โจมตีอยู่ และชนกับบ้าน
- ถ้าใช่ เริ่มโจมตีบ้าน
**หมายเหตุ**: ต้องตรวจสอบ !isAttacking เพื่อไม่ให้ขัดจังหวะการโจมตี defensive units

## Methods - State & Rendering

### takeDamage(int damage)
```java
public void takeDamage(int damage)
```
**วัตถุประสงค์**: รับความเสียหายและลดเลือด
**การทำงาน**:
1. ลด currentHealth
2. ถ้า health <= 0:
   - ตั้ง isDead = true
   - เรียก coinManager.addCoins(getCoinReward())

### getCurrentHealth()
```java
public int getCurrentHealth()
```
return เลือดปัจจุบัน

### kill()
```java
public void kill()
```
ตั้ง isDead = true

### isDead()
```java
public boolean isDead()
```
return true ถ้าศัตรูตาย

### draw(Graphics graphics)
```java
public void draw(Graphics graphics)
```
**วัตถุประสงค์**: วาดศัตรูบนหน้าจอ
**การทำงาน**:
1. วาดรูปภาพที่ตำแหน่งปัจจุบัน (ใช้ offset)
2. วาดแถบเลือด

### drawHealthBar(Graphics graphics)
```java
protected void drawHealthBar(Graphics graphics)
```
**วัตถุประสงค์**: วาดแถบเลือดเหนือศัตรู
**การทำงาน**:
1. คำนวณเปอร์เซ็นต์เลือด
2. วาดพื้นหลังสีเทา
3. วาดแถบเลือดสีเขียว/เหลือง/แดง ตามเปอร์เซ็นต์

### setAllEnemies(ArrayList<BaseEnemy> enemies)
```java
public void setAllEnemies(ArrayList<BaseEnemy> enemies)
```
**วัตถุประสงค์**: ตั้งค่าอ้างอิงถึงศัตรูทั้งหมดสำหรับหลีกเลี่ยงการชน
**พารามิเตอร์**: `enemies` - รายการศัตรูทั้งหมดในเกม
**การใช้งาน**: เรียกโดย WaveManager เมื่อสร้างศัตรูใหม่

### getBounds()
```java
public Rectangle getBounds()
```
**วัตถุประสงค์**: ดึง bounding box สำหรับการตรวจสอบการชน
**return**: Rectangle ที่ตำแหน่งและขนาดของศัตรู

## คลาสที่สืบทอด

### Enemy.java
- เลือด: 3,000
- ความเสียหาย: 500
- ความเร็ว: 0.5
- ขนาด: 64 พิกเซล
- เหรียญ: 15

### EnemyBoss.java
- เลือด: 10,000 (3.33x)
- ความเสียหาย: 1,000 (2x)
- ความเร็ว: 0.3 (ช้ากว่า)
- ขนาด: 96 พิกเซล (1.5x)
- เหรียญ: 100 (6.67x)

## จุดเด่นของการออกแบบ

### Code Reuse
- ลดการเขียนโค้ดซ้ำระหว่าง Enemy และ Boss
- Logic การเคลื่อนที่และการต่อสู้เหมือนกัน
- ใช้ Defensive interface เพื่อจัดการหน่วยป้องกันแบบ polymorphic

### Consistency
- ศัตรูทั้งหมดทำงานแบบเดียวกัน
- ง่ายต่อการเพิ่มศัตรูประเภทใหม่

### Flexibility
- คลาสลูกกำหนดค่าเฉพาะของตัวเอง
- สามารถ override methods ได้หากต้องการพฤติกรรมพิเศษ

### Maintainability
- แก้ไข logic พื้นฐานที่เดียว
- ง่ายต่อการดูแลและแก้ไข
- ใช้ helper methods เพื่อลดความซับซ้อน

## ความสัมพันธ์กับคลาสอื่น
- **Enemy.java / EnemyBoss.java**: สืบทอดจาก BaseEnemy
- **Map.java**: ใช้สำหรับ pathfinding และ collision detection
- **CoinManager.java**: ให้รางวัลเหรียญเมื่อฆ่าศัตรู
- **Tank/Magic/Archer.java**: เป้าหมายที่ศัตรูโจมตี
- **House.java**: เป้าหมายสุดท้ายของศัตรู
- **Constants.java**: ใช้ค่าคงที่สำหรับศัตรูแต่ละประเภท

## การเปลี่ยนแปลงจากเดิม
- ลบระบบป้องกันการซ้อนทับศัตรู (ตัวแปร totalEnemyCount, attackPositionOffset, isPositionLocked)
- ลบ method positionForAttack() และ resetEnemyCount()
- ลบ abstract method getPositionOffsetMultiplier()
- ศัตรูสามารถซ้อนทับกันได้เมื่อโจมตีเป้าหมายเดียวกัน
- **เพิ่มระบบหลีกเลี่ยงการชนขณะเดิน**: ศัตรูจะรอถ้ามีศัตรูตัวอื่นอยู่ใกล้เกินไป (ไม่ทับกันขณะเดิน)
- ศัตรูที่กำลังโจมตีจะไม่ขวางทาง ทำให้ศัตรูตัวอื่นเดินผ่านได้

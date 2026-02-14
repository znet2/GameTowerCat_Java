# Enemy.java - ระบบศัตรูและ AI

## ภาพรวม
Enemy เป็นคลาสที่จัดการศัตรูในเกม รวมถึงการเคลื่อนที่ตาม pathfinding, การต่อสู้กับหน่วยป้องกัน, และ AI behavior

## คลาสและการสืบทอด
```java
public class Enemy
```
- คลาสอิสระที่ไม่สืบทอดจากคลาสอื่น

## ตัวแปรสำคัญ

### ตัวแปรภาพและตำแหน่ง
- `Image enemyImage` - รูปภาพศัตรู
- `double positionX, positionY` - ตำแหน่งปัจจุบันเป็นพิกเซล

### ระบบ Pathfinding
- `ArrayList<Point> movementPath` - เส้นทางการเคลื่อนที่
- `int currentPathIndex` - จุดปัจจุบันในเส้นทาง

### การอ้างอิงเกม
- `Map gameMap` - อ้างอิงไปยังแผนที่
- `House targetHouse` - บ้านที่ต้องโจมตี

### สถานะการต่อสู้
- `boolean isAttacking` - กำลังโจมตีหรือไม่
- `int attackTimer` - ตัวจับเวลาการโจมตี
- `Object currentAttackTarget` - เป้าหมายที่กำลังโจมตี

### สถานะศัตรู
- `boolean isDead` - ตายหรือยัง
- `int currentHealth` - เลือดปัจจุบัน

### การจัดการตำแหน่ง
- `static int totalEnemyCount` - จำนวนศัตรูทั้งหมด (สำหรับ offset)
- `int attackPositionOffset` - offset เฉพาะตัวเพื่อป้องกันการซ้อนทับ
- `boolean isPositionLocked` - ล็อคตำแหน่งขณะโจมตี

## Methods อย่างละเอียด

### Constructor
```java
public Enemy(Map gameMap, CoinManager coinManager)
```
**วัตถุประสงค์**: สร้างศัตรูใหม่และตั้งค่าเส้นทาง
**พารามิเตอร์**:
- `gameMap` - แผนที่สำหรับ pathfinding
- `coinManager` - ระบบเหรียญ (ไม่ได้ใช้ในโหมดป้องกัน)
**การทำงาน**:
1. เก็บอ้างอิงแผนที่และบ้าน
2. โหลดรูปภาพศัตรู
3. คำนวณ `attackPositionOffset` เฉพาะตัว
4. เพิ่ม `totalEnemyCount`
5. เรียก `buildMovementPath()` - สร้างเส้นทาง
6. เรียก `initializeStartingPosition()` - ตั้งตำแหน่งเริ่มต้น

### buildMovementPath()
```java
private void buildMovementPath()
```
**วัตถุประสงค์**: สร้างเส้นทางจากจุดเริ่มต้นไปยังบ้าน
**การทำงาน**:
1. ดึงข้อมูลแผนที่และขนาดไทล์
2. เรียก `findRoadStartPosition()` - หาจุดเริ่มต้น
3. เรียก `findHousePosition()` - หาจุดปลายทาง
4. เรียก `createPathUsingBFS()` - สร้างเส้นทางด้วย BFS

### findRoadStartPosition(int[][] mapGrid)
```java
private Point findRoadStartPosition(int[][] mapGrid)
```
**วัตถุประสงค์**: หาไทล์ถนนซ้ายสุดเป็นจุดเริ่มต้น
**การทำงาน**:
1. วนลูปผ่านทุกไทล์ในแผนที่
2. หาไทล์ถนน (ค่า 0) ที่อยู่ซ้ายสุด
3. return Point(column, row) ของจุดเริ่มต้น

### findHousePosition(int[][] mapGrid)
```java
private Point findHousePosition(int[][] mapGrid)
```
**วัตถุประสงค์**: หาไทล์ถนนขวาสุดในแถวเดียวกับจุดเริ่มต้น
**การทำงาน**:
1. หาแถวของจุดเริ่มต้น
2. หาไทล์ถนนขวาสุดในแถวนั้น
3. หากไม่พบ ใช้ fallback: หาไทล์ถนนที่ใกล้บ้านที่สุด

### createPathUsingBFS(int[][] mapGrid, int tileSize, Point start, Point goal)
```java
private void createPathUsingBFS(int[][] mapGrid, int tileSize, Point start, Point goal)
```
**วัตถุประสงค์**: สร้างเส้นทางด้วยอัลกอริทึม BFS
**การทำงาน**:
1. **เริ่มต้น BFS**:
   - สร้าง queue, visited array, parent array
   - เพิ่มจุดเริ่มต้นใน queue
2. **วนลูป BFS**:
   - ดึงจุดจาก queue
   - ตรวจสอบว่าถึงเป้าหมายหรือยัง
   - ตรวจสอบ 4 ทิศทาง (บน, ล่าง, ซ้าย, ขวา)
   - เพิ่มจุดใหม่ที่เป็นไทล์ถนนใน queue
3. **สร้างเส้นทาง**: เรียก `reconstructPath()` เมื่อพบเป้าหมาย
4. **Fallback**: เรียก `createFallbackPath()` หากไม่พบเส้นทาง

### reconstructPath(Point[][] parent, Point start, Point goal, int tileSize)
```java
private void reconstructPath(Point[][] parent, Point start, Point goal, int tileSize)
```
**วัตถุประสงค์**: สร้างเส้นทางจาก parent array ของ BFS
**การทำงาน**:
1. เริ่มจากเป้าหมายย้อนกลับไปจุดเริ่มต้น
2. แปลงตำแหน่งกริดเป็นพิกเซล (ใช้จุดกึ่งกลางไทล์)
3. เพิ่มจุดเข้าไปใน movementPath

### initializeStartingPosition()
```java
private void initializeStartingPosition()
```
**วัตถุประสงค์**: ตั้งตำแหน่งเริ่มต้นของศัตรู
**การทำงาน**: ตั้ง positionX, positionY เป็นจุดแรกในเส้นทาง

### update()
```java
public void update()
```
**วัตถุประสงค์**: อัปเดตพฤติกรรมศัตรูทุก frame
**การทำงาน**:
1. ตรวจสอบว่าตายหรือยัง หากตายเรียก `handleDeath()`
2. ตรวจสอบการชนกับ Tank - เรียก `checkForTankCollision()`
3. ตรวจสอบการชนกับ Magic - เรียก `checkForMagicCollision()`
4. ตรวจสอบการชนกับ Archer - เรียก `checkForArcherCollision()`
5. หากกำลังโจมตี เรียก `processAttack()`
6. หากไม่ได้โจมตี เรียก `moveAlongPath()`
7. ตรวจสอบการชนกับบ้าน - เรียก `checkForHouseCollision()`
8. ตรวจสอบว่าถึงจุดสิ้นสุดหรือยัง - เรียก `checkIfReachedEnd()`

### checkForTankCollision()
```java
private boolean checkForTankCollision()
```
**วัตถุประสงค์**: ตรวจสอบการชนกับ Tank
**การทำงาน**:
1. วนลูปผ่าน Tank ทั้งหมดในแผนที่
2. ตรวจสอบว่า Tank ยังมีชีวิตและชนกับศัตรูหรือไม่
3. หากชน เรียก `positionForAttack()` และ `startAttacking()`
4. return true หากเริ่มการต่อสู้

### positionForAttack(Tank tank)
```java
private void positionForAttack(Tank tank)
```
**วัตถุประสงค์**: จัดตำแหน่งศัตรูสำหรับโจมตี Tank
**การทำงาน**:
1. ดึง bounds ของ Tank
2. ตั้งตำแหน่ง X ให้อยู่ทางซ้ายของ Tank
3. เพิ่ม `attackPositionOffset` เพื่อป้องกันการซ้อนทับ

### startAttacking(Object target)
```java
private void startAttacking(Object target)
```
**วัตถุประสงค์**: เริ่มโหมดโจมตี
**การทำงาน**:
1. ตั้ง `currentAttackTarget` เป็นเป้าหมาย
2. ตั้ง `isAttacking = true`
3. ตั้ง `isPositionLocked = true` เพื่อหยุดการเคลื่อนที่

### processAttack()
```java
private void processAttack()
```
**วัตถุประสงค์**: จัดการการโจมตีและ cooldown
**การทำงาน**:
1. เพิ่ม `attackTimer`
2. เมื่อ timer ถึง cooldown เรียก `executeAttack()`
3. รีเซ็ต timer

### executeAttack()
```java
private void executeAttack()
```
**วัตถุประสงค์**: ทำการโจมตีจริง
**การทำงาน**:
1. ตรวจสอบประเภทของเป้าหมาย (Tank, Magic, Archer, House)
2. ตรวจสอบว่าเป้าหมายยังมีชีวิตหรือไม่
3. เรียก `damage()` หรือ `takeDamage()` ของเป้าหมาย
4. หากเป้าหมายตาย เรียก `stopAttacking()`

### moveAlongPath()
```java
private void moveAlongPath()
```
**วัตถุประสงค์**: เคลื่อนที่ตามเส้นทางที่กำหนด
**การทำงาน**:
1. ตรวจสอบว่ายังมีจุดในเส้นทางหรือไม่
2. ดึงจุดปลายทางปัจจุบัน
3. เรียก `moveTowardsPoint()` เพื่อเคลื่อนที่
4. หากถึงจุดแล้ว เพิ่ม `currentPathIndex`

### moveTowardsPoint(Point targetPoint)
```java
private boolean moveTowardsPoint(Point targetPoint)
```
**วัตถุประสงค์**: เคลื่อนที่ไปยังจุดเป้าหมายด้วยความเร็วคงที่
**การทำงาน**:
1. คำนวณ vector ระยะทาง (deltaX, deltaY)
2. คำนวณระยะทางรวม
3. หากระยะทางมากกว่าความเร็ว:
   - เคลื่อนที่ด้วยความเร็วคงที่ตาม vector
   - return false
4. หากระยะทางน้อยกว่าความเร็ว:
   - ตั้งตำแหน่งเป็นจุดเป้าหมาย
   - return true

### checkForHouseCollision()
```java
private void checkForHouseCollision()
```
**วัตถุประสงค์**: ตรวจสอบการชนกับบ้าน
**การทำงาน**:
1. ตรวจสอบว่าตำแหน่งไม่ถูกล็อคและชนกับบ้าน
2. จัดตำแหน่งให้อยู่ทางซ้ายของบ้าน
3. ล็อคตำแหน่งและเริ่มโจมตี

### stopAttacking()
```java
private void stopAttacking()
```
**วัตถุประสงค์**: หยุดการโจมตีและกลับไปเคลื่อนที่
**การทำงาน**:
1. ตั้ง `isAttacking = false`
2. ตั้ง `isPositionLocked = false`
3. ล้าง `currentAttackTarget`

### takeDamage(int damage)
```java
public void takeDamage(int damage)
```
**วัตถุประสงค์**: รับความเสียหาย
**การทำงาน**:
1. ลดเลือดตามจำนวนความเสียหาย
2. หากเลือดหมด ตั้ง `isDead = true`

### kill()
```java
public void kill()
```
**วัตถุประสงค์**: ฆ่าศัตรูทันที (ใช้โดยระบบภายนอก)
**การทำงาน**: ตั้ง `isDead = true`

### draw(Graphics graphics)
```java
public void draw(Graphics graphics)
```
**วัตถุประสงค์**: วาดศัตรูบนหน้าจอ
**การทำงาน**:
1. ตรวจสอบว่ายังมีชีวิตหรือไม่
2. วาดรูปศัตรูที่ตำแหน่งปัจจุบัน + offset
3. เรียก `drawHealthBar()` เพื่อวาดแถบเลือด

### drawHealthBar(Graphics graphics)
```java
private void drawHealthBar(Graphics graphics)
```
**วัตถุประสงค์**: วาดแถบเลือดเหนือศัตรู
**การทำงาน**:
1. ตรวจสอบว่าเลือดไม่เต็มหรือไม่
2. คำนวณตำแหน่งเหนือศัตรู
3. วาดพื้นหลังสีแดง
4. วาดแถบเลือดสีเขียวตามเปอร์เซ็นต์
5. วาดขอบสีดำ

### getBounds()
```java
public Rectangle getBounds()
```
**วัตถุประสงค์**: ให้ bounds สำหรับการตรวจสอบการชน
**การทำงาน**: return Rectangle ที่ตำแหน่งปัจจุบัน + offset

### resetEnemyCount()
```java
public static void resetEnemyCount()
```
**วัตถุประสงค์**: รีเซ็ตตัวนับศัตรูสำหรับ wave ใหม่
**การทำงาน**: ตั้ง `totalEnemyCount = 0`
**การใช้งาน**: เรียกโดย WaveManager เมื่อเริ่ม wave ใหม่

## ระบบ AI และพฤติกรรม

### การเคลื่อนที่
1. **Pathfinding**: ใช้ BFS หาเส้นทางสั้นสุดตามไทล์ถนน
2. **Smooth Movement**: เคลื่อนที่ด้วยความเร็วคงที่ระหว่างจุด
3. **Collision Avoidance**: ใช้ offset เพื่อป้องกันการซ้อนทับ

### การต่อสู้
1. **Target Priority**: Tank → Magic → Archer → House
2. **Attack Positioning**: จัดตำแหน่งทางซ้ายของเป้าหมาย
3. **Attack Cooldown**: โจมตีทุก 1 วินาที (60 frames)
4. **Position Locking**: หยุดเคลื่อนที่ขณะโจมตี

### การจัดการสถานะ
1. **Health System**: เลือด 50 จุด
2. **Death Handling**: ตายเมื่อเลือดหมดหรือถึงจุดสิ้นสุด
3. **Visual Feedback**: แถบเลือดและ offset สำหรับการแสดงผล

## ความสัมพันธ์กับคลาสอื่น
- **Map.java**: ใช้สำหรับ pathfinding และดึงรายการหน่วยป้องกัน
- **Tank/Magic/Archer.java**: เป้าหมายการโจมตี
- **House.java**: เป้าหมายสุดท้าย
- **WaveManager.java**: สร้างและจัดการศัตรู
- **Constants.java**: ใช้ค่าคงที่สำหรับสถิติและ path

## จุดเด่นของการออกแบบ
1. **Smart Pathfinding**: ใช้ BFS เพื่อหาเส้นทางที่ดีที่สุด
2. **Flexible Combat**: รองรับการโจมตีหลายประเภทเป้าหมาย
3. **Position Management**: ป้องกันการซ้อนทับด้วย offset system
4. **State Management**: จัดการสถานะการเคลื่อนที่และการต่อสู้อย่างชัดเจน
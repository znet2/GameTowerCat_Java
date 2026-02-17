# Enemy Class Documentation

## ภาพรวม
คลาส `Enemy` เป็นตัวแทนของศัตรูในเกม Tower Defense ที่เคลื่อนที่ตามเส้นทางที่กำหนดและโจมตีหน่วยป้องกัน (Tank, Magic, Archer) และบ้าน (House) ศัตรูจะใช้ BFS pathfinding เพื่อหาเส้นทางที่สั้นที่สุดตามถนน และจะหยุดโจมตีเมื่อพบหน่วยป้องกันหรือบ้าน

## Package
`com.towerdefense.entities.enemies`

## Imports
```java
import com.towerdefense.world.Map;
import com.towerdefense.entities.defensive.House;
import com.towerdefense.entities.defensive.Tank;
import com.towerdefense.entities.defensive.Magic;
import com.towerdefense.entities.defensive.Archer;
import com.towerdefense.managers.CoinManager;
import com.towerdefense.utils.Constants;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;
```

## คุณสมบัติหลัก (Fields)

### Visual Properties
- `enemyImage` (Image, final): รูปภาพของศัตรู

### Position and Movement
- `positionX` (double): ตำแหน่ง X ปัจจุบันของศัตรู
- `positionY` (double): ตำแหน่ง Y ปัจจุบันของศัตรู
- `movementPath` (ArrayList<Point>, final): รายการจุดที่ศัตรูจะเดินผ่าน
- `currentPathIndex` (int): ดัชนีของจุดปัจจุบันในเส้นทาง

### Game References
- `gameMap` (Map, final): อ้างอิงถึงแผนที่เกม
- `targetHouse` (House, final): อ้างอิงถึงบ้านที่เป็นเป้าหมาย
- `coinManager` (CoinManager, final): อ้างอิงถึงระบบเหรียญสำหรับให้รางวัลเมื่อฆ่าศัตรู

### Combat State
- `isAttacking` (boolean): สถานะว่าศัตรูกำลังโจมตีอยู่หรือไม่
- `attackTimer` (int): ตัวนับเวลาสำหรับการโจมตี
- `currentAttackTarget` (Object): เป้าหมายที่กำลังโจมตีอยู่

### Enemy State
- `isDead` (boolean): สถานะว่าศัตรูตายแล้วหรือไม่
- `currentHealth` (int): พลังชีวิตปัจจุบัน (เริ่มต้นที่ ENEMY_INITIAL_HEALTH)

### Position Management
- `totalEnemyCount` (static int): จำนวนศัตรูทั้งหมดที่ถูกสร้าง (ใช้สำหรับ offset)
- `attackPositionOffset` (int, final): ระยะ offset สำหรับตำแหน่งโจมตีเพื่อป้องกันศัตรูซ้อนกัน
- `isPositionLocked` (boolean): สถานะว่าตำแหน่งถูกล็อคหรือไม่ (ระหว่างโจมตี)

## Constructor

### `public Enemy(Map gameMap, CoinManager coinManager)`
สร้างศัตรูใหม่และตั้งค่าเส้นทางการเคลื่อนที่

**Parameters:**
- `gameMap` (Map): อ้างอิงถึงแผนที่เกมสำหรับ pathfinding และ collision detection
- `coinManager` (CoinManager): อ้างอิงถึงระบบเหรียญสำหรับให้รางวัลเมื่อฆ่าศัตรู

**การทำงาน:**
1. เก็บ reference ของ gameMap และ targetHouse
2. เก็บ reference ของ coinManager สำหรับให้รางวัลเหรียญ
3. โหลดรูปภาพศัตรูจาก Constants.Paths.ENEMY_IMAGE
4. คำนวณ attackPositionOffset ที่ไม่ซ้ำกันสำหรับศัตรูแต่ละตัว
5. เพิ่ม totalEnemyCount
6. สร้างเส้นทางการเคลื่อนที่ด้วย buildMovementPath()
7. ตั้งค่าตำแหน่งเริ่มต้นด้วย initializeStartingPosition()

## Methods - Pathfinding

### `private void buildMovementPath()`
สร้างเส้นทางการเคลื่อนที่จากช่องถนนซ้ายสุดไปยังบ้าน

**การทำงาน:**
1. ดึง mapGrid และ tileSize จาก gameMap
2. หาตำแหน่งเริ่มต้นบนถนนด้วย findRoadStartPosition()
3. หาตำแหน่งบ้านด้วย findHousePosition()
4. สร้างเส้นทางด้วย BFS algorithm ผ่าน createPathUsingBFS()

### `private Point findRoadStartPosition(int[][] mapGrid)`
หาช่องถนนซ้ายสุดในแผนที่เป็นจุดเริ่มต้น

**Parameters:**
- `mapGrid` (int[][]): อาร์เรย์ 2 มิติที่แทนช่องแผนที่

**Returns:**
- Point: จุดเริ่มต้น (column, row) ของถนนซ้ายสุด

**การทำงาน:**
1. วนลูปผ่านทุกช่องในแผนที่
2. หาช่องถนน (ค่า 0) ที่อยู่ซ้ายสุด
3. คืนค่าตำแหน่ง (column, row)

### `private Point findHousePosition(int[][] mapGrid)`
หาตำแหน่งบ้านบนแผนที่ (ช่องถนนขวาสุดในแถวเดียวกับจุดเริ่มต้น)

**Parameters:**
- `mapGrid` (int[][]): อาร์เรย์ 2 มิติที่แทนช่องแผนที่

**Returns:**
- Point: ตำแหน่งบ้าน (column, row)

**การทำงาน:**
1. หาแถวที่มีจุดเริ่มต้น
2. หาช่องถนนขวาสุดในแถวเดียวกัน
3. ถ้าไม่เจอ ใช้ fallback: หาช่องถนนที่ใกล้บ้านที่สุด
4. คืนค่าตำแหน่งที่เจอ

### `private void createPathUsingBFS(int[][] mapGrid, int tileSize, Point start, Point goal)`
สร้างเส้นทางโดยใช้ BFS algorithm เพื่อเดินตามช่องถนน

**Parameters:**
- `mapGrid` (int[][]): อาร์เรย์ 2 มิติที่แทนช่องแผนที่
- `tileSize` (int): ขนาดของแต่ละช่องเป็นพิกเซล
- `start` (Point): ตำแหน่งเริ่มต้น
- `goal` (Point): ตำแหน่งเป้าหมาย (ช่องถนนที่ใกล้บ้านที่สุด)

**การทำงาน:**
1. สร้าง queue, visited array, และ parent array
2. เพิ่ม start point ลง queue
3. วนลูป BFS:
   - ดึง point ปัจจุบันจาก queue
   - ถ้าถึง goal แล้ว ให้ reconstruct path และจบ
   - ตรวจสอบ 4 ทิศทาง (บน, ล่าง, ซ้าย, ขวา)
   - เพิ่ม neighbor ที่เป็นช่องถนนลง queue
4. ถ้าไม่เจอเส้นทาง ใช้ createFallbackPath()

### `private void reconstructPath(Point[][] parent, Point start, Point goal, int tileSize)`
สร้างเส้นทางจาก parent array ของ BFS

**Parameters:**
- `parent` (Point[][]): อาร์เรย์เก็บ parent nodes
- `start` (Point): ตำแหน่งเริ่มต้น
- `goal` (Point): ตำแหน่งเป้าหมาย
- `tileSize` (int): ขนาดของแต่ละช่องเป็นพิกเซล

**การทำงาน:**
1. สร้าง ArrayList เก็บเส้นทาง
2. ย้อนกลับจาก goal ไป start ผ่าน parent array
3. แปลงตำแหน่ง grid เป็นพิกเซล (ใช้จุดกึ่งกลางของช่อง)
4. เพิ่มจุดทั้งหมดลง movementPath

### `private void createFallbackPath(int[][] mapGrid, int tileSize, Point start)`
สร้างเส้นทาง fallback ถ้า BFS ล้มเหลว

**Parameters:**
- `mapGrid` (int[][]): อาร์เรย์ 2 มิติที่แทนช่องแผนที่
- `tileSize` (int): ขนาดของแต่ละช่องเป็นพิกเซล
- `start` (Point): ตำแหน่งเริ่มต้น

**การทำงาน:**
1. ใช้แถวเดียวกับจุดเริ่มต้น
2. สร้างเส้นทางตรงจากซ้ายไปขวาตามช่องถนน
3. เพิ่มจุดทั้งหมดลง movementPath

### `private void initializeStartingPosition()`
ตั้งค่าตำแหน่งเริ่มต้นของศัตรูที่จุดแรกในเส้นทาง

**การทำงาน:**
1. ดึงจุดแรกจาก movementPath
2. ตั้งค่า positionX และ positionY

## Methods - Update & Behavior

### `public void update()`
เมธอดหลักที่จัดการพฤติกรรมของศัตรูในแต่ละเฟรม

**การทำงาน:**
1. ถ้าศัตรูตายแล้ว ให้ return
2. ตรวจสอบการชนกับหน่วยป้องกัน (Tank, Magic, Archer)
3. ถ้ากำลังโจมตี ให้ประมวลผลการโจมตี
4. ถ้าไม่โจมตี ให้เคลื่อนที่ตามเส้นทาง
5. ตรวจสอบการชนกับบ้าน
6. ตรวจสอบว่าถึงจุดสิ้นสุดเส้นทางหรือไม่

### `private void checkIfReachedEnd()`
ตรวจสอบว่าศัตรูถึงจุดสิ้นสุดเส้นทางหรือไม่

**การทำงาน:**
- ถ้า currentPathIndex >= movementPath.size() ให้ตั้ง isDead = true

## Methods - Collision Detection

### `private boolean checkForDefensiveCollision()`
ตรวจสอบการชนกับหน่วยป้องกันทั้งหมด (Tank, Magic, Archer)

**Returns:**
- boolean: true ถ้าเกิดการชนและเริ่มโจมตี, false ถ้าไม่มีการชน

**การทำงาน:**
1. ตรวจสอบการชนกับ Tank ทุกตัว
2. ตรวจสอบการชนกับ Magic ทุกตัว
3. ตรวจสอบการชนกับ Archer ทุกตัว
4. ถ้าเจอการชน ให้วางตำแหน่งและเริ่มโจมตี
5. คืนค่า true ถ้าเจอการชน, false ถ้าไม่เจอ

### `private void positionForAttack(Object target)`
วางตำแหน่งศัตรูสำหรับโจมตีหน่วยป้องกัน

**Parameters:**
- `target` (Object): หน่วยป้องกันที่ถูกโจมตี (Tank, Magic, หรือ Archer)

**การทำงาน:**
1. ตรวจสอบประเภทของ target
2. ดึง bounds ของ target
3. วางศัตรูทางซ้ายของ target พร้อม offset เพื่อป้องกันการซ้อนกัน

### `private void checkForHouseCollision()`
ตรวจสอบการชนกับบ้านและเริ่มโจมตี

**การทำงาน:**
1. ตรวจสอบว่าตำแหน่งถูกล็อคหรือไม่
2. ตรวจสอบการชนกับบ้าน
3. ถ้าชน ให้วางตำแหน่งทางซ้ายของบ้าน
4. ล็อคตำแหน่งและเริ่มโจมตี

## Methods - Combat

### `private void startAttacking(Object target)`
เริ่มโหมดโจมตีเป้าหมาย

**Parameters:**
- `target` (Object): วัตถุที่ถูกโจมตี (Tank, Magic, Archer, หรือ House)

**การทำงาน:**
1. ตั้งค่า currentAttackTarget
2. ตั้งค่า isAttacking = true
3. ตั้งค่า isPositionLocked = true

### `private void processAttack()`
ประมวลผลเวลาและการโจมตี

**การทำงาน:**
1. เพิ่ม attackTimer
2. ถ้า attackTimer >= ENEMY_ATTACK_COOLDOWN_FRAMES ให้โจมตีและรีเซ็ต timer

### `private void executeAttack()`
ดำเนินการโจมตีเป้าหมายปัจจุบัน

**การทำงาน:**
1. ตรวจสอบประเภทของเป้าหมาย
2. ถ้าเป้าหมายเป็นหน่วยป้องกัน (Tank, Magic, Archer):
   - ตรวจสอบว่าตายแล้วหรือไม่
   - ถ้ายังไม่ตาย ให้สร้างความเสียหาย ENEMY_ATTACK_DAMAGE
   - ถ้าตายแล้ว ให้หยุดโจมตี
3. ถ้าเป้าหมายเป็นบ้าน ให้สร้างความเสียหาย

### `private void stopAttacking()`
หยุดโจมตีและรีเซ็ตสถานะการต่อสู้

**การทำงาน:**
1. ตั้งค่า isAttacking = false
2. ตั้งค่า isPositionLocked = false
3. ตั้งค่า currentAttackTarget = null

### `public void takeDamage(int damage)`
รับความเสียหายและลดพลังชีวิต

**Parameters:**
- `damage` (int): จำนวนความเสียหายที่จะใช้

**การทำงาน:**
1. ลด currentHealth ด้วยค่า damage
2. ถ้า currentHealth <= 0:
   - ตั้งค่า currentHealth = 0
   - ตั้งค่า isDead = true
   - เรียก coinManager.awardCoinsForEnemyKill() เพื่อให้รางวัลเหรียญ

## Methods - Movement

### `private void moveAlongPath()`
เคลื่อนที่ศัตรูตามเส้นทางที่กำหนด

**การทำงาน:**
1. ตรวจสอบว่ายังมีจุดในเส้นทางหรือไม่
2. ดึงจุดเป้าหมายปัจจุบัน
3. เคลื่อนที่ไปยังจุดนั้น
4. ถ้าถึงจุดแล้ว ให้เพิ่ม currentPathIndex

### `private boolean moveTowardsPoint(Point targetPoint)`
เคลื่อนที่ศัตรูไปยังจุดเฉพาะด้วยความเร็วคงที่

**Parameters:**
- `targetPoint` (Point): จุดปลายทางที่จะเคลื่อนที่ไป

**Returns:**
- boolean: true ถ้าถึงจุดเป้าหมายแล้ว, false ถ้ายังเคลื่อนที่อยู่

**การทำงาน:**
1. คำนวณ deltaX และ deltaY
2. คำนวณระยะทางถึงเป้าหมาย
3. ถ้าระยะทาง > ENEMY_SPEED:
   - เคลื่อนที่ไปยังเป้าหมายด้วยความเร็วคงที่
   - คืนค่า false
4. ถ้าระยะทาง <= ENEMY_SPEED:
   - ตั้งตำแหน่งเป็นจุดเป้าหมาย
   - คืนค่า true

## Methods - State

### `public int getCurrentHealth()`
ดึงพลังชีวิตปัจจุบันของศัตรู

**Returns:**
- int: ค่าพลังชีวิตปัจจุบัน

### `public int getMaxHealth()`
ดึงพลังชีวิตสูงสุดของศัตรู

**Returns:**
- int: ค่าพลังชีวิตสูงสุด (ENEMY_INITIAL_HEALTH)

### `public void kill()`
ทำเครื่องหมายศัตรูว่าตาย

**การทำงาน:**
- ตั้งค่า isDead = true

### `public boolean isDead()`
ตรวจสอบว่าศัตรูตายหรือไม่

**Returns:**
- boolean: true ถ้าศัตรูตาย, false ถ้ายังมีชีวิต

## Methods - Rendering

### `public void draw(Graphics graphics)`
วาดศัตรูบนหน้าจอ

**Parameters:**
- `graphics` (Graphics): Graphics context สำหรับการวาด

**การทำงาน:**
1. ถ้าศัตรูยังไม่ตาย:
   - วาดรูปภาพศัตรูที่ตำแหน่งปัจจุบัน (พร้อม offset)
   - วาดแถบพลังชีวิต

### `private void drawHealthBar(Graphics graphics)`
วาดแถบพลังชีวิตเหนือศัตรู

**Parameters:**
- `graphics` (Graphics): Graphics context สำหรับการวาด

**การทำงาน:**
1. ถ้าพลังชีวิตไม่เต็ม:
   - วาดพื้นหลังสีแดง
   - วาดแถบสีเขียวตามเปอร์เซ็นต์พลังชีวิต
   - วาดกรอบสีดำ

### `public Rectangle getBounds()`
ดึง collision bounds ของศัตรู

**Returns:**
- Rectangle: สี่เหลี่ยมที่แทน bounds ของศัตรู (พร้อม offset)

**การทำงาน:**
- สร้าง Rectangle จากตำแหน่งปัจจุบัน (พร้อม offset) และขนาดศัตรู

## Methods - Static

### `public static void resetEnemyCount()`
รีเซ็ตจำนวนศัตรูสำหรับเวฟใหม่

**การทำงาน:**
- ตั้งค่า totalEnemyCount = 0

**หมายเหตุ:** เรียกเมธอดนี้ที่จุดเริ่มต้นของแต่ละเวฟเพื่อรีเซ็ตตำแหน่งโจมตี

## ระบบเหรียญ (Coin System)

ศัตรูจะให้รางวัลเหรียญเมื่อถูกฆ่า:
- เมื่อ `takeDamage()` ทำให้พลังชีวิตเป็น 0 จะเรียก `coinManager.awardCoinsForEnemyKill()`
- จำนวนเหรียญที่ได้กำหนดโดย `Constants.Economy.COINS_PER_ENEMY_KILL` (50 เหรียญ)
- ศัตรูที่เดินถึงจุดสิ้นสุดเส้นทางจะไม่ให้รางวัลเหรียญ

## Constants ที่ใช้

จาก `Constants.Entities`:
- `ENEMY_INITIAL_HEALTH`: พลังชีวิตเริ่มต้นของศัตรู
- `ENEMY_SIZE`: ขนาดของศัตรู
- `ENEMY_X_OFFSET`: ระยะ offset แกน X สำหรับการวาด
- `ENEMY_Y_OFFSET`: ระยะ offset แกน Y สำหรับการวาด
- `ENEMY_SPEED`: ความเร็วการเคลื่อนที่
- `ENEMY_ATTACK_DAMAGE`: ความเสียหายต่อการโจมตี
- `ENEMY_ATTACK_COOLDOWN_FRAMES`: จำนวนเฟรมระหว่างการโจมตี
- `ENEMY_POSITION_OFFSET_MULTIPLIER`: ตัวคูณสำหรับ offset ตำแหน่งโจมตี

จาก `Constants.Paths`:
- `ENEMY_IMAGE`: เส้นทางไปยังรูปภาพศัตรู

จาก `Constants.Map`:
- `HOUSE_COLUMN`: คอลัมน์ของบ้าน
- `HOUSE_ROW`: แถวของบ้าน

จาก `Constants.Economy`:
- `COINS_PER_ENEMY_KILL`: จำนวนเหรียญที่ได้เมื่อฆ่าศัตรู (50 เหรียญ)

## การใช้งาน

```java
// สร้างศัตรูใหม่
Enemy enemy = new Enemy(gameMap, coinManager);

// อัปเดตศัตรูในแต่ละเฟรม
enemy.update();

// วาดศัตรู
enemy.draw(graphics);

// ให้ความเสียหายศัตรู (จะให้รางวัลเหรียญถ้าตาย)
enemy.takeDamage(10);

// ตรวจสอบว่าศัตรูตายหรือไม่
if (enemy.isDead()) {
    // ลบศัตรูออกจากเกม
}

// รีเซ็ตจำนวนศัตรูที่จุดเริ่มต้นของเวฟใหม่
Enemy.resetEnemyCount();
```

## หมายเหตุ

1. ศัตรูใช้ BFS pathfinding เพื่อหาเส้นทางที่สั้นที่สุดตามช่องถนน
2. ศัตรูจะหยุดและโจมตีเมื่อพบหน่วยป้องกัน (Tank, Magic, Archer) หรือบ้าน
3. ศัตรูแต่ละตัวมี attackPositionOffset ที่ไม่ซ้ำกันเพื่อป้องกันการซ้อนกัน
4. ศัตรูจะให้รางวัลเหรียญเมื่อถูกฆ่า แต่ไม่ให้รางวัลถ้าเดินถึงจุดสิ้นสุด
5. ระบบ collision detection ตรวจสอบทั้ง Tank, Magic, Archer และ House
6. ศัตรูจะโจมตีเป้าหมายจนกว่าเป้าหมายจะตาย จากนั้นจะเดินต่อ

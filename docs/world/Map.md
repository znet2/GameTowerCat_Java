# Map.java - ระบบแผนที่และการจัดการหน่วยป้องกัน

## ภาพรวม
Map เป็นคลาสที่จัดการแผนที่เกม การวาดไทล์ การวางหน่วยป้องกัน และการตรวจสอบการชน รวมถึงการอัปเดตหน่วยป้องกันทั้งหมด

## คลาสและการสืบทอด
```java
public class Map extends JPanel
```
- สืบทอดจาก `JPanel` สำหรับการแสดงผล

## ตัวแปรสำคัญ

### รายการหน่วยและวัตถุ
- `ArrayList<GameObject> mapObjects` - วัตถุบนแผนที่ (บ้าน)
- `ArrayList<Tank> defensiveTanks` - รายการ Tank ทั้งหมด
- `ArrayList<Magic> magicTowers` - รายการ Magic tower ทั้งหมด
- `ArrayList<Archer> archerTowers` - รายการ Archer tower ทั้งหมด
- `ArrayList<Assassin> assassins` - รายการ Assassin ทั้งหมด

### รูปภาพ
- `Image houseImage, tankImage, magicImage, archerImage, assassinImage` - รูปหน่วยต่างๆ
- `Image grassTile, roadTile, waterTile, waterUpTile, waterDownTile, waterLeftTile, waterRightTile, treeTile` - รูปไทล์ต่างๆ

### ข้อมูลแผนที่
- `int[][] mapGrid` - อาร์เรย์ 2 มิติขนาด 30x60 เก็บประเภทไทล์
  - 0 = ถนน, 1 = หญ้า, 2 = น้ำ, 3 = น้ำ_บน, 4 = น้ำ_ล่าง, 5 = น้ำ_ซ้าย, 6 = น้ำ_ขวา

## Methods อย่างละเอียด

### Constructor
```java
public Map()
```
**วัตถุประสงค์**: สร้างแผนที่และเริ่มต้นระบบ
**การทำงาน**:
1. เรียก `loadImages()` - โหลดรูปภาพทั้งหมด
2. เรียก `initializeMapObjects()` - สร้างวัตถุบนแผนที่ (บ้าน)
3. เรียก `configurePanel()` - ตั้งค่า panel

### loadImages()
```java
private void loadImages()
```
**วัตถุประสงค์**: โหลดรูปภาพทั้งหมดที่ใช้ในแผนที่
**การทำงาน**:
1. โหลดรูปไทล์ต่างๆ (หญ้า, ถนน, น้ำ, ต้นไม้)
2. โหลดรูปหน่วยป้องกัน (บ้าน, Tank, Magic, Archer, Assassin)
3. จัดการ IOException หากโหลดไม่สำเร็จ

### initializeMapObjects()
```java
private void initializeMapObjects()
```
**วัตถุประสงค์**: สร้างวัตถุคงที่บนแผนที่
**การทำงาน**:
- สร้าง House object ที่ตำแหน่งที่กำหนดใน Constants

### configurePanel()
```java
private void configurePanel()
```
**วัตถุประสงค์**: ตั้งค่าขนาด panel
**การทำงาน**:
- คำนวณขนาด panel จากขนาดแผนที่ (60 x 30 ไทล์ x 32 พิกเซล)

### paintComponent(Graphics graphics)
```java
@Override
protected void paintComponent(Graphics graphics)
```
**วัตถุประสงค์**: วาดแผนที่และหน่วยทั้งหมด
**การทำงาน**:
1. เรียก `super.paintComponent(graphics)` เพื่อล้างหน้าจอ
2. เรียก `renderMapTiles(graphics)` - วาดไทล์ทั้งหมด
3. เรียก `renderMapObjects(graphics)` - วาดวัตถุ (บ้าน)
4. เรียก `renderTanks(graphics)` - วาด Tank ทั้งหมด
5. เรียก `renderMagicTowers(graphics)` - วาด Magic tower ทั้งหมด
6. เรียก `renderArcherTowers(graphics)` - วาด Archer tower ทั้งหมด
7. เรียก `renderAssassins(graphics)` - วาด Assassin ทั้งหมด

### renderMapTiles(Graphics graphics)
```java
private void renderMapTiles(Graphics graphics)
```
**วัตถุประสงค์**: วาดไทล์ทั้งหมดบนแผนที่
**การทำงาน**:
1. วนลูปผ่านทุกตำแหน่งใน mapGrid
2. เรียก `getTileImage(mapGrid[row][col])` เพื่อเลือกรูปไทล์
3. วาดไทล์ที่ตำแหน่งที่ถูกต้อง (col * TILE_SIZE, row * TILE_SIZE)

### getTileImage(int tileType)
```java
private Image getTileImage(int tileType)
```
**วัตถุประสงค์**: เลือกรูปไทล์ตามประเภท
**พารามิเตอร์**: `tileType` - ประเภทไทล์ (0-7)
**การทำงาน**:
- ใช้ if-else เพื่อเลือกรูปไทล์ที่เหมาะสม
- return รูปหญ้าเป็นค่าเริ่มต้นหากไม่ตรงกับประเภทใด

### renderTanks(Graphics graphics)
```java
private void renderTanks(Graphics graphics)
```
**วัตถุประสงค์**: วาด Tank ทั้งหมด
**การทำงาน**: วนลูปเรียก `tank.draw(graphics)` สำหรับ Tank ทุกตัว

### getRawMap()
```java
public int[][] getRawMap()
```
**วัตถุประสงค์**: ให้คลาสอื่นเข้าถึงข้อมูลแผนที่
**การทำงาน**: return อ้างอิงของ mapGrid
**การใช้งาน**: ใช้สำหรับ pathfinding และการตรวจสอบประเภทไทล์

### getTileSize()
```java
public int getTileSize()
```
**วัตถุประสงค์**: ให้ขนาดไทล์สำหรับการคำนวณ
**การทำงาน**: return Constants.Map.TILE_SIZE (32)

### getMapWidth() / getMapHeight()
```java
public int getMapWidth()
public int getMapHeight()
```
**วัตถุประสงค์**: คำนวณขนาดแผนที่เป็นพิกเซล
**การทำงาน**:
- Width: จำนวนคอลัมน์ × ขนาดไทล์
- Height: จำนวนแถว × ขนาดไทล์

### getHouse()
```java
public House getHouse()
```
**วัตถุประสงค์**: ให้เข้าถึงวัตถุบ้าน
**การทำงาน**: return บ้านจาก mapObjects[0]

### getTileAtPixel(double pixelX, double pixelY)
```java
public int getTileAtPixel(double pixelX, double pixelY)
```
**วัตถุประสงค์**: แปลงตำแหน่งพิกเซลเป็นประเภทไทล์
**พารามิเตอร์**: ตำแหน่ง X, Y เป็นพิกเซล
**การทำงาน**:
1. แปลงพิกเซลเป็นตำแหน่งกริด
2. ตรวจสอบขอบเขต
3. return ประเภทไทล์ หรือ -1 หากอยู่นอกขอบเขต

### placeTank(int gridColumn, int gridRow)
```java
public void placeTank(int gridColumn, int gridRow)
```
**วัตถุประสงค์**: วาง Tank ใหม่บนแผนที่
**พารามิเตอร์**: ตำแหน่งกริดที่ต้องการวาง
**การทำงาน**:
1. สร้าง Tank object ใหม่
2. เพิ่มเข้าไปใน defensiveTanks list
3. เรียก `repaint()` เพื่ออัปเดตการแสดงผล

### placeMagic(int gridColumn, int gridRow, ArrayList<BaseEnemy> enemies)
```java
public void placeMagic(int gridColumn, int gridRow, ArrayList<BaseEnemy> enemies)
```
**วัตถุประสงค์**: วาง Magic tower ใหม่บนแผนที่
**พารามิเตอร์**: 
- ตำแหน่งกริด
- รายการศัตรูสำหรับการเล็ง (รวมทั้ง Enemy และ EnemyBoss)
**การทำงาน**:
1. สร้าง Magic object ใหม่พร้อมอ้างอิงศัตรู
2. เพิ่มเข้าไปใน magicTowers list
3. เรียก `repaint()`

### placeArcher(int gridColumn, int gridRow, ArrayList<BaseEnemy> enemies)
```java
public void placeArcher(int gridColumn, int gridRow, ArrayList<BaseEnemy> enemies)
```
**วัตถุประสงค์**: วาง Archer tower ใหม่บนแผนที่
**การทำงาน**: เหมือน placeMagic แต่สร้าง Archer object

### placeAssassin(int gridColumn, int gridRow, ArrayList<BaseEnemy> enemies)
```java
public void placeAssassin(int gridColumn, int gridRow, ArrayList<BaseEnemy> enemies)
```
**วัตถุประสงค์**: วาง Assassin ใหม่บนแผนที่
**การทำงาน**: เหมือน placeMagic แต่สร้าง Assassin object

### updateMagicTowers()
```java
public void updateMagicTowers()
```
**วัตถุประสงค์**: อัปเดต Magic tower ทั้งหมด
**การทำงาน**: วนลูปเรียก `magic.update()` สำหรับทุกตัว

### updateArcherTowers()
```java
public void updateArcherTowers()
```
**วัตถุประสงค์**: อัปเดต Archer tower ทั้งหมด
**การทำงาน**: วนลูปเรียก `archer.update()` สำหรับทุกตัว

### updateAssassins()
```java
public void updateAssassins()
```
**วัตถุประสงค์**: อัปเดต Assassin ทั้งหมด
**การทำงาน**: วนลูปเรียก `assassin.update()` สำหรับทุกตัว

### getTanks() / getMagicTowers() / getArcherTowers() / getAssassins()
```java
public ArrayList<Tank> getTanks()
public ArrayList<Magic> getMagicTowers()
public ArrayList<Archer> getArcherTowers()
public ArrayList<Assassin> getAssassins()
```
**วัตถุประสงค์**: ให้คลาสอื่นเข้าถึงรายการหน่วย
**การทำงาน**: return อ้างอิงของ ArrayList ที่เกี่ยวข้อง
**การใช้งาน**: ใช้โดย Enemy สำหรับการตรวจสอบการชน

### removeDeadTanks() / removeDeadMagicTowers() / removeDeadArcherTowers()
```java
public void removeDeadTanks()
public void removeDeadMagicTowers()
public void removeDeadArcherTowers()
```
**วัตถุประสงค์**: ลบหน่วยที่ตายแล้วออกจากเกม
**การทำงาน**: ใช้ `removeIf(Unit::isDead)` เพื่อลบหน่วยที่ตาย
**ความสำคัญ**: ป้องกัน memory leak และปัญหาการแสดงผล

### hasTankAt(int gridColumn, int gridRow)
```java
public boolean hasTankAt(int gridColumn, int gridRow)
```
**วัตถุประสงค์**: ตรวจสอบว่ามี Tank ที่ตำแหน่งนี้หรือไม่
**การทำงาน**: ใช้ stream().anyMatch() เพื่อหาว่ามี Tank ที่ตำแหน่งกริดที่ระบุหรือไม่
**return**: true หากพบ Tank, false หากไม่พบ
**หมายเหตุ**: ใช้ functional programming style เพื่อความกระชับ

### hasMagicAt(int gridColumn, int gridRow)
```java
public boolean hasMagicAt(int gridColumn, int gridRow)
```
**วัตถุประสงค์**: ตรวจสอบว่ามี Magic tower ที่ตำแหน่งนี้หรือไม่
**การทำงาน**: ใช้ stream().anyMatch() เพื่อหาว่ามี Magic tower ที่ตำแหน่งกริดที่ระบุหรือไม่
**return**: true หากพบ Magic tower, false หากไม่พบ

### hasArcherAt(int gridColumn, int gridRow)
```java
public boolean hasArcherAt(int gridColumn, int gridRow)
```
**วัตถุประสงค์**: ตรวจสอบว่ามี Archer tower ที่ตำแหน่งนี้หรือไม่
**การทำงาน**: ใช้ stream().anyMatch() เพื่อหาว่ามี Archer tower ที่ตำแหน่งกริดที่ระบุหรือไม่
**return**: true หากพบ Archer tower, false หากไม่พบ

### hasAssassinAt(int gridColumn, int gridRow)
```java
public boolean hasAssassinAt(int gridColumn, int gridRow)
```
**วัตถุประสงค์**: ตรวจสอบว่ามี Assassin ที่ตำแหน่งนี้หรือไม่
**การทำงาน**: ใช้ stream().anyMatch() เพื่อหาว่ามี Assassin ที่ตำแหน่งกริดที่ระบุหรือไม่
**return**: true หากพบ Assassin, false หากไม่พบ

### draw(Graphics graphics)
```java
public void draw(Graphics graphics)
```
**วัตถุประสงค์**: Method สำรองสำหรับการวาดจากภายนอก
**การทำงาน**: เรียก `paintComponent(graphics)`

## โครงสร้างแผนที่

### ขนาดแผนที่
- **กว้าง**: 60 ไทล์ (1920 พิกเซล)
- **สูง**: 30 ไทล์ (960 พิกเซล)
- **ขนาดไทล์**: 32x32 พิกเซล

### ประเภทไทล์
- **0 (ถนน)**: ศัตรูเดินได้, วางหน่วยได้
- **1 (หญ้า)**: ศัตรูเดินไม่ได้, วางหน่วยไม่ได้
- **2 (น้ำ)**: ตกแต่ง
- **3-6 (น้ำทิศทาง)**: ตกแต่งขอบน้ำ

### เส้นทางศัตรู
- เริ่มจากไทล์ถนนซ้ายสุด
- เดินตามไทล์ถนน (0) เท่านั้น
- ใช้ BFS pathfinding เพื่อหาเส้นทาง

## ความสัมพันธ์กับคลาสอื่น
- **GamePanel.java**: เรียกใช้ Map สำหรับการแสดงผลและการวางหน่วย
- **Enemy.java**: ใช้ข้อมูลแผนที่สำหรับ pathfinding และการชน
- **Tank/Magic/Archer/Assassin.java**: หน่วยป้องกันที่ Map จัดการ
- **House.java**: วัตถุหลักที่ต้องป้องกัน
- **Constants.java**: ใช้ค่าคงที่สำหรับขนาดและ path

## จุดเด่นของการออกแบบ
1. **การจัดการหน่วยแยกประเภท**: แต่ละประเภทหน่วยมี ArrayList แยกกัน
2. **การตรวจสอบการชน**: มี method สำหรับตรวจสอบการซ้อนทับ
3. **การจัดการหน่วยความจำ**: ลบหน่วยที่ตายแล้วอย่างสม่ำเสมอ
4. **ความยืดหยุ่น**: รองรับการเพิ่มประเภทหน่วยใหม่ได้ง่าย
# GamePanel.java - หัวใจหลักของเกม Tower Defense

## ภาพรวม
GamePanel เป็นคลาสหลักที่จัดการเกมลูป การแสดงผล การรับ input และการประสานงานระหว่างระบบต่างๆ ในเกม

## คลาสและการสืบทอด
```java
public class GamePanel extends JPanel implements Runnable, MouseListener, MouseMotionListener
```
- สืบทอดจาก `JPanel` สำหรับการแสดงผล
- ใช้ `Runnable` สำหรับ game loop thread
- ใช้ `MouseListener` และ `MouseMotionListener` สำหรับการจัดการเมาส์

## ตัวแปรสำคัญ

### ระบบเกม
- `Thread gameLoopThread` - Thread สำหรับ game loop
- `Map gameMap` - แผนที่เกม
- `ArrayList<BaseEnemy> activeEnemies` - รายการศัตรูที่ยังมีชีวิต (รวม Enemy และ Boss)
- `WaveManager waveManager` - จัดการ wave ของศัตรู
- `CoinManager coinManager` - จัดการระบบเหรียญ
- `JFrame parentFrame` - อ้างอิงไปยัง JFrame หลัก

### สถานะเกม
- `boolean isGameOver` - เกมจบหรือยัง
- `boolean isWin` - ชนะหรือแพ้

### UI Elements
- `Rectangle heroBarArea` - พื้นที่แถบ UI ด้านล่าง
- `Rectangle tankIconArea` - พื้นที่ไอคอน Tank
- `Rectangle magicIconArea` - พื้นที่ไอคอน Magic
- `Rectangle archerIconArea` - พื้นที่ไอคอน Archer
- `Rectangle assassinIconArea` - พื้นที่ไอคอน Assassin

### การจัดการ Input
- `boolean isDraggingTank/Magic/Archer/Assassin` - สถานะการลากหน่วย
- `int dragPositionX, dragPositionY` - ตำแหน่งเมาส์ขณะลาก

## Methods อย่างละเอียด

### Constructor
```java
public GamePanel(JFrame frame)
```
**วัตถุประสงค์**: สร้าง GamePanel และเริ่มต้นเกม
**การทำงาน**:
1. เก็บอ้างอิงของ JFrame หลัก
2. เรียก `initializeGameComponents()` - สร้างระบบเกม
3. เรียก `setupUserInterface()` - ตั้งค่า UI
4. เรียก `configurePanel()` - ตั้งค่า panel
5. เรียก `startGameLoop()` - เริ่ม game loop

### initializeGameComponents()
```java
private void initializeGameComponents()
```
**วัตถุประสงค์**: สร้างและเริ่มต้นระบบเกมหลัก
**การทำงาน**:
1. สร้าง `Map` ใหม่
2. สร้าง `CoinManager` ใหม่
3. สร้าง `WaveManager` พร้อมส่งอ้างอิงของ map, enemies, coins
4. เริ่ม wave แรก

### setupUserInterface()
```java
private void setupUserInterface()
```
**วัตถุประสงค์**: สร้างพื้นที่ UI สำหรับการโต้ตอบ
**การทำงาน**:
1. สร้าง `heroBarArea` - แถบ UI ด้านล่าง
2. สร้าง `tankIconArea` - พื้นที่ไอคอน Tank
3. สร้าง `magicIconArea` - พื้นที่ไอคอน Magic (ถัดจาก Tank)
4. สร้าง `archerIconArea` - พื้นที่ไอคอน Archer
5. สร้าง `assassinIconArea` - พื้นที่ไอคอน Assassin
6. คำนวณตำแหน่งให้เรียงกันในแถบ UI

### configurePanel()
```java
private void configurePanel()
```
**วัตถุประสงค์**: ตั้งค่าคุณสมบัติของ panel
**การทำงาน**:
1. กำหนดขนาด panel (แผนที่ + แถบ UI)
2. ตั้งสีพื้นหลังเป็นสีดำ
3. ทำให้ panel สามารถรับ focus ได้
4. เพิ่ม MouseListener และ MouseMotionListener

### startGameLoop()
```java
private void startGameLoop()
```
**วัตถุประสงค์**: เริ่ม thread สำหรับ game loop
**การทำงาน**:
1. สร้าง Thread ใหม่ด้วย GamePanel เป็น Runnable
2. เริ่ม thread

### run()
```java
@Override
public void run()
```
**วัตถุประสงค์**: Method หลักของ Runnable interface
**การทำงาน**: เรียก `runGameLoop()` เพื่อเริ่ม game loop

### runGameLoop()
```java
private void runGameLoop()
```
**วัตถุประสงค์**: Game loop หลักที่ทำงานที่ 60 FPS
**การทำงาน**:
1. ใช้ delta time เพื่อรักษา frame rate ให้คงที่
2. วนลูปไม่สิ้นสุด
3. คำนวณเวลาที่ผ่านไป
4. เมื่อถึงเวลา (1/60 วินาที) จะเรียก `updateGame()` และ `repaint()`

### updateGame()
```java
private void updateGame()
```
**วัตถุประสงค์**: อัปเดตสถานะเกมทุก frame
**การทำงาน**:
1. ตรวจสอบว่าเกมจบหรือยัง หากจบแล้วจะหยุดอัปเดต
2. เรียก `updateWaveManager()` - อัปเดตการสร้างศัตรู
3. เรียก `updateCoinManager()` - อัปเดตระบบเหรียญ
4. เรียก `updateEnemies()` - อัปเดตศัตรูทั้งหมด
5. เรียก `updateMagicTowers()` - อัปเดต Magic towers
6. เรียก `updateArcherTowers()` - อัปเดต Archer towers
7. เรียก `updateAssassins()` - อัปเดต Assassins
8. เรียก cleanup methods สำหรับหน่วยที่ตายแล้ว
9. เรียก `checkGameOver()` - ตรวจสอบเงื่อนไขแพ้
10. เรียก `checkForNextWave()` - ตรวจสอบการเริ่ม wave ใหม่

### updateEnemies()
```java
private void updateEnemies()
```
**วัตถุประสงค์**: อัปเดตศัตรูทั้งหมดและลบศัตรูที่ตาย
**การทำงาน**:
1. วนลูปศัตรูทั้งหมดจากหลังไปหน้า (เพื่อลบได้ปลอดภัย)
2. เรียก `enemy.update()` สำหรับแต่ละตัว
3. ตรวจสอบว่าศัตรูตายหรือไม่
4. ลบศัตรูที่ตายออกจากรายการ

### checkForNextWave()
```java
private void checkForNextWave()
```
**วัตถุประสงค์**: ตรวจสอบและเริ่ม wave ใหม่
**การทำงาน**:
1. ตรวจสอบว่า wave ปัจจุบันจบหรือยัง (ศัตรูหมดแล้ว)
2. หากจบแล้ว ให้รางวัลเหรียญโบนัส (50 เหรียญ) โดยเรียก `coinManager.awardCoinsForWaveComplete()`
3. ตรวจสอบว่า `currentWave > MAX_WAVES` หรือไม่
4. หากเกิน MAX_WAVES แล้ว (เล่นครบทุก wave) ตั้งสถานะชนะ
5. หากยังไม่ครบ เริ่ม wave ใหม่

**หมายเหตุ**: ใช้ `>` แทน `>=` เพราะ WaveManager เพิ่ม wave number หลังจบแต่ละ wave ทันที

### checkGameOver()
```java
private void checkGameOver()
```
**วัตถุประสงค์**: ตรวจสอบเงื่อนไขแพ้
**การทำงาน**:
1. ตรวจสอบเลือดของบ้าน
2. หากเลือดหมด ตั้งสถานะแพ้และแสดงหน้า Game Over

### showGameOverScreen()
```java
private void showGameOverScreen()
```
**วัตถุประสงค์**: แสดงหน้า Game Over
**การทำงาน**:
1. ใช้ `SwingUtilities.invokeLater()` เพื่อทำงานใน EDT
2. ลบ GamePanel ออกจาก JFrame
3. สร้าง GameOverPanel ใหม่พร้อมสถานะชนะ/แพ้
4. เพิ่ม GameOverPanel เข้าไปใน JFrame
5. อัปเดตการแสดงผล

### paintComponent(Graphics graphics)
```java
@Override
protected void paintComponent(Graphics graphics)
```
**วัตถุประสงค์**: วาดทุกอย่างบนหน้าจอ
**การทำงาน**:
1. เรียก `super.paintComponent(graphics)` เพื่อล้างหน้าจอ
2. เรียก `renderMap(graphics)` - วาดแผนที่
3. เรียก `renderEnemies(graphics)` - วาดศัตรู
4. เรียก `renderUserInterface(graphics)` - วาด UI
5. เรียก `renderCoinDisplay(graphics)` - วาดจำนวนเหรียญ

### renderMap(Graphics graphics)
```java
private void renderMap(Graphics graphics)
```
**วัตถุประสงค์**: วาดแผนที่รวมทั้งหน่วยป้องกัน
**การทำงาน**: เรียก `gameMap.draw(graphics)` เพื่อให้ Map วาดตัวเอง

### renderEnemies(Graphics graphics)
```java
private void renderEnemies(Graphics graphics)
```
**วัตถุประสงค์**: วาดศัตรูทั้งหมด
**การทำงาน**: วนลูปเรียก `enemy.draw(graphics)` สำหรับศัตรูทุกตัว

### renderUserInterface(Graphics graphics)
```java
private void renderUserInterface(Graphics graphics)
```
**วัตถุประสงค์**: วาด UI ทั้งหมด
**การทำงาน**:
1. เรียก `drawHeroBar(graphics)` - วาดแถบ UI
2. เรียก `drawTankIcon(graphics)` - วาดไอคอน Tank
3. เรียก `drawMagicIcon(graphics)` - วาดไอคอน Magic
4. เรียก `drawArcherIcon(graphics)` - วาดไอคอน Archer
5. เรียก `drawAssassinIcon(graphics)` - วาดไอคอน Assassin
6. เรียก `drawDragPreview(graphics)` - วาดตัวอย่างขณะลาก
7. เรียก `drawHouseHealthBar(graphics)` - วาดแถบเลือดบ้าน

### drawTankIcon(Graphics graphics)
```java
private void drawTankIcon(Graphics graphics)
```
**วัตถุประสงค์**: วาดไอคอน Tank พร้อมราคา
**การทำงาน**:
1. ตรวจสอบว่าซื้อ Tank ได้หรือไม่
2. เลือกสีตามความสามารถในการซื้อ (ส้ม = ซื้อได้, เทา = ซื้อไม่ได้)
3. วาดสี่เหลี่ยมสำหรับไอคอน
4. วาดขอบสีดำ
5. วาดข้อความ "Tank" และราคา

### drawDragPreview(Graphics graphics)
```java
private void drawDragPreview(Graphics graphics)
```
**วัตถุประสงค์**: วาดตัวอย่างหน่วยขณะลาก
**การทำงาน**:
1. ตรวจสอบว่ากำลังลากหน่วยใดอยู่
2. เลือกสีตามประเภทหน่วย (โปร่งใส)
3. วาดสี่เหลี่ยมที่ตำแหน่งเมาส์

### drawHouseHealthBar(Graphics graphics)
```java
private void drawHouseHealthBar(Graphics graphics)
```
**วัตถุประสงค์**: วาดแถบเลือดของบ้าน
**การทำงาน**:
1. คำนวณตำแหน่งมุมขวาบน
2. ดึงเลือดปัจจุบันและเลือดสูงสุดของบ้าน
3. คำนวณเปอร์เซ็นต์เลือด
4. วาดพื้นหลังสีแดง
5. วาดแถบเลือดสีเขียวตามเปอร์เซ็นต์
6. วาดขอบสีดำ
7. วาดข้อความแสดงเลือด

### mousePressed(MouseEvent event)
```java
@Override
public void mousePressed(MouseEvent event)
```
**วัตถุประสงค์**: จัดการการกดเมาส์
**การทำงาน**:
1. ตรวจสอบว่าคลิกที่ไอคอน Tank หรือไม่ และซื้อได้หรือไม่
2. หากใช่ เรียก `startTankDrag(event)`
3. ทำเช่นเดียวกันสำหรับ Magic, Archer, Assassin

### startTankDrag(MouseEvent event)
```java
private void startTankDrag(MouseEvent event)
```
**วัตถุประสงค์**: เริ่มการลาก Tank
**การทำงาน**:
1. ตั้ง `isDraggingTank = true`
2. บันทึกตำแหน่งเมาส์เริ่มต้น

### mouseDragged(MouseEvent event)
```java
@Override
public void mouseDragged(MouseEvent event)
```
**วัตถุประสงค์**: จัดการการลากเมาส์
**การทำงาน**:
1. ตรวจสอบว่ากำลังลากหน่วยใดอยู่
2. เรียก `updateDragPosition(event)` เพื่ออัปเดตตำแหน่ง

### mouseReleased(MouseEvent event)
```java
@Override
public void mouseReleased(MouseEvent event)
```
**วัตถุประสงค์**: จัดการการปล่อยเมาส์
**การทำงาน**:
1. ตรวจสอบว่ากำลังลากหน่วยใด
2. เรียก method ที่เหมาะสมสำหรับการวางหน่วย
3. หยุดการลาก

### attemptTankPlacement(MouseEvent event)
```java
private void attemptTankPlacement(MouseEvent event)
```
**วัตถุประสงค์**: พยายามวาง Tank
**การทำงาน**:
1. แปลงตำแหน่งเมาส์เป็นตำแหน่งกริด
2. ตรวจสอบว่าตำแหน่งถูกต้องหรือไม่
3. ตรวจสอบว่าซื้อได้หรือไม่
4. หากผ่านทั้งหมด วาง Tank และหักเงิน

### isValidPlacementPosition(int gridColumn, int gridRow)
```java
private boolean isValidPlacementPosition(int gridColumn, int gridRow)
```
**วัตถุประสงค์**: ตรวจสอบว่าตำแหน่งวางหน่วยถูกต้องหรือไม่
**การทำงาน**:
1. ตรวจสอบว่าอยู่ในขอบเขตแผนที่หรือไม่
2. ตรวจสอบว่าเป็นช่องถนนหรือไม่
3. ตรวจสอบว่าไม่มีหน่วยอื่นอยู่แล้วหรือไม่

### getCoinManager()
```java
public CoinManager getCoinManager()
```
**วัตถุประสงค์**: ให้คลาสอื่นเข้าถึง CoinManager
**การทำงาน**: return อ้างอิงของ coinManager

## ความสัมพันธ์กับคลาสอื่น
- **Map.java**: ใช้สำหรับแผนที่และการวางหน่วย
- **Enemy.java**: จัดการรายการศัตรู
- **WaveManager.java**: จัดการ wave ของศัตรู
- **CoinManager.java**: จัดการระบบเหรียญ
- **Constants.java**: ใช้ค่าคงที่ต่างๆ
- **MathUtils.java**: ใช้สำหรับการคำนวณ
# WaveManager.java - ระบบจัดการ Wave ศัตรู

## ภาพรวม
WaveManager เป็นคลาสที่จัดการการสร้างและควบคุม wave ของศัตรู รวมถึงการกำหนดจำนวนศัตรู ระยะเวลาการสร้าง และการเปลี่ยน wave

## คลาสและการสืบทอด
```java
public class WaveManager
```
- คลาสอิสระที่ไม่สืบทอดจากคลาสอื่น

## ตัวแปรสำคัญ

### สถานะ Wave
- `int currentWaveNumber` - หมายเลข wave ปัจจุบัน (เริ่มต้น 1)
- `int enemiesToSpawnInWave` - จำนวนศัตรูที่ต้องสร้างใน wave นี้
- `int enemiesSpawnedInWave` - จำนวนศัตรูที่สร้างไปแล้ว
- `int spawnTimer` - ตัวจับเวลาการสร้างศัตรู
- `boolean isCurrentlySpawning` - กำลังสร้างศัตรูหรือไม่

### การอ้างอิงเกม
- `Map gameMap` - อ้างอิงไปยังแผนที่สำหรับการสร้างศัตรู
- `ArrayList<Enemy> activeEnemies` - รายการศัตรูที่ยังมีชีวิต
- `CoinManager coinManager` - อ้างอิงไปยังระบบเหรียญ

## Methods อย่างละเอียด

### Constructor
```java
public WaveManager(Map gameMap, ArrayList<Enemy> enemyList, CoinManager coinManager)
```
**วัตถุประสงค์**: สร้าง WaveManager และเชื่อมต่อกับระบบเกม
**พารามิเตอร์**:
- `gameMap` - แผนที่สำหรับการสร้างศัตรู
- `enemyList` - รายการศัตรูที่ยังมีชีวิต
- `coinManager` - ระบบเหรียญสำหรับรางวัลการฆ่าศัตรู
**การทำงาน**:
1. เก็บอ้างอิงของระบบต่างๆ
2. ไม่เริ่ม wave อัตโนมัติ (ต้องเรียก startNextWave())

### startNextWave()
```java
public void startNextWave()
```
**วัตถุประสงค์**: เริ่ม wave ใหม่
**การทำงาน**:
1. เรียก `Enemy.resetEnemyCount()` - รีเซ็ตตัวนับศัตรูสำหรับ positioning
2. เรียก `calculateEnemiesForWave()` - คำนวณจำนวนศัตรูใน wave นี้
3. เรียก `resetWaveState()` - รีเซ็ตสถานะ wave
4. เรียก `announceWaveStart()` - ประกาศเริ่ม wave

### calculateEnemiesForWave()
```java
private void calculateEnemiesForWave()
```
**วัตถุประสงค์**: คำนวณจำนวนศัตรูใน wave ปัจจุบัน
**การทำงาน**:
- ใช้สูตร: `BASE_ENEMIES_PER_WAVE + currentWaveNumber * ENEMIES_INCREASE_PER_WAVE`
- Wave 1: 3 + 1×2 = 5 ตัว
- Wave 2: 3 + 2×2 = 7 ตัว
- Wave 3: 3 + 3×2 = 9 ตัว
- Wave 4: 3 + 4×2 = 11 ตัว

### resetWaveState()
```java
private void resetWaveState()
```
**วัตถุประสงค์**: รีเซ็ตสถานะสำหรับ wave ใหม่
**การทำงาน**:
1. ตั้ง `enemiesSpawnedInWave = 0`
2. ตั้ง `spawnTimer = 0`
3. ตั้ง `isCurrentlySpawning = true`

### announceWaveStart()
```java
private void announceWaveStart()
```
**วัตถุประสงค์**: ประกาศการเริ่ม wave ใหม่
**การทำงาน**: พิมพ์ข้อความ "Wave X started" ใน console

### update()
```java
public void update()
```
**วัตถุประสงค์**: อัปเดต WaveManager ทุก frame
**การทำงาน**:
1. ตรวจสอบว่ากำลังสร้างศัตรูหรือไม่
2. หากไม่ได้สร้าง return ทันที
3. เรียก `updateSpawnTimer()` - อัปเดตตัวจับเวลา
4. ตรวจสอบ `shouldSpawnEnemy()` - ควรสร้างศัตรูหรือไม่
5. หากควรสร้าง เรียก `spawnEnemy()`
6. ตรวจสอบ `hasFinishedSpawning()` - สร้างครบหรือยัง
7. หากครบแล้ว เรียก `completeWave()`

### updateSpawnTimer()
```java
private void updateSpawnTimer()
```
**วัตถุประสงค์**: เพิ่มตัวจับเวลาการสร้างศัตรู
**การทำงาน**: เพิ่ม `spawnTimer++`

### shouldSpawnEnemy()
```java
private boolean shouldSpawnEnemy()
```
**วัตถุประสงค์**: ตรวจสอบว่าควรสร้างศัตรูหรือไม่
**การทำงาน**:
1. ตรวจสอบว่า timer ถึง delay แล้วหรือยัง (30 frames = 0.5 วินาที)
2. ตรวจสอบว่ายังมีศัตรูที่ต้องสร้างหรือไม่
3. return true หากทั้งสองเงื่อนไขเป็นจริง

### spawnEnemy()
```java
private void spawnEnemy()
```
**วัตถุประสงค์**: สร้างศัตรูใหม่
**การทำงาน**:
1. สร้าง Enemy object ใหม่
2. เพิ่มเข้าไปใน activeEnemies list
3. เพิ่ม `enemiesSpawnedInWave`
4. รีเซ็ต `spawnTimer = 0`

### hasFinishedSpawning()
```java
private boolean hasFinishedSpawning()
```
**วัตถุประสงค์**: ตรวจสอบว่าสร้างศัตรูครบแล้วหรือยัง
**การทำงาน**: return true หาก `enemiesSpawnedInWave >= enemiesToSpawnInWave`

### completeWave()
```java
private void completeWave()
```
**วัตถุประสงค์**: จบ wave ปัจจุบันและเตรียมพร้อมสำหรับ wave ถัดไป
**การทำงาน**:
1. ตั้ง `isCurrentlySpawning = false`
2. เพิ่ม `currentWaveNumber++`

### isWaveFinished()
```java
public boolean isWaveFinished()
```
**วัตถุประสงค์**: ตรวจสอบว่า wave จบสมบูรณ์หรือยัง
**การทำงาน**:
1. ตรวจสอบว่าหยุดสร้างศัตรูแล้ว (`!isCurrentlySpawning`)
2. ตรวจสอบว่าศัตรูทั้งหมดตายแล้ว (`activeEnemies.isEmpty()`)
3. return true หากทั้งสองเงื่อนไขเป็นจริง

### getCurrentWave()
```java
public int getCurrentWave()
```
**วัตถุประสงค์**: ดึงหมายเลข wave ปัจจุบัน
**การทำงาน**: return `currentWaveNumber`
**การใช้งาน**: ใช้โดย GamePanel เพื่อตรวจสอบการชนะ

## ระบบการทำงาน

### การเริ่ม Wave
1. **Initialization**: เรียก startNextWave() จาก GamePanel
2. **Enemy Count Calculation**: คำนวณจำนวนศัตรูตามสูตร
3. **State Reset**: รีเซ็ตตัวแปรสถานะทั้งหมด
4. **Announcement**: ประกาศ wave ใหม่

### การสร้างศัตรู
1. **Timer Management**: นับ frame ตั้งแต่ศัตรูตัวสุดท้าย
2. **Spawn Condition**: ตรวจสอบ timer และจำนวนที่เหลือ
3. **Enemy Creation**: สร้าง Enemy object ใหม่
4. **List Management**: เพิ่มเข้าไปใน activeEnemies list

### การจบ Wave
1. **Spawn Completion**: สร้างศัตรูครบตามจำนวนที่กำหนด
2. **Enemy Elimination**: รอให้ศัตรูทั้งหมดตาย
3. **Wave Transition**: เตรียมพร้อมสำหรับ wave ถัดไป

## การกำหนดค่าและสมดุล

### จำนวนศัตรูต่อ Wave
- **Base**: 3 ตัว
- **Increase**: +2 ตัวต่อ wave
- **Formula**: 3 + (wave × 2)

### ระยะเวลาการสร้าง
- **Spawn Delay**: 30 frames (0.5 วินาที)
- **Consistent**: ระยะเวลาเท่ากันทุก wave

### จำนวน Wave ทั้งหมด
- **Total Waves**: 4 waves
- **Win Condition**: ผ่าน wave 4 แล้วชนะ

## ความสัมพันธ์กับคลาสอื่น

### GamePanel.java
- **Initialization**: GamePanel สร้าง WaveManager
- **Update**: GamePanel เรียก update() ทุก frame
- **Wave Check**: GamePanel ตรวจสอบ isWaveFinished()
- **Next Wave**: GamePanel เรียก startNextWave()

### Enemy.java
- **Creation**: WaveManager สร้าง Enemy objects
- **Reset**: เรียก Enemy.resetEnemyCount() ทุก wave
- **Management**: Enemy ถูกเพิ่มเข้าไปใน activeEnemies list

### Map.java
- **Reference**: ส่ง Map reference ให้ Enemy constructor
- **Pathfinding**: Enemy ใช้ Map สำหรับหาเส้นทาง

### CoinManager.java
- **Reference**: ส่ง CoinManager reference ให้ Enemy
- **Rewards**: (ไม่ได้ใช้ในโหมดป้องกันปัจจุบัน)

### Constants.java
- **Configuration**: ใช้ค่าคงที่จาก Constants.Waves
- **BASE_ENEMIES_PER_WAVE**: 3
- **ENEMIES_INCREASE_PER_WAVE**: 2
- **SPAWN_DELAY_FRAMES**: 30
- **MAX_WAVES**: 4

## จุดเด่นของการออกแบบ

### ความยืดหยุ่น
- สามารถปรับจำนวนศัตรูและ wave ได้ง่าย
- แยกการจัดการ wave ออกจาก GamePanel

### การควบคุมจังหวะ
- ควบคุมระยะเวลาการสร้างศัตรูได้แม่นยำ
- ป้องกันการสร้างศัตรูพร้อมกันทั้งหมด

### การจัดการสถานะ
- แยกสถานะการสร้างและการจบ wave ชัดเจน
- ตรวจสอบเงื่อนไขการเปลี่ยน wave อย่างถูกต้อง

### ความสมดุลของเกม
- เพิ่มความยากตามลำดับ wave
- ให้เวลาผู้เล่นเตรียมตัวระหว่าง wave

## การปรับแต่งและขยายระบบ
1. **เพิ่มประเภทศัตรู**: แก้ไข spawnEnemy() เพื่อสร้างศัตรูหลายประเภท
2. **ปรับความยาก**: แก้ไขค่าใน Constants.Waves
3. **เพิ่ม Wave พิเศษ**: เพิ่มเงื่อนไขพิเศษใน calculateEnemiesForWave()
4. **ระบบ Boss**: เพิ่มการสร้าง Boss ใน wave สุดท้าย
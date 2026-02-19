# WaveManager.java - ระบบจัดการ Wave ศัตรู

## ภาพรวม
WaveManager เป็นคลาสที่จัดการการสร้างและควบคุม wave ของศัตรู รวมถึงการกำหนดจำนวนศัตรู ระยะเวลาการสร้าง และการเปลี่ยน wave บอสจะเริ่มปรากฏตั้งแต่ wave 3 และเพิ่มจำนวนทุก 2 wave (wave 3: 1 บอส, wave 5: 2 บอส, wave 7: 3 บอส)

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
- `ArrayList<BaseEnemy> activeEnemies` - รายการศัตรูที่ยังมีชีวิต (รวม Enemy และ Boss)
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
1. เรียก `calculateEnemiesForWave()` - คำนวณจำนวนศัตรูใน wave นี้
2. เรียก `resetWaveState()` - รีเซ็ตสถานะ wave
3. เรียก `announceWaveStart()` - ประกาศเริ่ม wave

### calculateEnemiesForWave()
```java
private void calculateEnemiesForWave()
```
**วัตถุประสงค์**: คำนวณจำนวนศัตรูใน wave ปัจจุบัน
**การทำงาน**:
- ใช้สูตร: `BASE_ENEMIES_PER_WAVE + currentWaveNumber * ENEMIES_INCREASE_PER_WAVE + bossCount`
- Wave 1: 3 + 1×3 = 6 ตัว (ไม่มีบอส)
- Wave 2: 3 + 2×3 = 9 ตัว (ไม่มีบอส)
- Wave 3: 3 + 3×3 + 1 = 13 ตัว (บอส 1 ตัว)
- Wave 4: 3 + 4×3 = 15 ตัว (ไม่มีบอส)
- Wave 5: 3 + 5×3 + 2 = 20 ตัว (บอส 2 ตัว)
- Wave 6: 3 + 6×3 = 21 ตัว (ไม่มีบอส)
- Wave 7: 3 + 7×3 + 3 = 27 ตัว (บอส 3 ตัว)

**หมายเหตุ**: บอสจะเกิดตั้งแต่ wave 3 และเพิ่มทุก 2 wave

### calculateBossCount()
```java
private int calculateBossCount()
```
**วัตถุประสงค์**: คำนวณจำนวนบอสใน wave ปัจจุบัน
**การทำงาน**:
- Wave < 3: return 0 (ไม่มีบอส)
- Wave >= 3: return `(waveNumber - 3) / 2 + 1`
  - Wave 3: (3-3)/2 + 1 = 1 บอส
  - Wave 4: (4-3)/2 + 1 = 1 บอส (ปัดเศษลง)
  - Wave 5: (5-3)/2 + 1 = 2 บอส
  - Wave 6: (6-3)/2 + 1 = 2 บอส (ปัดเศษลง)
  - Wave 7: (7-3)/2 + 1 = 3 บอส

### shouldHaveBoss()
```java
private boolean shouldHaveBoss()
```
**วัตถุประสงค์**: ตรวจสอบว่า wave นี้ควรมีบอสหรือไม่
**การทำงาน**: return true ถ้า `currentWaveNumber >= 3`
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
**การทำงาน**: 
- ตั้งค่า waveAnnouncementText = "Wave X"
- ตั้งค่า waveAnnouncementTimer = 180 frames (3 วินาที)
**หมายเหตุ**: ไม่มี console output เพื่อความสะอาดของโค้ด

### update()
```java
public void update()
```
**วัตถุประสงค์**: อัปเดต WaveManager ทุก frame
**การทำงาน**:
1. อัปเดต waveAnnouncementTimer (ลดลงทีละ 1 ถ้ามากกว่า 0)
2. ตรวจสอบว่ากำลังสร้างศัตรูหรือไม่
3. หากไม่ได้สร้าง return ทันที
4. เรียก `updateSpawnTimer()` - อัปเดตตัวจับเวลา
5. ตรวจสอบ `shouldSpawnEnemy()` - ควรสร้างศัตรูหรือไม่
6. หากควรสร้าง เรียก `spawnEnemy()`
7. ตรวจสอบ `hasFinishedSpawning()` - สร้างครบหรือยัง
8. หากครบแล้ว เรียก `completeWave()`

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
**วัตถุประสงค์**: สร้างศัตรูใหม่และตั้งค่าระบบหลีกเลี่ยงการชน
**การทำงาน**:
1. ตรวจสอบว่าควร spawn Boss หรือไม่ (wave >= 3 และเป็นศัตรูตัวสุดท้าย)
2. ถ้าควร spawn Boss: สร้าง EnemyBoss
3. ถ้าไม่ใช่: สร้าง Enemy ปกติ
4. เรียก `newEnemy.setAllEnemies(activeEnemies)` - ให้ศัตรูใหม่อ้างอิงรายการศัตรูทั้งหมด
5. เพิ่มศัตรูใหม่เข้าไปใน activeEnemies list
6. อัปเดตศัตรูทั้งหมดที่มีอยู่ให้อ้างอิงรายการที่อัปเดตแล้ว (loop ผ่านทุกตัว)
7. เพิ่ม `enemiesSpawnedInWave`
8. รีเซ็ต `spawnTimer = 0`

**หมายเหตุ**: การตั้งค่า allEnemies ให้ศัตรูทุกตัวทำให้พวกมันสามารถหลีกเลี่ยงการชนกันขณะเดินได้

### shouldSpawnBoss()
```java
private boolean shouldSpawnBoss()
```
**วัตถุประสงค์**: ตรวจสอบว่าควร spawn Boss หรือไม่
**การทำงาน**:
1. ตรวจสอบว่า wave นี้ควรมีบอสหรือไม่ (`shouldHaveBoss()`)
2. คำนวณจำนวนบอสที่ควรมี (`calculateBossCount()`)
3. คำนวณจำนวนศัตรูปกติ = `enemiesToSpawnInWave - bossCount`
4. return true ถ้าสร้างศัตรูปกติครบแล้ว (`enemiesSpawnedInWave >= normalEnemyCount`)

**ตัวอย่าง Wave 3**:
- enemiesToSpawnInWave = 13
- bossCount = 1
- normalEnemyCount = 12
- บอสจะ spawn เมื่อ enemiesSpawnedInWave >= 12 (ตัวที่ 13)

**ตัวอย่าง Wave 5**:
- enemiesToSpawnInWave = 20
- bossCount = 2
- normalEnemyCount = 18
- บอสจะ spawn เมื่อ enemiesSpawnedInWave >= 18 (ตัวที่ 19 และ 20)

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
1. ตั้ง `isCurrentlySpawning = false` - หยุดการสร้างศัตรู
2. เพิ่ม `currentWaveNumber++` - เพิ่มหมายเลข wave ทันที

**หมายเหตุ**: การเพิ่ม wave number ทันทีหลังจบการ spawn ทำให้ต้องใช้เงื่อนไข `currentWave > MAX_WAVES` ในการเช็คชนะ (ไม่ใช่ `>=`)

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

### getWaveAnnouncementText()
```java
public String getWaveAnnouncementText()
```
**วัตถุประสงค์**: ดึงข้อความประกาศ wave
**การทำงาน**: return waveAnnouncementText ถ้า timer > 0, ไม่เช่นนั้น return ""
**การใช้งาน**: ใช้โดย GamePanel เพื่อแสดงข้อความ wave บนหน้าจอ

### shouldShowWaveAnnouncement()
```java
public boolean shouldShowWaveAnnouncement()
```
**วัตถุประสงค์**: ตรวจสอบว่าควรแสดงข้อความประกาศ wave หรือไม่
**การทำงาน**: return true ถ้า waveAnnouncementTimer > 0
**การใช้งาน**: ใช้โดย GamePanel เพื่อตรวจสอบก่อนวาดข้อความ

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
4. **Collision Avoidance Setup**: ตั้งค่าอ้างอิงรายการศัตรูทั้งหมดให้ศัตรูใหม่
5. **List Management**: เพิ่มเข้าไปใน activeEnemies list
6. **Update All Enemies**: อัปเดตศัตรูทั้งหมดให้อ้างอิงรายการที่อัปเดตแล้ว

### การจบ Wave
1. **Spawn Completion**: สร้างศัตรูครบตามจำนวนที่กำหนด → `currentWaveNumber++` ทันที
2. **Enemy Elimination**: รอให้ศัตรูทั้งหมดตาย
3. **Wave Check**: GamePanel เช็คว่า `currentWave > MAX_WAVES` (ใช้ `>` เพราะ wave number เพิ่มแล้ว)
4. **Wave Transition**: หากยังไม่ครบ เริ่ม wave ถัดไป / หากครบแล้ว ชนะเกม

## การกำหนดค่าและสมดุล

### จำนวนศัตรูต่อ Wave
- **Base**: 3 ตัว
- **Increase**: +3 ตัวต่อ wave
- **Formula**: 3 + (wave × 3)

### ระยะเวลาการสร้าง
- **Spawn Delay**: 30 frames (0.5 วินาที)
- **Consistent**: ระยะเวลาเท่ากันทุก wave

### จำนวน Wave ทั้งหมด
- **Total Waves**: 5 waves
- **Win Condition**: ผ่าน wave 5 แล้วชนะ

## ความสัมพันธ์กับคลาสอื่น

### GamePanel.java
- **Initialization**: GamePanel สร้าง WaveManager
- **Update**: GamePanel เรียก update() ทุก frame
- **Wave Check**: GamePanel ตรวจสอบ isWaveFinished()
- **Next Wave**: GamePanel เรียก startNextWave()

### BaseEnemy.java
- **Creation**: WaveManager สร้าง Enemy และ EnemyBoss objects
- **Management**: ศัตรูถูกเพิ่มเข้าไปใน activeEnemies list
- **Boss Spawn**: Wave 3+ spawn Boss ตามสูตร (wave-3)/2 + 1
  - Wave 3: 1 บอส
  - Wave 5: 2 บอส
  - Wave 7: 3 บอส
- **Collision Avoidance**: WaveManager ตั้งค่า allEnemies reference ให้ศัตรูทุกตัวเพื่อหลีกเลี่ยงการชนกัน

### Map.java
- **Reference**: ส่ง Map reference ให้ Enemy constructor
- **Pathfinding**: Enemy ใช้ Map สำหรับหาเส้นทาง

### CoinManager.java
- **Reference**: ส่ง CoinManager reference ให้ Enemy
- **Rewards**: (ไม่ได้ใช้ในโหมดป้องกันปัจจุบัน)

### Constants.java
- **Configuration**: ใช้ค่าคงที่จาก Constants.Waves
- **BASE_ENEMIES_PER_WAVE**: 3
- **ENEMIES_INCREASE_PER_WAVE**: 3
- **SPAWN_DELAY_FRAMES**: 30
- **MAX_WAVES**: 5

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

### ระบบหลีกเลี่ยงการชน
- ตั้งค่าอ้างอิงรายการศัตรูให้ศัตรูทุกตัว
- ศัตรูสามารถตรวจสอบตำแหน่งศัตรูตัวอื่นและหลีกเลี่ยงการชนกันขณะเดิน
- อัปเดตรายการอ้างอิงทุกครั้งที่มีศัตรูใหม่ spawn

## การปรับแต่งและขยายระบบ
1. **เพิ่มประเภทศัตรู**: แก้ไข spawnEnemy() เพื่อสร้างศัตรูหลายประเภท
2. **ปรับความยาก**: แก้ไขค่าใน Constants.Waves
3. **เพิ่ม Wave พิเศษ**: เพิ่มเงื่อนไขพิเศษใน calculateEnemiesForWave()
4. **ระบบ Boss**: บอสเกิดตั้งแต่ wave 3 และเพิ่มทุก 2 wave
   - Wave 3: 1 บอส (ตัวสุดท้าย)
   - Wave 4: ไม่มีบอส
   - Wave 5: 2 บอส (2 ตัวสุดท้าย)
   - Wave 6: ไม่มีบอส
   - Wave 7: 3 บอส (3 ตัวสุดท้าย)

## ตารางจำนวนศัตรูและบอสแต่ละ Wave

| Wave | ศัตรูปกติ | บอส | รวม | หมายเหตุ |
|------|----------|-----|-----|----------|
| 1 | 6 | 0 | 6 | - |
| 2 | 9 | 0 | 9 | - |
| 3 | 12 | 1 | 13 | บอสตัวแรก |
| 4 | 15 | 0 | 15 | - |
| 5 | 18 | 2 | 20 | บอส 2 ตัว |
| 6 | 21 | 0 | 21 | - |
| 7 | 24 | 3 | 27 | บอส 3 ตัว |
| 8 | 27 | 0 | 27 | - |
| 9 | 30 | 4 | 34 | บอส 4 ตัว |

**สูตร**:
- ศัตรูปกติ: `3 + (wave × 3)`
- บอส: `(wave - 3) / 2 + 1` (ถ้า wave >= 3, ปัดเศษลง)
- รวม: ศัตรูปกติ + บอส
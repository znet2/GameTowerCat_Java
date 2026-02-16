# CoinManager.java - ระบบจัดการเหรียญ

## ภาพรวม
CoinManager เป็นคลาสที่จัดการระบบเหรียญของผู้เล่น รวมถึงการหาเงิน การใช้จ่าย การตรวจสอบความสามารถในการซื้อ และการแสดงผล

## คลาสและการสืบทอด
```java
public class CoinManager
```
- คลาสอิสระที่ไม่สืบทอดจากคลาสอื่น

## ตัวแปรสำคัญ

### สถานะเหรียญ
- `int currentCoins` - จำนวนเหรียญปัจจุบัน

## Methods อย่างละเอียด

### Constructor
```java
public CoinManager()
```
**วัตถุประสงค์**: สร้าง CoinManager และตั้งค่าเริ่มต้น
**การทำงาน**:
- ตั้งเหรียญเริ่มต้นเป็น `Constants.Economy.STARTING_COINS` (50 เหรียญ)

### getCurrentCoins()
```java
public int getCurrentCoins()
```
**วัตถุประสงค์**: ดึงจำนวนเหรียญปัจจุบัน
**การทำงาน**: return `currentCoins`
**การใช้งาน**: ใช้สำหรับการแสดงผลและการตรวจสอบ

### addCoins(int amount)
```java
public void addCoins(int amount)
```
**วัตถุประสงค์**: เพิ่มเหรียญให้ผู้เล่น
**พารามิเตอร์**: `amount` - จำนวนเหรียญที่ต้องการเพิ่ม (ต้องเป็นค่าบวก)
**การทำงาน**:
1. ตรวจสอบว่า amount > 0
2. หากใช่ เพิ่ม `currentCoins += amount`
**การใช้งาน**: ใช้เมื่อได้รับรางวัลหรือรายได้ passive

### spendCoins(int amount)
```java
public boolean spendCoins(int amount)
```
**วัตถุประสงค์**: ใช้จ่ายเหรียญสำหรับการซื้อ
**พารามิเตอร์**: `amount` - จำนวนเหรียญที่ต้องการใช้
**การทำงาน**:
1. เรียก `canAfford(amount)` เพื่อตรวจสอบความสามารถในการซื้อ
2. หากซื้อได้:
   - ลด `currentCoins -= amount`
   - return true
3. หากซื้อไม่ได้: return false
**การใช้งาน**: ใช้สำหรับการซื้อหน่วยป้องกันทั้งหมด

### canAfford(int amount)
```java
public boolean canAfford(int amount)
```
**วัตถุประสงค์**: ตรวจสอบว่าสามารถซื้อได้หรือไม่
**พารามิเตอร์**: `amount` - ราคาที่ต้องการตรวจสอบ
**การทำงาน**:
1. ตรวจสอบว่า `currentCoins >= amount`
2. ตรวจสอบว่า `amount >= 0`
3. return true หากทั้งสองเงื่อนไขเป็นจริง
**การใช้งาน**: ใช้สำหรับการตรวจสอบก่อนซื้อและการแสดงผล UI

### awardCoinsForEnemyKill()
```java
public void awardCoinsForEnemyKill()
```
**วัตถุประสงค์**: ให้รางวัลเหรียญสำหรับการฆ่าศัตรู
**การทำงาน**: เรียก `addCoins(Constants.Economy.COINS_PER_ENEMY_KILL)` (10 เหรียญ)
**การใช้งาน**: เรียกโดย Enemy เมื่อถูกฆ่า

### awardCoinsForWaveComplete()
```java
public void awardCoinsForWaveComplete()
```
**วัตถุประสงค์**: ให้รางวัลเหรียญโบนัสเมื่อจบ wave
**การทำงาน**: เรียก `addCoins(Constants.Economy.COINS_PER_WAVE_COMPLETE)` (50 เหรียญ)
**การใช้งาน**: เรียกโดย GamePanel เมื่อจบแต่ละ wave

### renderCoinDisplay(Graphics graphics, int screenWidth, int screenHeight)
```java
public void renderCoinDisplay(Graphics graphics, int screenWidth, int screenHeight)
```
**วัตถุประสงค์**: วาดการแสดงผลเหรียญบนหน้าจอ
**พารามิเตอร์**:
- `graphics` - Graphics context สำหรับการวาด
- `screenWidth, screenHeight` - ขนาดหน้าจอสำหรับการจัดตำแหน่ง
**การทำงาน**:
1. **สร้างข้อความ**: "Coins: " + currentCoins
2. **ตั้งค่าฟอนต์**: ใช้ Constants.UI.COIN_FONT
3. **คำนวณขนาดข้อความ**: ใช้ FontMetrics
4. **คำนวณตำแหน่ง**: มุมขวาล่างพร้อม padding
5. **วาดพื้นหลัง**: สี่เหลี่ยมสีดำโปร่งใส
6. **วาดข้อความ**: สีเหลืองที่ตำแหน่งที่คำนวณ

### resetCoins()
```java
public void resetCoins()
```
**วัตถุประสงค์**: รีเซ็ตเหรียญกลับเป็นค่าเริ่มต้น
**การทำงาน**: ตั้ง `currentCoins = Constants.Economy.STARTING_COINS`
**การใช้งาน**: ใช้เมื่อเริ่มเกมใหม่หรือ restart

## ระบบเศรษฐกิจ

### รายได้ (Income)
1. **เหรียญเริ่มต้น**: 50 เหรียญ
2. **Enemy Kill Reward**: 10 เหรียญต่อตัว (เมื่อศัตรูถูกฆ่า)
3. **Wave Complete Bonus**: 50 เหรียญต่อ wave (เมื่อจบแต่ละ wave)

### รายจ่าย (Expenses)
- **Tank**: 25 เหรียญ (ใช้ `spendCoins(Constants.Entities.TANK_COST)`)
- **Magic**: 10 เหรียญ (ใช้ `spendCoins(Constants.Entities.MAGIC_COST)`)
- **Archer**: 15 เหรียญ (ใช้ `spendCoins(Constants.Entities.ARCHER_COST)`)
- **Assassin**: 20 เหรียญ (ใช้ `spendCoins(Constants.Entities.ASSASSIN_COST)`)

### การแสดงผล
1. **ตำแหน่ง**: มุมขวาล่างของหน้าจอ
2. **สี**: ข้อความสีเหลือง พื้นหลังสีดำโปร่งใส
3. **ฟอนต์**: Arial Bold 16pt
4. **รูปแบบ**: "Coins: XXX"

## การทำงานของระบบ

### Income System
```
1. Starting Coins: 50 coins (เริ่มเกม)
2. Enemy Kill: +10 coins (ทุกครั้งที่ฆ่าศัตรู)
3. Wave Complete: +50 coins (ทุกครั้งที่จบ wave)
```

### Purchase Validation
```
1. Check: currentCoins >= cost
2. Check: cost >= 0
3. If valid: Deduct coins, return true
4. If invalid: Return false

ทุกหน่วยใช้ spendCoins() โดยตรง:
- Tank: spendCoins(Constants.Entities.TANK_COST)
- Magic: spendCoins(Constants.Entities.MAGIC_COST)
- Archer: spendCoins(Constants.Entities.ARCHER_COST)
- Assassin: spendCoins(Constants.Entities.ASSASSIN_COST)
```

### UI Integration
```
1. Calculate text dimensions
2. Position at bottom-right with padding
3. Draw background rectangle
4. Draw text on top
```

## ความสัมพันธ์กับคลาสอื่น

### GamePanel.java
- **Purchase**: GamePanel เรียก spendCoins() เมื่อวางหน่วยป้องกัน (Tank, Magic, Archer, Assassin)
- **Validation**: GamePanel เรียก canAfford() เพื่อตรวจสอบ UI
- **Display**: GamePanel เรียก renderCoinDisplay() เพื่อแสดงผล
- **Wave Bonus**: GamePanel เรียก awardCoinsForWaveComplete() เมื่อจบ wave

### WaveManager.java
- **Reference**: WaveManager เก็บอ้างอิง CoinManager
- **Enemy Creation**: ส่ง CoinManager ให้ Enemy constructor

### Enemy.java
- **Reference**: Enemy เก็บอ้างอิง CoinManager
- **Rewards**: Enemy เรียก awardCoinsForEnemyKill() เมื่อถูกฆ่า

### Constants.java
- **Configuration**: ใช้ค่าคงที่จาก Constants.Economy
- **STARTING_COINS**: 50
- **COINS_PER_ENEMY_KILL**: 10
- **COINS_PER_WAVE_COMPLETE**: 50

## จุดเด่นของการออกแบบ

### ความปลอดภัย
- ตรวจสอบค่าลบและการใช้จ่ายเกินจำนวน
- แยกการตรวจสอบและการใช้จ่ายออกจากกัน

### ความยืดหยุ่น
- สามารถปรับค่าต่างๆ ผ่าน Constants ได้ง่าย
- ใช้ `spendCoins()` โดยตรงสำหรับทุกหน่วย - ไม่ต้องมี wrapper method

### ความเรียบง่าย
- ไม่มี method ซ้ำซ้อนสำหรับแต่ละหน่วย
- ใช้ `spendCoins()` และ `canAfford()` ครอบคลุมทุกการซื้อ

### การแสดงผลที่ดี
- แสดงผลแบบ real-time
- มีพื้นหลังเพื่อความชัดเจน
- ตำแหน่งที่เหมาะสม

### ระบบเศรษฐกิจที่สมดุล
- รายได้จากการฆ่าศัตรูและจบ wave
- ราคาหน่วยที่แตกต่างกันตามความแข็งแกร่ง
- เหรียญเริ่มต้นที่เพียงพอสำหรับการเริ่มเกม
- โบนัส wave ช่วยให้สามารถซื้อหน่วยเพิ่มได้

## การปรับแต่งและขยายระบบ
1. **เพิ่มประเภทรายได้**: เพิ่ม method สำหรับรายได้จากแหล่งอื่น
2. **ระบบอัพเกรด**: เพิ่มการซื้ออัพเกรดหน่วย
3. **ระบบดอกเบี้ย**: เพิ่มดอกเบี้ยตามจำนวนเหรียญ
4. **รางวัลพิเศษ**: เพิ่มรางวัลจากการผ่าน wave
5. **ระบบร้านค้า**: เพิ่มการซื้อไอเทมพิเศษ
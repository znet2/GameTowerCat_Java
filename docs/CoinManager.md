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
- `int passiveIncomeTimer` - ตัวจับเวลาสำหรับรายได้ passive

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

### update()
```java
public void update()
```
**วัตถุประสงค์**: อัปเดต CoinManager ทุก frame
**การทำงาน**:
1. เพิ่ม `passiveIncomeTimer++`
2. ตรวจสอบว่า timer ถึง interval หรือยัง (300 frames = 5 วินาที)
3. หากถึงแล้ว:
   - เรียก `addCoins(Constants.Economy.PASSIVE_INCOME_AMOUNT)` (5 เหรียญ)
   - รีเซ็ต `passiveIncomeTimer = 0`

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
**หมายเหตุ**: ไม่ได้ใช้ในโหมดป้องกันปัจจุบัน เพราะ Tank ไม่ฆ่าศัตรู

### getTankCost()
```java
public int getTankCost()
```
**วัตถุประสงค์**: ดึงราคา Tank
**การทำงาน**: return `Constants.Entities.TANK_COST` (25 เหรียญ)
**การใช้งาน**: ใช้สำหรับการแสดงผล UI และการตรวจสอบ

### purchaseTank()
```java
public boolean purchaseTank()
```
**วัตถุประสงค์**: ซื้อ Tank
**การทำงาน**: เรียก `spendCoins(Constants.Entities.TANK_COST)`
**การใช้งาน**: ใช้โดย GamePanel เมื่อผู้เล่นวาง Tank

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

### getEnemyKillReward()
```java
public int getEnemyKillReward()
```
**วัตถุประสงค์**: ดึงจำนวนเหรียญรางวัลการฆ่าศัตรู
**การทำงาน**: return `Constants.Economy.COINS_PER_ENEMY_KILL` (10 เหรียญ)
**การใช้งาน**: ใช้สำหรับการแสดงข้อมูลและการปรับสมดุล

## ระบบเศรษฐกิจ

### รายได้ (Income)
1. **เหรียญเริ่มต้น**: 50 เหรียญ
2. **Passive Income**: 5 เหรียญทุก 5 วินาที
3. **Enemy Kill Reward**: 10 เหรียญต่อตัว (ไม่ได้ใช้ในปัจจุบัน)

### รายจ่าย (Expenses)
1. **Tank**: 25 เหรียญ
2. **Magic**: 10 เหรียญ
3. **Archer**: 15 เหรียญ
4. **Assassin**: 20 เหรียญ

### การแสดงผล
1. **ตำแหน่ง**: มุมขวาล่างของหน้าจอ
2. **สี**: ข้อความสีเหลือง พื้นหลังสีดำโปร่งใส
3. **ฟอนต์**: Arial Bold 16pt
4. **รูปแบบ**: "Coins: XXX"

## การทำงานของระบบ

### Passive Income System
```
Timer: 0 → 300 frames (5 seconds)
Action: Add 5 coins
Reset: Timer = 0
Repeat: Continuously
```

### Purchase Validation
```
1. Check: currentCoins >= cost
2. Check: cost >= 0
3. If valid: Deduct coins, return true
4. If invalid: Return false
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
- **Update**: GamePanel เรียก update() ทุก frame
- **Purchase**: GamePanel เรียก purchaseTank() เมื่อวาง Tank
- **Validation**: GamePanel เรียก canAfford() เพื่อตรวจสอบ UI
- **Display**: GamePanel เรียก renderCoinDisplay() เพื่อแสดงผล

### WaveManager.java
- **Reference**: WaveManager เก็บอ้างอิง CoinManager
- **Enemy Creation**: ส่ง CoinManager ให้ Enemy constructor

### Enemy.java
- **Reference**: Enemy เก็บอ้างอิง CoinManager
- **Rewards**: (ไม่ได้ใช้ในโหมดป้องกันปัจจุบัน)

### Constants.java
- **Configuration**: ใช้ค่าคงที่จาก Constants.Economy
- **STARTING_COINS**: 50
- **COINS_PER_ENEMY_KILL**: 10
- **PASSIVE_INCOME_AMOUNT**: 5
- **PASSIVE_INCOME_INTERVAL_FRAMES**: 300

## จุดเด่นของการออกแบบ

### ความปลอดภัย
- ตรวจสอบค่าลบและการใช้จ่ายเกินจำนวน
- แยกการตรวจสอบและการใช้จ่ายออกจากกัน

### ความยืดหยุ่น
- สามารถปรับค่าต่างๆ ผ่าน Constants ได้ง่าย
- รองรับการเพิ่มประเภทการซื้อใหม่

### การแสดงผลที่ดี
- แสดงผลแบบ real-time
- มีพื้นหลังเพื่อความชัดเจน
- ตำแหน่งที่เหมาะสม

### ระบบเศรษฐกิจที่สมดุล
- รายได้ passive ที่สม่ำเสมอ
- ราคาหน่วยที่แตกต่างกันตามความแข็งแกร่ง
- เหรียญเริ่มต้นที่เพียงพอสำหรับการเริ่มเกม

## การปรับแต่งและขยายระบบ
1. **เพิ่มประเภทรายได้**: เพิ่ม method สำหรับรายได้จากแหล่งอื่น
2. **ระบบอัพเกรด**: เพิ่มการซื้ออัพเกรดหน่วย
3. **ระบบดอกเบี้ย**: เพิ่มดอกเบี้ยตามจำนวนเหรียญ
4. **รางวัลพิเศษ**: เพิ่มรางวัลจากการผ่าน wave
5. **ระบบร้านค้า**: เพิ่มการซื้อไอเทมพิเศษ
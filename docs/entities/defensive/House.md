# House.java - บ้านที่ต้องป้องกัน

## ภาพรวม
House เป็นวัตถุหลักที่ผู้เล่นต้องป้องกันในเกม Tower Defense หากบ้านถูกทำลาย (เลือดหมด) ผู้เล่นจะแพ้

## คลาสและการสืบทอด
```java
public class House extends GameObject implements Defensive
```
- สืบทอดจาก `GameObject` สำหรับคุณสมบัติพื้นฐาน (ตำแหน่ง, ขนาด, การวาด)
- ใช้ `Defensive` interface สำหรับระบบการป้องกัน (takeDamage, isDestroyed, etc.)

## ตัวแปรสำคัญ

### สถานะปัจจุบัน
- `int currentHealth` - เลือดปัจจุบัน (เริ่มต้น 100 จาก Constants)

## Methods สำคัญ

### Constructor
```java
public House(int gridColumn, int gridRow, int tileSize, Image houseImage)
```
**การทำงาน**:
1. เรียก constructor ของ GameObject ด้วยขนาด 7x7 ไทล์
2. ตั้งค่าเลือดเริ่มต้นจาก Constants

**หมายเหตุ**: House เป็นวัตถุที่ใหญ่ที่สุดในเกม (7x7 ไทล์ = 224x224 พิกเซล)

### takeDamage(int damageAmount)
**การทำงาน**:
1. ลดเลือดตามจำนวนความเสียหาย
2. ตรวจสอบไม่ให้เลือดต่ำกว่า MINIMUM_HEALTH
3. แสดงเลือดปัจจุบันใน console

**หมายเหตุ**: House ไม่มีค่าป้องกัน รับความเสียหายเต็มจำนวน

### isDestroyed()
return true หากเลือดหมด (currentHealth <= 0)

**การใช้งาน**: GamePanel ตรวจสอบเพื่อจบเกม (แพ้)

### getBounds()
return Rectangle สำหรับการตรวจสอบการชนกับศัตรู

### getHealth() (Legacy Method)
return เลือดปัจจุบัน - ใช้เพื่อความเข้ากันได้กับโค้ดเดิม

**หมายเหตุ**: ควรใช้ `getCurrentHealth()` แทนในโค้ดใหม่

### draw(Graphics graphics)
วาดรูปภาพบ้านเท่านั้น (ไม่มีแถบเลือด)

## ค่าคงที่ที่ใช้จาก Constants
- `HOUSE_INITIAL_HEALTH = 100` - เลือดเริ่มต้น
- `HOUSE_WIDTH_TILES = 7` - ความกว้าง 7 ไทล์
- `HOUSE_HEIGHT_TILES = 7` - ความสูง 7 ไทล์
- `MINIMUM_HEALTH = 0` - เลือดต่ำสุด

## Methods ที่ได้จาก Base Classes/Interfaces
- `getGridColumn(int)`, `getGridRow(int)` - จาก GameObject
- `getHealthPercentage()` - จาก Defensive interface
- `damage(int)`, `isDead()` - legacy methods จาก Defensive interface
- `getCurrentHealth()`, `getMaxHealth()`, `getDefenseRating()` - จาก Defensive interface

## การใช้งานในเกม

### Map.java
```java
// สร้างบ้านที่ตำแหน่งที่กำหนด
private void initializeMapObjects() {
    mapObjects.add(new House(
        Constants.Map.HOUSE_COLUMN, 
        Constants.Map.HOUSE_ROW,
        Constants.Map.TILE_SIZE, 
        houseImage
    ));
}

// ดึงบ้านเพื่อใช้งาน
public House getHouse() {
    return (House) mapObjects.get(0);
}
```

### Enemy.java
```java
// ศัตรูโจมตีบ้าน
if (currentAttackTarget instanceof House) {
    targetHouse.damage(Constants.Entities.ENEMY_ATTACK_DAMAGE);
}
```

### GamePanel.java
```java
// ตรวจสอบว่าเกมจบหรือยัง (บ้านถูกทำลาย)
private void checkGameOver() {
    if (gameMap.getHouse().getHealth() <= 0) {
        isGameOver = true;
        isWin = false;
    }
}

// แสดงแถบเลือดของบ้าน
private void drawHouseHealthBar(Graphics graphics) {
    int currentHealth = gameMap.getHouse().getHealth();
    int maxHealth = Constants.Entities.HOUSE_INITIAL_HEALTH;
    double healthPercentage = (double) currentHealth / maxHealth;
    
    // วาดแถบเลือด...
}
```

## จุดเด่น
- เป็นวัตถุหลักที่ต้องป้องกัน
- ขนาดใหญ่ (7x7 ไทล์)
- เลือดปานกลาง (100)
- ไม่มีค่าป้องกัน
- ไม่สามารถเคลื่อนที่หรือโจมตีได้

## ความสำคัญในเกม
- หากบ้านถูกทำลาย ผู้เล่นแพ้ทันที
- ศัตรูจะพยายามเดินไปยังบ้านและโจมตี
- ผู้เล่นต้องวางหน่วยป้องกันเพื่อหยุดศัตรูก่อนถึงบ้าน

## ความแตกต่างจากหน่วยป้องกันอื่น
- ไม่สามารถวางได้ (มีตำแหน่งคงที่)
- ไม่มีแถบเลือดแสดงบนตัว (แสดงที่ UI แทน)
- ขนาดใหญ่กว่าหน่วยอื่นมาก (7x7 vs 2x2)
- เป็นเป้าหมายสุดท้ายของศัตรู

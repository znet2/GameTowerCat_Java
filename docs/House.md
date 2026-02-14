# House.java - บ้านที่ต้องป้องกัน

## ภาพรวม
House เป็นวัตถุหลักที่ผู้เล่นต้องป้องกันในเกม Tower Defense บ้านสามารถรับความเสียหายจากศัตรูและมีระบบเลือด หากบ้านถูกทำลาย ผู้เล่นจะแพ้

## คลาสและการสืบทอด
```java
public class House extends GameObject
```
- สืบทอดจาก `GameObject` สำหรับคุณสมบัติพื้นฐาน
- ไม่ implement Defensive interface (ใช้ method ของตัวเองแทน)

## ตัวแปรสำคัญ

### ค่าคงที่
- `static final int MINIMUM_HEALTH = 0` - เลือดต่ำสุด

### สถานะปัจจุบัน
- `int currentHealth` - เลือดปัจจุบัน (เริ่มต้น 100)

## Methods อย่างละเอียด

### Constructor
```java
public House(int gridColumn, int gridRow, int tileSize, Image houseImage)
```
**วัตถุประสงค์**: สร้างบ้านที่ตำแหน่งกริดที่กำหนด
**พารามิเตอร์**:
- `gridColumn` - คอลัมน์ในกริด
- `gridRow` - แถวในกริด
- `tileSize` - ขนาดไทล์เป็นพิกเซล
- `houseImage` - รูปภาพบ้าน

**การทำงาน**:
1. เรียก constructor ของ GameObject
2. ใช้ขนาด `Constants.Entities.HOUSE_WIDTH_TILES` x `Constants.Entities.HOUSE_HEIGHT_TILES` (7x7 ไทล์)
3. ตั้งเลือดเริ่มต้นเป็น `Constants.Entities.HOUSE_INITIAL_HEALTH` (100)

### damage(int damageAmount)
```java
public void damage(int damageAmount)
```
**วัตถุประสงค์**: รับความเสียหายและลดเลือด
**พารามิเตอร์**: `damageAmount` - จำนวนความเสียหายที่ได้รับ
**การทำงาน**:
1. ลดเลือดปัจจุบัน: `currentHealth -= damageAmount`
2. ตรวจสอบไม่ให้เลือดต่ำกว่า MINIMUM_HEALTH (0)
3. แสดงเลือดปัจจุบันใน console: "House HP: X"

**ตัวอย่างการใช้งาน**:
```java
// ศัตรูโจมตีบ้าน
house.damage(Constants.Entities.ENEMY_ATTACK_DAMAGE); // ลดเลือด 5
```

### getBounds()
```java
public Rectangle getBounds()
```
**วัตถุประสงค์**: ดึง collision bounds ของบ้าน
**การทำงาน**: return Rectangle ที่ตำแหน่งและขนาดของบ้าน
**การใช้งาน**: ใช้สำหรับการตรวจสอบการชนกับศัตรู

**ตัวอย่าง**:
```java
Rectangle houseBounds = house.getBounds();
if (enemyBounds.intersects(houseBounds)) {
    // ศัตรูชนบ้าน - เริ่มโจมตี
}
```

### getHealth()
```java
public int getHealth()
```
**วัตถุประสงค์**: ดึงเลือดปัจจุบันของบ้าน
**การทำงาน**: return `currentHealth`
**การใช้งาน**: ใช้สำหรับการแสดงผล UI และตรวจสอบเงื่อนไขแพ้

**ตัวอย่าง**:
```java
int houseHealth = house.getHealth();
if (houseHealth <= 0) {
    // เกมจบ - ผู้เล่นแพ้
    gameOver();
}
```

### draw(Graphics graphics)
```java
@Override
public void draw(Graphics graphics)
```
**วัตถุประสงค์**: วาดบ้านบนหน้าจอ
**การทำงาน**: เรียก `super.draw(graphics)` เพื่อวาดรูปภาพบ้าน
**หมายเหตุ**: วาดเฉพาะรูปภาพ ไม่มีแถบเลือดหรือเอฟเฟกต์พิเศษ

## คุณสมบัติเฉพาะของ House

### ขนาดใหญ่
- **ขนาด**: 7x7 ไทล์ (224x224 พิกเซล)
- **ใหญ่ที่สุด**: ใหญ่กว่าหน่วยป้องกันทั้งหมด
- **เป้าหมายใหญ่**: ง่ายต่อการโจมตีของศัตรู

### ตำแหน่งคงที่
- **ตำแหน่ง**: กำหนดใน Constants (คอลัมน์ 32, แถว 4)
- **ไม่เคลื่อนที่**: อยู่ที่เดิมตลอดเกม
- **จุดหมายปลายทาง**: ศัตรูเดินมาหาบ้าน

### ระบบเลือด
- **เลือดเริ่มต้น**: 100 จุด
- **ไม่มีค่าป้องกัน**: รับความเสียหายเต็มจำนวน
- **ไม่ฟื้นฟู**: เลือดไม่เพิ่มขึ้นเอง

### การแสดงผล
- **รูปภาพ**: castle.png
- **ไม่มีแถบเลือด**: แสดงเลือดผ่าน UI แยกต่างหาก
- **ไม่มีแอนิเมชัน**: รูปภาพคงที่

## การใช้งานในเกม

### การสร้างบ้าน
```java
// ใน Map.java
private void initializeMapObjects() {
    mapObjects.add(new House(
        Constants.Map.HOUSE_COLUMN,    // 32
        Constants.Map.HOUSE_ROW,       // 4
        Constants.Map.TILE_SIZE,       // 32
        houseImage                     // castle.png
    ));
}
```

### การเข้าถึงบ้าน
```java
// ใน Map.java
public House getHouse() {
    return (House) mapObjects.get(0);
}

// ใน GamePanel.java
House house = gameMap.getHouse();
int houseHealth = house.getHealth();
```

### การแสดงเลือดบ้าน
```java
// ใน GamePanel.java
private void drawHouseHealthBar(Graphics graphics) {
    int currentHealth = gameMap.getHouse().getHealth();
    int maxHealth = Constants.Entities.HOUSE_INITIAL_HEALTH;
    double healthPercentage = (double) currentHealth / maxHealth;
    
    // วาดแถบเลือดที่มุมขวาบน
    // ...
}
```

### การตรวจสอบเงื่อนไขแพ้
```java
// ใน GamePanel.java
private void checkGameOver() {
    if (gameMap.getHouse().getHealth() <= 0) {
        isGameOver = true;
        isWin = false;
        showGameOverScreen();
    }
}
```

## การโจมตีจากศัตรู

### การตรวจสอบการชน
```java
// ใน Enemy.java
private void checkForHouseCollision() {
    if (!isPositionLocked && getBounds().intersects(targetHouse.getBounds())) {
        Rectangle houseBounds = targetHouse.getBounds();
        positionX = houseBounds.x - Constants.Entities.ENEMY_SIZE - attackPositionOffset;
        
        isPositionLocked = true;
        startAttacking(targetHouse);
    }
}
```

### การโจมตี
```java
// ใน Enemy.java
private void executeAttack() {
    if (currentAttackTarget instanceof House) {
        targetHouse.damage(Constants.Entities.ENEMY_ATTACK_DAMAGE); // 5 ความเสียหาย
    }
}
```

## ความแตกต่างจาก Defensive Units

### ไม่ใช้ Defensive Interface
- House ไม่ implement Defensive interface
- ใช้ method `damage()` แทน `takeDamage()`
- ใช้ method `getHealth()` แทน `getCurrentHealth()`

### ไม่สามารถถูกทำลายและลบออก
- House ไม่มี method `isDead()` หรือ `isDestroyed()`
- ไม่ถูกลบออกจากเกมเมื่อเลือดหมด
- เกมจบเมื่อเลือดหมด แทนที่จะลบวัตถุ

### ไม่มีค่าป้องกัน
- ไม่มี `getDefenseRating()`
- รับความเสียหายเต็มจำนวนเสมอ

## ความสัมพันธ์กับคลาสอื่น

### Map.java
- **การสร้าง**: Map สร้างและเก็บ House ใน mapObjects
- **การเข้าถึง**: ให้ method getHouse() สำหรับเข้าถึง
- **การแสดงผล**: วาด House ผ่าน renderMapObjects()

### Enemy.java
- **เป้าหมาย**: House เป็นเป้าหมายสุดท้ายของศัตรู
- **การโจมตี**: ศัตรูโจมตี House เมื่อไม่มีหน่วยป้องกันขวาง
- **Pathfinding**: ศัตรูหาเส้นทางไปยัง House

### GamePanel.java
- **การแสดงเลือด**: แสดงแถบเลือด House ที่มุมขวาบน
- **เงื่อนไขแพ้**: ตรวจสอบเลือด House เพื่อจบเกม
- **การอัปเดต**: ไม่ต้องอัปเดต House (ไม่มีพฤติกรรมเคลื่อนไหว)

### Constants.java
- **ขนาด**: ใช้ HOUSE_WIDTH_TILES และ HOUSE_HEIGHT_TILES
- **เลือด**: ใช้ HOUSE_INITIAL_HEALTH
- **ตำแหน่ง**: ใช้ HOUSE_COLUMN และ HOUSE_ROW

## จุดเด่นของการออกแบบ

### ความเรียบง่าย
- House มีหน้าที่เดียว: เป็นเป้าหมายที่ต้องป้องกัน
- ไม่มีพฤติกรรมซับซ้อน เช่น การโจมตี หรือการเคลื่อนที่

### ความสำคัญในเกม
- เป็นจุดศูนย์กลางของเกม Tower Defense
- เงื่อนไขชนะ/แพ้ขึ้นอยู่กับ House

### การแสดงผลที่ชัดเจน
- ขนาดใหญ่ทำให้เห็นได้ชัดเจน
- ตำแหน่งคงที่ทำให้ผู้เล่นรู้จุดที่ต้องป้องกัน

### ความเข้ากันได้
- ทำงานร่วมกับระบบอื่นได้ดี
- ไม่ซับซ้อนเกินความจำเป็น

## การปรับปรุงที่เป็นไปได้

### เพิ่ม Defensive Interface
```java
public class House extends GameObject implements Defensive {
    @Override
    public void takeDamage(int damageAmount) {
        damage(damageAmount);
    }
    
    @Override
    public boolean isDestroyed() {
        return currentHealth <= 0;
    }
    
    // ... other methods
}
```

### เพิ่มระบบซ่อมแซม
```java
public void repair(int repairAmount) {
    currentHealth = Math.min(currentHealth + repairAmount, 
                           Constants.Entities.HOUSE_INITIAL_HEALTH);
}
```

### เพิ่มระบบอัพเกรด
```java
private int maxHealth = Constants.Entities.HOUSE_INITIAL_HEALTH;
private int defenseRating = 0;

public void upgradeDefense(int additionalDefense) {
    this.defenseRating += additionalDefense;
}
```
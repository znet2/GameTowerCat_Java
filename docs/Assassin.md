# Assassin.java - กับดักนักฆ่า

## ภาพรวม
Assassin เป็นหน่วยป้องกันแบบกับดักที่โจมตีศัตรูที่เดินผ่าน เป็นกับดักซ่อนที่ทำความเสียหายสูงเมื่อศัตรูเข้ามาในระยะ ไม่สามารถถูกโจมตีโดยศัตรูและโจมตีเฉพาะเมื่อศัตรูอยู่ในระยะเท่านั้น

## คลาสและการสืบทอด
```java
public class Assassin extends GameObject
```
- สืบทอดจาก `GameObject` สำหรับคุณสมบัติพื้นฐาน
- ไม่ implement Defensive interface (ไม่สามารถถูกโจมตีได้)

## ตัวแปรสำคัญ

### ค่าคงที่
- `static final int Y_OFFSET = -30` - offset การแสดงผลในแนวตั้ง
- `static final int ATTACK_COOLDOWN = 30` - cooldown การโจมตี (0.5 วินาที)
- `static final int ATTACK_ANIMATION_DURATION = 15` - ระยะเวลาแอนิเมชัน (0.25 วินาที)

### การอ้างอิงและการจัดการ
- `ArrayList<Enemy> enemyList` - รายการศัตรูสำหรับการตรวจสอบ
- `ArrayList<Enemy> attackedEnemies` - รายการศัตรูที่โจมตีไปแล้ว
- `int attackCooldown` - ตัวจับเวลา cooldown
- `boolean isAttacking` - สถานะการโจมตี
- `int attackAnimationTimer` - ตัวจับเวลาแอนิเมชัน

### รูปภาพ
- `Image normalImage` - รูปภาพปกติ
- `Image attackImage` - รูปภาพเมื่อโจมตี

## Methods อย่างละเอียด

### Constructor
```java
public Assassin(int gridColumn, int gridRow, int tileSize, Image assassinImage, ArrayList<Enemy> enemies)
```
**วัตถุประสงค์**: สร้าง Assassin ที่ตำแหน่งกริดที่กำหนด
**พารามิเตอร์**:
- `gridColumn, gridRow` - ตำแหน่งในกริด
- `tileSize` - ขนาดไทล์
- `assassinImage` - รูปภาพ Assassin
- `enemies` - รายการศัตรูสำหรับการตรวจสอบ

**การทำงาน**:
1. เรียก constructor ของ GameObject ด้วยขนาด 2x2 ไทล์
2. เก็บอ้างอิงรายการศัตรูและรูปภาพปกติ
3. โหลดรูปภาพโจมตี (assasinAttack.png)
4. หากโหลดไม่สำเร็จ ใช้รูปปกติแทน

### update()
```java
public void update()
```
**วัตถุประสงค์**: อัปเดต Assassin ทุก frame
**การทำงาน**:
1. **อัปเดตแอนิเมชันการโจมตี**:
   - เพิ่ม attackAnimationTimer หากกำลังโจมตี
   - เปลี่ยนกลับเป็นรูปปกติเมื่อแอนิเมชันจบ
2. **ลด Attack Cooldown**: ลด attackCooldown หากมากกว่า 0
3. **ตรวจสอบศัตรูในระยะ**: วนลูปผ่านศัตรูทั้งหมด
4. **โจมตีศัตรู**: โจมตีศัตรูที่อยู่ในระยะและยังไม่เคยโจมตี
5. **ทำความสะอาด**: ลบศัตรูที่ตายแล้วออกจาก attackedEnemies

### calculateDistanceToEnemy(Enemy enemy)
```java
private double calculateDistanceToEnemy(Enemy enemy)
```
**วัตถุประสงค์**: คำนวณระยะทางไปยังศัตรู
**การทำงาน**:
1. หาจุดกึ่งกลางของ Assassin
2. หาจุดกึ่งกลางของศัตรู
3. ใช้ MathUtils.calculateDistance() คำนวณระยะทาง
4. return ระยะทางเป็นพิกเซล

### attackEnemy(Enemy enemy)
```java
private void attackEnemy(Enemy enemy)
```
**วัตถุประสงค์**: โจมตีศัตรู
**การทำงาน**:
1. **เปลี่ยนรูปภาพ**: เปลี่ยนเป็น attackImage
2. **ตั้งสถานะ**: ตั้ง isAttacking = true และรีเซ็ต timer
3. **ทำความเสียหาย**: เรียก enemy.takeDamage() ด้วยความเสียหาย 30
4. **แสดงข้อความ**: พิมพ์ข้อความการโจมตีใน console

### getGridColumn(int tileSize) / getGridRow(int tileSize)
```java
public int getGridColumn(int tileSize)
public int getGridRow(int tileSize)
```
**วัตถุประสงค์**: แปลงตำแหน่งพิกเซลเป็นกริด
**การทำงาน**: หารตำแหน่งด้วยขนาดไทล์
**การใช้งาน**: ใช้สำหรับการตรวจสอบตำแหน่งและการวางหน่วย

### draw(Graphics graphics)
```java
@Override
public void draw(Graphics graphics)
```
**วัตถุประสงค์**: วาด Assassin บนหน้าจอ
**การทำงาน**:
1. วาดรูป Assassin ที่ตำแหน่งปัจจุบัน + Y_OFFSET
2. ไม่มีแถบเลือด (เพราะไม่สามารถถูกโจมตีได้)

## ระบบการทำงานของ Assassin

### การตรวจสอบศัตรู
```java
for (Enemy enemy : enemyList) {
    if (enemy.isDead()) {
        continue; // ข้ามศัตรูที่ตายแล้ว
    }

    double distance = calculateDistanceToEnemy(enemy);
    if (distance <= Constants.Entities.ASSASSIN_ATTACK_RANGE) {
        // ศัตรูอยู่ในระยะโจมตี (80 พิกเซล)
        if (!attackedEnemies.contains(enemy) && attackCooldown == 0) {
            attackEnemy(enemy);
            attackedEnemies.add(enemy);
            attackCooldown = ATTACK_COOLDOWN;
        }
    } else {
        // ศัตรูออกจากระยะ - ลบออกจากรายการที่โจมตีแล้ว
        attackedEnemies.remove(enemy);
    }
}
```

### ระบบ Cooldown
- **Attack Cooldown**: 30 frames (0.5 วินาที)
- **ป้องกันการโจมตีซ้ำ**: ไม่สามารถโจมตีศัตรูตัวเดิมติดต่อกันได้
- **รีเซ็ตเมื่อศัตรูออกจากระยะ**: ศัตรูสามารถถูกโจมตีอีกครั้งหากกลับเข้ามาในระยะ

### ระบบแอนิเมชัน
- **รูปปกติ**: assasin.png
- **รูปโจมตี**: assasinAttack.png (15 frames = 0.25 วินาที)
- **การเปลี่ยน**: เปลี่ยนเป็นรูปโจมตีเมื่อโจมตี แล้วเปลี่ยนกลับ

## คุณสมบัติเฉพาะของ Assassin

### กับดักซ่อน
- **ไม่สามารถถูกโจมตี**: ศัตรูไม่สามารถโจมตี Assassin ได้
- **ไม่มีเลือด**: ไม่มีระบบเลือดหรือการทำลาย
- **การป้องกันแบบ Passive**: ทำงานอัตโนมัติเมื่อศัตรูเข้ามาในระยะ

### ระยะโจมตีสั้น
- **ระยะโจมตี**: 80 พิกเซล (สั้นที่สุดในบรรดาหน่วยป้องกัน)
- **การโจมตี Melee**: ต้องให้ศัตรูเข้ามาใกล้มาก
- **เหมาะสำหรับจุดคอขวด**: วางในจุดที่ศัตรูต้องผ่าน

### ความเสียหายปานกลาง
- **ความเสียหาย**: 30 จุด
- **มากกว่า Magic**: มากกว่าการโจมตีปกติของ Magic (5)
- **น้อยกว่า Archer**: น้อยกว่า Archer มาก (100,000)

### ระบบการโจมตีแบบ One-Time
- **โจมตีครั้งเดียวต่อศัตรู**: ไม่โจมตีศัตรูตัวเดิมซ้ำ
- **รีเซ็ตเมื่อออกจากระยะ**: ศัตรูสามารถถูกโจมตีอีกครั้งหากกลับมา
- **เหมาะสำหรับศัตรูที่เดินผ่าน**: ไม่เหมาะสำหรับศัตรูที่หยุดนิ่ง

## การใช้งานในเกม

### การวาง Assassin
```java
// ใน GamePanel.java
private void attemptAssassinPlacement(MouseEvent event) {
    int gridColumn = MathUtils.pixelToGrid(event.getX(), gameMap.getTileSize());
    int gridRow = MathUtils.pixelToGrid(event.getY(), gameMap.getTileSize());

    if (isValidPlacementPosition(gridColumn, gridRow) && 
        coinManager.spendCoins(Constants.Entities.ASSASSIN_COST)) {
        gameMap.placeAssassin(gridColumn, gridRow, activeEnemies);
    }
}
```

### การอัปเดต Assassin
```java
// ใน Map.java
public void updateAssassins() {
    for (Assassin assassin : assassins) {
        assassin.update();
    }
}
```

### การตรวจสอบตำแหน่ง
```java
// ใน Map.java
public boolean hasAssassinAt(int gridColumn, int gridRow) {
    for (Assassin assassin : assassins) {
        if (assassin.getGridColumn(Constants.Map.TILE_SIZE) == gridColumn &&
            assassin.getGridRow(Constants.Map.TILE_SIZE) == gridRow) {
            return true;
        }
    }
    return false;
}
```

## กลยุทธ์การใช้งาน

### จุดคอขวด (Chokepoints)
- วางในจุดที่ศัตรูต้องผ่านทุกตัว
- ใช้ประโยชน์จากการโจมตีครั้งเดียวต่อศัตรู
- เหมาะสำหรับเส้นทางแคบ

### การสนับสนุนหน่วยอื่น
- ใช้ร่วมกับ Tank เพื่อชะลอศัตรู
- ให้ Assassin โจมตีก่อน แล้วให้หน่วยอื่นจัดการต่อ
- เหมาะสำหรับการลดเลือดศัตรูก่อนเข้าสู่การต่อสู้หลัก

### การวางตำแหน่ง
- วางใกล้เส้นทางศัตรู แต่ไม่บนเส้นทาง
- หลีกเลี่ยงการวางในจุดที่ศัตรูอาจหยุดนิ่ง
- พิจารณาระยะโจมตี 80 พิกเซลในการวางตำแหน่ง

## ความสัมพันธ์กับคลาสอื่น

### Enemy.java
- **เป้าหมาย**: Assassin โจมตี Enemy เมื่ออยู่ในระยะ
- **ไม่ถูกโจมตี**: Enemy ไม่สามารถโจมตี Assassin ได้
- **การตรวจสอบระยะ**: ใช้ Enemy.getBounds() เพื่อคำนวณระยะทาง

### Map.java
- **การจัดการ**: Map เก็บและอัปเดต Assassin ทั้งหมด
- **การวาง**: Map มี method สำหรับวาง Assassin
- **การตรวจสอบ**: Map ตรวจสอบการซ้อนทับของ Assassin

### GamePanel.java
- **การซื้อ**: ผู้เล่นสามารถซื้อและวาง Assassin ได้
- **ราคา**: 20 เหรียญ (แพงกว่า Magic และ Archer)
- **UI**: แสดงไอคอน Assassin สีม่วง

### Constants.java
- **ราคา**: ASSASSIN_COST = 20
- **ความเสียหาย**: ASSASSIN_ATTACK_DAMAGE = 30
- **ระยะโจมตี**: ASSASSIN_ATTACK_RANGE = 80
- **รูปภาพ**: ASSASSIN_IMAGE, ASSASSIN_ATTACK_IMAGE

### MathUtils.java
- **การคำนวณระยะทาง**: ใช้ calculateDistance() เพื่อหาระยะทางไปยังศัตรู

## จุดเด่นของการออกแบบ

### ความเป็นเอกลักษณ์
- แตกต่างจากหน่วยป้องกันอื่นที่สามารถถูกโจมตีได้
- มีกลไกการโจมตีแบบ one-time ที่ไม่เหมือนใคร

### ความสมดุล
- ระยะโจมตีสั้น ชดเชยด้วยการไม่สามารถถูกโจมตี
- ความเสียหายปานกลาง เหมาะสำหรับบทบาทสนับสนุน

### การใช้งานที่หลากหลาย
- เหมาะสำหรับกลยุทธ์หลายแบบ
- สามารถใช้เป็นหน่วยหลักหรือสนับสนุนได้

### ความเรียบง่าย
- ไม่มีระบบเลือดหรือการทำลาย
- ง่ายต่อการเข้าใจและใช้งาน

## การปรับปรุงที่เป็นไปได้

### เพิ่มระบบ Stealth
```java
private boolean isVisible = false;
private int stealthCooldown = 0;

public void activateStealth() {
    isVisible = false;
    stealthCooldown = 180; // 3 วินาที
}
```

### เพิ่มการโจมตีหลายครั้ง
```java
private int attacksPerEnemy = 1;
private Map<Enemy, Integer> enemyAttackCount = new HashMap<>();

private boolean canAttackEnemy(Enemy enemy) {
    return enemyAttackCount.getOrDefault(enemy, 0) < attacksPerEnemy;
}
```

### เพิ่มเอฟเฟกต์พิเศษ
```java
private void attackEnemy(Enemy enemy) {
    enemy.takeDamage(Constants.Entities.ASSASSIN_ATTACK_DAMAGE);
    enemy.applyPoison(10, 60); // พิษ 10 ความเสียหาย เป็นเวลา 1 วินาที
}
```
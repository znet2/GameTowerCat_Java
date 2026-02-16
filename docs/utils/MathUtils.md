# MathUtils.java - คลาสยูทิลิตี้สำหรับคณิตศาสตร์

## ภาพรวม
MathUtils เป็น utility class ที่รวบรวม methods ทางคณิตศาสตร์ที่ใช้บ่อยในเกม เช่น การคำนวณระยะทาง และการแปลงพิกัดระหว่างกริดและพิกเซล

## คลาส
```java
public class MathUtils
```
- Utility class ที่ไม่สามารถสร้าง instance ได้ (private constructor)
- ทุก method เป็น static สามารถเรียกใช้ได้โดยตรง

## Methods

### calculateDistance(double x1, double y1, double x2, double y2)
```java
public static double calculateDistance(double x1, double y1, double x2, double y2)
```
**วัตถุประสงค์**: คำนวณระยะทางแบบ Euclidean ระหว่างสองจุด

**พารามิเตอร์**:
- `x1, y1` - พิกัดของจุดแรก
- `x2, y2` - พิกัดของจุดที่สอง

**การทำงาน**:
1. คำนวณ deltaX = x2 - x1
2. คำนวณ deltaY = y2 - y1
3. return √(deltaX² + deltaY²)

**การใช้งาน**:
- Magic tower: คำนวณระยะทางไปยังศัตรูเพื่อตรวจสอบว่าอยู่ในระยะโจมตีหรือไม่
- Archer tower: คำนวณระยะทางไปยังศัตรู
- Assassin: คำนวณระยะทางไปยังศัตรูเพื่อโจมตี

**ตัวอย่าง**:
```java
// คำนวณระยะทางจาก Magic tower ไปยังศัตรู
int magicCenterX = positionX + objectWidth / 2;
int magicCenterY = positionY + objectHeight / 2;
int enemyCenterX = enemyBounds.x + enemyBounds.width / 2;
int enemyCenterY = enemyBounds.y + enemyBounds.height / 2;

double distance = MathUtils.calculateDistance(
    magicCenterX, magicCenterY, 
    enemyCenterX, enemyCenterY
);

if (distance <= Constants.Entities.MAGIC_ATTACK_RANGE) {
    // ศัตรูอยู่ในระยะโจมตี
}
```

### pixelToGrid(int pixelPosition, int tileSize)
```java
public static int pixelToGrid(int pixelPosition, int tileSize)
```
**วัตถุประสงค์**: แปลงพิกัดพิกเซลเป็นตำแหน่งกริด

**พารามิเตอร์**:
- `pixelPosition` - ตำแหน่งเป็นพิกเซล
- `tileSize` - ขนาดของแต่ละไทล์เป็นพิกเซล

**การทำงาน**: return pixelPosition / tileSize

**การใช้งาน**:
- GamePanel: แปลงตำแหน่งเมาส์เป็นตำแหน่งกริดเพื่อวางหน่วย
- ใช้ในการตรวจสอบว่าผู้เล่นคลิกที่ไทล์ไหน

**ตัวอย่าง**:
```java
// แปลงตำแหน่งเมาส์เป็นกริด
int gridColumn = MathUtils.pixelToGrid(event.getX(), gameMap.getTileSize());
int gridRow = MathUtils.pixelToGrid(event.getY(), gameMap.getTileSize());

// ตรวจสอบว่าสามารถวางหน่วยได้หรือไม่
if (isValidPlacementPosition(gridColumn, gridRow)) {
    gameMap.placeTank(gridColumn, gridRow);
}
```

### isWithinBounds(int x, int y, int width, int height)
```java
public static boolean isWithinBounds(int x, int y, int width, int height)
```
**วัตถุประสงค์**: ตรวจสอบว่าจุดอยู่ภายในขอบเขตหรือไม่

**พารามิเตอร์**:
- `x, y` - พิกัดที่ต้องการตรวจสอบ
- `width` - ความกว้างของขอบเขต
- `height` - ความสูงของขอบเขต

**การทำงาน**: return true หาก x >= 0 && y >= 0 && x < width && y < height

**การใช้งาน**:
- GamePanel: ตรวจสอบว่าตำแหน่งกริดอยู่ภายในแผนที่หรือไม่

**ตัวอย่าง**:
```java
// ตรวจสอบว่าตำแหน่งอยู่ในแผนที่หรือไม่
int[][] mapGrid = gameMap.getRawMap();
boolean isInBounds = MathUtils.isWithinBounds(
    gridColumn, gridRow, 
    mapGrid[0].length, mapGrid.length
);

if (!isInBounds) {
    // ตำแหน่งอยู่นอกแผนที่
    return false;
}
```

## การใช้งานในเกม

### Magic.java
```java
private double calculateDistanceToTarget(Enemy enemy) {
    int magicCenterX = positionX + objectWidth / 2;
    int magicCenterY = positionY + objectHeight / 2;
    Rectangle enemyBounds = getEnemyBounds(enemy);
    int enemyCenterX = enemyBounds.x + enemyBounds.width / 2;
    int enemyCenterY = enemyBounds.y + enemyBounds.height / 2;
    
    return MathUtils.calculateDistance(magicCenterX, magicCenterY, 
                                      enemyCenterX, enemyCenterY);
}
```

### Archer.java
```java
private double calculateDistanceToTarget(Enemy enemy) {
    int archerCenterX = positionX + objectWidth / 2;
    int archerCenterY = positionY + objectHeight / 2;
    Rectangle enemyBounds = enemy.getBounds();
    int enemyCenterX = enemyBounds.x + enemyBounds.width / 2;
    int enemyCenterY = enemyBounds.y + enemyBounds.height / 2;
    
    return MathUtils.calculateDistance(archerCenterX, archerCenterY, 
                                      enemyCenterX, enemyCenterY);
}
```

### Assassin.java
```java
private double calculateDistanceToEnemy(Enemy enemy) {
    int assassinCenterX = positionX + objectWidth / 2;
    int assassinCenterY = positionY + objectHeight / 2;
    Rectangle enemyBounds = enemy.getBounds();
    int enemyCenterX = enemyBounds.x + enemyBounds.width / 2;
    int enemyCenterY = enemyBounds.y + enemyBounds.height / 2;
    
    return MathUtils.calculateDistance(assassinCenterX, assassinCenterY, 
                                      enemyCenterX, enemyCenterY);
}
```

### GamePanel.java
```java
private void attemptTankPlacement(MouseEvent event) {
    int gridColumn = MathUtils.pixelToGrid(event.getX(), gameMap.getTileSize());
    int gridRow = MathUtils.pixelToGrid(event.getY(), gameMap.getTileSize());
    
    if (isValidPlacementPosition(gridColumn, gridRow)) {
        gameMap.placeTank(gridColumn, gridRow);
    }
}

private boolean isWithinMapBounds(int gridColumn, int gridRow) {
    int[][] mapGrid = gameMap.getRawMap();
    return MathUtils.isWithinBounds(gridColumn, gridRow, 
                                    mapGrid[0].length, mapGrid.length);
}
```

## จุดเด่นของการออกแบบ

### Utility Class Pattern
- ใช้ private constructor ป้องกันการสร้าง instance
- ทุก method เป็น static
- ไม่มี state (stateless)

### Single Responsibility
- แต่ละ method ทำหน้าที่เดียวชัดเจน
- ง่ายต่อการทดสอบและบำรุงรักษา

### Reusability
- สามารถใช้ซ้ำได้ทั่วทั้งโปรเจค
- ลดการเขียนโค้ดซ้ำ

## ความสัมพันธ์กับคลาสอื่น

### Defensive Units (Magic, Archer, Assassin)
- ใช้ `calculateDistance()` สำหรับการคำนวณระยะทางไปยังศัตรู
- ตรวจสอบว่าศัตรูอยู่ในระยะโจมตีหรือไม่

### GamePanel
- ใช้ `pixelToGrid()` สำหรับแปลงตำแหน่งเมาส์เป็นกริด
- ใช้ `isWithinBounds()` สำหรับตรวจสอบตำแหน่งที่ถูกต้อง

### Constants
- ใช้ค่าคงที่จาก Constants สำหรับ tile size และระยะโจมตี

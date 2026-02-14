# MathUtils.java - คลาสยูทิลิตี้สำหรับการคำนวณทางคณิตศาสตร์

## ภาพรวม
MathUtils เป็นคลาสยูทิลิตี้ที่รวบรวม method สำหรับการคำนวณทางคณิตศาสตร์ที่ใช้บ่อยในเกม เช่น การคำนวณระยะทาง การจัดการ vector และการแปลงพิกัด

## คลาสและการสืบทอด
```java
public class MathUtils
```
- คลาสยูทิลิตี้ที่มี static methods เท่านั้น
- `private constructor` - ป้องกันการสร้าง instance

## Methods อย่างละเอียด

### calculateDistance(Point point1, Point point2)
```java
public static double calculateDistance(Point point1, Point point2)
```
**วัตถุประสงค์**: คำนวณระยะทาง Euclidean ระหว่างสอง Point
**พารามิเตอร์**:
- `point1` - จุดแรก
- `point2` - จุดที่สอง
**การทำงาน**:
1. คำนวณ deltaX = point2.x - point1.x
2. คำนวณ deltaY = point2.y - point1.y
3. ใช้ Pythagorean theorem: √(deltaX² + deltaY²)
4. return ระยะทางเป็น double

**ตัวอย่างการใช้งาน**:
```java
Point start = new Point(0, 0);
Point end = new Point(3, 4);
double distance = MathUtils.calculateDistance(start, end); // 5.0
```

### calculateDistance(double x1, double y1, double x2, double y2)
```java
public static double calculateDistance(double x1, double y1, double x2, double y2)
```
**วัตถุประสงค์**: คำนวณระยะทางระหว่างสองจุดพิกัด
**พารามิเตอร์**:
- `x1, y1` - พิกัดของจุดแรก
- `x2, y2` - พิกัดของจุดที่สอง
**การทำงาน**:
1. คำนวณ deltaX = x2 - x1
2. คำนวณ deltaY = y2 - y1
3. ใช้ Pythagorean theorem: √(deltaX² + deltaY²)
4. return ระยะทางเป็น double

**ตัวอย่างการใช้งาน**:
```java
double distance = MathUtils.calculateDistance(0, 0, 3, 4); // 5.0

// ใช้ในการคำนวณระยะทางระหว่างหน่วยและศัตรู
double distanceToEnemy = MathUtils.calculateDistance(
    towerX, towerY, 
    enemyX, enemyY
);
```

### normalizeVector(double deltaX, double deltaY)
```java
public static Point normalizeVector(double deltaX, double deltaY)
```
**วัตถุประสงค์**: ทำให้ vector มีความยาวเป็น 1 (unit vector)
**พารามิเตอร์**:
- `deltaX` - component X ของ vector
- `deltaY` - component Y ของ vector
**การทำงาน**:
1. คำนวณความยาว vector: √(deltaX² + deltaY²)
2. หากความยาวเป็น 0 return Point(0, 0)
3. หารแต่ละ component ด้วยความยาว
4. return Point ที่มี normalized components

**ตัวอย่างการใช้งาน**:
```java
Point normalized = MathUtils.normalizeVector(3, 4);
// Result: Point(0.6, 0.8) - vector หน่วยที่ชี้ไปทิศทางเดียวกัน
```

**การใช้งานในเกม**:
```java
// หาทิศทางการเคลื่อนที่
double dx = targetX - currentX;
double dy = targetY - currentY;
Point direction = MathUtils.normalizeVector(dx, dy);

// เคลื่อนที่ด้วยความเร็วคงที่
currentX += direction.x * speed;
currentY += direction.y * speed;
```

### clamp(int value, int min, int max)
```java
public static int clamp(int value, int min, int max)
```
**วัตถุประสงค์**: จำกัดค่าให้อยู่ในช่วงที่กำหนด
**พารามิเตอร์**:
- `value` - ค่าที่ต้องการจำกัด
- `min` - ค่าต่ำสุดที่อนุญาต
- `max` - ค่าสูงสุดที่อนุญาต
**การทำงาน**:
1. ใช้ Math.max(min, value) เพื่อให้ไม่ต่ำกว่า min
2. ใช้ Math.min(max, result) เพื่อให้ไม่สูงกว่า max
3. return ค่าที่ถูกจำกัด

**ตัวอย่างการใช้งาน**:
```java
int health = MathUtils.clamp(currentHealth, 0, maxHealth);
int volume = MathUtils.clamp(userVolume, 0, 100);
```

### clamp(double value, double min, double max)
```java
public static double clamp(double value, double min, double max)
```
**วัตถุประสงค์**: จำกัดค่า double ให้อยู่ในช่วงที่กำหนด
**การทำงาน**: เหมือน clamp(int) แต่ใช้กับ double

**ตัวอย่างการใช้งาน**:
```java
double speed = MathUtils.clamp(calculatedSpeed, 0.0, maxSpeed);
double angle = MathUtils.clamp(rotation, 0.0, 2 * Math.PI);
```

### gridToPixel(int gridPosition, int tileSize)
```java
public static int gridToPixel(int gridPosition, int tileSize)
```
**วัตถุประสงค์**: แปลงตำแหน่งกริดเป็นพิกัดพิกเซล
**พารามิเตอร์**:
- `gridPosition` - ตำแหน่งในหน่วยกริด
- `tileSize` - ขนาดของแต่ละไทล์เป็นพิกเซล
**การทำงาน**: return gridPosition * tileSize

**ตัวอย่างการใช้งาน**:
```java
int pixelX = MathUtils.gridToPixel(5, 32); // 160 pixels
int pixelY = MathUtils.gridToPixel(3, 32); // 96 pixels

// ใช้ในการวางวัตถุบนแผนที่
int objectX = MathUtils.gridToPixel(gridColumn, Constants.Map.TILE_SIZE);
int objectY = MathUtils.gridToPixel(gridRow, Constants.Map.TILE_SIZE);
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

**ตัวอย่างการใช้งาน**:
```java
int gridX = MathUtils.pixelToGrid(160, 32); // 5
int gridY = MathUtils.pixelToGrid(96, 32);  // 3

// ใช้ในการแปลงตำแหน่งเมาส์เป็นกริด
int gridColumn = MathUtils.pixelToGrid(mouseX, gameMap.getTileSize());
int gridRow = MathUtils.pixelToGrid(mouseY, gameMap.getTileSize());
```

### isWithinBounds(int x, int y, int width, int height)
```java
public static boolean isWithinBounds(int x, int y, int width, int height)
```
**วัตถุประสงค์**: ตรวจสอบว่าจุดอยู่ในขอบเขตหรือไม่
**พารามิเตอร์**:
- `x, y` - พิกัดที่ต้องการตรวจสอบ
- `width, height` - ขนาดของขอบเขต
**การทำงาน**:
1. ตรวจสอบ x >= 0 && y >= 0
2. ตรวจสอบ x < width && y < height
3. return true หากทั้งสองเงื่อนไขเป็นจริง

**ตัวอย่างการใช้งาน**:
```java
boolean isValid = MathUtils.isWithinBounds(gridX, gridY, mapWidth, mapHeight);

// ใช้ในการตรวจสอบการวางหน่วย
if (MathUtils.isWithinBounds(gridColumn, gridRow, mapColumns, mapRows)) {
    // สามารถวางได้
}
```

## การใช้งานในเกม

### การคำนวณระยะทางสำหรับการโจมตี
```java
// ใน Magic.java
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

### การแปลงตำแหน่งเมาส์เป็นกริด
```java
// ใน GamePanel.java
private void attemptTankPlacement(MouseEvent event) {
    int gridColumn = MathUtils.pixelToGrid(event.getX(), gameMap.getTileSize());
    int gridRow = MathUtils.pixelToGrid(event.getY(), gameMap.getTileSize());

    if (isValidPlacementPosition(gridColumn, gridRow)) {
        gameMap.placeTank(gridColumn, gridRow);
    }
}
```

### การตรวจสอบขอบเขตแผนที่
```java
// ใน Enemy.java (สำหรับ pathfinding)
private boolean isWithinMapBounds(int gridColumn, int gridRow) {
    int[][] mapGrid = gameMap.getRawMap();
    return MathUtils.isWithinBounds(gridColumn, gridRow, 
                                   mapGrid[0].length, mapGrid.length);
}
```

### การจำกัดค่าเลือด
```java
// ใน Tank.java
public void takeDamage(int damageAmount) {
    currentHealth -= damageAmount;
    currentHealth = MathUtils.clamp(currentHealth, 0, maxHealth);
}
```

## ข้อดีของการใช้ MathUtils

### การรวมศูนย์
- รวบรวมฟังก์ชันคณิตศาสตร์ไว้ที่เดียว
- ง่ายต่อการหาและใช้งาน
- ลดการเขียนโค้ดซ้ำ

### ความถูกต้อง
- ใช้สูตรมาตรฐานที่ถูกต้อง
- จัดการ edge cases (เช่น vector ความยาว 0)
- ป้องกันข้อผิดพลาดทางคณิตศาสตร์

### ประสิทธิภาพ
- ใช้ static methods ไม่ต้องสร้าง instance
- การคำนวณที่เรียบง่ายและรวดเร็ว
- ไม่มี overhead ของ object creation

### ความยืดหยุ่น
- รองรับทั้ง int และ double
- มีหลายรูปแบบของ method เดียวกัน (overloading)
- ใช้งานได้หลากหลาย

## ความสัมพันธ์กับคลาสอื่น

### Magic.java / Archer.java
- ใช้ calculateDistance() เพื่อหาระยะทางไปยังศัตรู
- ตรวจสอบว่าศัตรูอยู่ในระยะโจมตีหรือไม่

### GamePanel.java
- ใช้ pixelToGrid() เพื่อแปลงตำแหน่งเมาส์
- ใช้ isWithinBounds() เพื่อตรวจสอบการวางหน่วย

### Enemy.java
- ใช้ isWithinBounds() ใน pathfinding
- ใช้ calculateDistance() เพื่อคำนวณการเคลื่อนที่

### Projectiles (Arrow, MagicBall)
- ใช้ calculateDistance() เพื่อตรวจสอบการชนเป้าหมาย
- ใช้ normalizeVector() สำหรับการเคลื่อนที่ (ถ้ามี)

### GameObject.java
- ใช้ gridToPixel() ใน constructor เพื่อแปลงตำแหน่ง

## จุดเด่นของการออกแบบ

### Utility Class Pattern
- ใช้ static methods เท่านั้น
- ป้องกันการสร้าง instance ด้วย private constructor
- เป็น pure functions ไม่มี side effects

### Method Overloading
- มีหลายรูปแบบของ method เดียวกัน
- รองรับทั้ง Point objects และ primitive values
- ใช้งานได้สะดวกในสถานการณ์ต่างๆ

### Clear Naming
- ชื่อ method อธิบายการทำงานได้ชัดเจน
- พารามิเตอร์มีชื่อที่เข้าใจง่าย
- ไม่ต้องดู implementation ก็รู้ว่าทำอะไร

### Mathematical Accuracy
- ใช้สูตรคณิตศาสตร์ที่ถูกต้อง
- จัดการ edge cases อย่างเหมาะสม
- ให้ผลลัพธ์ที่แม่นยำ

## การปรับปรุงที่เป็นไปได้

### เพิ่ม Vector Operations
```java
public static Point addVectors(Point v1, Point v2) {
    return new Point(v1.x + v2.x, v1.y + v2.y);
}

public static Point multiplyVector(Point vector, double scalar) {
    return new Point((int)(vector.x * scalar), (int)(vector.y * scalar));
}

public static double dotProduct(Point v1, Point v2) {
    return v1.x * v2.x + v1.y * v2.y;
}
```

### เพิ่ม Angle Calculations
```java
public static double calculateAngle(double x1, double y1, double x2, double y2) {
    return Math.atan2(y2 - y1, x2 - x1);
}

public static Point rotatePoint(Point point, double angle) {
    double cos = Math.cos(angle);
    double sin = Math.sin(angle);
    return new Point(
        (int)(point.x * cos - point.y * sin),
        (int)(point.x * sin + point.y * cos)
    );
}
```

### เพิ่ม Interpolation
```java
public static double lerp(double start, double end, double t) {
    return start + t * (end - start);
}

public static Point lerpPoint(Point start, Point end, double t) {
    return new Point(
        (int)lerp(start.x, end.x, t),
        (int)lerp(start.y, end.y, t)
    );
}
```
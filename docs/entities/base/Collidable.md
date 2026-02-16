# Collidable.java - Interface สำหรับการตรวจสอบการชน

## ภาพรวม
Collidable เป็น interface ที่กำหนดพฤติกรรมสำหรับวัตถุที่สามารถมีส่วนร่วมในการตรวจสอบการชนได้ ให้ collision bounds สำหรับการโต้ตอบกับวัตถุอื่นในเกม

## ประเภท Interface
```java
public interface Collidable
```
- Interface สำหรับวัตถุที่สามารถตรวจสอบการชนได้
- ใช้ Rectangle สำหรับกำหนดพื้นที่การชน

## Methods ที่ต้อง Implement

### getCollisionBounds()
```java
Rectangle getCollisionBounds()
```
**วัตถุประสงค์**: ดึง collision bounds ของวัตถุ
**การทำงาน**: return Rectangle ที่แสดงพื้นที่การชนของวัตถุ
**การใช้งาน**: ใช้สำหรับการตรวจสอบการชนกับวัตถุอื่น

**ตัวอย่างการ implement**:
```java
@Override
public Rectangle getCollisionBounds() {
    return new Rectangle(positionX, positionY, objectWidth, objectHeight);
}
```

## Default Methods

### collidesWith(Collidable other)
```java
default boolean collidesWith(Collidable other) {
    return getCollisionBounds().intersects(other.getCollisionBounds());
}
```
**วัตถุประสงค์**: ตรวจสอบว่าวัตถุนี้ชนกับวัตถุอื่นหรือไม่
**พารามิเตอร์**: `other` - วัตถุอื่นที่ต้องการตรวจสอบการชน
**การทำงาน**:
1. ดึง collision bounds ของวัตถุนี้
2. ดึง collision bounds ของวัตถุอื่น
3. ใช้ `Rectangle.intersects()` ตรวจสอบการทับซ้อน
4. return true หากมีการชน

**ข้อดีของ Default Method**:
- ไม่บังคับให้ทุกคลาสต้อง implement
- ใช้ logic เดียวกันสำหรับการตรวจสอบการชน
- สามารถ override ได้หากต้องการพฤติกรรมพิเศษ

## การใช้งาน Interface

### การ Implement พื้นฐาน
```java
public class Tank extends GameObject implements Defensive, Collidable {
    private static final int Y_OFFSET = -30;
    
    @Override
    public Rectangle getCollisionBounds() {
        return new Rectangle(positionX, positionY + Y_OFFSET, objectWidth, objectHeight);
    }
}
```

### การใช้งานการตรวจสอบการชน
```java
public class Enemy {
    public boolean checkForTankCollision() {
        for (Tank tank : gameMap.getTanks()) {
            if (!tank.isDead() && getBounds().intersects(tank.getCollisionBounds())) {
                // พบการชน - เริ่มโจมตี
                startAttacking(tank);
                return true;
            }
        }
        return false;
    }
}
```

### การใช้ Default Method
```java
public void checkCollisions(List<Collidable> objects) {
    for (int i = 0; i < objects.size(); i++) {
        for (int j = i + 1; j < objects.size(); j++) {
            if (objects.get(i).collidesWith(objects.get(j))) {
                handleCollision(objects.get(i), objects.get(j));
            }
        }
    }
}
```

## คลาสที่ Implement Interface นี้

### Tank.java
```java
@Override
public Rectangle getCollisionBounds() {
    return new Rectangle(positionX, positionY + Y_OFFSET, objectWidth, objectHeight);
}
```
- **พิเศษ**: ใช้ Y_OFFSET เพื่อปรับตำแหน่งการชน
- **การใช้งาน**: ศัตรูตรวจสอบการชนเพื่อโจมตี

### Magic.java
```java
@Override
public Rectangle getCollisionBounds() {
    return new Rectangle(positionX, positionY + Y_OFFSET, objectWidth, objectHeight);
}
```
- **พิเศษ**: ใช้ Y_OFFSET เหมือน Tank
- **การใช้งาน**: ศัตรูสามารถโจมตี Magic tower ได้

### Archer.java
```java
@Override
public Rectangle getCollisionBounds() {
    return new Rectangle(positionX, positionY + Y_OFFSET, objectWidth, objectHeight);
}
```
- **พิเศษ**: ใช้ Y_OFFSET เหมือนหน่วยอื่น
- **การใช้งาน**: ศัตรูสามารถโจมตี Archer tower ได้

## ประเภทการชนในเกม

### Enemy vs Defensive Units
```java
// ใน Enemy.java
private boolean checkForTankCollision() {
    for (Tank tank : gameMap.getTanks()) {
        if (!tank.isDead() && getBounds().intersects(tank.getCollisionBounds())) {
            positionForAttack(tank);
            startAttacking(tank);
            return true;
        }
    }
    return false;
}
```

### Enemy vs House
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

### Projectile vs Enemy
```java
// ใน Arrow.java / MagicBall.java
public void update() {
    // ... movement code ...
    
    // Check collision with target
    Rectangle targetBounds = target.getBounds();
    if (getBounds().intersects(targetBounds)) {
        hitTarget();
    }
}
```

## การออกแบบ Collision Bounds

### พื้นฐาน - ใช้ขนาดเต็ม
```java
public Rectangle getCollisionBounds() {
    return new Rectangle(positionX, positionY, objectWidth, objectHeight);
}
```

### ปรับตำแหน่ง - ใช้ Offset
```java
public Rectangle getCollisionBounds() {
    return new Rectangle(positionX, positionY + Y_OFFSET, objectWidth, objectHeight);
}
```

### ปรับขนาด - ใช้ขนาดเล็กกว่า
```java
public Rectangle getCollisionBounds() {
    int margin = 5;
    return new Rectangle(
        positionX + margin, 
        positionY + margin, 
        objectWidth - 2 * margin, 
        objectHeight - 2 * margin
    );
}
```

### รูปร่างพิเศษ - ใช้วงกลม
```java
public Rectangle getCollisionBounds() {
    int radius = Math.min(objectWidth, objectHeight) / 2;
    int centerX = positionX + objectWidth / 2;
    int centerY = positionY + objectHeight / 2;
    return new Rectangle(
        centerX - radius, 
        centerY - radius, 
        radius * 2, 
        radius * 2
    );
}
```

## ข้อดีของการใช้ Interface

### ความยืดหยุ่น (Flexibility)
- วัตถุใดก็ตามสามารถมีการชนได้
- ไม่จำกัดเฉพาะคลาสที่สืบทอดจากคลาสเดียว
- รองรับ multiple inheritance ผ่าน interfaces

### การจัดการแบบ Polymorphism
```java
List<Collidable> collidableObjects = new ArrayList<>();
collidableObjects.add(tank);
collidableObjects.add(magic);
collidableObjects.add(archer);
collidableObjects.add(enemy);

// ตรวจสอบการชนทั้งหมดแบบเดียวกัน
for (Collidable obj1 : collidableObjects) {
    for (Collidable obj2 : collidableObjects) {
        if (obj1 != obj2 && obj1.collidesWith(obj2)) {
            handleCollision(obj1, obj2);
        }
    }
}
```

### การแยกความรับผิดชอบ
- Interface กำหนดสิ่งที่ต้องทำได้
- คลาสที่ implement จัดการรายละเอียด
- ระบบการชนไม่ต้องรู้ประเภทของวัตถุ

## การปรับปรุงและขยายระบบ

### เพิ่ม Collision Layers
```java
public interface Collidable {
    Rectangle getCollisionBounds();
    
    default int getCollisionLayer() {
        return 0; // Default layer
    }
    
    default boolean canCollideWith(Collidable other) {
        return getCollisionLayer() == other.getCollisionLayer();
    }
}
```

### เพิ่ม Collision Types
```java
public enum CollisionType {
    SOLID,      // บล็อคการเคลื่อนที่
    TRIGGER,    // เรียก event แต่ไม่บล็อค
    DAMAGE      // ทำความเสียหาย
}

public interface Collidable {
    Rectangle getCollisionBounds();
    CollisionType getCollisionType();
}
```

### เพิ่ม Collision Events
```java
public interface Collidable {
    Rectangle getCollisionBounds();
    
    default void onCollisionEnter(Collidable other) {
        // เรียกเมื่อเริ่มชน
    }
    
    default void onCollisionExit(Collidable other) {
        // เรียกเมื่อหยุดชน
    }
}
```

## ความสัมพันธ์กับระบบอื่น

### Enemy.java
- ใช้ Collidable เพื่อตรวจสอบการชนกับหน่วยป้องกัน
- เรียก `getCollisionBounds()` เพื่อดึงพื้นที่การชน

### Projectiles (Arrow, MagicBall)
- ใช้เพื่อตรวจสอบการชนกับเป้าหมาย
- ทำความเสียหายเมื่อชน

### Map.java
- อาจใช้สำหรับการตรวจสอบการวางหน่วย
- ป้องกันการวางซ้อนทับ

## จุดเด่นของการออกแบบ

### Simplicity
- Interface เรียบง่าย มีเพียง 1 method บังคับ
- Default method ช่วยลดการเขียนโค้ดซ้ำ

### Performance
- ใช้ Rectangle.intersects() ที่มีประสิทธิภาพ
- ไม่ซับซ้อนเกินความจำเป็น

### Extensibility
- เพิ่มประเภทการชนใหม่ได้ง่าย
- รองรับการปรับปรุงในอนาคต

### Type Safety
- รับประกันว่าวัตถุมีความสามารถในการตรวจสอบการชน
- ป้องกันข้อผิดพลาดในการเรียกใช้
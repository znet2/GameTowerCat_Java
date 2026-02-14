# GameObject.java - คลาสพื้นฐานสำหรับวัตถุในเกม

## ภาพรวม
GameObject เป็น abstract base class สำหรับวัตถุทั้งหมดที่สามารถวาดบนแผนที่ได้ ให้ฟังก์ชันพื้นฐานสำหรับตำแหน่ง ขนาด และการแสดงผล

## คลาสและการสืบทอด
```java
public abstract class GameObject
```
- `abstract class` - ไม่สามารถสร้าง instance ได้โดยตรง
- เป็นคลาสพื้นฐานสำหรับวัตถุทั้งหมดในเกม

## ตัวแปรสำคัญ

### ตัวแปรตำแหน่งและขนาด
- `protected int positionX, positionY` - ตำแหน่งวัตถุเป็นพิกเซล
- `protected int objectWidth, objectHeight` - ขนาดวัตถุเป็นพิกเซล
- `protected Image objectImage` - รูปภาพของวัตถุ

### การใช้ Protected
- ใช้ `protected` เพื่อให้คลาสลูกเข้าถึงได้
- ป้องกันการเข้าถึงจากภายนอกโดยตรง
- รักษาหลักการ encapsulation

## Methods อย่างละเอียด

### Constructor
```java
public GameObject(int gridColumn, int gridRow, int tileSize,
                  int widthInTiles, int heightInTiles,
                  Image image)
```
**วัตถุประสงค์**: สร้างวัตถุเกมด้วยการจัดตำแหน่งแบบกริด
**พารามิเตอร์**:
- `gridColumn` - ตำแหน่งคอลัมน์ในกริดไทล์
- `gridRow` - ตำแหน่งแถวในกริดไทล์
- `tileSize` - ขนาดแต่ละไทล์เป็นพิกเซล
- `widthInTiles` - ความกว้างของวัตถุเป็นจำนวนไทล์
- `heightInTiles` - ความสูงของวัตถุเป็นจำนวนไทล์
- `image` - รูปภาพที่แสดงวัตถุ

**การทำงาน**:
1. **แปลงกริดเป็นพิกเซล**: 
   - `positionX = gridColumn * tileSize`
   - `positionY = gridRow * tileSize`
2. **คำนวณขนาดวัตถุ**:
   - `objectWidth = widthInTiles * tileSize`
   - `objectHeight = heightInTiles * tileSize`
3. **เก็บรูปภาพ**: `objectImage = image`

### draw(Graphics graphics)
```java
public void draw(Graphics graphics)
```
**วัตถุประสงค์**: วาดวัตถุบนหน้าจอ
**พารามิเตอร์**: `graphics` - Graphics context สำหรับการวาด
**การทำงาน**:
1. ใช้ `graphics.drawImage()` วาดรูปภาพ
2. วาดที่ตำแหน่ง `(positionX, positionY)`
3. ใช้ขนาด `(objectWidth, objectHeight)`
4. ส่ง `null` เป็น ImageObserver

**หมายเหตุ**: Method นี้สามารถ override ได้ในคลาสลูกเพื่อการแสดงผลพิเศษ

## การใช้งานและตัวอย่าง

### การสร้างคลาสลูก
```java
public class Tank extends GameObject implements Defensive {
    public Tank(int gridColumn, int gridRow, int tileSize, Image tankImage) {
        // Tank ขนาด 2x2 ไทล์
        super(gridColumn, gridRow, tileSize, 2, 2, tankImage);
    }
    
    @Override
    public void draw(Graphics graphics) {
        // เรียกการวาดพื้นฐาน
        super.draw(graphics);
        
        // เพิ่มการวาดพิเศษ (เช่น แถบเลือด)
        drawHealthBar(graphics);
    }
}
```

### การใช้ตัวแปร Protected
```java
public class House extends GameObject {
    public House(int gridColumn, int gridRow, int tileSize, Image houseImage) {
        super(gridColumn, gridRow, tileSize, 7, 7, houseImage);
    }
    
    public Rectangle getBounds() {
        // เข้าถึงตัวแปร protected ได้
        return new Rectangle(positionX, positionY, objectWidth, objectHeight);
    }
}
```

## คลาสที่สืบทอดจาก GameObject

### Tank.java
- **ขนาด**: 2x2 ไทล์ (64x64 พิกเซล)
- **พิเศษ**: เปลี่ยนรูปภาพเมื่อถูกโจมตี
- **การวาด**: เพิ่มแถบเลือดและ Y offset

### Magic.java
- **ขนาด**: 2x2 ไทล์ (64x64 พิกเซล)
- **พิเศษ**: เปลี่ยนรูปเป็น bomb เมื่อใช้เวทมนตร์
- **การวาด**: วาด magic balls และแถบเลือด

### Archer.java
- **ขนาด**: 2x2 ไทล์ (64x64 พิกเซล)
- **พิเศษ**: เปลี่ยนรูปเมื่อโจมตี
- **การวาด**: วาดลูกธนุและแถบเลือด

### House.java
- **ขนาด**: 7x7 ไทล์ (224x224 พิกเซล)
- **พิเศษ**: วัตถุที่ใหญ่ที่สุด
- **การวาด**: วาดเฉพาะรูปภาพ

### Assassin.java
- **ขนาด**: 2x2 ไทล์ (64x64 พิกเซล)
- **พิเศษ**: เปลี่ยนรูปเมื่อโจมตี
- **การวาด**: ไม่มีแถบเลือด (ไม่สามารถถูกโจมตีได้)

## ข้อดีของการออกแบบ

### Code Reuse
- ลดการเขียนโค้ดซ้ำสำหรับตำแหน่งและการวาด
- ทุกวัตถุมีพฤติกรรมพื้นฐานเหมือนกัน

### Consistency
- ทุกวัตถุใช้ระบบกริดเดียวกัน
- การแปลงตำแหน่งทำแบบเดียวกัน

### Flexibility
- คลาสลูกสามารถ override draw() ได้
- เพิ่มพฤติกรรมพิเศษได้ง่าย

### Maintainability
- แก้ไขการทำงานพื้นฐานที่เดียว
- ง่ายต่อการเพิ่มฟีเจอร์ใหม่

## การจัดการตำแหน่งและขนาด

### ระบบกริด
```
Grid Position (2, 3) with tileSize 32:
positionX = 2 * 32 = 64 pixels
positionY = 3 * 32 = 96 pixels
```

### ขนาดวัตถุ
```
Object size 2x2 tiles with tileSize 32:
objectWidth = 2 * 32 = 64 pixels
objectHeight = 2 * 32 = 64 pixels
```

### การวาด
```
graphics.drawImage(image, 64, 96, 64, 64, null)
```

## ความสัมพันธ์กับระบบอื่น

### Map.java
- ใช้ GameObject สำหรับวัตถุบนแผนที่
- เรียก draw() เพื่อแสดงผลวัตถุทั้งหมด

### GamePanel.java
- ใช้ระบบกริดเดียวกันสำหรับการวางหน่วย
- แปลงตำแหน่งเมาส์เป็นกริด

### Constants.java
- ใช้ TILE_SIZE สำหรับการคำนวณ
- ใช้ขนาดวัตถุจาก Constants

## จุดเด่นของการออกแบบ

### Abstract Class vs Interface
- ใช้ abstract class เพราะมีการ implement ที่ใช้ร่วมกัน
- ให้ default behavior สำหรับการวาด
- รองรับการ override สำหรับพฤติกรรมพิเศษ

### Protected Members
- ให้คลาสลูกเข้าถึงได้โดยตรง
- ไม่ต้องสร้าง getter/setter สำหรับทุกตัวแปร
- รักษาความปลอดภัยจากการเข้าถึงภายนอก

### Grid-Based Positioning
- ง่ายต่อการจัดตำแหน่งวัตถุ
- สอดคล้องกับระบบไทล์ของแผนที่
- ลดความซับซ้อนในการคำนวณ

### Scalable Design
- เพิ่มประเภทวัตถุใหม่ได้ง่าย
- รองรับการเปลี่ยนขนาดไทล์
- ยืดหยุ่นในการปรับแต่ง

## การขยายและปรับปรุง

### เพิ่ม Methods ใหม่
```java
public abstract class GameObject {
    // ... existing code ...
    
    // เพิ่ม method สำหรับการชน
    public Rectangle getBounds() {
        return new Rectangle(positionX, positionY, objectWidth, objectHeight);
    }
    
    // เพิ่ม method สำหรับการเคลื่อนที่
    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }
}
```

### รองรับ Animation
```java
protected Image[] animationFrames;
protected int currentFrame;

public void updateAnimation() {
    // Logic สำหรับ animation
}
```

### เพิ่ม Layer System
```java
protected int drawLayer = 0;

public int getDrawLayer() {
    return drawLayer;
}
```